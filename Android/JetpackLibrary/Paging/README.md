# Jetpack Paging 라이브러리란 무엇인가?

[**Jetpack Paging 라이브러리**](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)는 대규모 데이터셋을 **청크(chunks), 즉 "페이지(pages)" 단위로 로드하고 표시**하는 과정을 돕기 위해 설계된 안드로이드 아키텍처 컴포넌트입니다. 이는 데이터베이스나 API와 같은 소스에서 효율적으로 데이터를 가져와야 하는 애플리케이션에 특히 유용하며, **메모리 사용량을 최소화하고 RecyclerView 기반 UI의 전반적인 성능을 향상**시킵니다.

Paging 라이브러리는 점진적으로 데이터를 로딩하는 구조화된 접근 방식을 제공합니다. 데이터 캐싱, 재시도 메커니즘, 효율적인 메모리 사용과 같은 핵심적인 측면을 별도의 설정 없이 바로(out of the box) 처리합니다. 이 라이브러리는 로컬 데이터 소스(예: Room 데이터베이스)와 원격 소스(예: 네트워크 API) 또는 이 둘의 조합을 모두 지원합니다.

---
## Paging 라이브러리의 주요 구성 요소

* **`PagingData`**: 점진적으로 로드된 데이터의 스트림을 나타냅니다. 관찰(observe)될 수 있으며 `RecyclerView`와 같은 UI 컴포넌트에 제출될 수 있습니다.
* **`PagingSource`**: 데이터 소스에서 데이터가 로드되는 방식을 정의하는 책임을 집니다. 위치나 ID와 같은 키를 기반으로 데이터 페이지를 로드하는 메서드를 제공합니다.
* **`Pager`**: `PagingSource`와 `PagingData` 사이의 중재자 역할을 합니다. `PagingData` 스트림의 생명주기를 관리합니다.
* **`RemoteMediator`**: 로컬 캐싱과 원격 API 데이터를 결합할 때 경계 조건(boundary conditions)을 구현하는 데 사용됩니다.

---
## Paging 라이브러리 작동 방식

Paging 라이브러리는 데이터를 페이지로 분할하여 효율적인 데이터 로딩을 가능하게 합니다. 사용자가 `RecyclerView`를 스크롤하면, 라이브러리는 필요에 따라 새로운 데이터 페이지를 가져와 최소한의 메모리 사용을 보장합니다. 이 라이브러리는 `Flow` 또는 `LiveData`와 원활하게 작동하여 데이터 변경을 관찰하고 그에 따라 UI를 업데이트합니다.

**일반적인 워크플로우는 다음과 같습니다.**

1.  데이터를 가져오는 방법을 지정하기 위해 **`PagingSource`** 를 정의합니다.
2.  **`Pager`** 를 사용하여 `PagingData`의 `Flow`를 생성합니다.
3.  **`ViewModel`** 에서 `PagingData`를 관찰하고, `RecyclerView`에서 렌더링하기 위해 **`PagingDataAdapter`** 에 제출합니다.

---
## Jetpack Paging 구현 예시

### 1. PagingSource 구현
먼저, 아래와 같이 네트워크에서 데이터를 가져오는 `PagingSource`를 구현해야 합니다.
```kotlin
class ExamplePagingSource(
    private val apiService: ApiService
) : PagingSource<Int, ExampleData>() {

    // 데이터를 로드하는 로직
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ExampleData> {
        val page = params.key ?: 1 // 현재 페이지 번호, null이면 1로 시작
        return try {
            val response = apiService.getData(page, params.loadSize)
            LoadResult.Page(
                data = response.items,
                prevKey = if (page == 1) null else page - 1, // 이전 페이지 키
                nextKey = if (response.items.isEmpty()) null else page + 1 // 다음 페이지 키
            )
        } catch (e: Exception) {
            LoadResult.Error(e) // 오류 발생 시
        }
    }

    // 페이지 키를 가져오는 로직 (앵커 위치 기반)
    override fun getRefreshKey(state: PagingState<Int, ExampleData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
```

### 2. Repository에서 Pager 생성
다음으로, `PagingSource`와 `PagingData` 사이를 중재하기 위해 Repository에서 `Pager`를 생성합니다.
```kotlin
class ExampleRepository(private val apiService: ApiService) {
    fun getExampleData(): Flow<PagingData<ExampleData>> {
        return Pager(
            config = PagingConfig(pageSize = 20), // 한 페이지에 로드할 아이템 수
            pagingSourceFactory = { ExamplePagingSource(apiService) } // PagingSource 제공
        ).flow
    }
}
```

### 3. ViewModel에서 PagingData 관찰
다음으로, `ViewModel`에서 `PagingData`를 관찰할 수 있습니다.
```kotlin
class ExampleViewModel(private val repository: ExampleRepository) : ViewModel() {
    val exampleData: Flow<PagingData<ExampleData>> = repository.getExampleData()
        .cachedIn(viewModelScope) // viewModelScope 내에서 데이터 캐싱
}
```

