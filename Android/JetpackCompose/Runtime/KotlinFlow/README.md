# 컴포저블 함수 내에서 코틀린 Flow를 안전하게 수집하는 방법

컴포저블 함수 내에서 Flow를 수집하는 것은 UI 상태를 처리하고 데이터 변경에 반응하는 일반적인 방법입니다. 그러나 올바르게 처리되지 않으면 메모리 누수, 과도한 리컴포지션 또는 성능 문제로 이어질 수 있습니다. Compose에서 플로우를 안전하게 수집하기 위해, **`collectAsState`** 와 **`collectAsStateWithLifecycle`** 라는 두 가지 주요 접근 방식이 널리 사용됩니다. 이들의 차이점과 각각을 언제 사용해야 하는지 이해하는 것은 최적의 성능 및 생명주기 관리에 필수적입니다.

-----

## `collectAsState` 사용하기

`collectAsState`는 `Flow`를 수집하여 `State` 객체로 변환하는 편리한 API입니다. 이를 통해 수집된 데이터를 컴포저블 함수 내에서 직접 사용할 수 있으며, 플로우가 새로운 값을 방출(emit)할 때 리컴포지션을 트리거합니다.

```kotlin
@Composable
fun UserProfileScreen(viewModel: UserViewModel) {
    val userName by viewModel.userNameFlow.collectAsState(initial = "skydoves")

    Column {
        Text(text = "User: $userName")
    }
}
```

이 예시에서:

  * `viewModel`의 `userNameFlow`(`Flow`의 한 종류)가 상태로 수집됩니다.
  * `userNameFlow`가 새로운 값을 방출할 때마다 UI가 리컴포지션됩니다.
  * 관찰은 컴포지션 내에서 일어나고 컴포저블 함수가 컴포지션을 벗어나면 중지됩니다.

`collectAsState`는 많은 경우에 잘 작동하지만, **안드로이드 생명주기를 자동으로 존중하지는 않습니다.** `collectAsState`는 안드로이드에 종속되지 않는(Android-agnostic) API이므로, 안드로이드 생명주기에 대한 인식이 없습니다. 이는 컴포저블 함수가 메모리에 남아 있지만 활발하게 표시되지 않을 때(예: 사용자가 다른 화면으로 이동했을 때), **Flow는 여전히 수집되어 잠재적으로 불필요한 리소스 사용으로 이어질 수 있음**을 의미합니다.

-----

## `collectAsStateWithLifecycle` 사용하기

`collectAsStateWithLifecycle`은 **`Flow` 수집이 UI의 생명주기에 연결되도록 보장**하는 더 안전한 대안입니다. 컴포저블 함수가 포그라운드에 있지 않을 때 수집을 자동으로 일시 중지하여 불필요한 백그라운드 작업을 방지합니다. 이는 안드로이드 생명주기를 인식하도록 설계된 `androidx.lifecycle:lifecycle-runtime-compose` 패키지의 일부입니다.

```kotlin
@Composable
fun UserProfileScreen(viewModel: UserViewModel) {
    // lifecycle-runtime-compose 의존성 추가 필요
    val userName by viewModel.userNameFlow.collectAsStateWithLifecycle(initialValue = "skydoves")

    Column {
        Text(text = "User: $userName")
    }
}
```

#### `collectAsStateWithLifecycle`의 주요 장점:

  * 호스팅하는 액티비티나 프래그먼트의 **생명주기를 존중**합니다.
  * UI가 백그라운드에 있을 때(예: 화면 회전 또는 내비게이션 중) **수집이 자동으로 일시 중지**됩니다.
  * 더 이상 필요하지 않을 때 `Flow`가 계속 실행되는 것을 방지하여 **메모리 누수를 예방**합니다.

-----

## 올바른 접근 방식 선택하기

  * **`collectAsState` 사용:** 플로우 수집이 컴포저블이 컴포지션에 있는 동안 항상 활성 상태여야 할 때 사용합니다.
  * **`collectAsStateWithLifecycle` 사용:** 컴포저블이 보이지 않을 때 불필요한 작업을 피하기 위해 플로우 수집이 안드로이드 생명주기를 존중해야 할 때 사용합니다.

-----

## 요약

