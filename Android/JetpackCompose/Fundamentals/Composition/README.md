# 컴포지션(Composition)이란 무엇이며 어떻게 생성하나요?

[**컴포지션(Composition)**](https://developer.android.com/develop/ui/compose/lifecycle#composition-anatomy) 은 컴포저블(composable) 함수를 실행하여 생성되는, 여러분의 앱 **UI를 나타내는 결과물**입니다. 이는 `Composer`를 활용하여 UI를 **컴포저블의 트리 구조**로 구성하고 이 트리를 동적으로 관리합니다. 컴포지션은 상태를 기록하고, UI를 효율적으로 업데이트하기 위해 노드 트리에 필요한 변경 사항을 적용하는데, 이 과정을 **리컴포지션(recomposition)** 이라고 합니다. 본질적으로, 컴포지션은 런타임에 UI 구조와 컴포저블 함수의 상태를 모두 관리하는 Jetpack Compose의 중추 역할을 합니다.

-----

## 컴포지션 생성하기

컴포지션은 컴포저블 함수를 화면에 렌더링될 수 있는 UI 계층 구조로 변환하는 과정을 의미합니다. 컴포지션은 Jetpack Compose가 작동하는 방식의 핵심으로, 프레임워크가 상태의 변경을 추적하고 UI를 효율적으로 업데이트할 수 있게 합니다. 컴포지션을 생성하고 관리하는 방법은 다음과 같습니다.

### 1. `ComponentActivity.setContent()` 함수 사용

컴포지션을 생성하는 가장 일반적인 방법은 `ComponentActivity`나 `ComposeView`에서 제공하는 **`setContent` 함수**를 사용하는 것입니다. 이 함수는 컴포지션을 초기화하고 그 안에 표시될 콘텐츠를 정의합니다.

```kotlin
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent // setContent 임포트
import androidx.compose.runtime.Composable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setContent 블록 내에서 컴포저블 함수를 호출하여 UI를 정의
        setContent {
            MyComposableContent()
        }
    }
}

@Composable
fun MyComposableContent() {
    // 여기에 UI 컴포넌트들을 정의합니다
}
```

위 예시에서 볼 수 있듯이, `setContent`는 컴포저블 함수를 렌더링하고 컴포지션을 시작하는 책임을 지며, Compose UI의 진입점(entry point)이 됩니다.

#### setContent() 내부 동작 살펴보기

`ComponentActivity.setContent`를 더 깊이 들여다보면, 내부적으로 **`ComposeView`에 의존**하며, `ComposeView.setContent`를 호출하여 컴포지션을 초기화하고 생성한다는 것을 알 수 있습니다.

```kotlin
// ComponentActivity의 setContent 확장 함수 내부 구현 (개념적 표현)
public fun ComponentActivity.setContent(
    parent: CompositionContext? = null,
    content: @Composable () -> Unit
) {
    // 1. 기존에 ComposeView가 있는지 확인
    val existingComposeView = window.decorView
        .findViewById<ViewGroup>(android.R.id.content)
        .getChildAt(0) as? ComposeView

    if (existingComposeView != null) {
        // 2. 기존 ComposeView가 있다면 재사용
        with(existingComposeView) {
            setParentCompositionContext(parent)
            setContent(content)
        }
    } else {
        // 3. 기존 ComposeView가 없다면 새로 생성
        ComposeView(this).apply {
            // setContentView 이전에 content와 parent를 설정하여
            // ComposeView가 연결(attach)될 때 컴포지션을 생성하도록 함
            setParentCompositionContext(parent)
            setContent(content)
            // 인플레이션 과정과 연결 리스너들이 이미 존재하는 것을 볼 수 있도록
            // content view를 설정하기 전에 뷰 트리 소유자들을 설정
            setOwners()
            setContentView(this, DefaultActivityContentLayoutParams)
        }
    }
}
```

이 함수를 단계별로 분석해 보면, 먼저 액티비티의 뷰 계층에서 기존 `ComposeView`를 가져오려고 시도한 후, 찾지 못하면 새 인스턴스를 생성합니다.

**1단계: 기존 `ComposeView` 확인**

```kotlin
val existingComposeView =
    window.decorView.findViewById<ViewGroup>(android.R.id.content).getChildAt(0) as? ComposeView
```

이 단계는 액티비티에 이미 `ComposeView`가 있는 경우, 불필요하게 새 인스턴스를 생성하는 대신 재사용할 수 있도록 보장합니다.

**2단계: 기존 `ComposeView` 재사용**
기존 `ComposeView`가 발견되면, 제공된 경우 새로운 부모 `CompositionContext`와 새로운 컴포저블 콘텐츠로 업데이트됩니다.

```kotlin
if (existingComposeView != null)
    with(existingComposeView) {
        setParentCompositionContext(parent)
        setContent(content)
    }
```

**3단계: 새 `ComposeView` 생성**
`ComposeView`가 없으면, 함수는 새 인스턴스를 생성하고 액티비티 레이아웃에 추가하기 전에 설정합니다.

```kotlin
ComposeView(this).apply {
    setParentCompositionContext(parent)
    setContent(content)
    setOwners()
    setContentView(this, DefaultActivityContentLayoutParams)
}
```

위 코드에서 볼 수 있듯이, 안드로이드를 타겟팅하는 Compose UI는 내부적으로 **전통적인 뷰 시스템**, 특히 `ComposeView` 내에서 최종적으로 렌더링됩니다. 이는 Jetpack Compose와 안드로이드 SDK 렌더링 시스템 간의 **다리(bridge) 역할**을 합니다.

-----

### 2. XML 레이아웃에 Compose 삽입하기 (`ComposeView`)

만약 전통적인 안드로이드 뷰 계층 구조에 Compose를 통합하고 싶다면, **`ComposeView`** 를 사용할 수 있습니다. 이를 통해 XML로 정의된 레이아웃 내에 컴포지션을 생성할 수 있으며, 이는 `setContent` API가 내부적으로 작동하는 방식과 유사하게 기능합니다.

**프로그래밍 방식:**

```kotlin
import androidx.compose.ui.platform.ComposeView

// ...
val composeView = ComposeView(context).apply {
    setContent {
        MyComposableContent()
    }
}
// 이 composeView를 기존 ViewGroup에 추가
```

**XML 레이아웃 방식:**
XML 레이아웃에 `<androidx.compose.ui.platform.ComposeView>` 태그를 포함할 수도 있습니다.

```xml
<LinearLayout ...>
    <TextView ... />
    <androidx.compose.ui.platform.ComposeView
        android:id="@+id/my_compose_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
    <Button ... />
</LinearLayout>
```

-----

## 요약

**컴포지션**은 컴포저블 함수를 렌더링하여 Compose UI의 계층적 구조를 구축하고 관리하는 과정입니다. 컴포지션을 생성하는 것은 컴포저블 함수로 UI를 정의하고, `ComponentActivity.setContent`나 `ComposeView`를 기반으로 하는 `ComposeView.setContent`와 같은 메커니즘을 사용하여 컴포지션을 초기화하는 것을 포함합니다.

-----

## Q. `ComposeView`는 전통적인 뷰 시스템과 Compose UI 시스템을 어떻게 연결하며, 언제 사용해야 하나요?

`ComposeView`는 전통적인 안드로이드 뷰(View) 시스템과 현대적인 Jetpack Compose UI 시스템이라는 두 가지 다른 패러다임을 함께 사용할 수 있도록 연결해주는 핵심적인 **상호운용성(interoperability)** 컴포넌트입니다.

-----

### 1. `ComposeView`의 역할: 두 UI 시스템을 잇는 다리

`ComposeView`는 두 UI 시스템 사이에서 다리(bridge) 역할을 수행하며, 다음과 같은 방식으로 작동합니다.

#### 1.1. `ComposeView`는 안드로이드 `View`다

  * 가장 중요한 점은 `ComposeView`가 `androidx.compose.ui.platform.ComposeView`라는 클래스로, 안드로이드의 전통적인 `View` 시스템의 일부(정확히는 `ViewGroup`을 상속)라는 것입니다.
  * 이는 `ComposeView`가 다른 `View`들(예: `TextView`, `Button`, `LinearLayout`)처럼 XML 레이아웃에 배치될 수 있고, `LayoutParams`를 가질 수 있으며, 프로그래밍 방식으로 `ViewGroup`에 추가될 수 있음을 의미합니다. 즉, 전통적인 뷰 계층 구조에 완벽하게 참여할 수 있습니다.

#### 1.2. 컴포지션(Composition)을 호스팅하는 컨테이너

  * `ComposeView`의 주된 역할은 Jetpack Compose로 만들어진 UI 계층 구조, 즉 **컴포지션(Composition)을 자신의 내부에 호스팅하는 컨테이너**가 되는 것입니다.
  * 개발자는 `composeView.setContent { ... }` 메서드를 사용하여, 해당 `ComposeView` 내부에 렌더링될 `@Composable` 함수들을 정의합니다.

#### 1.3. 연결 메커니즘

`ComposeView`는 두 시스템 간의 '번역기'처럼 작동합니다.

  * **뷰 시스템 → Compose:**
      * `ComposeView`는 부모로부터 전통적인 뷰 시스템의 생명주기 콜백(`onAttachedToWindow`, `onDetachedFromWindow` 등)과 레이아웃 제약 조건(`onMeasure`, `onLayout`)을 전달받습니다.
      * `ComposeView`는 이 정보를 사용하여 자신의 내부에 있는 Compose UI 트리의 생명주기(`LifecycleOwner`), 상태(`ViewModelStoreOwner`, `SavedStateRegistryOwner`), 그리고 레이아웃 제약 조건(`Constraints`)을 적절히 관리하고 설정합니다.
  * **Compose → 뷰 시스템:**
      * Compose의 렌더링 파이프라인(컴포지션 → 레이아웃 → 그리기)이 완료되면, 최종적으로 그려질 UI는 `ComposeView`의 `onDraw` 단계에서 해당 뷰의 `Canvas`에 렌더링됩니다.
      * 즉, `ComposeView`는 전통적인 뷰 시스템의 그리기 파이프라인 내에서 Compose 콘텐츠를 위한 렌더링 표면(surface) 역할을 합니다.

-----

### 2. `ComposeView`를 사용해야 하는 시나리오

`ComposeView`는 주로 기존 뷰 시스템과 Compose를 함께 사용해야 할 때, 특히 점진적인 전환 과정에서 매우 유용합니다.

#### 2.1. 점진적인 마이그레이션 (Gradual Migration)

  * **시나리오:** 전체가 전통적인 뷰 시스템(XML 레이아웃 및 프래그먼트)으로 구축된 대규모의 기존 애플리케이션이 있을 때, 전체 앱을 한 번에 Compose로 재작성하는 것은 비현실적이고 위험합니다.
  * **사용법:**
      * 화면 단위 또는 컴포넌트 단위로 점진적인 마이그레이션을 시작할 수 있습니다.
      * 기존 XML 레이아웃의 특정 부분(예: `FrameLayout`이나 `LinearLayout`)을 `<androidx.compose.ui.platform.ComposeView>` 태그로 교체하고, 해당 부분만 Compose로 새롭게 구현할 수 있습니다. 이를 통해 나머지 XML 구조나 액티비티 로직을 건드리지 않고도 새로운 기술을 점진적으로 도입할 수 있습니다.

#### 2.2. 기존 뷰 기반 아키텍처에 새로운 Compose 화면 추가

  * **시나리오:** 앱의 화면 이동이 `FragmentTransaction`을 사용하는 전통적인 프래그먼트 기반으로 구현되어 있을 때, 새로운 화면(`NewFeatureFragment`)은 전체를 Compose로 작성하고 싶을 경우.
  * **사용법:**
      * `NewFeatureFragment`의 `onCreateView()` 메서드에서 프로그래밍 방식으로 `ComposeView` 인스턴스를 생성하고, `setContent { ... }`를 통해 UI를 정의한 후 이 `ComposeView`를 반환합니다.
      * 앱의 나머지 내비게이션 시스템은 기존 방식 그대로 프래그먼트를 다루게 되므로, 새로운 화면만 Compose로 깔끔하게 구현할 수 있습니다.
    <!-- end list -->
    ```kotlin
    // NewFeatureFragment.kt
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MyNewScreenInCompose()
            }
        }
    }
    ```

#### 2.3. 재사용 가능한 Compose 컴포넌트를 기존 뷰 시스템에서 활용

  * **시나리오:** Jetpack Compose로 매우 멋지고 재사용 가능한 UI 컴포넌트(예: 커스텀 차트, 복잡한 입력 필드)를 만들었고, 이 컴포넌트를 여전히 XML로 작성된 여러 기존 화면에서 사용하고 싶을 경우.
  * **사용법:** 해당 컴포넌트가 필요한 각 XML 레이아웃에 `<ComposeView>`를 배치하고, 코드에서 각 `ComposeView` 인스턴스의 `setContent`를 호출하여 재사용 가능한 `@Composable` 함수를 표시합니다.

#### 2.4. 특정 기능에 Compose가 더 적합할 때

  * **시나리오:** 대부분의 화면은 XML로 간단하게 구현할 수 있지만, 특정 부분에 복잡하고 동적인 애니메이션이나 상태에 따라 크게 변하는 UI가 필요하여 Compose로 구현하는 것이 훨씬 쉬울 경우.
  * **사용법:** 주된 레이아웃은 XML로 구성하고, 해당 특정 부분에만 `ComposeView`를 사용하여 Compose의 장점을 활용합니다.

-----

### 3. (참고) 반대 방향의 통합: `AndroidView` 컴포저블

반대로, 대부분의 UI가 Jetpack Compose로 작성된 환경에서 `MapView`, `AdView`와 같은 전통적인 안드로이드 뷰나 레거시 커스텀 뷰를 포함해야 할 때는, **`AndroidView`** 라는 컴포저블을 사용하여 기존 뷰를 Compose UI 계층 구조 내에 삽입할 수 있습니다.

-----

### 4. 결론

`ComposeView`는 **전통적인 뷰 시스템과 현대적인 Compose UI를 연결하는 핵심적인 상호운용성 도구**입니다. 이는 기존 프로젝트를 Compose로 **점진적으로 마이그레이션**하거나, 하나의 앱 내에서 **두 UI 툴킷의 장점을 모두 활용**하고자 할 때 필수적으로 사용됩니다. `ComposeView`를 올바르게 이해하고 사용하면, 대규모 코드베이스의 현대화 작업을 더 안전하고 체계적으로 진행할 수 있습니다.