### 4. RecyclerView에서 데이터 표시
마지막으로, 아래 예시와 같이 `PagingDataAdapter`를 확장하는 사용자 정의 `RecyclerView.Adapter`를 생성하여 `RecyclerView`에 데이터를 표시할 수 있습니다.
```kotlin
class ExampleAdapter : PagingDataAdapter<ExampleData, ExampleViewHolder>(DIFF_CALLBACK) {
    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        val item = getItem(position)
        // getItem()은 PagingDataAdapter 내부에서 페이징된 데이터 처리
        item?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.example_item, parent, false)
        return ExampleViewHolder(view)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ExampleData>() {
            override fun areItemsTheSame(oldItem: ExampleData, newItem: ExampleData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ExampleData, newItem: ExampleData): Boolean {
                return oldItem == newItem
            }
        }
    }
}
```

---
## 요약

Jetpack Paging 라이브러리는 점진적인 데이터 로딩 구현을 돕습니다. `PagingSource`, `Pager`, `PagingDataAdapter`를 포함한 주요 구성 요소들은 대규모 데이터셋 처리를 용이하게 합니다. 이는 특히 무한 스크롤, 페이지네이션된 API 또는 대용량 데이터베이스를 다루는 애플리케이션에 유용하며, 라이브러리가 데이터 가져오기 및 UI 업데이트를 관리하는 동안 개발자가 애플리케이션 로직에 더 집중할 수 있게 합니다.

---
## Q. Paging 라이브러리는 데이터 로딩 중 오류를 어떻게 처리하며, 페이지네이션된 데이터 흐름에서 오류 처리 및 재시도 메커니즘을 구현하기 위한 권장 전략은 무엇인가요?

Jetpack Paging 라이브러리는 대용량 데이터 로딩 시 발생할 수 있는 네트워크 오류나 데이터베이스 오류 등을 처리하고, 사용자에게 재시도 옵션을 제공하는 체계적인 방법을 제공합니다.

---
### 1. Paging 라이브러리가 데이터 로딩 중 오류를 처리하는 방식

Paging 라이브러리의 오류 처리는 데이터 소스 레벨에서 시작하여 UI 레벨까지 상태가 전파되는 방식으로 작동합니다.

#### 1.1. PagingSource에서의 오류 반환 (`LoadResult.Error`)

* 오류 처리의 가장 기본이 되는 부분은 **`PagingSource`** 의 `load()` 메서드입니다. 이 메서드 내에서 데이터를 가져오는 동안 (예: 네트워크 API 호출) 예외가 발생하면, `try-catch` 블록으로 이를 잡아내고 결과를 **`LoadResult.Error(throwable)`** 형태로 반환해야 합니다.

    ```kotlin
    class ExamplePagingSource(private val api: ApiService) : PagingSource<Int, Data>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
            val page = params.key ?: 1
            return try {
                val response = api.getData(page)
                LoadResult.Page(
                    data = response.items,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (response.items.isEmpty()) null else page + 1
                )
            } catch (e: IOException) {
                // 네트워크 오류 처리
                return LoadResult.Error(e)
            } catch (e: HttpException) {
                // HTTP 오류 처리
                return LoadResult.Error(e)
            }
        }
        // ...
    }
    ```

#### 1.2. LoadState를 통한 오류 상태 전파

* `PagingSource`에서 반환된 `LoadResult.Error`는 Paging 라이브러리 내부적으로 처리되어 UI 레이어로 **`LoadState`** 라는 객체를 통해 전달됩니다.
* `PagingDataAdapter`는 현재 로딩 상태를 나타내는 `loadStateFlow: Flow<CombinedLoadStates>`를 제공합니다. 개발자는 이 Flow를 수집(collect)하여 로딩 중, 성공, 오류 상태를 감지할 수 있습니다.
* **`CombinedLoadStates`** 객체는 로딩의 각 단계별 상태(`refresh`, `append`, `prepend`)를 포함합니다.
    * **`refresh`**: 전체 데이터를 처음 로드하거나 새로고침할 때의 상태.
    * **`append`**: 목록의 끝에 다음 페이지를 로드할 때의 상태.
    * **`prepend`**: 목록의 시작 부분에 이전 페이지를 로드할 때의 상태.
* 이 각각의 상태는 `LoadState.Loading`, `LoadState.NotLoading`, `LoadState.Error` 중 하나가 될 수 있으며, `LoadState.Error`는 발생한 `Throwable` 객체를 포함하고 있어 UI에서 오류 원인을 파악하고 표시하는 데 사용할 수 있습니다.

---
### 2. 오류 처리 및 재시도 메커니즘 구현 권장 전략

UI에서 `LoadState`를 관찰하고 상태에 따라 적절한 UI를 보여주며, 사용자에게 재시도 옵션을 제공하는 것이 핵심 전략입니다.