Compose에서 `Flow`를 안전하게 수집하려면 생명주기 인식을 올바르게 처리해야 합니다. `collectAsState`는 상태가 변경될 때 리컴포지션을 보장하지만 UI가 비활성 상태일 때 수집을 일시 중지하지 않습니다. 반면에, `collectAsStateWithLifecycle`은 UI가 백그라운드에 있을 때 수집을 일시 중지하여 불필요한 작업을 줄이고 메모리 누수를 방지합니다. 올바른 메서드를 선택하는 것은 연속적인 수집이 필요한지, 아니면 생명주기를 인식하는 `Flow` 처리가 필요한지에 따라 달라집니다. 이 주제에 대한 더 깊은 이해를 원한다면 [Consuming flows safely in Jetpack Compose](https://medium.com/androiddevelopers/consuming-flows-safely-in-jetpack-compose-cde014d0d5a3) (영문)를 확인해 보세요.

-----

## Q. `collectAsState`는 생명주기를 인식하지 못하고 UI가 보이지 않을 때도 수집을 계속하여 잠재적으로 메모리 누수를 유발할 수 있습니다. 이 문제를 어떻게 해결할 수 있나요?

`collectAsState`가 UI 생명주기를 인식하지 못하는 문제는 **`collectAsStateWithLifecycle` API를 사용**하여 해결하는 것이 가장 권장되는 방법입니다. 이 API는 UI가 `STARTED` 상태 이상일 때만 `Flow`를 수집하고, 백그라운드로 전환되면 수집을 자동으로 중지하여 리소스 낭비와 잠재적인 메모리 누수를 방지합니다.

-----

## 1. 해결책 1: `collectAsStateWithLifecycle` 사용 (권장) ✅

`collectAsState`의 생명주기 미인식 문제를 해결하기 위해 Jetpack 라이프사이클 팀에서 직접 제공하는 가장 이상적인 해결책입니다.

### 작동 방식

  * 이 함수는 `androidx.lifecycle:lifecycle-runtime-compose` 라이브러리에 포함되어 있습니다.
  * 내부적으로 컴포저블의 `LifecycleOwner`를 사용하여, 생명주기가 특정 상태(기본값: `Lifecycle.State.STARTED`)에 도달했을 때만 `Flow` 수집(collect)을 시작합니다.
  * 만약 앱이 백그라운드로 이동하는 등 생명주기가 `STARTED` 상태 미만으로 내려가면, `Flow` 수집은 자동으로 중단됩니다.
  * 이후 앱이 다시 포그라운드로 돌아와 생명주기가 `STARTED` 상태가 되면, 수집이 자동으로 재개됩니다.

### 사용 예시

`collectAsState`를 `collectAsStateWithLifecycle`로 교체하기만 하면 됩니다.

```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle // 임포트 필요

@Composable
fun MySafeScreen(viewModel: MyViewModel) {
    // ViewModel의 Flow를 생명주기 안전하게 수집
    val uiState by viewModel.myUiStateFlow.collectAsStateWithLifecycle()

    // uiState를 사용하여 UI 그리기
}
```

### 장점

  * **단순함:** 단 한 줄의 변경으로 생명주기 문제를 해결할 수 있습니다.
  * **안전함:** 불필요한 리소스 사용 및 메모리 누수 가능성을 근본적으로 차단합니다.
  * **효율성:** UI가 보이지 않을 때는 데이터 처리나 업데이트 작업을 수행하지 않으므로 CPU 및 배터리 사용량을 절약합니다.

-----

## 2. 해결책 2: `repeatOnLifecycle`을 사용한 수동 수집

`collectAsStateWithLifecycle`은 내부적으로 `LaunchedEffect`와 `repeatOnLifecycle` API를 조합한 편리한 래퍼(wrapper)입니다. 만약 더 세밀한 제어가 필요하거나 내부 동작을 이해하고 싶다면, 이 패턴을 직접 구현할 수도 있습니다.

### 작동 방식

1.  `LaunchedEffect`를 사용하여 컴포저블의 생명주기 동안 실행될 코루틴을 시작합니다.
2.  `repeatOnLifecycle(Lifecycle.State.STARTED)` 블록 내에서 `Flow`를 수집합니다.
3.  `repeatOnLifecycle`은 생명주기가 `STARTED` 상태에 진입하면 블록 내부의 코루틴(즉, `collect` 작업)을 시작하고, `STOPPED` 상태가 되면 코루틴을 자동으로 취소합니다.

### 사용 예시

```kotlin
@Composable
fun MyManualScreen(viewModel: MyViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current
    
    // 1. Flow를 기억하고, 초기값을 설정한 State<T>를 생성
    val uiState by remember(viewModel.myUiStateFlow, lifecycleOwner) {
        viewModel.myUiStateFlow.stateIn(
            scope = lifecycleOwner.lifecycleScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )
    }.collectAsState() // 이 collectAsState는 주로 초기값 표시와 타입 변환에 사용됨

    // 위 코드는 아래와 같이 더 명시적으로 작성할 수 있습니다.
    val uiState = remember { mutableStateOf(UiState.Loading) }
    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.myUiStateFlow.collect { state ->
                    uiState.value = state
                }
            }
        }
    }
}
```

> **참고:** 위 예시는 `collectAsStateWithLifecycle`이 내부적으로 얼마나 많은 상용구 코드를 줄여주는지 보여줍니다. 대부분의 경우, 이처럼 수동으로 구현할 필요 없이 `collectAsStateWithLifecycle`을 사용하는 것이 훨씬 간단하고 안전합니다.

-----

## 3. 결론: 어떤 방법을 선택해야 하는가?

**대부분의 안드로이드 UI 관련 시나리오에서는 `collectAsStateWithLifecycle`을 사용하는 것이 정답에 가깝습니다.**

이 함수는 Jetpack 팀이 `Flow`를 Compose UI에서 안전하게 사용하도록 특별히 설계한 API로, 코드의 복잡성을 크게 줄여주고 생명주기 관련 버그를 효과적으로 방지해 줍니다. 수동으로 `repeatOnLifecycle`을 사용하는 방식은 `Flow` 수집 외에 다른 생명주기 기반 작업을 동일한 코루틴 내에서 처리해야 하는 매우 특수한 경우에만 고려할 수 있습니다.