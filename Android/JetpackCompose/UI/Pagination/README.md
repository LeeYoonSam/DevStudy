# 레이지 리스트(Lazy Lists)로 페이지네이션(Pagination)을 구현하는 방법

페이지네이션은 부드러운 UI 성능을 유지하면서 리스트의 대규모 데이터셋을 효율적으로 처리하는 데 필수적입니다. 페이지네이션된 데이터를 관리하는 방법을 제공하는 [Jetpack Paging 라이브러리](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)를 포함하여 다양한 솔루션이 존재합니다. 그러나 사용자가 리스트의 끝에 도달했을 때 동적으로 더 많은 데이터를 로드함으로써 서드파티 라이브러리 없이도 무한 스크롤을 구현하는 것 또한 가능합니다.

-----

## 페이지네이션을 위한 스크롤 위치 감지

페이지네이션을 구현하는 일반적인 전략은 사용자가 마지막으로 보이는 아이템에 도달했을 때를 관찰한 다음 데이터 로드를 트리거하는 것입니다. 이는 아래 예시와 같이 `LazyListState`를 사용하여 달성할 수 있습니다.

```kotlin
@Composable
fun PaginatedList(viewModel: ListViewModel) {
    val listState = rememberLazyListState()
    val items by viewModel.items.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val threshold = 2 // 마지막에서 2번째 아이템이 보이면 다음 페이지 로드
    val shouldLoadMore by remember {
        derivedStateOf {
            val totalItemsCount = listState.layoutInfo.totalItemsCount
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            (lastVisibleItemIndex + threshold >= totalItemsCount) && !isLoading
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { shouldLoadMore }
            .distinctUntilChanged()
            .filter { it }
            .collect { viewModel.loadMoreItems() }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        items(items) { item ->
            Text(modifier = Modifier.padding(8.dp), text = "$item")
        }

        item {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
        }
    }
}
```

이 예시에서:

  * `LazyListState`는 스크롤 위치를 모니터링하는 데 사용됩니다.
  * `snapshotFlow`는 마지막으로 보이는 아이템의 인덱스를 추적합니다.
  * 임계값과 함께 계산하여 마지막 아이템에 도달하면, `loadMoreItems()`가 트리거되어 다음 데이터 페이지를 가져옵니다.

-----

## 페이지네이션 관리를 위한 ViewModel

페이지네이션 로직을 관리하기 위해, ViewModel은 데이터를 점진적으로 로드하는 데 사용됩니다.

```kotlin
private class ListViewModel : ViewModel() {
    // mutableStateListOf 대신 StateFlow를 사용하여 상태를 관리하는 것이 더 일반적인 패턴입니다.
    private val _items = MutableStateFlow<List<Int>>(emptyList())
    val items: StateFlow<List<Int>> = _items

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var currentPage = 0

    fun loadMoreItems() {
        if (_isLoading.value) return // 로딩 중에는 중복 요청 방지

        _isLoading.value = true
        viewModelScope.launch {
            delay(1000) // 네트워크 요청 시뮬레이션
            val newItems = List(20) { (currentPage * 20) + it }
            _items.value = _items.value + newItems // 기존 리스트에 새 아이템 추가
            currentPage++
            _isLoading.value = false
        }
    }
}
```

이 구현에서:

  * `_items`는 더 많은 페이지가 로드될 때 업데이트되는 상태 리스트 데이터를 보유합니다.
  * `loadMoreItems()`는 호출될 때 새로운 데이터를 가져옵니다.
  * `isLoading`은 페이지가 이미 로드되는 동안 중복 요청을 방지합니다.

-----

## 요약

페이지네이션은 `LazyListState`를 사용하여 추가 데이터가 로드되어야 할 시점을 감지함으로써 레이지 리스트에서 효율적으로 구현될 수 있습니다. 이 접근 방식은 새로운 아이템이 필요할 때만 가져오도록 보장하여, 불필요한 리컴포지션을 줄이고 성능을 향상시킵니다. 레이지 로딩을 활용함으로써, 부드러운 스크롤링과 최적의 리소스 관리를 유지하면서 대규모 데이터셋을 원활하게 표시할 수 있습니다.

-----

## Q. 더 많은 아이템이 로드되어야 할 때를 감지하기 위해 어떤 API나 상태 메커니즘을 사용하겠습니까?

더 많은 아이템을 로드해야 할 시점을 감지하기 위해, **`rememberLazyListState`** 를 사용하여 스크롤 상태를 얻고, **`derivedStateOf`** 를 사용하여 '마지막 아이템에 거의 도달했는지' 여부를 효율적으로 계산합니다. 마지막으로, 이 계산된 조건의 변경을 **`snapshotFlow`** 로 관찰하여 실제 데이터 로딩을 트리거하는 것이 가장 효과적인 방법입니다.


