# 컴포저블 함수 내에서 코루틴 스코프를 안전하게 생성하는 방법

Jetpack Compose에서, 컴포저블 함수 내에 코루틴 스코프를 안전하게 생성하고 관리하는 권장 접근 방식은 [**`rememberCoroutineScope`**](https://developer.android.com/develop/ui/compose/side-effects#remembercoroutinescope) 를 사용하는 것입니다. 이는 코루틴 스코프가 컴포지션(composition)에 연결되도록 보장하여 잠재적인 메모리 누수와 부적절한 리소스 사용을 방지합니다.

-----

## 왜 `rememberCoroutineScope`를 사용해야 하나요?

Jetpack Compose의 `rememberCoroutineScope`는 **컴포지션을 인식하는(composition-aware) 코루틴 스코프**를 제공하여, 컴포저블이 컴포지션을 벗어날 때 활성 상태인 모든 코루틴을 **자동으로 취소**합니다. 이로 인해 수동으로 생명주기를 관리할 필요 없이 컴포저블 내에서 코루틴을 안전하게 시작할 수 있습니다.

### 사용 예시

```kotlin
@Composable
fun CounterWithReset() {
    var count by remember { mutableStateOf(0) }
    // 컴포지션에 연결된 코루틴 스코프를 가져옴
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Count: $count", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { count++ }) {
            Text("Increment")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            // 버튼 클릭 시 코루틴 시작
            coroutineScope.launch {
                // 리셋을 위한 지연 시뮬레이션
                delay(1000)
                count = 0
            }
        }) {
            Text("Reset After 1s")
        }
    }
}
```

-----

## 작동 방식

  * **컴포지션 인식 (Composition Awareness):**
    `rememberCoroutineScope`에 의해 생성된 코루틴 스코프는 컴포지션의 범위로 한정됩니다. 이는 해당 스코프 내에서 시작된 모든 코루틴이 컴포저블이 컴포지션에서 제거될 때 취소되도록 보장합니다.
  * **상태 관리 (State Management):**
    `remember` API는 리컴포지션 간에 유지되어야 하는 상태 값을 보유하고 관리하는 데 사용됩니다. `rememberCoroutineScope`와 함께 사용될 때, 비동기 작업을 안전하게 관리하는 데 도움이 됩니다.
  * **메모리 누수 방지 (Avoids Memory Leaks):**
    `GlobalScope`를 사용하거나 코루틴 스코프를 수동으로 관리하는 것과 달리, `rememberCoroutineScope`는 새로운 코루틴 스코프를 생성하고 컴포저블이 더 이상 사용되지 않을 때 리소스가 제대로 정리되도록 보장합니다.

-----

## 모범 사례

  * `rememberCoroutineScope`는 컴포지션 생명주기에 묶인 **가볍고, UI에 특화된 작업**에 사용하세요. 컴포지션의 범위를 넘어서는 더 오래 실행되거나 공유되는 작업의 경우, 적절한 생명주기 관리와 예기치 않은 취소를 피하기 위해 `viewModelScope`나 `lifecycleScope`와 같은 더 넓은 스코프를 사용하는 것을 선호하세요.
  * 컴포저블 내에서 직접 코루틴 스코프를 생성하는 것을 피하세요. 이는 수동 정리가 필요하며 메모리 누수를 유발할 수 있습니다.
  * `rememberCoroutineScope`를 사용할 때조차, 컴포저블 내부의 비동기 로직, 특히 비즈니스 로직과 관련된 것은 제한하고, 복잡한 작업은 더 나은 유지보수성과 성능을 위해 ViewModel이나 다른 아키텍처 계층에 위임하세요.

-----

## 요약

`rememberCoroutineScope`는 컴포저블 함수 내부에 코루틴 스코프를 생성하기 위한 유용하고 안전한 API입니다. 코루틴 스코프를 컴포지션에 연결함으로써, 리소스의 적절한 정리를 보장하고 메모리 누수를 방지합니다. 그러나 기본적으로 메인 스레드에서 시작되므로, 신중하게 사용하고 네트워크 요청이나 데이터베이스 쿼리와 같은 비즈니스 로직을 직접 실행하는 것을 피해야 합니다.

-----

## 💡 프로 팁: `rememberCoroutineScope` 내부 구현

`rememberCoroutineScope`를 사용하여 컴포저블 함수 내에서 안전하게 새로운 코루틴 스코프를 만드는 방법을 탐색했습니다. 이제 그것이 어떻게 작동하고 왜 컴포지션을 인식하는지 이해하기 위해 내부 구현을 더 깊이 파고들어 보겠습니다.

### `rememberCoroutineScope`의 내부 구현 코드

아래는 `rememberCoroutineScope`의 내부 구현입니다.

```kotlin
@Composable
inline fun rememberCoroutineScope(
    crossinline getContext: @DisallowComposableCalls () -> CoroutineContext =
        { EmptyCoroutineContext }
): CoroutineScope {
    val composer = currentComposer
    val wrapper = remember {
        CompositionScopedCoroutineScopeCanceller(
            createCompositionCoroutineScope(getContext(), composer)
        )
    }
    return wrapper.coroutineScope
}
```

이 함수는 `CompositionScopedCoroutineScopeCanceller`와 `createCompositionCoroutineScope`라는 두 가지 핵심 부분으로 구성됩니다.

### 1. `CompositionScopedCoroutineScopeCanceller`

`CompositionScopedCoroutineScopeCanceller` 클래스는 코루틴 스코프가 컴포지션 생명주기를 인식하도록 보장합니다. 그 구현은 다음과 같습니다.

```kotlin
internal class CompositionScopedCoroutineScopeCanceller(
    val coroutineScope: CoroutineScope
) : RememberObserver {
    override fun onRemembered() {
        // 할 일 없음
    }

    override fun onForgotten() {
        // 컴포지션을 떠날 때 스코프 취소
        coroutineScope.cancel(LeftCompositionCancellationException())
    }

    override fun onAbandoned() {
        // 컴포지션을 떠날 때 스코프 취소
        coroutineScope.cancel(LeftCompositionCancellationException())
    }
}
```

#### 주요 특징

  * **`RememberObserver` 구현:** 이를 통해 컴포저블의 생명주기를 추적할 수 있습니다.
  * **컴포지션 인식:** 컴포저블이 컴포지션에서 제거되면, `onForgotten` 또는 `onAbandoned` 메서드가 트리거되고, `coroutineScope.cancel()`을 사용하여 코루틴 스코프가 취소됩니다.
  * **안전한 정리:** 이는 스코프 내에서 시작된 모든 코루틴이 컴포저블이 컴포지션을 떠날 때 종료되도록 보장합니다.

### 2. `createCompositionCoroutineScope`

`createCompositionCoroutineScope` 함수는 주어진 코루틴 컨텍스트로 새로운 코루틴 스코프를 생성하는 책임을 집니다. 그 구현은 다음과 같습니다.

```kotlin
internal fun createCompositionCoroutineScope(
    coroutineContext: CoroutineContext,
    composer: Composer
) = if (coroutineContext[Job] != null) {
    // 부모 Job이 포함된 경우 예외 발생
    CoroutineScope(
        Job().apply {
            completeExceptionally(
                IllegalArgumentException(
                    "CoroutineContext supplied to " +
                        "rememberCoroutineScope may not include a parent job"
                )
            )
        }
    )
} else {
    // Composer의 컨텍스트와 새 Job을 결합하여 스코프 생성
    val applyContext = composer.applyCoroutineContext
    CoroutineScope(applyContext + Job(applyContext[Job]) + coroutineContext)
}
```

#### 주요 특징

  * **부모 Job 제한:** 제공된 `CoroutineContext`가 부모 Job을 포함하면 예외가 발생합니다. 이는 `rememberCoroutineScope`가 독립적인 스코프를 생성하도록 보장합니다.
  * **Job 관리:** 새로운 `Job`이 코루틴 컨텍스트에 추가되어 스코프를 관리합니다.
  * **Composer와의 통합:** 함수는 `composer.applyCoroutineContext`를 새로 생성된 `Job`과 결합하여 코루틴 스코프를 설정합니다.

### `rememberCoroutineScope`의 내부 작동 방식 요약

1.  **스코프 생성:** `createCompositionCoroutineScope`를 사용하여 새로운 코루틴 스코프가 생성되고 `CompositionScopedCoroutineScopeCanceller`에 전달됩니다.
2.  **컴포지션 인식:** `CompositionScopedCoroutineScopeCanceller`는 컴포지션 생명주기를 관찰합니다. 컴포저블이 컴포지션에서 제거되면 스코프는 메모리 누수를 방지하기 위해 취소됩니다.
3.  **안전한 사용:** 이 통합은 스코프 내에서 시작된 모든 코루틴이 컴포저블이 컴포지션을 나갈 때 자동으로 취소되도록 보장합니다.

### 요약

`rememberCoroutineScope` 함수는 컴포지션에 연결된, 컴포지션을 인식하는 코루틴 스코프를 생성합니다. 이는 `CompositionScopedCoroutineScopeCanceller`를 사용하여 컴포저블이 컴포지션에서 제거될 때 스코프를 취소하고, 리소스의 안전한 정리를 보장합니다. 그 내부 구현을 이해하면 Jetpack Compose가 UI 개발을 위해 어떻게 안전하고 효율적인 코루틴 관리를 제공하는지 알 수 있습니다.

-----

## Q. 컴포저블 내에서 직접 코루틴을 시작할 때의 위험 요소는 무엇이며, 이를 어떻게 피할 수 있나요?

Jetpack Compose 환경에서 컴포저블 함수 내부에 코루틴을 잘못 시작하면, 예측하기 어려운 버그, 메모리 누수, 그리고 심각한 성능 저하를 유발할 수 있습니다. 따라서 코루틴을 안전하게 사용하는 방법을 이해하는 것은 매우 중요합니다.

-----

### 1. 컴포저블 내에서 직접 코루틴을 시작할 때의 주요 위험 요소

#### 1.1. 코루틴 누수 및 리소스 낭비 (잘못된 스코프 사용)

  * **위험:** 컴포저블 내에서 컴포지션의 생명주기와 연결되지 않은 코루틴 스코프(예: `GlobalScope` 또는 수동으로 생성한 `CoroutineScope(Dispatchers.Default)`)를 사용하여 코루틴을 시작하는 경우입니다.
  * **문제점:** 컴포저블은 리컴포지션 과정에서 화면에서 사라지거나 다른 화면으로 대체될 수 있습니다. 하지만 `GlobalScope` 등으로 시작된 코루틴은 해당 컴포저블이 사라져도 **계속해서 실행**됩니다.
  * **결과:**
      * **리소스 낭비:** 더 이상 필요 없는 UI를 위해 백그라운드에서 계속 네트워크, CPU, 메모리 자원을 소모합니다.
      * **메모리 누수:** 실행 중인 코루틴이 액티비티나 프래그먼트의 컨텍스트(context) 또는 다른 UI 관련 객체에 대한 참조를 가지고 있다면, 가비지 컬렉터(GC)가 이를 수거하지 못해 심각한 메모리 누수가 발생합니다.
      * **비정상 종료:** 코루틴이 작업을 마친 후, 이미 파괴된 UI를 업데이트하려고 시도하여 `Exception`을 발생시키고 앱이 비정상 종료될 수 있습니다.

#### 1.2. 의도치 않은 반복 실행 (리컴포지션 시 부작용)

  * **위험:** `LaunchedEffect`와 같은 부작용 핸들러 없이, 컴포저블 함수의 본문에 직접 `scope.launch { ... }`와 같은 코루틴 시작 코드를 작성하는 경우입니다.
  * **문제점:** 컴포저블 함수는 상태 변경에 따라 초당 여러 번도 재실행(리컴포지션)될 수 있습니다. `scope.launch` 코드가 함수 본문에 직접 위치하면, **리컴포지션이 일어날 때마다 새로운 코루틴이 계속해서 실행**됩니다.
  * **결과:**
      * **리소스 과부하:** 순식간에 수백, 수천 개의 코루틴이 실행되어 시스템에 과부하를 주고, 중복된 네트워크 요청을 보내며, 결국 앱을 비정상 종료시킬 수 있습니다.
      * **오작동:** 앱의 로직이 예측 불가능하게 여러 번 실행되어 잘못된 동작을 유발합니다.

-----

### 2. 이러한 위험을 피하고 안전하게 코루틴을 사용하는 방법

이러한 위험을 피하기 위해, Jetpack Compose는 컴포저블의 생명주기와 코루틴을 안전하게 연결하는 두 가지 주요 메커니즘을 제공합니다.

#### 2.1. 해결책 1: 생명주기를 인식하는 스코프 사용 (`rememberCoroutineScope`)

  * **사용 시점:** **사용자 이벤트 핸들러**(예: `onClick`, `onLongClick` 등) 내부에서 코루틴을 시작해야 할 때 사용합니다. 이 코루틴은 특정 상태를 변경하거나, 스크롤을 이동시키는 등 UI와 관련된 단발성 작업을 수행합니다.
  * **안전한 이유:** `rememberCoroutineScope`는 컴포지션에 연결된 코루틴 스코프를 반환합니다. 이 스코프는 **해당 컴포저블이 컴포지션을 벗어나면(즉, 화면에서 사라지면) 자동으로 취소**됩니다. 따라서 이 스코프에서 시작된 모든 코루틴은 컴포저블이 사라질 때 함께 정리되어 리소스 누수나 불필요한 작업을 방지합니다.
  * **올바른 사용 예시:**
    ```kotlin
    @Composable
    fun MyButtonWithCoroutine() {
        val scope = rememberCoroutineScope() // 컴포지션에 연결된 스코프 가져오기

        Button(onClick = {
            // onClick 콜백은 리컴포지션 시마다 실행되지 않음.
            // 따라서 이 안에서 코루틴을 시작하는 것은 안전함.
            scope.launch {
                // 네트워크 요청이나 데이터베이스 작업 등
                val result = performBackgroundTask()
                updateUi(result)
            }
        }) {
            Text("작업 시작")
        }
    }
    ```

#### 2.2. 해결책 2: 부작용(Side-Effect) 핸들러 사용 (`LaunchedEffect`)

  * **사용 시점:** 컴포저블이 **화면에 처음 나타났을 때** 또는 **특정 상태(key) 값이 변경되었을 때** `suspend` 함수나 코루틴을 **단 한 번** 실행하고 싶을 때 사용합니다.
  * **안전한 이유:** `LaunchedEffect`는 컴포저블의 생명주기에 맞춰 코루틴의 실행과 취소를 자동으로 관리합니다.
      * `LaunchedEffect(Unit) { ... }` 또는 `LaunchedEffect(true) { ... }`: 컴포저블이 처음 컴포지션에 추가될 때 블록 내부 코드를 한 번만 실행하고, 컴포지션을 벗어나면 코루틴을 취소합니다.
      * `LaunchedEffect(key1) { ... }`: `key1`의 값이 변경될 때마다 기존 코루틴을 취소하고 새로운 코루틴을 시작합니다.
  * **올바른 사용 예시:**
    ```kotlin
    @Composable
    fun UserProfileScreen(userId: String, viewModel: MyViewModel) {
        // userId가 변경될 때마다 사용자 데이터를 다시 불러옴
        LaunchedEffect(userId) {
            viewModel.loadUserData(userId)
        }

        // ... UI ...
    }
    ```

-----

### 3. 결론

컴포저블 내에서 코루틴을 안전하게 시작하기 위한 핵심 규칙은 다음과 같습니다.

> **컴포저블 함수 본문에서 직접 코루틴을 시작하지 말고, 항상 생명주기를 인식하는 메커니즘을 사용해야 합니다.**

  * **사용자 이벤트(클릭 등)에 대한 응답**으로 코루틴을 시작하려면 **`rememberCoroutineScope`** 를 사용하세요.
  * **컴포지션 진입 또는 특정 상태 값의 변경에 대한 응답**으로 코루틴이나 `suspend` 함수를 실행하려면 **`LaunchedEffect`** 를 사용하세요.

이 두 가지 방법을 올바르게 사용하면, 리컴포지션 과정에서 발생하는 여러 위험을 피하고 안정적이며 성능이 뛰어난 Compose 애플리케이션을 만들 수 있습니다.