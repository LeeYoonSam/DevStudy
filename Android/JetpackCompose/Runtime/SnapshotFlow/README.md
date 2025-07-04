# snapshotFlow란 무엇인가?

[`snapshotFlow`](https://developer.android.com/develop/ui/compose/side-effects#snapshotFlow)는 Compose가 내부적으로 상태 변경을 효율적으로 관리하고 관찰하는 데 사용하는 **스냅샷 시스템(Snapshot system)** 내의 변경 사항을 관찰하는 함수입니다. 관찰 중인 상태가 변경될 때마다, Flow는 업데이트된 값을 방출(emit)합니다.

간단히 말해, `snapshotFlow`는 Compose의 상태 변경을 수신하여 이를 `Flow`로 내보내, 개발자가 상태 업데이트를 관리하고 이에 반응하기 위해 표준 코루틴 기반 작업을 사용할 수 있게 해줍니다.

### `snapshotFlow`의 주요 특징

  * **상태 관찰:** Compose의 스냅샷 시스템을 사용하여 상태 변경을 수신합니다.
  * **스레드 안전성:** Compose의 스냅샷 격리(snapshot isolation) 내에서 상태 읽기 및 방출이 발생하도록 보장하여 경쟁 조건(race condition)을 피합니다.
  * **유휴 상태 건너뛰기:** 상태 값이 변경될 때만 값이 방출되고 유휴 리컴포지션(idle recomposition) 중에는 업데이트를 건너뛰도록 보장합니다.
  * **취소 인식:** Flow를 수집(collect)하는 코루틴이 취소되면 관찰을 자동으로 취소하여 컴포지션을 인식합니다.

-----

### `snapshotFlow` 사용 시점

  * **코루틴과 연동:** Compose의 상태를 코루틴 플로우와 연결하여 변환, 플로우 결합, 또는 업데이트 조절(throttling)과 같은 고급 작업을 수행해야 할 때.
  * **UI와 관련 없는 부작용:** 분석 이벤트 전송이나 백엔드 호출 트리거와 같이 UI에 직접적으로 묶여서는 안 되는 작업을 수행할 때.

-----

### `snapshotFlow` 사용 방법: 페이지네이션 예시

또 다른 시나리오는 사용자가 목록을 스크롤할 때 페이지네이션을 처리하는 것입니다. `snapshotFlow`를 사용하여 필요할 때만 추가 데이터를 로드할 수 있습니다. 이 접근 방식은 `shouldLoadMore` 조건이 `true`로 평가될 때만 페이지네이션 요청이 전송되도록 보장하여, 동일한 아이템 세트에 대한 중복 요청을 방지합니다.

```kotlin
// 1. LazyColumn의 스크롤 상태를 기억
val listState = rememberLazyListState()
val threshold = 2 // 마지막에서 2개 아이템이 보이면 다음 페이지 로드

LazyColumn(state = listState) {
    // ... items ...
}

// 2. 현재 로딩 상태를 ViewModel에서 관찰
val isLoadingShorts by shortsViewModel.isLoadingShorts.collectAsStateWithLifecycle()

// 3. 페이지를 더 로드해야 하는지 여부를 결정하는 파생 상태
val shouldLoadMore by remember {
    derivedStateOf {
        val totalItemsCount = listState.layoutInfo.totalItemsCount
        val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
        (lastVisibleItemIndex + threshold >= totalItemsCount) && !isLoadingShorts
    }
}

// 4. listState가 변경될 때마다 재실행
LaunchedEffect(listState) {
    // 'shouldLoadMore'의 변경 사항을 관찰하는 Flow 생성
    snapshotFlow { shouldLoadMore }
        .map { shouldLoad -> shouldLoad } // 로딩 조건을 직접 매핑
        .distinctUntilChanged() // 값이 실제로 변경될 때만 방출
        .filter { it } // 'shouldLoadMore'가 true일 때만 계속 진행
        .collect {
            // 추가 아이템을 로드하기 위한 페이지네이션 이벤트 트리거
            shortsViewModel.handleShortsEvent(event = ShortsEvent.LoadMore)
        }
}
```

이 예시는 `snapshotFlow`를 사용하여 `LazyColumn`의 스크롤 상태를 모니터링하고, 특정 아이템(전체 아이템 수 - 임계값)이 화면에 보이면 추가 아이템을 로드하는 이벤트를 트리거합니다. `map`, `distinctUntilChanged`, `filter`와 같은 연산자는 추가 이벤트가 트리거되어야 하는지 결정하는 데 사용되어, 스크롤 동작당 이벤트가 한 번만 발생하도록 보장합니다.

-----

### `snapshotFlow`의 내부 작동 원리

1.  제공된 람다를 **스냅샷 옵저버(Snapshot observer)** 내에서 평가합니다.
2.  람다가 어떤 상태 변수에 접근하면, Compose는 **의존성을 등록**합니다.
3.  상태가 변경되면, Compose는 `snapshotFlow`에 이를 **알리고**, 새로운 값을 방출하도록 트리거합니다.

-----

### 사용 시 유의사항

`snapshotFlow`를 사용할 때 다음 사항을 명심하세요.

  * **스레드 안전성 및 수집:** 수집된 플로우가 컴포지션 외부에서 수집되는 것을 피하고 메모리 누수를 방지하기 위해, 일반적으로 `LaunchedEffect`를 사용하여 코루틴 스코프 내에서 처리되도록 보장해야 합니다.
  * **방출 빈도:** `snapshotFlow`는 람다 내에서 읽는 스냅샷 상태가 변경될 때마다 값을 방출하므로, 빈번하게 발생할 수 있습니다. `distinctUntilChanged()`나 `debounce()`와 같은 연산자를 사용하여 불필요한 방출을 줄이고 성능을 최적화하세요.
  * **스냅샷 격리:** 플로우는 Compose의 스냅샷 시스템을 존중하며, 이는 값이 격리된 스냅샷을 기반으로 방출됨을 의미합니다. 다른 플로우나 `suspend` 함수와 결합할 때는 스냅샷 생명주기 내에서 예상대로 동작하는지 신중해야 합니다.

-----

### 요약

`snapshotFlow`는 상태 관찰 시스템을 코틀린의 코루틴 플로우와 연결하는 부작용 핸들러 API 중 하나입니다. 이를 통해 고급 코루틴 작업을 사용하여 Compose의 상태 변경에 반응할 수 있으므로, 분석, 데이터 동기화 및 기타 UI와 관련 없는 이펙트와 같은 시나리오에 이상적입니다. 효과적으로 사용하는 방법을 이해하면 Compose 애플리케이션에서 더 고급 사용 사례를 구현할 수 있습니다.

-----

## Q. 어떤 시나리오에서 ViewModel의 Flow를 직접 관찰하는 대신 `snapshotFlow`를 사용하는 것을 선호하며, 그 방출 동작을 어떻게 최적화하겠습니까?

`snapshotFlow`는 **Compose UI 내부의 상태(`State`)** 를 코틀린 `Flow`로 변환하여, 코루틴의 강력한 스트림 처리 기능을 활용하고 싶을 때 사용합니다. 반면, `ViewModel`의 `Flow`를 직접 관찰하는 것은 **UI 외부(데이터 또는 비즈니스 계층)에서 비롯된 상태**를 UI에 반영하기 위한 표준적인 방법입니다.

따라서 어떤 것을 사용할지는 **관찰하고자 하는 데이터의 출처가 어디인가**에 따라 결정됩니다.

-----

## 1. ViewModel의 Flow 대신 `snapshotFlow`를 사용하는 시나리오

`ViewModel`의 `Flow`는 아키텍처상 상위 계층의 데이터를 UI로 전달하는 단방향 데이터 흐름에 적합합니다. 하지만 다음과 같이 **UI 자체의 상태 변화로부터** 비동기적인 로직을 파생시켜야 할 때 `snapshotFlow`가 매우 유용합니다.

### 1.1. UI 상태 변화에 따른 non-UI 부작용 처리 📊

  * **시나리오:** 사용자가 `LazyColumn`을 스크롤할 때, 특정 아이템이 화면에 보이면 분석(analytics) 이벤트를 전송해야 하는 경우.
  * **이유:** 스크롤 위치(`LazyListState`)는 순전히 UI에 속한 상태이며, `ViewModel`은 이 상태를 직접 알지 못하는 것이 좋은 설계입니다. `snapshotFlow`를 사용하면 컴포저블 내에서 `LazyListState`의 변화를 `Flow`로 변환하여, 특정 조건이 충족되었을 때(예: `firstVisibleItemIndex > 10`) 분석 이벤트를 보내는 로직을 깔끔하게 처리할 수 있습니다.

### 1.2. 여러 Compose 상태를 결합하여 이벤트 트리거 🔗

  * **시나리오:** 여러 `TextField`의 입력값과 `Checkbox`의 체크 상태 등, **UI 내부에 `remember`로 관리되는 여러 상태**가 모두 특정 조건을 만족했을 때만 "전송" 버튼을 활성화하거나 특정 API를 호출하고 싶을 때.
  * **이유:** 이 모든 UI 상태를 `ViewModel`로 끌어올리는(hoist) 것은 과도한 상용구 코드를 유발할 수 있습니다. `snapshotFlow`를 사용하면 이러한 여러 상태들을 하나의 `Flow`로 묶어 관찰하고, 결합된 상태가 조건을 만족할 때 이벤트를 한 번만 트리거할 수 있습니다.

### 1.3. Flow 연산자를 UI 상태에 적용 (디바운싱 등) ⏳

  * **시나리오:** 사용자가 검색창(`TextField`)에 텍스트를 입력할 때, **타이핑이 멈춘 후에만** (예: 300ms 후) 검색 API를 호출하고 싶을 때 (디바운싱, Debouncing).
  * **이유:** `TextField`의 `value`는 `State<String>`으로, 매우 빈번하게 변경됩니다. 이 상태를 `snapshotFlow`로 감싸 `Flow`로 변환하면, **`debounce()`** 와 같은 강력한 `Flow` 연산자를 적용할 수 있습니다.
    ```kotlin
    @Composable
    fun SearchBar(onSearch: (String) -> Unit) {
        var query by remember { mutableStateOf("") }

        // query 상태가 변경될 때마다 Flow가 방출됨
        LaunchedEffect(Unit) {
            snapshotFlow { query }
                .debounce(300L) // 300ms 동안 추가 변경이 없으면 값을 통과시킴
                .distinctUntilChanged() // 이전과 동일한 값은 무시
                .collect { searchQuery ->
                    onSearch(searchQuery) // 최적화된 검색 실행
                }
        }

        TextField(value = query, onValueChange = { query = it })
    }
    ```

-----

## 2. `snapshotFlow`의 방출(Emission) 동작 최적화 방법

`snapshotFlow`는 관찰하는 상태가 변경될 때마다 값을 방출하므로, 특히 스크롤과 같이 빈번한 상태 변경 시 불필요한 작업이 실행될 수 있습니다. 이를 최적화하기 위해 다음과 같은 `Flow` 연산자를 함께 사용하는 것이 일반적입니다.

### `distinctUntilChanged()`: 중복 방출 방지

  * **역할:** `Flow`에서 연속으로 동일한 값이 방출되는 것을 막아줍니다. `snapshotFlow`는 리컴포지션 중에 상태 값이 실제로는 변경되지 않았더라도 재평가되어 동일한 값을 다시 방출할 수 있습니다. `distinctUntilChanged()`는 이러한 중복을 제거하여 다운스트림(downstream)의 작업이 꼭 필요할 때만 실행되도록 보장합니다.

### `debounce(timeoutMillis)`: 빈번한 변경 그룹화

  * **역할:** 위 검색창 예시처럼, 값이 매우 빠르게 연속적으로 변경될 때 사용됩니다. 지정된 시간(`timeoutMillis`) 동안 새로운 값이 들어오지 않으면, 가장 마지막 값을 방출합니다. 사용자의 입력이 멈추기를 기다리는 시나리오에 적합합니다.

### `filter { ... }`: 조건에 맞는 값만 처리

  * **역할:** 특정 조건을 만족하는 값만 통과시킵니다. 예를 들어, `snapshotFlow`를 사용하여 페이지네이션을 구현할 때, "다음 페이지를 로드해야 한다"는 조건이 `true`가 되는 순간만 포착하기 위해 `filter { it }`을 사용할 수 있습니다.

-----

## 3. 결론

  * **`ViewModel`의 `Flow`를 직접 관찰하는 경우:** **비즈니스 로직이나 데이터 계층에서 비롯된 상태**를 UI에 반영할 때 사용합니다. 이는 단방향 데이터 흐름의 표준적인 방식입니다.
  * **`snapshotFlow`를 사용하는 경우:** **컴포저블 내부의 UI 상태(예: 스크롤 위치, 텍스트 필드 값 등)의 변화를 기반으로** 복잡한 비동기 로직이나 부작용을 트리거하고 싶을 때 사용합니다. 이는 UI 상태를 `Flow`의 강력한 연산자 세계와 연결하는 다리 역할을 합니다.

상황에 맞는 도구를 선택하고, `snapshotFlow` 사용 시에는 `distinctUntilChanged`나 `debounce`와 같은 연산자로 방출 동작을 최적화하여 효율적이고 예측 가능한 코드를 작성하는 것이 중요합니다.