### 1. `rememberLazyListState`: 스크롤 상태의 근원 📍

가장 먼저 필요한 것은 `LazyColumn`이나 `LazyRow`의 현재 스크롤 상태를 아는 것입니다. **`rememberLazyListState()`** API는 `LazyListState` 객체를 생성하고 컴포지션 간에 기억해 줍니다.

  * **역할:** 이 `LazyListState` 객체는 현재 화면에 보이는 아이템들의 정보, 전체 아이템 개수, 스크롤 위치 등 `LazyColumn`의 모든 상태 정보를 담고 있는 **단일 진실 공급원(Single Source of Truth)** 입니다.
  * **핵심 정보:** 페이지네이션 트리거를 계산하기 위해 주로 다음 정보를 사용합니다.
      * **`listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index`**: 현재 화면에 보이는 아이템 중 가장 마지막 아이템의 인덱스.
      * **`listState.layoutInfo.totalItemsCount`**: 어댑터에 있는 전체 아이템의 개수.

-----

### 2. `derivedStateOf`: 효율적인 조건 계산 🧠

스크롤 이벤트는 매우 빈번하게 발생합니다. 매 스크롤 픽셀마다 로딩 조건을 계속해서 재계산하고 리컴포지션을 유발하는 것은 비효율적입니다. **`derivedStateOf`** 는 이러한 문제를 해결합니다.

  * **역할:** 하나 이상의 상태 객체로부터 파생된 값을 계산합니다. `derivedStateOf`의 가장 큰 장점은, 내부에서 의존하는 상태(`listState` 등)가 변경될 때마다 계산을 다시 하기는 하지만, **계산된 최종 결과값이 이전과 다를 경우에만** 이 상태를 읽는 컴포저블의 리컴포지션을 트리거한다는 것입니다.
  * **구현:** "다음 페이지를 로드해야 하는가?" 라는 `Boolean` 조건을 `derivedStateOf`로 만듭니다.
    ```kotlin
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val totalItemsCount = listState.layoutInfo.totalItemsCount

            // 마지막으로 보이는 아이템의 인덱스가 (전체 아이템 수 - 임계값)보다 크거나 같고,
            // 현재 로딩 중이 아닐 때 true를 반환
            lastVisibleItemIndex >= totalItemsCount - 1 - threshold && !isLoading
        }
    }
    ```
  * **효과:** `lastVisibleItemIndex`는 계속 변하지만, `shouldLoadMore`의 값은 로딩 조건이 `false`에서 `true`로 바뀌는 **단 한 번의 순간**에만 변경됩니다. 이는 불필요한 리컴포지션을 방지하는 핵심적인 최적화입니다.

-----

### 3. `snapshotFlow`: 상태 변화를 이벤트로 변환 🌊

이제 `shouldLoadMore`라는 상태가 `true`로 바뀌는 "순간"을 포착하여 데이터 로딩이라는 **일회성 이벤트**를 발생시켜야 합니다. 이를 위해 **`snapshotFlow`** 를 사용합니다.

  * **역할:** Compose의 `State` 객체를 코틀린의 `Flow`로 변환합니다. `snapshotFlow` 블록 내에서 읽는 상태가 변경될 때마다 `Flow`는 새로운 값을 방출(emit)합니다.
  * **구현:** `LaunchedEffect` 내에서 `snapshotFlow`를 사용하여 `shouldLoadMore` 상태의 변경을 `Flow`로 만들고, `Flow`의 연산자들을 활용하여 로딩 로직을 트리거합니다.
    ```kotlin
    LaunchedEffect(shouldLoadMore) { // shouldLoadMore가 true가 되면 이 블록이 실행됨
        if (shouldLoadMore) {
            viewModel.loadMoreItems()
        }
    }

    // 또는 더 정교한 Flow 연산자 사용
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { lastVisibleItemIndex ->
                if (lastVisibleItemIndex == listState.layoutInfo.totalItemsCount - 1 - threshold && !isLoading) {
                    viewModel.loadMoreItems()
                }
            }
    }
    ```

-----

### 4. 요약: 세 가지 메커니즘의 조합

효율적인 페이지네이션 트리거는 다음과 같은 세 가지 API와 상태 메커니즘의 조합으로 이루어집니다.

