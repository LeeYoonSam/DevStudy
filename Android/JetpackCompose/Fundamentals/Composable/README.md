# 컴포저블 함수는 내부적으로 어떻게 작동하나요?

Jetpack Compose는 선언적 UI를 만들기 위한 구성 요소(building blocks)로 **`@Composable` 함수**를 도입합니다. 내부적으로, 이러한 함수들은 **Compose 컴파일러 플러그인(Compose Compiler Plugin)** 에 의존하여 일반 코틀린 코드를 반응형, 상태 주도 UI 업데이트를 지원하는 구조로 변환합니다.

### 컴파일러 변환 (Compiler Transformation)

함수에 `@Composable` 어노테이션을 붙이면, Compose 컴파일러 플러그인이 코틀린 컴파일 과정을 가로챕니다. 컴파일러는 이 함수를 표준 코틀린 함수로 취급하는 대신, Compose의 반응형 시스템을 활성화하기 위해 **추가적인 파라미터와 로직을 주입**합니다. 핵심적인 추가 요소 중 하나는 숨겨진 `Composer` 객체로, 이는 컴포지션의 상태를 추적하고 UI 상태가 변경될 때 리컴포지션(recomposition)을 처리합니다.

```kotlin
@Composable
fun MyComposable() {
    Text("Hello, Compose!")
}
```

내부적으로, 이는 상태 관리를 위한 `Composer` 객체와 다른 메타데이터를 포함하는 버전으로 변환됩니다.

### 컴포지션과 리컴포지션 (Composition and Recomposition)

Compose 런타임은 `@Composable` 함수의 생명주기를 관리합니다. **컴포지션(Composition)** 단계에서 런타임은 컴포저블 함수들을 실행하고 UI 트리를 구축합니다. 이 트리는 **슬롯 테이블(Slot Table)** 이라는 데이터 구조에 저장되며, 이는 Compose가 UI를 효율적으로 관리하고 업데이트하는 데 도움이 됩니다.

상태가 변경되면 **리컴포지션(Recomposition)** 이 트리거됩니다. Compose는 UI 트리를 처음부터 다시 만드는 대신, 슬롯 테이블을 사용하여 UI의 어느 부분이 업데이트되어야 하는지 결정하고 해당 컴포저블 함수들만 선택적으로 재실행합니다.

### remember와 상태 관리 (remember and State Management)

상태를 관리하기 위해 Compose는 `remember` 및 `State`와 같은 API를 제공합니다. 이러한 메커니즘은 런타임 및 컴파일러와 긴밀하게 협력하여 리컴포지션 전반에 걸쳐 상태를 보존하며, UI가 앱의 데이터 모델과 일관성을 유지하도록 보장합니다.

```kotlin
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }
    Button(onClick = { count++ }) {
        Text("Count: $count")
    }
}
```

여기서 `remember`는 `count` 상태가 리컴포지션 전반에 걸쳐 메모리에 유지되도록 보장하고, `mutableStateOf`는 상태가 변경될 때 Compose에 알려 리컴포지션을 트리거합니다.

-----

### 요약

