# 대규모 데이터셋 로딩을 위한 페이징 시스템의 필요성 및 RecyclerView 구현 방법

페이징 시스템은 특히 대규모 데이터셋을 다룰 때 데이터를 로드하고 표시하는 방식을 최적화합니다. 데이터를 더 작고 관리 가능한 청크(chunks) 단위로 가져옴으로써, 부드러운 앱 성능과 더 나은 사용자 경험을 보장합니다.

## 왜 페이징 시스템이 필수적인가?

### 💾 메모리 사용량 감소

데이터를 더 작은 페이지 단위로 로드하면, 메모리 사용량이 크게 줄어 잠재적인 메모리 부족 오류(Out-of-Memory Error)를 피할 수 있습니다.

### ⏱️ 초기 로드 시간 단축

현재 뷰에 필요한 데이터만 가져와 렌더링하므로 초기 로드 시간이 더 빨라집니다.

### 🌐 네트워크 사용량 최소화

필요할 때만 추가 데이터를 요청하기 때문에 네트워크 사용량이 최소화되어, 특히 제한된 대역폭 시나리오에서 효율적인 리소스 사용을 보장합니다.

### ✨ 사용자 경험 향상 (부드러운 스크롤)

페이징 시스템은 사용자가 탐색할 때 데이터를 동적으로 로드하여 목록이나 그리드에서 부드러운 스크롤링을 가능하게 합니다. 이 접근 방식은 기기나 네트워크에 과부하를 주지 않고 원활하고 반응성이 뛰어난 인터페이스를 제공하므로, 무한 스크롤이나 대용량 데이터 소스를 가진 애플리케이션에 특히 유용합니다.

-----

## RecyclerView로 수동 페이징 시스템을 구현하는 단계

수동으로 페이징 시스템을 만들려면 다음 단계를 따를 수 있습니다.

### 1단계: RecyclerView.Adapter 및 ViewHolder 생성

첫 번째 단계는 `RecyclerView.Adapter` 또는 `ListAdapter`를 해당하는 `ViewHolder`와 함께 만드는 것입니다. 이러한 컴포넌트들은 데이터셋을 관리하고 `RecyclerView`에 바인딩하는 데 필수적입니다. 어댑터는 데이터 소스를 처리하고, 뷰홀더는 데이터셋의 개별 아이템이 어떻게 표시될지를 정의합니다.

```kotlin
class PokedexAdapter: ListAdapter<Pokemon, PokedexAdapter.PokedexViewHolder>(diffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PokedexViewHolder {
        val binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokedexViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokedexViewHolder, position: Int) {
        // ... 데이터 바인딩 로직 ...
    }

    inner class PokedexViewHolder(
        private val binding: ItemPokemonBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        // ... 뷰 홀더 구현 ...
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Pokemon>() {
            override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
                oldItem == newItem
        }
    }
}
```

### 2단계: RecyclerView에 `addOnScrollListener` 추가

다음으로, `RecyclerView`의 스크롤 상태를 모니터링하기 위해 `addOnScrollListener`를 구현합니다. 이를 통해 사용자가 마지막으로 보이는 아이템까지 스크롤했는지 감지할 수 있습니다. 만약 마지막으로 보이는 아이템이 데이터셋의 끝에 가까워지면, 네트워크나 데이터베이스에서 다음 데이터 세트를 로드하도록 트리거합니다. 더 부드러운 로딩을 보장하려면, 임계값(threshold)을 사용하여 사용자가 끝에 도달하기 전에 데이터를 미리 가져와 스크롤 경험의 지연이나 중단을 방지합니다.

```kotlin
recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager // 또는 GridLayoutManager
        val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

        val thresholds = 4 // 마지막에서 4개 아이템이 보이기 시작하면 다음 페이지 로드
        if (lastVisiblePosition + thresholds > adapter.itemCount && !viewModel.isLoading) {
            viewModel.loadNextPage()
        }
    }
})
```

### 3단계: RecyclerView.Adapter에 새 데이터셋 추가

`viewModel.loadNextPage()`가 성공적으로 트리거되면, 새로 가져온 데이터셋을 `RecyclerView.Adapter`의 기존 데이터에 추가(append)합니다. 이는 목록이 새 아이템으로 원활하게 업데이트되도록 보장합니다.

-----

## 요약