1.  **`rememberLazyListState`** 를 통해 스크롤 위치라는 **원본 데이터**를 얻습니다.
2.  **`derivedStateOf`** 를 사용해 이 원본 데이터를 바탕으로 "다음 페이지를 로드해야 하는가?"라는 **조건부 상태**를 효율적으로 계산합니다.
3.  **`snapshotFlow`** (주로 `LaunchedEffect`와 함께)를 사용해 이 조건부 상태의 변경을 **실행 가능한 이벤트**로 변환하여 `ViewModel`의 데이터 로딩 함수를 호출합니다.

이러한 조합은 불필요한 계산과 리컴포지션을 최소화하면서도, 정확한 시점에 다음 페이지를 로드하여 사용자에게 부드러운 무한 스크롤 경험을 제공하는 가장 권장되는 방법입니다.


## Q. `LazyListState`는 페이지네이션에서 어떤 역할을 하며, `derivedStateOf`와 `snapshotFlow`는 데이터 로딩 로직을 최적화하는 데 어떻게 도움이 되나요? 이 플로우에서 `distinctUntilChanged()`가 중요한 이유는 무엇인가요?

**`LazyListState`** 는 현재 스크롤 위치와 전체 아이템 개수 등 **페이지네이션 계산에 필요한 원본 데이터**를 제공합니다. **`derivedStateOf`** 는 이 데이터를 바탕으로 '다음 페이지를 로드해야 하는가'라는 **조건을 효율적으로 계산**하고, **`snapshotFlow`** 는 이 조건의 결과가 바뀔 때만 **데이터 로딩을 트리거하는 이벤트 스트림**을 만듭니다. 마지막으로 **`distinctUntilChanged()`** 는 이 스트림에서 중복된 값을 제거하여, **데이터 로드 함수가 단 한 번만 호출되도록 보장**하는 중요한 역할을 합니다.

-----

### 1. `LazyListState`의 역할: 스크롤 상태 정보 제공 📍

**`LazyListState`** 는 `LazyColumn`이나 `LazyRow`의 현재 스크롤 상태에 대한 모든 정보를 담고 있는 **단일 진실 공급원(Single Source of Truth)** 입니다. 페이지네이션에서는 주로 다음 두 가지 정보를 얻기 위해 사용됩니다.

  * **`listState.layoutInfo.visibleItemsInfo`**: 현재 화면에 보이는 아이템들의 정보를 담은 리스트로, 이 리스트의 마지막 아이템(`lastOrNull()`)의 인덱스(`index`)를 통해 사용자가 어디까지 스크롤했는지 알 수 있습니다.
  * **`listState.layoutInfo.totalItemsCount`**: 현재 어댑터에 있는 전체 아이템의 개수입니다.

이 두 정보를 조합하면 "사용자가 목록의 끝에 거의 도달했는가?"를 판단할 수 있습니다.

-----

### 2. `derivedStateOf`와 `snapshotFlow`를 통한 로딩 로직 최적화 🚀

### `derivedStateOf`: 효율적인 조건 계산

사용자가 스크롤할 때 `LazyListState`의 값은 매우 빈번하게 변경됩니다. 만약 이 모든 변경 시점마다 로딩 조건을 검사하고 리컴포지션을 유발한다면 매우 비효율적일 것입니다.

**`derivedStateOf`** 는 이러한 문제를 해결합니다. `derivedStateOf` 블록은 의존하는 상태(`listState`)가 변경될 때마다 재계산되지만, **계산된 최종 결과값이 이전과 다를 경우에만** 이 상태를 읽는 컴포저블의 리컴포지션을 트리거합니다.

```kotlin
val shouldLoadMore by remember {
    derivedStateOf {
        // ... lastVisibleItemIndex와 totalItemsCount 계산 로직 ...
        (lastVisibleItemIndex + threshold >= totalItemsCount) && !isLoading
    }
}
```

위 코드에서 `shouldLoadMore`의 값은 스크롤 위치가 계속 바뀌더라도 로딩 조건이 `false`에서 `true`로 바뀌는 **결정적인 순간에만 변경**됩니다. 이는 불필요한 재계산을 방지하는 핵심적인 최적화입니다.

### `snapshotFlow`: 상태를 이벤트 스트림으로 변환

`derivedStateOf`를 통해 계산된 `shouldLoadMore`라는 `State`를 **실행 가능한 이벤트**로 변환하기 위해 **`snapshotFlow`** 를 사용합니다.

`snapshotFlow`는 Compose의 `State` 객체를 코틀린의 `Flow`로 변환해주는 다리 역할을 합니다. 이를 통해 우리는 `debounce`, `filter`, `distinctUntilChanged`와 같은 강력한 `Flow` 연산자를 UI 상태에 직접 적용할 수 있게 됩니다.

