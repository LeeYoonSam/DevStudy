# 컴포저블 함수 내에서 부작용(Side-Effect)을 처리하는 방법

Jetpack Compose는 컴포저블의 리컴포지션 스코프(recomposition scope) 외부에서 작업을 처리할 수 있도록 여러 [**부작용 핸들러(side-effect handler) API**](https://developer.android.com/develop/ui/compose/side-effects)를 제공합니다. 이러한 API들은 안드로이드 프레임워크와 상호작용하거나, 컴포지션 이벤트를 관리하거나, 상태 변경에 기반하여 이펙트를 트리거하는 시나리오를 다루는 데 필수적입니다. 세 가지 주요 부작용 핸들러 API인 `LaunchedEffect`, `DisposableEffect`, `SideEffect`에 대해 탐색해 보겠습니다.

-----

## 주요 부작용 핸들러 API

### 1\. `LaunchedEffect`: 컴포저블 스코프에서 `suspend` 함수 실행하기

`LaunchedEffect`는 컴포저블의 컴포지션 내에서 실행되는 코루틴을 시작하는 데 사용됩니다. 이 코루틴은 `LaunchedEffect`에 전달된 **`key`가 변경되면 취소되고 다시 시작**됩니다. 이는 컴포저블이 컴포지션에 진입할 때 발생해야 하는 데이터 가져오기, 애니메이션 시작, 또는 이벤트 수신과 같은 작업에 유용합니다.

#### 주요 특징

  * 컴포저블이 컴포지션에 진입할 때 **한 번 실행**됩니다.
  * `key`가 변경되면 **자동으로 취소되고 다시 시작**됩니다.
  * **컴포지션을 인식**하므로, 컴포저블이 컴포지션을 벗어나면 자동으로 취소됩니다.

예를 들어, 사용자가 목록의 아이템을 클릭할 때 네트워크에서 추가 데이터를 가져와야 한다면, `LaunchedEffect`를 사용하여 이를 원활하게 처리할 수 있습니다. 이는 `LaunchedEffect`에 제공된 키 중 하나라도 변경될 때마다 작업이 다시 실행될 수 있도록 보장합니다.

```kotlin
var selectedPoster: Poster? by remember { mutableStateOf(null) }

// selectedPoster 값이 변경될 때마다 LaunchedEffect 블록이 다시 실행됩니다.
LaunchedEffect(key1 = selectedPoster) {
    // 네트워크에서 추가 정보를 가져오기 위해 ViewModel에 이벤트를 보냅니다.
    selectedPoster?.let { poster ->
        viewModel.fetchPosterDetails(poster.id)
    }
}
```

`LaunchedEffect`를 사용하여 `Flow`를 안전하게 관찰할 수도 있습니다. `LaunchedEffect`에 의해 시작된 코루틴은 컴포저블 함수가 컴포지션을 벗어날 때 자동으로 취소되어 불필요한 리소스 사용을 방지합니다. 또한, 리컴포지션 중에는 코루틴이 다시 시작되지 않습니다. 이펙트가 호출 위치의 생명주기와 일치하도록 보장하려면, `Unit`이나 `true`와 같은 상수를 `key` 파라미터로 전달할 수 있습니다. 이는 키가 변경되지 않는 한 이펙트가 단 한 번만 실행되도록 보장합니다.

```kotlin
LaunchedEffect(key1 = Unit) {
    stateFlow
       .distinctUntilChanged()
       .filter { it.marked }
       .collect { .. }
}
```

### 2\. `DisposableEffect`: 정리가 필요한 이펙트

`DisposableEffect`는 컴포저블의 컴포지션에 바인딩된 **리소스나 정리(cleanup) 작업을 관리**하는 데 사용됩니다. `LaunchedEffect`와 달리, `DisposableEffect`는 컴포저블이 컴포지션을 벗어날 때 리소스를 해제하기 위한 `onDispose` 람다를 `DisposableEffectScope`와 함께 제공합니다.

#### 주요 특징

  * 리스너, 옵저버, 또는 구독(subscription)과 같은 리소스를 관리하는 데 이상적입니다.
  * `onDispose` 콜백을 통해 적절한 정리를 보장합니다.

예를 들어, 생명주기 이벤트에 따라 분석 이벤트를 보내야 하는 경우, `LifecycleObserver`를 사용하여 이를 달성할 수 있습니다. `DisposableEffect`를 사용하여 컴포저블이 컴포지션에 진입할 때 옵저버를 등록하고, 컴포저블이 컴포지션을 벗어날 때 자동으로 등록 해제할 수 있습니다. 이는 적절한 정리를 보장하고 메모리 누수를 방지합니다.

```kotlin
@Composable
fun HomeScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onStart: () -> Unit, // '시작됨' 분석 이벤트를 보내는 람다
    onStop: () -> Unit // '중지됨' 분석 이벤트를 보내는 람다
) {
    // 새로운 람다가 제공될 때 현재 람다들을 안전하게 업데이트
    val currentOnStart by rememberUpdatedState(onStart)
    val currentOnStop by rememberUpdatedState(onStop)

    // 만약 `lifecycleOwner`가 변경되면, 이펙트를 해제하고 재설정
    DisposableEffect(lifecycleOwner) {
        // 기억된 콜백을 트리거하는 옵저버 생성
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                currentOnStart()
            } else if (event == Lifecycle.Event.ON_STOP) {
                currentOnStop()
            }
        }

        // 생명주기에 옵저버 추가
        lifecycleOwner.lifecycle.addObserver(observer)

        // 이펙트가 컴포지션을 떠날 때(onDispose), 옵저버 제거
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
```

### 3\. `SideEffect`: Compose 상태를 non-Compose 코드로 발행하기

`SideEffect`는 **매 리컴포지션이 성공적으로 완료된 직후에 실행되어야 하는 작업**을 위해 사용됩니다. 컴포저블이 리컴포지션된 후에 실행이 보장되므로, ViewModel의 UI 상태나 외부 라이브러리와 같이 컴포지션의 일부가 아닌 외부 시스템과 Compose 상태를 동기화하는 데 적합합니다.

#### 주요 특징

  * 매 성공적인 리컴포지션 이후에 실행됩니다.
  * non-Compose 컴포넌트와의 상태 동기화에 유용합니다.

예를 들어, 분석 라이브러리가 후속 분석 이벤트에 사용자 정의 메타데이터(예: "사용자 속성")를 첨부하여 사용자 집단을 세분화하는 것을 지원한다면, `SideEffect`를 사용하여 현재 사용자의 `userType`이 원활하게 업데이트되도록 보장할 수 있습니다. 이 접근 방식은 라이브러리의 상태가 Compose 애플리케이션의 현재 상태와 동기화된 상태를 유지하도록 보장합니다.

```kotlin
@Composable
fun rememberFirebaseAnalytics(user: User): FirebaseAnalytics {
    val analytics: FirebaseAnalytics = remember {
        FirebaseAnalytics.getInstance()
    }

    // 매 성공적인 컴포지션마다, FirebaseAnalytics에 현재 User의
    // userType을 업데이트하여, 향후 분석 이벤트에 이 메타데이터가
    // 첨부되도록 보장합니다.
    SideEffect {
        analytics.setUserProperty("userType", user.userType)
    }
    return analytics
}
```

`SideEffect`를 사용하는 또 다른 예는 아래와 같이 리컴포지션이 완료된 후에만 Lottie 애니메이션을 시작하거나 non-Composable 액션을 트리거해야 할 때입니다.

```kotlin
SideEffect {
    lottieAnimationView.playAnimation() // 가장 최근의 리컴포지션 이후에만 실행
}
```

-----

## 요약

각 부작용 핸들러 API는 다음과 같이 별개의 목적을 수행합니다.

  * **`LaunchedEffect`**: 코루틴 기반 작업을 시작하거나 `key` 파라미터 변경에 따라 작업을 다시 시작하는 데 사용합니다.
  * **`DisposableEffect`**: 컴포저블의 컴포지션에 묶인 리소스를 관리하고 정리하는 데 사용합니다.
  * **`SideEffect`**: 매 리컴포지션 직후에 적용되어야 하는 작업을 실행하고 외부 시스템을 Compose 상태와 동기화하는 데 사용합니다.

이러한 부작용 핸들러 API를 언제 어떻게 사용해야 하는지 이해하면, 깔끔하고 선언적인 접근 방식을 유지하면서 부작용을 효과적으로 관리하는 데 도움이 될 수 있습니다. 세 가지 주요 부작용 처리 API와 그 내부 작동 방식에 대한 더 깊은 이해를 원한다면, [Understanding the Internals of Side-Effect Handlers in Jetpack Compose](https://proandroiddev.com/understanding-the-internals-of-side-effect-handlers-in-jetpack-compose-d55fbf914fde) (영문)를 확인해 보세요.

-----

## Q. `LaunchedEffect`는 컴포저블에서 `suspend` 함수를 관리하는 데 어떻게 도움이 되며, 그 `key`가 변경되면 어떤 일이 발생하나요?

`LaunchedEffect`는 컴포저블의 생명주기에 맞춰 코루틴을 안전하게 실행하고, 컴포저블이 화면에서 사라지면 자동으로 취소하여 **메모리 누수나 불필요한 작업을 막아줍니다.** `key` 값이 변경되면, **기존에 실행 중이던 코루틴은 취소되고 새로운 `key` 값으로 코루틴이 다시 시작**되어, 데이터 변경에 맞춰 부작용(side-effect)을 다시 실행할 수 있습니다.

-----

### `suspend` 함수 안전하게 실행하기

컴포저블 함수는 상태 변경에 따라 언제든, 그리고 여러 번 재실행(리컴포지션)될 수 있습니다. 만약 컴포저블 본문에서 `suspend` 함수를 직접 호출하면, 리컴포지션이 일어날 때마다 해당 함수가 반복적으로 호출되는 문제가 발생합니다. 또한, 화면에서 사라진 컴포저블이 시작한 코루틴을 수동으로 취소하지 않으면 리소스 낭비나 메모리 누수로 이어질 수 있습니다.

`LaunchedEffect`는 이러한 문제를 해결하기 위해 **컴포지션의 생명주기에 연결된 코루틴 스코프**를 제공합니다.

  * **진입 시 실행:** `LaunchedEffect`는 컴포저블이 처음 화면에 그려질 때(컴포지션에 진입할 때) 내부의 코드 블록을 실행합니다.
  * **이탈 시 자동 취소:** 컴포저블이 화면에서 사라지면(컴포지션을 벗어나면), `LaunchedEffect`는 내부에서 실행 중이던 코루틴을 **자동으로 취소**합니다. 이 덕분에 개발자는 수동으로 코루틴의 생명주기를 관리할 필요가 없습니다.

<!-- end list -->

```kotlin
@Composable
fun UserProfileScreen(userId: String, viewModel: MyViewModel) {
    // 이 컴포저블이 화면에 처음 나타날 때 데이터 로딩 시작
    LaunchedEffect(Unit) { // key를 Unit으로 지정하여 한 번만 실행
        viewModel.loadUserData(userId)
    }
    // ... UI ...
}
```

-----

### `key` 변경 시 동작 방식: 취소 및 재시작

`LaunchedEffect`에 전달하는 `key` 파라미터는 이펙트의 실행을 제어하는 중요한 역할을 합니다.

  * **동작 방식:** `LaunchedEffect`는 리컴포지션이 일어날 때마다 전달받은 `key` 값이 이전과 동일한지 비교합니다. 만약 `key` 값이 **변경되었다면**, 다음과 같은 일이 발생합니다.

    1.  **기존 코루틴 취소:** 이전에 실행 중이던 `LaunchedEffect`의 코루틴이 있다면 즉시 취소됩니다.
    2.  **새 코루틴 시작:** 변경된 새로운 `key` 값으로 코드 블록을 다시 실행하여 새로운 코루틴을 시작합니다.

  * **주요 사용 사례 📝:**
    사용자의 선택에 따라 다른 데이터를 불러와야 하는 상세 화면에서 매우 유용합니다. 예를 들어, 사용자가 목록에서 다른 아이템을 선택하여 `selectedItemId` 상태가 변경되었다고 가정해 봅시다.

    ```kotlin
    @Composable
    fun ItemDetailScreen(selectedItemId: Int, viewModel: MyViewModel) {
        // selectedItemId가 변경될 때마다 이 블록이 다시 실행됩니다.
        LaunchedEffect(selectedItemId) {
            // 이전에 실행 중이던 데이터 요청은 취소되고,
            // 새로운 ID로 데이터를 다시 요청합니다.
            viewModel.fetchItemDetails(selectedItemId)
        }
        // ... 상세 정보 UI ...
    }
    ```

    이 방식을 통해 사용자가 다른 아이템을 선택할 때마다 이전의 불필요한 네트워크 요청은 취소되고, 항상 최신 선택에 맞는 데이터를 효율적으로 가져올 수 있습니다.

-----

### 요약: `LaunchedEffect`의 역할

`LaunchedEffect`는 컴포저블 함수 내에서 `suspend` 함수나 장기 실행 작업을 다루기 위한 **표준적이고 안전한 방법**입니다. 이는 **생명주기를 인식하는 코루틴 스코프**를 제공하여 불필요한 작업과 메모리 누수를 방지하고, **`key` 시스템**을 통해 특정 데이터나 상태의 변경에 반응하여 작업을 취소하고 다시 시작하는 로직을 매우 간결하게 구현할 수 있도록 돕습니다.


## Q. 언제 `LaunchedEffect` 대신 `DisposableEffect`를 사용하겠습니까?

`LaunchedEffect`는 컴포저블이 화면에 나타날 때 **일회성 비동기 작업**을 실행하고 사라질 때 자동으로 취소하는 데 사용하는 반면, `DisposableEffect`는 리스너 등록, 브로드캐스트 리시버 구독, 또는 라이브러리 초기화처럼 컴포저블이 사라질 때 **반드시 호출해야 하는 정리(cleanup) 작업**이 필요한 경우에 사용합니다. 핵심은 **`onDispose` 콜백의 유무**입니다.

-----

### 1\. 두 이펙트 핸들러의 핵심 차이: 정리(Cleanup) 로직의 필요성

`LaunchedEffect`와 `DisposableEffect`는 모두 컴포저블의 생명주기에 맞춰 특정 코드를 실행하는 부작용(side-effect) 핸들러이지만, 그들의 가장 큰 차이점은 **컴포저블이 화면에서 사라질 때 정리 작업이 필요한가**에 있습니다.

  * **`LaunchedEffect`:**
    주로 `suspend` 함수나 비동기 코루틴 작업을 실행하기 위해 사용됩니다. 컴포저블이 컴포지션을 벗어나면 `LaunchedEffect`는 진행 중이던 코루틴을 \*\*취소(cancel)\*\*합니다. 이는 네트워크 요청이나 애니메이션처럼 중간에 멈춰도 괜찮은 작업에 적합합니다. 하지만 취소는 협조적으로 이루어지며, 특정 정리 코드가 반드시 실행된다는 보장은 없습니다.

  * **`DisposableEffect`:**
    리스너 등록이나 구독처럼, **시작(등록)과 쌍을 이루는 명시적인 정리(해제) 작업이 반드시 필요한 경우**에 사용됩니다. `DisposableEffect`는 컴포저블이 컴포지션을 떠나거나 `key`가 변경될 때 **`onDispose` 블록의 실행을 보장**합니다.

-----

### 2\. `DisposableEffect`를 사용해야 하는 시나리오

"시작"과 "정리"가 명확한 쌍으로 이루어져야 하는 모든 경우에 `DisposableEffect`를 사용해야 합니다.

#### 2.1. 리스너(Listener) 등록 및 해제 🎧

  * **시나리오:** 안드로이드 프레임워크의 특정 이벤트를 수신하기 위해 `BroadcastReceiver`나 `SensorManager`의 리스너를 등록해야 할 때.
  * **이유:** 이러한 리스너들은 컴포저블이 더 이상 보이지 않을 때 등록 해제하지 않으면 메모리 누수나 불필요한 배터리 소모를 유발합니다. 코루틴 취소만으로는 `unregisterReceiver()`나 `unregisterListener()`가 호출되지 않습니다.
  * **구현:** `DisposableEffect`의 주 블록에서 리스너를 등록하고, `onDispose` 블록에서 등록을 해제합니다.

#### 2.2. 생명주기 옵저버(LifecycleObserver) 관리 🔄

  * **시나리오:** 호스팅하는 액티비티나 프래그먼트의 생명주기(예: `ON_START`, `ON_STOP`)에 따라 특정 작업(예: 분석 이벤트 전송)을 수행해야 할 때.
  * **이유:** `LifecycleObserver`는 생명주기에 추가(`addObserver`)된 후, 더 이상 필요 없을 때 반드시 제거(`removeObserver`)해야 합니다.
  * **구현:** `DisposableEffect`에 `LifecycleOwner`를 `key`로 전달하고, 주 블록에서 `addObserver`, `onDispose` 블록에서 `removeObserver`를 호출합니다.

<!-- end list -->

```kotlin
@Composable
fun AnalyticsTracker(lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current) {
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                // onStart 이벤트 전송
            } else if (event == Lifecycle.Event.ON_STOP) {
                // onStop 이벤트 전송
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            // 컴포저블이 사라질 때 옵저버를 반드시 제거
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
```

#### 2.3. 뷰(View) 기반 라이브러리 연동 🔗

  * **시나리오:** `AndroidView` 컴포저블을 사용하여 기존 뷰 기반 라이브러리(예: 차트 라이브러리, 광고 SDK 뷰)를 Compose에 통합할 때. 해당 라이브러리가 `start()`/`stop()` 또는 `resume()`/`pause()`와 같은 생명주기 관리 메서드를 요구하는 경우.
  * **이유:** 이러한 뷰 기반 라이브러리의 리소스를 올바르게 관리하고 해제하려면 명시적인 정리 호출이 필요합니다.
  * **구현:** `DisposableEffect`의 주 블록에서 `start()`나 `resume()`을, `onDispose` 블록에서 `stop()`이나 `pause()`를 호출합니다.

#### 2.4. 구독(Subscription) 관리

  * **시나리오:** 코루틴 `Flow`가 아닌 다른 종류의 데이터 스트림(예: 특정 콜백 기반 라이브러리)을 구독하고, 화면을 벗어날 때 구독을 해지해야 하는 경우.
  * **이유:** 구독을 해지하지 않으면 백그라운드에서 불필요한 데이터 업데이트를 계속 받게 되어 성능 문제나 메모리 누수를 일으킬 수 있습니다.
  * **구현:** `DisposableEffect`에서 구독을 시작하고, `onDispose`에서 구독 해지 메서드를 호출합니다.

-----

### 3\. 요약: 선택 가이드

어떤 이펙트 핸들러를 사용할지 결정하는 가장 간단한 기준은 다음과 같습니다.

> **컴포저블이 화면에서 사라질 때 "반드시 실행되어야 하는 정리(cleanup) 코드"가 있는가?**

  * **예 (YES):** 리스너 등록 해제, 구독 취소, 리소스 해제 등 반드시 호출해야 하는 정리 로직이 있다면 **`DisposableEffect`** 를 사용해야 합니다.
  * **아니요 (NO):** 단순히 `suspend` 함수를 실행하거나 코루틴을 시작하고, 화면을 벗어날 때 **코루틴이 자동으로 취소되기만 하면 충분**하다면 **`LaunchedEffect`** 를 사용합니다.

이 기준에 따라 적절한 API를 선택하면, 부작용을 안전하고 예측 가능하게 관리하여 견고한 Compose 애플리케이션을 만들 수 있습니다.


## Q. `SideEffect`의 사용 사례를 설명하고, `LaunchedEffect`와 어떻게 다른지 설명해주세요.

`SideEffect`는 **매 리컴포지션이 성공적으로 완료될 때마다** non-Compose 코드와 상태를 동기화하기 위해 사용됩니다. 반면, `LaunchedEffect`는 컴포저블이 처음 화면에 나타나거나 `key`가 변경될 때 **단 한 번** 코루틴을 시작하여 비동기 작업을 처리하는 데 사용됩니다. 핵심 차이는 **실행 빈도**와 **코루틴 스코프의 유무**입니다.

-----

### 1\. SideEffect: 컴포지션 상태를 외부와 동기화 🔗

`SideEffect`는 컴포저블 함수가 성공적으로 리컴포지션을 마칠 때마다 실행될 코드 블록을 예약하는 데 사용됩니다.

#### 작동 방식 및 목적

`SideEffect`의 주된 목적은 **Compose의 상태를 Compose가 관리하지 않는 객체(non-Compose code)와 공유하거나 동기화**하는 것입니다. 컴포저블 함수는 여러 번 호출되거나 건너뛸 수 있으므로 함수 본문에서 non-Compose 객체를 직접 수정하는 것은 위험합니다. `SideEffect`는 컴포지션이 성공적으로 완료된 후에만 실행되므로, 외부 객체가 항상 최신의 올바른 상태 값을 받도록 보장할 수 있습니다.

`SideEffect`에 전달된 람다는 `suspend` 함수가 아니며, 메인 스레드에서 동기적으로 실행됩니다.

#### 주요 사용 사례

  * **분석 라이브러리와 통합:** 현재 UI에 표시된 상태에 따라 `FirebaseAnalytics`와 같은 외부 분석 라이브러리의 사용자 속성(user property)을 업데이트할 때.

<!-- end list -->

```kotlin
@Composable
fun UserProfile(user: User, analytics: FirebaseAnalytics) {
    // user 상태가 변경되어 리컴포지션이 성공할 때마다 실행됨
    SideEffect {
        // FirebaseAnalytics는 non-Compose 객체이므로 SideEffect를 통해 상태를 전달
        analytics.setUserProperty("user_type", user.type)
    }

    Text("User: ${user.name}")
}
```

  * **레거시 뷰(View) 객체와 통합:** `AndroidView`로 감싼 레거시 뷰 객체의 속성을 Compose 상태에 따라 업데이트해야 할 때.

-----

### 2\. LaunchedEffect: 컴포지션 생명주기에 맞춘 비동기 작업 🚀

`LaunchedEffect`는 컴포저블의 생명주기 범위 내에서 `suspend` 함수나 장기 실행 작업을 실행하기 위해 사용됩니다.

#### 작동 방식 및 목적

`LaunchedEffect`는 컴포저블이 컴포지션에 처음 진입할 때(또는 `key`가 변경될 때) 코루틴을 시작합니다. 이 코루틴은 컴포저블이 컴포지션을 벗어나면 자동으로 취소됩니다.

주된 목적은 컴포저블의 생명주기와 연결된 **비동기 작업**(예: 네트워크 요청, 데이터베이스 쿼리, 타이머)을 안전하게 처리하는 것입니다.

#### 주요 사용 사례

  * 화면이 나타날 때 초기 데이터를 불러오는 네트워크 요청 실행.
  * 특정 조건이 충족되었을 때 스낵바를 표시하고 일정 시간 후 사라지게 하는 작업.
  * `key` 값 변경에 따라 새로운 데이터를 가져오는 작업.

-----

### 3\. SideEffect와 LaunchedEffect의 주요 차이점

| 구분 | `SideEffect` | `LaunchedEffect` |
| --- | --- | --- |
| **실행 시점** | **매 성공적인 리컴포지션 후** | 컴포지션에 **처음 진입**하거나 **`key`가 변경**될 때 |
| **실행 빈도** | 상태 변경 시 잠재적으로 **여러 번** | **한 번** (또는 `key` 변경 시마다 한 번) |
| **코루틴 스코프** | **없음** (일반 람다 `() -> Unit`) | **있음** (`suspend` 람다 `suspend CoroutineScope.() -> Unit`) |
| **주요 목적** | Compose 상태를 **non-Compose 코드와 동기화** | 컴포저블 생명주기 내에서 **비동기 작업 실행** |

-----

### 4\. 결론: 언제 무엇을 사용해야 하는가?

두 이펙트 핸들러의 사용 사례는 명확히 구분됩니다.

  * **`SideEffect`를 사용해야 할 때:**

      * 성공적으로 화면이 그려진 후, 그 **최종 상태를 외부(non-Compose) 세계와 공유**하고 싶을 때.
      * 비동기 작업이 필요 없고, 리컴포지션이 완료될 때마다 실행되어야 하는 동기적인 작업에 사용합니다.

  * **`LaunchedEffect`를 사용해야 할 때:**

      * 컴포저블의 생명주기 내에서 **네트워크 요청, 데이터베이스 접근, 타이머 등 비동기적이거나 오래 걸리는 작업**을 시작하고 싶을 때.
      * 해당 작업이 컴포저블이 화면에서 사라지면 자동으로 취소되기를 바랄 때.

이 둘의 차이점을 이해하면 부작용을 더 안전하고 의도에 맞게 처리하여 예측 가능하고 안정적인 Compose 코드를 작성할 수 있습니다.
