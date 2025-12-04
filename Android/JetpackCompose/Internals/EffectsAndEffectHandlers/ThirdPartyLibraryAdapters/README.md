# 서드 파티 라이브러리 어댑터 (Third Party Library Adapters)

Jetpack Compose에서 **Observable**, **Flow**, **LiveData**와 같은 서드 파티 라이브러리의 데이터 유형을 Compose State로 변환하는 방법을 알아봅니다.

---

## 어댑터 종속성

Jetpack Compose는 가장 자주 사용되는 서드 파티 유형에 대한 어댑터를 제공합니다. 사용하려는 라이브러리에 따라 다음 종속성을 추가해야 합니다.

```kotlin
// Flow 어댑터 포함
implementation "androidx.compose.runtime:runtime:$compose_version"

// LiveData 어댑터
implementation "androidx.compose.runtime:runtime-livedata:$compose_version"

// RxJava2 어댑터
implementation "androidx.compose.runtime:runtime-rxjava2:$compose_version"
```

---

## 어댑터의 동작 원리

> 모든 어댑터는 **이펙트 핸들러**에 위임하여 동작합니다.

어댑터의 내부 동작 흐름:
1. 서드 파티 라이브러리 API를 사용하여 **옵저버를 등록**
2. 방출된 모든 요소를 **MutableState**로 매핑
3. 어댑터 함수를 통해 **불변의 State**로 노출

```mermaid
flowchart LR
    A[서드 파티 데이터 소스] --> B[어댑터 함수]
    B --> C[MutableState로 매핑]
    C --> D[불변 State로 노출]
    D --> E[Composable에서 사용]
```

---

## LiveData 어댑터

`observeAsState()`를 사용하여 LiveData를 Compose State로 변환합니다.

```kotlin
class MyComposableVM : ViewModel() {
  private val _user = MutableLiveData(User("John"))
  val user: LiveData<User> = _user
  //...
}

@Composable
fun MyComposable() {
  val viewModel = viewModel<MyComposableVM>()

  val user by viewModel.user.observeAsState()

  Text("Username: ${user?.name}")
}
```

### 내부 구현

`observeAsState`의 구현은 **DisposableEffect** 핸들러에 의존합니다.

- [observeAsState 소스 코드](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/runtime/runtime-livedata/src/main/java/androidx/compose/runtime/livedata/LiveDataAdapter.kt)

---

## RxJava2 어댑터

`subscribeAsState()`를 사용하여 Observable을 Compose State로 변환합니다.

```kotlin
class MyComposableVM : ViewModel() {
  val user: Observable<ViewState> = Observable.just(ViewState.Loading)
  //...
}

@Composable
fun MyComposable() {
  val viewModel = viewModel<MyComposableVM>()

  val uiState by viewModel.user.subscribeAsState(ViewState.Loading)

  when (uiState) {
    ViewState.Loading -> TODO("Show loading")
    ViewState.Error -> TODO("Show Snackbar")
    is ViewState.Content -> TODO("Show content")
  }
}
```

### 내부 구현

- [subscribeAsState 소스 코드](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/runtime/runtime-rxjava2/src/main/java/androidx/compose/runtime/rxjava2/RxJava2Adapter.kt)
- **Flowable**에도 동일한 확장 함수가 제공됩니다.

---

## KotlinX Coroutines Flow 어댑터

`collectAsState()`를 사용하여 Flow를 Compose State로 변환합니다.

```kotlin
class MyComposableVM : ViewModel() {
  val user: Flow<ViewState> = flowOf(ViewState.Loading)
  //...
}

@Composable
fun MyComposable() {
  val viewModel = viewModel<MyComposableVM>()

  val uiState by viewModel.user.collectAsState(ViewState.Loading)

  when (uiState) {
    ViewState.Loading -> TODO("Show loading")
    ViewState.Error -> TODO("Show Snackbar")
    is ViewState.Content -> TODO("Show content")
  }
}
```

### 내부 구현

`collectAsState`의 구현은 다른 어댑터들과 조금 다릅니다.

- Flow는 **일시 중단(suspend) 컨텍스트**에서 소비되어야 하기 때문에
- `LaunchedEffect`에 위임하는 **`produceState`** 에 의존합니다.

- [collectAsState 소스 코드](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/runtime/runtime/src/commonMain/kotlin/androidx/compose/runtime/SnapshotFlow.kt)

---

## 어댑터 비교

| 라이브러리 | 어댑터 함수 | 의존하는 이펙트 핸들러 |
|-----------|------------|---------------------|
| **LiveData** | `observeAsState()` | `DisposableEffect` |
| **RxJava2** | `subscribeAsState()` | `DisposableEffect` |
| **Flow** | `collectAsState()` | `produceState` → `LaunchedEffect` |

---

## 커스텀 어댑터 작성

통합하려는 라이브러리가 있을 경우, 동일한 패턴을 따라 **자신만의 어댑터를 쉽게 작성**할 수 있습니다.

커스텀 어댑터 작성 시 고려 사항:
- 적절한 **이펙트 핸들러** 선택 (일시 중단 여부에 따라)
- **구독/해제** 로직 구현
- **MutableState**를 통한 값 업데이트

---

## 요약

- Jetpack Compose는 **LiveData**, **RxJava2**, **Flow**를 위한 공식 어댑터를 제공한다.
- 모든 어댑터는 내부적으로 **이펙트 핸들러**(`DisposableEffect`, `LaunchedEffect`)에 위임한다.
- `observeAsState()`, `subscribeAsState()`, `collectAsState()` 함수로 서드 파티 데이터를 **Compose State**로 변환한다.
- Flow 어댑터는 일시 중단 컨텍스트가 필요하므로 **`produceState`** 를 통해 `LaunchedEffect`에 위임한다.
- 동일한 패턴을 따라 **커스텀 어댑터**를 쉽게 작성할 수 있다.