-----

### 3. `distinctUntilChanged()`의 중요성: 중복 호출 방지 🛡️

`snapshotFlow`는 블록 내에서 읽는 상태가 변경될 때마다 새로운 값을 방출(emit)합니다. 그런데 리컴포지션은 여러 이유로 발생할 수 있으며, 이 과정에서 `shouldLoadMore`가 `true`인 상태가 여러 번 감지될 수 있습니다. 만약 이 `true` 값이 방출될 때마다 데이터 로딩 함수를 호출한다면, **동일한 페이지에 대한 네트워크 요청이 중복으로 발생**할 수 있습니다.

**`distinctUntilChanged()`** 는 바로 이 문제를 해결합니다.

  * **역할:** `Flow` 스트림에서 **연속으로 중복된 값이 방출되는 것을 막아줍니다.**
  * **효과:** `[false, false, true, true, true, false]`와 같은 스트림이 있다면, `distinctUntilChanged()`를 거치면 `[false, true, false]`와 같이 값이 실제로 변경되는 순간에만 방출됩니다.
  * **페이지네이션에서의 중요성:** `snapshotFlow { shouldLoadMore }.distinctUntilChanged()`를 사용하면, `shouldLoadMore`의 상태가 `false`에서 `true`로 **전환되는 단 한 번의 순간**에만 `true` 값을 통과시킵니다. 그 이후에 `shouldLoadMore`가 계속 `true`로 유지되더라도 추가적인 방출은 없습니다. 이를 통해 `loadMoreItems()` 함수가 **단 한 번만 호출**되도록 보장할 수 있습니다.

-----

### 4. 전체 플로우 요약

1.  사용자가 스크롤하면 **`LazyListState`** 가 업데이트됩니다.
2.  **`derivedStateOf`** 는 `LazyListState`의 값을 바탕으로 `shouldLoadMore`라는 `Boolean` 상태를 효율적으로 재계산합니다.
3.  `shouldLoadMore`의 결과가 `false`에서 `true`로 변경되면, **`snapshotFlow`** 가 이 변경을 감지하고 `true` 값을 `Flow`로 방출합니다.
4.  **`distinctUntilChanged()`** 연산자가 연속적인 `true` 값의 중복 방출을 막아줍니다.
5.  **`filter { it }`** 연산자가 `true` 값만 통과시킵니다.
6.  `collect` 블록이 실행되어 `ViewModel`의 데이터 로딩 함수를 **단 한 번만 안전하게 호출**합니다.

이러한 메커니즘들의 조합은 불필요한 계산과 중복된 네트워크 요청을 방지하면서, 정확한 시점에 다음 페이지를 로드하는 매우 효율적이고 견고한 페이지네이션 시스템을 구축하게 해줍니다.


## Q. 사용자가 리스트를 빠르게 스크롤할 때 페이지네이션 중에 중복 네트워크 호출이나 데이터 로딩을 어떻게 방지할 수 있나요?

페이지네이션 중 빠른 스크롤로 인한 중복 네트워크 호출은 **`isLoading`과 같은 상태 플래그(State Flag)를 사용**하여 방지하는 것이 가장 일반적입니다. 데이터 로딩을 시작할 때 이 플래그를 `true`로 설정하고, 로딩이 완료되면 `false`로 되돌려 **이미 요청이 진행 중일 때는 추가 요청을 보내지 않도록** 막습니다.

또한, **`snapshotFlow`** 와 함께 **`distinctUntilChanged()`** 를 사용하면 '더 로드해야 한다'는 신호가 중복으로 발생하는 것을 방지하여 더욱 견고하게 구현할 수 있습니다.

### 1. 상태 플래그(State Flag)를 이용한 중복 요청 방지 (가장 기본적인 방법) 🚩

가장 기본적이고 필수적인 방법은 `ViewModel` 내에 현재 데이터 로딩 중인지를 나타내는 상태 플래그(예: `isLoading`)를 두는 것입니다.

  * **작동 방식:**

    1.  `ViewModel`에 `private val _isLoading = MutableStateFlow(false)`와 같이 로딩 상태를 관리하는 `StateFlow`를 선언합니다.
    2.  다음 페이지를 로드하는 함수(`loadMoreItems`)가 호출되면, **가장 먼저 `isLoading` 상태를 확인**합니다. 만약 `true`라면 (즉, 이미 다른 요청이 진행 중이라면) 즉시 함수를 종료하여 중복 요청을 막습니다.
    3.  `isLoading`이 `false`라면, 실제 네트워크 요청을 시작하기 직전에 `_isLoading.value = true`로 설정합니다.
    4.  네트워크 요청이 성공하든 실패하든, 작업이 완료된 후에는 `finally` 블록 등에서 반드시 `_isLoading.value = false`로 되돌려 다음 요청을 받을 수 있도록 준비합니다.

  * **UI에서의 활용:**
    UI(컴포저블)에서 다음 페이지 로드를 트리거하는 조건(예: `derivedStateOf` 블록)에도 `!isLoading`을 포함시켜, 로딩 중일 때는 스크롤 위치가 임계값을 넘더라도 트리거 조건이 `true`가 되지 않도록 이중으로 방지합니다.

