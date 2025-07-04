# `rememberUpdatedState`의 목적과 작동 방식은 무엇인가요?

[`rememberUpdatedState`](https://developer.android.com/develop/ui/compose/side-effects#rememberupdatedstate) 함수는 컴포저블(composable)의 컨텍스트 내에서 **상태 업데이트를 안전하게 처리**하도록 돕는 유틸리티 함수입니다. 이는 이전 리컴포지션에서 생성된 람다나 콜백에서도 **항상 최신 상태 값을 사용**하도록 보장합니다.

컴포저블에서 콜백이나 람다를 생성할 때, 해당 함수가 이미 컴포지션되었다면 그 안에서 참조하는 상태 값들이 자동으로 업데이트되지 않을 수 있습니다. 바로 이때 `rememberUpdatedState`가 사용됩니다. 이 함수는 오래된(stale) 상태와 관련된 잠재적인 버그를 피하면서 항상 최신 상태 값을 사용할 수 있도록 하는 메커니즘을 제공합니다.

-----

## `rememberUpdatedState` 작동 방식

`rememberUpdatedState` 함수는 상태의 가장 최근 값을 기억하고 상태가 변경될 때마다 이를 업데이트합니다. 이 함수는 `State<T>` 객체를 반환하며, 컴포저블이나 람다 내에서 이 객체를 읽어 현재 값에 접근할 수 있습니다.

### 함수 시그니처

```kotlin
@Composable
fun <T> rememberUpdatedState(newValue: T): State<T>
```

-----

## 사용 사례

`rememberUpdatedState`는 다음과 같은 시나리오에서 특히 유용합니다.

  * **오래 실행되는 이펙트에 콜백이 전달될 때:** 람다나 콜백이 최신 상태를 사용해야 하지만, 이전 컴포지션에서 생성되었을 경우.
  * **애니메이션 또는 부작용(Side-Effect) API:** 리컴포지션을 넘어 지속되는 `LaunchedEffect`, `DisposableEffect` 또는 애니메이션과 함께 사용될 때.

### `rememberUpdatedState`의 실제 사용 예시

```kotlin
@Composable
fun TimerWithCallback(
    onTimeout: () -> Unit, // 시간이 다 되었을 때 호출될 콜백
    timeoutMillis: Long = 5000L
) {
    // onTimeout 람다의 최신 버전을 추적
    val currentOnTimeout by rememberUpdatedState(onTimeout)

    // TimerWithCallback의 생명주기와 일치하는 이펙트 생성
    // TimerWithCallback이 리컴포지션되어도 delay는 다시 시작되지 않아야 함
    LaunchedEffect(true) { // key를 true로 설정하여 한 번만 실행되도록 함
        delay(timeoutMillis)
        currentOnTimeout() // 항상 최신 콜백이 사용되도록 보장
    }

    Text(text = "타이머가 $timeoutMillis 밀리초 동안 실행 중입니다")
}
```

#### 예시 설명

  * **`rememberUpdatedState`가 없다면:** 타이머가 실행되는 동안 `onTimeout` 콜백이 변경되더라도, 지연 시간이 끝난 후에는 **초기 컴포지션에서 캡처된 이전 콜백**이 호출될 수 있습니다.
  * **`rememberUpdatedState`가 있다면:** `onTimeout` 콜백은 **항상 최신 상태**로 유지되며, 지연 시간이 끝나면 가장 최근에 제공된 함수가 호출되도록 보장합니다.

-----

## 주요 이점

  * **오래된 상태(Stale State) 방지:** 오래 실행되는 이펙트에서 오래된 상태 참조로 인해 발생하는 버그를 방지합니다.
  * **안전한 컴포지션 처리:** `LaunchedEffect`나 `DisposableEffect`와 같은 컴포지션을 인식하는 API와 원활하게 작동합니다.
  * **간단한 통합:** 상태가 항상 최신 값을 반영하도록 보장하기 위해 최소한의 코드 변경만 필요합니다.

-----

## 요약

`rememberUpdatedState`는 콜백이나 오래 실행되는 이펙트에서 상태 업데이트를 관리하기 위한 부작용 핸들러 API 중 하나입니다. 이는 가장 최근의 상태 값이 항상 사용되도록 보장하여, 오래된 데이터로 인한 잠재적인 문제를 피하게 해줍니다.

-----

## 💡 프로 팁: `rememberUpdatedState` 내부 구현

`rememberUpdatedState`의 동작을 탐색해 보았는데, 언뜻 보기에는 복잡해 보일 수 있습니다. 그러나 그 내부 구현은 매우 간단합니다.

### 내부 구현 코드

```kotlin
@Composable
fun <T> rememberUpdatedState(newValue: T): State<T> =
    remember { mutableStateOf(newValue) }.apply { value = newValue }
```

위 코드에서 볼 수 있듯이, `rememberUpdatedState`는 제공된 `newValue`를 상태로 저장하고 `apply` 스코프 함수를 사용하여 그 값을 업데이트합니다. `rememberUpdatedState`를 사용하는 컴포저블 함수가 리컴포지션될 때마다 이 함수가 호출되고, 이전에 기억된 상태는 새로운 `newValue` 파라미터로 업데이트됩니다. 내부적으로 그 작동은 표면적으로 보이는 것보다 간단합니다.

  * **`remember { mutableStateOf(newValue) }`**: `mutableStateOf` 객체는 **단 한 번만 생성**되어 리컴포지션 간에 유지됩니다. 처음에는 `newValue`로 초기화됩니다.
  * **`.apply { value = newValue }`**: **매 리컴포지션마다** 이 부분이 실행됩니다. `remember`에 의해 유지된 동일한 `MutableState` 객체의 `.value` 프로퍼티를 최신의 `newValue`로 업데이트합니다.

이러한 방식으로, `rememberUpdatedState`는 `State` 객체의 참조 자체는 고정시키면서 그 내부의 값만 최신으로 유지하여, 오래된 클로저 문제를 해결합니다.

-----

## Q. `LaunchedEffect`를 사용하여 지연된 작업을 트리거하는 컴포저블에서, 지연 시간이 끝난 후 최신 람다가 호출되도록 어떻게 보장할 수 있나요?

`LaunchedEffect`를 사용하여 지연된 작업을 수행할 때, 지연 시간이 끝난 후 **최신 람다(lambda)가 호출되도록 보장**하려면 **`rememberUpdatedState`** API를 사용해야 합니다.

-----

### 1. 문제점: 지연된 작업과 오래된(Stale) 람다

`LaunchedEffect`는 컴포지션에 처음 진입하거나 `key`가 변경될 때 내부의 코루틴 블록을 시작합니다. 이때, 코루틴은 **시작되던 시점의 람다를 캡처(capture)** 합니다.

만약 `LaunchedEffect` 내부에서 `delay()`와 같은 `suspend` 함수가 실행되는 동안, 부모 컴포저블이 리컴포지션되어 새로운 람다를 전달하더라도, 이미 실행 중인 `LaunchedEffect`는 이 변경을 알지 못합니다. 그 결과, `delay()`가 끝난 후에는 **이전 컴포지션의 오래된(stale) 람다**를 호출하게 되어 예기치 않은 버그가 발생할 수 있습니다.

**잘못된 예시:**

```kotlin
@Composable
fun LandingScreen(onTimeout: () -> Unit) {
    // onTimeout 람다가 변경되어도 LaunchedEffect는 재시작되지 않음
    LaunchedEffect(Unit) {
        delay(3000)
        // 3초 전의 onTimeout 람다를 호출할 위험이 있음
        onTimeout()
    }
    // ...
}
```

위 코드에서 `LandingScreen`이 리컴포지션되어 새로운 `onTimeout` 람다를 받더라도, `LaunchedEffect`는 `key`가 `Unit`으로 고정되어 있으므로 재시작되지 않고, 3초가 지난 후에는 초기 컴포지션 시점의 `onTimeout`을 호출하게 됩니다.

-----

### 2. 해결책: `rememberUpdatedState`로 최신 람다 참조 유지

`rememberUpdatedState`는 이러한 "오래된 람다" 문제를 해결하기 위해 만들어진 전용 API입니다.

#### 2.1. 작동 방식

`rememberUpdatedState`는 특정 값(이 경우엔 람다)을 `State` 객체로 감쌉니다.

  * 이 `State` 객체의 **참조 자체는 리컴포지션 간에 안정적으로 유지**됩니다.
  * 하지만 `State` 객체가 감싸고 있는 **내부 값(`.value`)은 매 리컴포지션마다 최신 값으로 업데이트**됩니다.

#### 2.2. 적용 방법

1.  컴포저블에 전달된 람다 파라미터를 `rememberUpdatedState`로 감싸서 최신 버전을 추적하는 새로운 참조를 만듭니다.
2.  `LaunchedEffect` 내부에서는 이 새로운 참조를 통해 람다를 호출합니다.

**올바른 예시:**

```kotlin
@Composable
fun LandingScreen(onTimeout: () -> Unit) {
    // 1. onTimeout 람다를 rememberUpdatedState로 감싸 최신 버전을 추적합니다.
    val updatedOnTimeout by rememberUpdatedState(onTimeout)

    LaunchedEffect(Unit) {
        delay(3000)
        // 2. 3초가 지난 후, 'updatedOnTimeout'을 호출합니다.
        // 이 참조는 항상 가장 최근의 onTimeout 람다를 가리킵니다.
        updatedOnTimeout()
    }
    // ...
}
```

#### 2.3. 왜 이것이 작동하는가?

  * `LaunchedEffect`의 코루틴은 시작될 때 안정적인 `updatedOnTimeout`이라는 `State` 객체의 참조를 캡처합니다.
  * `LandingScreen`이 리컴포지션될 때마다, `rememberUpdatedState`는 `updatedOnTimeout`의 내부 값(`.value`)을 새로운 `onTimeout` 람다로 조용히 업데이트합니다.
  * `LaunchedEffect` 자체는 재시작되지 않지만, 3초의 `delay`가 끝난 후 `updatedOnTimeout()`을 호출하는 시점에는, 그 내부 값이 이미 최신 람다로 교체되어 있으므로 **항상 가장 최근에 전달된 람다**가 실행되는 것이 보장됩니다.

-----

### 3. 결론

`LaunchedEffect`와 같이 컴포지션 시점과 실제 실행 시점 사이에 시간 차이가 있는 오래 실행되는 작업(long-running task) 내에서 람다를 호출해야 할 때는, 항상 **`rememberUpdatedState`** 를 사용하여 해당 람다를 감싸야 합니다. 이는 의도치 않은 오래된 상태나 콜백이 실행되는 것을 방지하고, 리컴포지션과 관계없이 항상 최신 로직이 실행되도록 보장하는 Jetpack Compose의 핵심적인 안전장치입니다.