#### 2.1. UI에 오류 상태 표시하기

* **`loadStateFlow` 관찰:** Activity나 Fragment에서 `PagingDataAdapter`의 `loadStateFlow`를 수집하여 로딩 상태 변화에 따라 UI를 업데이트합니다.

    ```kotlin
    // Fragment 내에서
    lifecycleScope.launch {
        pagingAdapter.loadStateFlow.collectLatest { loadStates ->
            // 초기 로드(refresh) 상태 처리
            when (val refreshState = loadStates.refresh) {
                is LoadState.Loading -> {
                    // 로딩 중 UI 표시 (예: ProgressBar)
                    progressBar.isVisible = true
                    errorView.isVisible = false
                }
                is LoadState.Error -> {
                    // 오류 발생 UI 표시
                    progressBar.isVisible = false
                    errorView.isVisible = true
                    errorTextView.text = refreshState.error.localizedMessage
                }
                is LoadState.NotLoading -> {
                    // 로딩 완료 UI
                    progressBar.isVisible = false
                    errorView.isVisible = false
                }
            }
        }
    }
    ```

#### 2.2. 재시도(Retry) 기능 구현하기

* **`PagingDataAdapter.retry()` 메서드:** `PagingDataAdapter`는 실패한 로드 작업을 다시 시도하는 `retry()` 메서드를 제공합니다.
* **재시도 버튼과 연결:** 오류 UI에 "재시도" 버튼을 만들고, 이 버튼의 클릭 리스너에서 `pagingAdapter.retry()`를 호출하기만 하면 됩니다.

    ```kotlin
    // 오류 UI의 재시도 버튼에 리스너 설정
    retryButton.setOnClickListener {
        pagingAdapter.retry()
    }
    ```

#### 2.3. LoadStateAdapter를 사용한 향상된 UX 구현

초기 로드 실패 시에는 전체 화면에 오류를 표시하는 것이 적합하지만, 페이지를 추가로 로드하는 `append`나 `prepend` 작업 실패 시에는 목록 하단이나 상단에 작은 오류 메시지와 재시도 버튼을 보여주는 것이 사용자 경험에 더 좋습니다. 이를 위해 **`LoadStateAdapter`** 를 사용합니다.

* **`LoadStateAdapter`란?**
    로딩 상태(로딩 스피너, 오류 메시지, 재시도 버튼 등)를 표시하기 위한 전용 `RecyclerView.Adapter`입니다.
* **구현 방법:**
    1.  `LoadState`를 아이템으로 받는 `ViewHolder`와 `LoadStateAdapter`를 생성합니다.
        ```kotlin
        class MyLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<MyLoadStateViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) = MyLoadStateViewHolder.create(parent, retry)
            override fun onBindViewHolder(holder: MyLoadStateViewHolder, loadState: LoadState) = holder.bind(loadState)
        }

        class MyLoadStateViewHolder(view: View, retry: () -> Unit) : RecyclerView.ViewHolder(view) {
            // ... view 초기화 ...
            init {
                retryButton.setOnClickListener { retry() }
            }
            fun bind(loadState: LoadState) {
                progressBar.isVisible = loadState is LoadState.Loading
                retryButton.isVisible = loadState is LoadState.Error
                errorMsg.isVisible = loadState is LoadState.Error
            }
            // ...
        }
        ```
    2.  `PagingDataAdapter`에 헤더(header) 및 푸터(footer)로 `LoadStateAdapter`를 연결합니다.
        ```kotlin
        val pagingAdapter = MyPagingAdapter()
        recyclerView.adapter = pagingAdapter.withLoadStateHeaderAndFooter(
            header = MyLoadStateAdapter { pagingAdapter.retry() },
            footer = MyLoadStateAdapter { pagingAdapter.retry() }
        )
        ```
* **결과:**
    * 이제 사용자가 스크롤하여 다음 페이지를 로드할 때, 로딩 중이면 목록 하단에 로딩 스피너가, 오류 발생 시에는 오류 메시지와 재시도 버튼이 자동으로 표시됩니다.

---
### 3. 요약: 오류 처리 및 재시도 전략

1.  **`PagingSource`에서** `try-catch`를 사용하여 예외를 잡고 **`LoadResult.Error`** 를 반환합니다.
2.  **UI 레이어(Activity/Fragment)에서** `PagingDataAdapter`의 **`loadStateFlow`** 를 관찰하여 로딩 상태의 변화를 감지합니다.
3.  **초기 로드(`refresh`)** 실패 시에는 전체 화면에 오류 메시지와 **`pagingAdapter.retry()`** 를 호출하는 재시도 버튼을 표시합니다.
4.  **페이지 추가 로드(`append`/`prepend`)** 실패 시에는 **`LoadStateAdapter`** 를 사용하여 목록의 끝이나 시작 부분에 로딩 상태(로딩 중/오류)와 재시도 버튼을 표시하여 더 나은 사용자 경험을 제공합니다.