데이터를 페이지로 분할하고, 스크롤 이벤트를 관찰하며, 어댑터를 동적으로 업데이트함으로써 수동 페이징 시스템을 만들 수 있습니다. 이를 구현하는 방법은 다양하지만, [**Jetpack Paging 라이브러리**](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)를 활용하는 것이 또 다른 접근법을 제공합니다. 대안으로, 오픈소스 라이브러리의 [RecyclerViewPaginator](https://github.com/skydoves/BaseRecyclerViewAdapter?tab=readme-ov-file#recyclerviewpaginator)와 같은 사용자 정의 접근 방식을 탐색하는 것도 학습 자료로 가치가 있을 수 있습니다.

-----

## Q. 앱이 API에서 대규모 데이터셋을 가져와 RecyclerView에 표시합니다. 부드러운 스크롤을 보장하고 메모리 사용량을 줄이기 위해 효율적인 페이징 시스템을 어떻게 구현하겠습니까?

API에서 대규모 데이터셋을 가져와 `RecyclerView`에 표시할 때, 부드러운 스크롤을 보장하고 메모리 사용량을 줄이기 위한 가장 효율적인 방법은 **Jetpack Paging 3 라이브러리**를 사용하는 것입니다.

Paging 라이브러리는 대용량 데이터를 작은 페이지(chunks) 단위로 나누어 로드하고, `RecyclerView`와 긴밀하게 통합되어 데이터 로딩, UI 업데이트, 오류 처리, 재시도 등 복잡한 과정을 자동화하고 최적화해주는 강력한 솔루션입니다.

---
### 1. Jetpack Paging 3 라이브러리 선택 및 이유

수동으로 페이징 시스템을 구현할 수도 있지만(`addOnScrollListener` 사용 등), 이는 상태 관리, 경쟁 조건(race condition), 오류 처리, 구성 변경 시 데이터 유지 등 많은 복잡한 문제를 야기합니다. Paging 3 라이브러리는 이러한 문제들을 해결하기 위해 설계되었습니다.

* **자동화된 데이터 로딩:** 사용자가 스크롤할 때 다음 페이지를 자동으로 요청하고 로드합니다.
* **메모리 효율성:** 전체 데이터셋이 아닌, 현재 화면에 필요한 일부 데이터만 메모리에 유지합니다.
* **시스템 리소스 존중:** 네트워크 및 시스템 리소스를 효율적으로 사용합니다.
* **오류 처리 및 재시도:** 데이터 로딩 실패 시 오류 상태를 쉽게 감지하고 재시도 메커니즘을 간편하게 구현할 수 있습니다.
* **아키텍처 통합:** 코루틴, Flow, LiveData, ViewModel, Hilt 등 다른 Jetpack 컴포넌트와 완벽하게 통합됩니다.

---
### 2. 효율적인 페이징 시스템 구현 단계

Jetpack Paging 3 라이브러리를 사용한 구현 단계는 다음과 같습니다.

#### 1단계: 데이터 소스 정의 (`PagingSource`)
API로부터 데이터를 **어떻게** 가져올지를 정의하는 부분입니다. `PagingSource`를 상속받아 `load` 함수를 구현합니다.

* **`load()` 함수:** `suspend` 함수이므로 코루틴을 사용하여 안전하게 네트워크 요청을 할 수 있습니다.
* **`LoadParams`:** 로드할 페이지 번호(`key`)와 페이지 크기(`loadSize`) 같은 정보를 담고 있습니다.
* **`LoadResult`:** 로딩 결과를 반환합니다. 성공 시 `LoadResult.Page`, 실패 시 `LoadResult.Error`를 반환합니다.

**예시 코드 (`ItemPagingSource.kt`):**
```kotlin
class ItemPagingSource(private val apiService: MyApiService) : PagingSource<Int, Item>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        val pageNumber = params.key ?: 1 // 현재 페이지, null이면 첫 페이지(1)
        return try {
            // API를 통해 데이터 로드
            val response = apiService.getItems(page = pageNumber, size = params.loadSize)
            val items = response.items

            LoadResult.Page(
                data = items,
                prevKey = if (pageNumber == 1) null else pageNumber - 1, // 이전 페이지 키
                nextKey = if (items.isEmpty()) null else pageNumber + 1 // 다음 페이지 키
            )
        } catch (e: Exception) {
            // 오류 발생 시
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        // 새로고침 시 시작할 페이지 키를 결정하는 로직
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
```

#### 2단계: 데이터 스트림 구성 (`Pager`)
`Pager`는 `PagingSource`로부터 데이터를 받아 UI로 전달될 `PagingData`의 스트림(`Flow` 또는 `Observable`)을 생성합니다. 이 과정은 보통 `Repository` 계층에서 수행됩니다.

* **`PagingConfig`:** 페이지 크기(`pageSize`), 미리 로드할 아이템 개수(`prefetchDistance`) 등을 설정하여 성능을 최적화합니다. `prefetchDistance`는 사용자가 스크롤하여 목록 끝에 도달하기 전에 미리 다음 페이지를 로드하게 하여 부드러운 스크롤 경험을 제공하는 데 중요합니다.

**예시 코드 (`ItemRepository.kt`):**
```kotlin
class ItemRepository(private val apiService: MyApiService) {
    fun getItemStream(): Flow<PagingData<Item>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20, // 한 페이지에 로드할 아이템 수
                prefetchDistance = 5, // 목록 끝에서 5개 아이템이 남았을 때 다음 페이지 로드 시작
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ItemPagingSource(apiService) } // PagingSource 제공
        ).flow
    }
}
```

#### 3단계: ViewModel에서 데이터 처리 및 캐싱
ViewModel은 `Repository`로부터 `PagingData` 스트림을 받아 UI가 소비할 수 있도록 준비합니다.

* **`.cachedIn(viewModelScope)`:** 이 연산자는 **매우 중요**합니다. 구성 변경(예: 화면 회전) 시에도 페이징 데이터를 메모리에 캐싱하여, UI가 재생성될 때 데이터를 처음부터 다시 불러오는 것을 방지하고 스크롤 위치를 유지할 수 있게 해줍니다.

**예시 코드 (`ItemViewModel.kt`):**
```kotlin
@HiltViewModel
class ItemViewModel @Inject constructor(
    private val repository: ItemRepository
) : ViewModel() {

    val itemPagingDataFlow: Flow<PagingData<Item>> =
        repository.getItemStream().cachedIn(viewModelScope)
}
```

#### 4단계: UI에 데이터 표시 (`PagingDataAdapter`)
`PagingDataAdapter`는 `PagingData`를 `RecyclerView`에 표시하기 위해 특별히 설계된 어댑터입니다.

* `PagingDataAdapter`는 페이지가 로드될 때 자동으로 리스트 업데이트를 처리합니다.
* 효율적인 업데이트를 위해 `DiffUtil.ItemCallback`을 필수로 요구합니다.
* UI(Activity/Fragment)에서는 ViewModel의 `Flow`를 수집(collect)하여 어댑터에 데이터를 제출(`submitData`)합니다.

**예시 코드 (`ItemFragment.kt` 및 `ItemAdapter.kt`):**
```kotlin
// ItemAdapter.kt
class ItemAdapter : PagingDataAdapter<Item, ItemViewHolder>(ITEM_COMPARATOR) {
    // ... onCreateViewHolder 및 onBindViewHolder 구현 ...

    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem
        }
    }
}

// ItemFragment.kt
@AndroidEntryPoint
class ItemFragment : Fragment() {
    private val viewModel: ItemViewModel by viewModels()
    private val itemAdapter = ItemAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // RecyclerView 설정
        binding.recyclerView.adapter = itemAdapter

        // ViewModel의 데이터 스트림을 관찰하고 어댑터에 데이터 제출
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.itemPagingDataFlow.collectLatest { pagingData ->
                itemAdapter.submitData(pagingData)
            }
        }
    }
}
```

---
### 3. 결론

대규모 데이터셋을 API로부터 받아 `RecyclerView`에 표시할 때, **Jetpack Paging 3 라이브러리**를 사용하는 것이 가장 효율적이고 안정적인 방법입니다. `PagingSource`로 데이터 로딩 방법을 정의하고, `Pager`로 데이터 스트림을 구성하며, `ViewModel`에서 `.cachedIn()`으로 데이터를 캐싱하고, `PagingDataAdapter`를 통해 UI에 표시하는 체계적인 단계를 따르면, 개발자는 복잡한 페이징 로직을 직접 구현하는 수고를 덜고 **메모리 사용량을 최소화하면서 부드러운 스크롤 경험**을 사용자에게 제공할 수 있습니다.


## Q. RecyclerView로 수동 페이징 시스템을 구현할 때 어떤 어려움이 발생할 수 있으며, 원활한 사용자 경험을 제공하기 위해 이를 어떻게 완화할 수 있나요?

`RecyclerView`로 수동 페이징 시스템을 구현하는 것은 가능하지만, 원활한 사용자 경험을 제공하기 위해서는 여러 가지 복잡하고 어려운 문제들을 직접 해결해야 합니다.

---
### 1. 수동 페이징 시스템 구현 시 발생하는 어려움

#### 1.1. 로딩 시점 및 중복 요청 제어
* **언제 다음 페이지를 요청할 것인가?:** `addOnScrollListener`를 사용하여 사용자가 목록의 끝에 도달했는지 감지해야 합니다. 만약 너무 늦게 요청하면 사용자는 로딩 스피너를 보며 기다려야 하고, 너무 일찍 요청하면 불필요한 데이터를 미리 불러오게 됩니다. 적절한 임계값(threshold)을 찾는 것이 어렵습니다.
* **중복 요청 문제:** 사용자가 목록 끝에서 위아래로 빠르게 스크롤하면, 로딩 조건을 만족하는 스크롤 이벤트가 여러 번 발생하여 동일한 다음 페이지를 중복으로 요청할 수 있습니다. 이를 막기 위해 `isLoading`과 같은 상태 플래그를 수동으로 관리해야 합니다.
* **초기 데이터가 화면을 채우지 못하는 경우:** 처음 로드한 데이터의 양이 화면 전체를 채우지 못하면 스크롤이 발생하지 않아, 다음 페이지를 로드하기 위한 스크롤 이벤트가 절대로 트리거되지 않는 문제가 발생할 수 있습니다.

#### 1.2. 복잡한 상태 관리
* **로딩/오류/마지막 페이지 상태:** 현재 데이터를 로딩 중인지(`isLoading`), 오류가 발생했는지(`isError`), 데이터의 끝에 도달했는지(`isLastPage`) 등의 상태를 모두 직접 추적하고 관리해야 합니다.
* **페이지 키 관리:** 현재 페이지 번호나 다음 페이지를 요청하기 위한 토큰(token) 값을 직접 저장하고, 성공적으로 로드될 때마다 업데이트해야 합니다.

#### 1.3. 오류 처리 및 재시도 로직 구현
* **API 실패 시 UI 피드백:** 다음 페이지 로딩에 실패했을 때, 이를 사용자에게 어떻게 알릴지 결정해야 합니다. 일반적으로 목록 하단에 "오류가 발생했습니다. 재시도"와 같은 UI를 보여줘야 합니다.
* **재시도 메커니즘:** 재시도 버튼을 누르면 실패했던 바로 그 페이지를 다시 요청하는 로직을 구현해야 합니다. 이는 어댑터의 뷰홀더에서 발생한 클릭 이벤트를 ViewModel까지 전달해야 하는 복잡한 통신 구조를 필요로 합니다.
* **UI 복잡도 증가:** 로딩 스피너, 오류 메시지, 일반 데이터 아이템을 모두 표시하기 위해 `RecyclerView.Adapter`에서 여러 뷰 타입(multi-view type)을 구현해야 합니다.

#### 1.4. 경쟁 조건(Race Condition) 및 스레딩 문제
* `onScrolled` 이벤트는 UI 스레드에서, 네트워크 요청은 백그라운드 스레드에서 발생합니다. `isLoading`과 같은 상태 플래그를 여러 스레드에서 접근하고 수정할 때, 동기화 문제를 고려하지 않으면 경쟁 조건이 발생하여 앱이 오작동할 수 있습니다. (예: 네트워크 응답이 도착하기 직전에 또 다른 스크롤 이벤트가 로딩을 트리거하는 경우)

#### 1.5. 구성 변경(Configuration Changes) 처리
* 화면 회전과 같은 구성 변경이 발생하면 액티비티나 프래그먼트가 재생성됩니다. 이때 지금까지 로드한 모든 데이터 목록, 현재 페이지 번호, 스크롤 위치 등 모든 상태를 `ViewModel`을 사용하여 수동으로 보존하고 복원하지 않으면, 처음부터 다시 데이터를 로드하게 되어 매우 나쁜 사용자 경험을 제공합니다.

#### 1.6. 데이터 일관성 유지
* 사용자가 목록 하단에서 다음 페이지를 로드하는 동시에 위로 스크롤하여 당겨서 새로고침(pull-to-refresh)을 시도하는 경우, 두 데이터 로딩 요청이 충돌할 수 있습니다. 이러한 동시성 문제를 처리하고 데이터의 일관성을 유지하는 로직을 직접 구현해야 합니다.

---
### 2. 어려움 완화 및 원활한 사용자 경험 제공 전략

이러한 어려움들을 완화하고 원활한 사용자 경험을 제공하기 위한 전략은 다음과 같습니다.

#### 2.1. 로딩 트리거 최적화
* **임계값(Threshold) 사용:** 목록의 맨 마지막 아이템이 보일 때가 아닌, 마지막 아이템으로부터 몇 개(예: 4-5개) 앞선 아이템이 보이기 시작할 때 다음 페이지를 미리 로드(**pre-fetching**)하여 사용자가 로딩을 기다리지 않도록 합니다.
* **`isLoading` 상태 플래그 활용:** `ViewModel`에 `isLoading`과 같은 `Boolean` 상태를 두고, 데이터 로딩을 시작할 때 `true`로, 로딩이 완료(성공 또는 실패)되면 `false`로 설정하여 중복 요청을 방지합니다.
* **초기 화면 채움 확인:** 초기 데이터를 로드한 후, 목록의 아이템들이 화면을 채우기에 부족하다면 스크롤이 불가능하므로 즉시 다음 페이지를 요청하는 로직을 추가합니다.

#### 2.2. ViewModel을 통한 상태 중앙 관리
* `isLoading`, `currentPageKey`, `isError`, `isLastPage` 등 페이징과 관련된 모든 상태와 현재까지 로드된 전체 데이터 목록을 **`ViewModel`** 에서 중앙 집중적으로 관리합니다.
* `LiveData`나 `StateFlow`를 사용하여 이러한 상태들을 UI에 노출시키고, UI는 상태 변화에 따라 반응적으로 업데이트되도록 합니다.

#### 2.3. 다중 뷰 타입을 활용한 UI 상태 표시
* `RecyclerView.Adapter` 내에서 `getItemViewType()` 메서드를 재정의하여 일반 데이터 아이템 외에, 목록의 끝에 표시될 **로딩 상태(스피너) 아이템**과 **오류 상태(재시도 버튼) 아이템**을 위한 별도의 뷰 타입을 정의합니다.
* `onBindViewHolder()`에서는 상태에 따라 적절한 뷰를 보여주도록 분기 처리합니다.

#### 2.4. 인터페이스 콜백을 통한 재시도 구현
* 오류 상태 뷰홀더의 "재시도" 버튼 클릭 이벤트를 처리하기 위해 인터페이스 콜백을 정의합니다. 어댑터에서 이 인터페이스를 구현하고, 프래그먼트나 액티비티는 이 콜백을 받아 `ViewModel`의 재시도 함수를 호출하도록 연결합니다.

#### 2.5. 코루틴 등 비동기 처리 도구 활용
* 네트워크 요청과 같은 비동기 작업을 코루틴을 사용하여 체계적으로 관리하고, `viewModelScope`를 활용하여 뷰모델의 생명주기와 작업의 생명주기를 일치시켜 메모리 누수를 방지합니다.

### 3. 최종 권장 사항: Jetpack Paging 라이브러리 사용

**결론적으로, 위에서 언급된 모든 어려움을 가장 효과적으로 완화하는 방법은 Jetpack Paging 라이브러리를 사용하는 것입니다.**

Paging 라이브러리는 수동으로 구현할 때 발생하는 거의 모든 문제들(로딩 트리거, 상태 관리, 오류 처리 및 재시도, 구성 변경, 데이터 캐싱 등)에 대한 표준적이고 견고한 해결책을 이미 내장하고 있습니다. 개발자는 데이터 로딩 방법을 정의하는 `PagingSource`만 구현하면, 라이브러리가 나머지 복잡한 과정을 알아서 처리해 줍니다. 따라서 특별한 이유가 없다면, 대규모 데이터셋을 다룰 때는 **수동 구현 대신 Jetpack Paging 라이브러리를 사용하는 것이 강력하게 권장됩니다.**