-----

### 2. `snapshotFlow`와 `distinctUntilChanged()`를 이용한 이벤트 중복 방지 🌊

상태 플래그 방식은 이미 시작된 요청을 막는 데 효과적이지만, 로딩을 시작하라는 "이벤트" 자체가 여러 번 발생하는 것을 막아주지는 못할 수 있습니다. `snapshotFlow`와 `Flow` 연산자를 사용하면 이 "이벤트" 자체를 더 정교하게 제어할 수 있습니다.

  * **작동 방식:**
    사용자가 스크롤 임계값 주변에서 위아래로 빠르게 움직이면, '더 로드해야 한다'는 조건이 짧은 시간 동안 여러 번 `true`가 될 수 있습니다.

    1.  `snapshotFlow`는 이 상태 변화를 `Flow` 스트림으로 변환합니다.
    2.  **`distinctUntilChanged()`** 연산자는 이 스트림에서 **연속으로 중복된 값을 제거**합니다. 예를 들어, `[false, true, true, true, false]`와 같은 스트림이 있다면, `[false, true, false]`로 변환됩니다.
    3.  **`filter { it }`** 연산자는 `true` 값만 통과시킵니다.

  * **효과:**
    이 조합을 통해, '다음 페이지를 로드하라'는 최종 신호(이벤트)는 **로딩 조건이 `false`에서 `true`로 바뀌는 단 한 번의 순간에만 발생**하게 됩니다. 이는 사용자의 변덕스러운 스크롤 동작에도 데이터 로딩 함수가 단 한 번만 호출되도록 보장하는 매우 견고한 방법입니다.

-----

### 3. 구현 예시 코드

아래 코드는 위 두 가지 전략을 모두 결합한 모범 사례입니다.

```kotlin
// ViewModel
class ListViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<Int>>(emptyList())
    val items: StateFlow<List<Int>> = _items

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private var currentPage = 0

    fun loadMoreItems() {
        // 1. 상태 플래그로 중복 요청 방지
        if (_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                delay(1000) // 네트워크 요청 시뮬레이션
                val newItems = List(20) { (currentPage * 20) + it }
                _items.value = _items.value + newItems
                currentPage++
            } finally {
                _isLoading.value = false
            }
        }
    }
}

// Composable
@Composable
fun PaginatedList(viewModel: ListViewModel) {
    val listState = rememberLazyListListState()
    val items by viewModel.items.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val totalItemsCount = listState.layoutInfo.totalItemsCount
            // 2. isLoading 플래그를 조건에 포함
            (lastVisibleItemIndex >= totalItemsCount - 5) && !isLoading
        }
    }

    LaunchedEffect(shouldLoadMore) {
        // 3. snapshotFlow와 연산자로 이벤트 중복 방지
        // shouldLoadMore가 true로 바뀌는 순간에만 viewModel.loadMoreItems() 호출
        if (shouldLoadMore) {
             viewModel.loadMoreItems()
        }
    }

    // LazyColumn 구현...
}
```

-----

### 4. 결론

사용자가 리스트를 빠르게 스크롤할 때 중복 데이터 로딩을 방지하기 위한 가장 견고한 전략은 두 가지를 함께 사용하는 것입니다.

1.  **상태 관리:** `ViewModel`에 **`isLoading` 상태 플래그**를 두어 이미 진행 중인 네트워크 요청이 있을 때 새로운 요청이 시작되지 않도록 막습니다.
2.  **이벤트 중복 제거:** UI에서는 **`snapshotFlow`와 `distinctUntilChanged()`** 를 사용하여 스크롤 위치에 따라 로딩을 시작하라는 "이벤트"가 단 한 번만 발생하도록 보장합니다.

이 두 가지 전략을 결합하면, 사용자의 어떤 스크롤 동작에도 효율적이고 안정적으로 반응하는 페이지네이션 시스템을 구축할 수 있습니다.