내부적으로, 컴포저블 함수는 **Compose 컴파일러 플러그인**을 활용하여 코틀린 코드를 **Compose 런타임**에 의해 관리되는 반응형 UI 컴포넌트로 변환합니다. 이 시스템은 `remember`와 같은 상태 관리 도구와 결합되어, 상태 변경에 대응하여 효율적인 렌더링과 동적 UI 업데이트를 보장합니다. 이러한 아키텍처가 바로 Jetpack Compose를 선언적이고 효율적인 UI 프레임워크로 만드는 것입니다. 더 깊은 이해를 원한다면 [Android Developers: Under the hood of Jetpack Compose](https://medium.com/androiddevelopers/under-the-hood-of-jetpack-compose-part-2-of-2-37b2c20c6cdd) (영문)를 읽어보세요.

-----

## 💡 프로 팁: Compose 컴파일러와 Composer

**Compose 컴파일러**는 빌드 과정에서 `@Composable` 함수의 중간 표현(IR, Intermediate Representation)을 최적화된 코드로 변환합니다. 이 변환은 UI 렌더링과 상태를 효율적으로 관리하고, Compose의 선언적 UI 패러다임을 가능하게 하는 데 필수적입니다. 이 과정의 핵심 구성 요소 중 하나는 **Composer**이며, 이는 컴포저블 함수의 상태를 추적하고 관리하는 중심 역할을 합니다.

### 컴포저블 함수의 변환

함수에 `@Composable` 어노테이션을 붙이면, Compose 컴파일러는 모든 컴포저블 함수에 암묵적인 `Composer` 파라미터를 추가하여 그 구조를 수정합니다. 이 파라미터는 컴포저블 함수와 Compose 런타임 간의 다리 역할을 하여, 런타임이 UI 상태, 리컴포지션 및 기타 핵심 기능들을 효율적으로 관리할 수 있게 합니다.

예를 들어, 다음과 같은 간단한 컴포저블 함수는:

```kotlin
@Composable
fun Greeting(name: String) {
    Text("Hello, $name!")
}
```

Compose 컴파일러에 의해 개념적으로 다음과 같은 함수로 변환됩니다.

```kotlin
fun Greeting(name: String, composer: Composer, changed: Int) {
    composer.startRestartGroup(key) // 리컴포지션 범위 시작
    val isChanged = composer.changed(name) // name 파라미터가 변경되었는지 확인
    if (isChanged || ...) { // 변경되었거나 다른 조건 충족 시
        Text("Hello, $name!")
    }
    composer.endRestartGroup()?.updateScope { ... } // 리컴포지션 범위 종료 및 업데이트 로직 설정
}
```

이러한 변환은 입력 파라미터(이 예에서는 `name`)의 변경에 따라 리컴포지션이 필요한지 여부를 런타임이 결정할 수 있는 훅(hook)을 도입합니다. 이 훅을 활용하여 Compose는 변경되지 않은 UI 부분을 리컴포지션하지 않고 건너뛰어 성능을 최적화할 수 있습니다.

### Composer의 역할

[**Composer**](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/runtime/runtime/src/commonMain/kotlin/androidx/compose/runtime/Composer.kt;l=475?q=composer)는 Compose 런타임의 저수준(low-level) API로, 중앙 상태 관리자 역할을 합니다. 이는 Compose 코틀린 컴파일러 플러그인이 타겟으로 하는 인터페이스이며 코드 생성 헬퍼에 의해 사용됩니다.

주요 책임은 다음과 같습니다.

  * **상태 관리:** 상태를 추적하고 입력값이 변경될 때 컴포저블 함수가 올바르게 리컴포지션되도록 보장합니다.
  * **UI 계층 구조 구축:** UI의 트리 구조를 유지하고 상태가 변경될 때 노드를 효율적으로 업데이트합니다.
  * **최적화:** 입력 파라미터의 변경 사항을 분석하여 UI의 어느 부분을 리컴포지션해야 하는지 결정합니다.
  * **리컴포지션 제어:** 특정 컴포저블에 대한 리컴포지션의 시작, 건너뛰기 또는 종료를 포함한 리컴포지션의 생명주기를 조율합니다.

Composer는 리컴포지션이 **점진적(incremental)** 으로 이루어지도록 보장합니다. 즉, UI 트리의 필요한 부분만 업데이트되어 불필요한 계산을 줄이고 성능을 향상시킵니다.

### 요약

Compose 컴파일러는 `@Composable` 함수를 변환하여 `Composer` 및 기타 필요한 파라미터를 주입하며, 이를 통해 Compose 런타임이 UI 상태와 리컴포지션을 효율적으로 관리할 수 있게 합니다. `Composer`는 Compose 런타임의 중추 역할을 하여 상태 추적, 리컴포지션, UI 업데이트를 처리합니다. 이러한 컴파일러와 런타임의 통합은 Jetpack Compose를 현대적이고 선언적인 UI를 구축하기 위한 고효율 프레임워크로 만듭니다.

-----

## 💡 프로 팁: 왜 컴포저블 함수 내에서 비즈니스 로직을 직접 호출하는 것을 피해야 하나요?

Jetpack Compose는 미래 지향적인 사고방식으로 설계되었으며, **멀티스레드 안전성**을 강조합니다. 현재 컴포저블 함수가 병렬로 실행되지는 않지만(지금은 Compose가 메인 스레드에서 작동함), Compose는 개발자가 코드를 작성할 때 **마치 멀티스레드 환경에서 실행될 수 있는 것처럼** 작성하도록 권장합니다. 이 접근 방식은 Compose가 향후 여러 코어를 활용하여 컴포저블을 병렬로 실행하는 잠재적인 최적화에 대비할 수 있게 합니다.

Jetpack Compose의 미래 버전은 백그라운드 스레드 풀에서 컴포저블 함수를 병렬로 실행하여 리컴포지션을 최적화할 수 있습니다. 그러나 이는 컴포저블 함수가 스레드 안전하지(thread-safe) 않다면 문제를 야기할 수 있습니다. 예를 들어, 컴포저블이 ViewModel과 상호작용할 때 여러 스레드에서 동시에 호출되면 예기치 않은 동작으로 이어질 수 있습니다.

컴포저블 함수가 멀티스레드 환경에서 원활하게 작동하도록 하려면 다음을 따르세요.

  * **부작용(Side Effects) 피하기:** 컴포저블 함수는 부작용이 없어야 합니다. 입력을 UI로 변환하는 것 외에 외부 상태나 범위 내 변수를 수정해서는 안 됩니다.
  * **콜백에서 부작용 트리거하기:** 부작용을 일으켜야 하는 모든 작업에는 `onClick`과 같은 콜백을 사용하세요. 이러한 콜백은 항상 UI 스레드에서 실행되어 일관성을 보장합니다.
  * **변경 가능한 공유 상태 피하기:** 컴포저블 함수 내에서 공유 변수를 수정하는 것을 피하세요. 이러한 쓰기 작업은 스레드 안전하지 않을 수 있습니다.

### 부작용이 없는/있는 컴포어블 예시

다음 예시는 적절하고 **부작용이 없는(side-effect-free)** 컴포저블을 보여줍니다.

```kotlin
@Composable
fun SideEffectFree() {
    Card {
        Text(text = "이것은 부작용이 없는 컴포저블입니다")
    }
}
```

이 함수는 오직 입력에만 의존하고 어떠한 외부 상태나 내부 변수를 수정하지 않으므로 스레드 안전합니다.

반면에, 다음 예시는 문제가 있는 부작용을 도입합니다.

```kotlin
@Composable
fun SideEffect() {
    var items = 0

    Card {
        Text(text = "$items") // 이와 같이 변경 가능한 공유 상태를 피해야 합니다.
        items++ // 리컴포지션될 때마다 이 값을 수정합니다.
    }
}
```

여기서 컴포저블은 지역 변수인 `items`를 수정하며, 리컴포지션될 때마다 값이 변경됩니다. 이러한 쓰기 작업은 스레드 안전하지 않으며 여러 스레드에서 실행될 때 잘못된 동작으로 이어질 수 있습니다.

-----

### 컴포저블 함수는 어떤 순서로든 실행될 수 있습니다

Jetpack Compose는 주로 메인 스레드에서 작동하지만 멀티스레딩을 염두에 두고 설계되어 미래 최적화를 위한 여지를 남겨두었습니다. 이는 컴포저블 함수가 여러 스레드에서 실행될 수 있는 것처럼 작성되어야 하며, 스레드 안전하고 실행 순서에 독립적이어야 함을 의미합니다.

컴포저블 함수가 작성된 순서대로 순차적으로 실행되는 것처럼 보일 수 있지만, **Compose는 이 동작을 보장하지 않습니다.** 만약 컴포-저블 함수가 여러 자식 컴포저블을 호출한다면, 그 자식들은 **어떤 순서로든 실행될 수 있습니다.** Compose는 컨텍스트에 따라 특정 UI 요소의 렌더링을 다른 것보다 우선시하여 UI 렌더링 프로세스를 최적화할 수 있습니다.

예를 들어, 행(row)을 렌더링하는 다음 코드를 고려해 보세요.

```kotlin
@Composable
fun TabLayout() {
    Row {
        FirstComposable { /* ... */ }
        SecondComposable { /* ... */ }
        ThirdComposable { /* ... */ }
    }
}
```

이 예시에서 `FirstComposable`, `SecondComposable`, `ThirdComposable`은 어떤 순서로든 실행될 수 있습니다. 정확성을 유지하려면 각 컴포저블은 자체적으로 완결되어야 하며, 공유 전역 변수 수정과 같은 부작용에 의존하는 것을 피해야 합니다. 예를 들어, `FirstComposable`이 전역 변수를 변경하고 `SecondComposable`이 그에 의존하도록 해서는 안 됩니다. 이러한 관행은 컴포저블이 예측 가능하고, 재사용 가능하며, Compose의 미래 멀티스레드 실행 모델 최적화와 호환되도록 보장합니다.

### 요약

컴포저블 함수는 현재 단일 스레드에서 실행되더라도 미래의 멀티스레드 실행을 염두에 두고 설계되었습니다. **부작용 없는 컴포저블**을 작성하는 것은 Compose가 발전함에 따라 정확성과 스레드 안전성을 보장하는 데 매우 중요합니다. 변경 가능한 공유 상태를 피하고 부작용에 콜백을 사용함으로써 견고하고 재사용 가능한 UI 컴포넌트를 만들 수 있습니다. 이러한 관행은 현재의 성능과 신뢰성을 향상시킬 뿐만 아니라, Compose가 여러 스레드에서 컴포저블을 병렬로 실행할 수 있는 잠재적인 최적화에 대비하는 코드이기도 합니다. 더 깊은 이해를 원한다면 [Thinking in Compose](https://developer.android.com/develop/ui/compose/mental-model)를 참조하세요.

-----

## Q. 함수에 `@Composable` 어노테이션을 붙이면 어떤 일이 발생하나요?

함수에 **`@Composable`** 어노테이션을 붙이는 것은 단순한 표식을 넘어, **Jetpack Compose 컴파일러 플러그인**에게 해당 함수를 특별한 방식으로 처리하도록 지시하는 강력한 신호입니다. 이 어노테이션이 붙으면, 일반 코틀린 함수는 Compose의 선언적 UI 시스템의 일부로 작동하는 **완전히 다른 구조의 코드로 변환**됩니다.

---
### 1. Compose 컴파일러의 변환 작업 트리거 ⚙️

가장 먼저 일어나는 일은 **Compose 컴파일러 플러그인이 해당 함수를 인식하고 변환 대상으로 삼는 것**입니다. 이 플러그인은 코틀린 컴파일 과정에 직접 관여하여, `@Composable` 함수를 Compose 런타임이 이해하고 관리할 수 있는 형태로 재작성합니다.

---
### 2. 함수의 시그니처 및 본문 변경 (컴파일 시점)

컴파일러는 개발자가 작성한 원래 함수의 바이트코드를 다음과 같이 수정합니다.

#### 2.1. 숨겨진 파라미터 주입
컴파일러는 함수의 시그니처에 개발자가 직접 작성하지 않은 **숨겨진 파라미터**들을 추가합니다. 가장 중요한 파라미터는 다음과 같습니다.
* **`$composer: Composer`**: 컴포지션의 상태를 추적하고, UI 트리를 구축하며, 리컴포지션을 관리하는 핵심 객체입니다.
* **`$changed: Int`**: 함수에 전달된 파라미터들이 이전 컴포지션과 비교하여 변경되었는지를 나타내는 비트마스크(bitmask)입니다. 이는 Compose 런타임이 불필요한 리컴포지션을 건너뛰는(skipping) 최적화를 수행하는 데 사용됩니다.

#### 2.2. 함수 본문 래핑(Wrapping)
원래 함수 내부에 작성된 코드는 컴포지션의 상태를 추적하고 관리하기 위한 **추가적인 로직으로 감싸집니다(wrapped)**.
* **`composer.startRestartGroup(...)`**: 리컴포지션이 가능한 범위의 시작을 표시합니다.
* **`composer.changed(...)`**: 주입된 `$changed` 값을 사용하여 입력값의 변경 여부를 확인하고 리컴포지션을 건너뛸지 결정합니다.
* **`composer.endRestartGroup()`**: 범위의 끝을 표시하고, 필요한 경우 이 컴포저블을 다시 시작(리컴포지션)하는 방법을 런타임에 제공합니다.

---
### 3. 결과적으로 발생하는 주요 특징 및 제약사항

컴파일러의 변환을 거친 `@Composable` 함수는 다음과 같은 새로운 특징과 제약사항을 갖게 됩니다.

#### 3.1. 컴포지션 컨텍스트(Composition Context) 내에서만 호출 가능
* 변환된 함수는 숨겨진 `Composer` 객체를 파라미터로 요구하게 됩니다. 이 `Composer` 객체는 오직 다른 `@Composable` 함수의 실행 중에만 제공될 수 있습니다.
* 이것이 바로 **"`@Composable` 함수는 다른 `@Composable` 함수 내에서만 호출될 수 있다"** 는 핵심 규칙이 존재하는 이유입니다.

#### 3.2. UI 트리의 일부가 됨
* 함수는 더 이상 단순한 로직을 실행하는 것이 아니라, `Text`, `Image`, `Column`과 같은 UI 요소를 **방출(emit)** 하여 Compose 런타임이 관리하는 UI 트리(내부적으로는 슬롯 테이블에 저장됨)의 일부를 기술하게 됩니다.

#### 3.3. 리컴포지션(Recomposition) 대상이 됨
* 함수는 이제 상태를 구독할 수 있게 됩니다. 만약 함수 내부에서 읽는 `State` 객체의 값이 변경되면, Compose 런타임은 이 함수를 다시 실행(리컴포지션)하여 UI를 최신 상태로 업데이트할 수 있습니다.

#### 3.4. 다른 컴포저블 API 사용 가능
* 함수 내부에서 `remember`, `LaunchedEffect`, `Modifier`, `CompositionLocalProvider` 등과 같이 컴포지션 컨텍스트 내에서만 의미를 갖는 다른 `@Composable` 함수나 API를 호출할 수 있게 됩니다.

---
### 4. 결론

결론적으로, 함수에 **`@Composable`** 어노테이션을 붙이는 것은 "이 함수는 UI의 일부를 그린다"고 표시하는 것을 넘어, **Compose 컴파일러가 해당 함수를 근본적으로 재작성**하도록 하는 지시입니다. 컴파일러는 함수에 상태 추적과 리컴포지션을 위한 '훅(hook)'(숨겨진 파라미터와 래핑 코드)을 주입하여, 평범한 코틀린 함수를 Compose 런타임과 완벽하게 통합되는 **상태를 인식하는(state-aware), 재구성 가능한(recomposable) UI 구성 요소**로 탈바꿈시킵니다.