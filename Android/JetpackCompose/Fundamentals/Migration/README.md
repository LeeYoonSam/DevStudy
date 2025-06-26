# XML 기반 프로젝트를 Jetpack Compose로 마이그레이션하는 전략은 무엇인가요?

XML 기반 UI를 Jetpack Compose로 마이그레이션하면 앱을 현대화하고 UI 개발을 단순화할 수 있습니다. 그러나 마이그레이션 과정은 기존 기능의 중단을 최소화하기 위해 신중한 계획과 실행이 필요합니다. 아래는 모범 사례와 공식 가이드라인에 기반한 주요 전략들입니다.

-----

## 주요 마이그레이션 전략

### 1\. 점진적 마이그레이션 (Incremental Migration)

점진적 마이그레이션은 동일한 프로젝트 내에서 XML과 Compose가 공존하도록 허용하면서 **Compose를 점진적으로 채택**하는 것을 포함합니다.

#### XML에 Compose 삽입하기 (`ComposeView` 사용)

`ComposeView`를 사용하여 기존 XML 레이아웃에 Compose 콘텐츠를 포함시킬 수 있습니다. 이 접근 방식은 XML 기반 화면의 특정 부분을 Compose 기능으로 향상시키고 싶을 때 유용합니다.

**XML 레이아웃에 `ComposeView` 추가:**

```xml
<androidx.compose.ui.platform.ComposeView
    android:id="@+id/compose_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

**코드에서 콘텐츠 설정:**

```kotlin
findViewById<ComposeView>(R.id.compose_view).setContent {
    Greeting("Hello Compose!")
}
```

#### Compose에 XML 삽입하기 (`AndroidView` 사용)

`AndroidView`를 사용하여 컴포저블 함수 내에 XML 기반 뷰를 포함시킬 수 있습니다. 이는 마이그레이션 중에 레거시 컴포넌트의 기능을 유지하는 데 도움이 됩니다.

```kotlin
@Composable
fun LegacyViewComposable() {
    AndroidView(factory = { context ->
        // XML 레이아웃을 인플레이트하여 기존 뷰를 생성
        LayoutInflater.from(context).inflate(R.layout.legacy_view, null)
    })
}
```

이러한 점진적 전략은 제어된 마이그레이션을 가능하게 하고 기존 기능이 손상될 위험을 최소화합니다.

### 2\. 화면 단위 마이그레이션 (Screen-by-Screen Migration)

실용적인 전략은 **한 번에 하나의 화면씩 마이그레이션**하는 것이며, 더 단순하거나 현대화가 가장 필요한 화면부터 시작합니다.

1.  Compose의 이점이 즉시 드러나는 화면(예: 동적 레이아웃 또는 복잡한 애니메이션이 있는 화면)을 식별합니다.
2.  이러한 화면들을 완전히 Compose로 다시 작성합니다.
3.  XML 레이아웃을 컴포저블 함수로 대체하고, Compose의 상태 주도 아키텍처를 활용합니다.

이 전략을 통해 개발자는 특정 기능에 집중하면서 점진적으로 Compose를 배울 수 있습니다. 화면을 마이그레이션할 때, 팀은 해당 화면을 Jetpack Compose로 완전히 전환한다는 명확한 목표를 갖게 되어 마이그레이션 과정을 시작하고 관리하기가 더 쉬워집니다.

### 3\. 점진적 컴포넌트 마이그레이션 (Gradual Component Migration)

전체 화면을 마이그레이션하는 대신, 개별적인 **재사용 가능한 컴포넌트**나 **디자인 시스템 전체**(예: 텍스트, 버튼 또는 사용자 정의 컴포넌트)를 마이그레이션할 수 있습니다. 아래 단계를 따를 수 있습니다.

1.  자주 사용되는 UI 컴포넌트나 디자인 시스템의 일부를 식별합니다.
2.  이러한 컴포넌트들을 컴포저블 함수로 다시 만듭니다.
3.  앱 내에서 해당 컴포넌트의 XML 버전을 새로운 컴포저블 컴포넌트로 교체합니다.

이 전략을 사용하면, 개별 컴포넌트를 전환하거나 디자인 시스템을 다시 구현함으로써 화면을 부분적으로 마이그레이션할 수 있습니다. 이 전략은 프로젝트 내 여러 화면에 걸쳐 공유되는 UI 컴포넌트에 대한 일관성을 보장하고 마이그레이션 노력을 줄여줍니다.

### 4\. 전체 재작성 (Full Rewrite)

방대한 레거시 코드를 가진 프로젝트나 **Compose 우선(Compose-first)** 을 목표로 하는 프로젝트의 경우, 전체 마이그레이션이 최선의 전략일 수 있습니다.

1.  테마, 레이아웃, 사용자 정의 컴포넌트를 Compose로 다시 만듭니다.
2.  XML을 완전히 대체하고, 전체 UI 스택에 Compose를 활용합니다.
3.  필요한 경우 앱의 아키텍처를 재정의하고, Compose 활용에 최적화된 MVI나 MVVM과 같은 현대적인 패턴을 채택합니다.

이 경우, 개발팀은 상당한 시간과 자원을 필요로 하며, 애플리케이션은 QA팀과 함께 철저한 테스트 및 검증을 거쳐야 합니다. 원활한 마이그레이션을 보장하려면 잘 구조화된 마이그레이션 계획이 매우 중요합니다. 만약 팀이 아직 Jetpack Compose에 익숙하지 않다면, 기능 퇴행(regression)을 유발할 위험이 증가하므로 신중한 계획이 더욱 필수적입니다.

### 5\. 라이브러리에 대한 상호운용성 활용

Compose와 XML의 상호운용성은 아직 Compose를 채택하지 않은 라이브러리까지 확장됩니다. [`ComposeView`](https://developer.android.com/develop/ui/compose/migrate/interoperability-apis/compose-in-views)나 [`AndroidView`](https://developer.android.com/develop/ui/compose/migrate/interoperability-apis/views-in-compose)를 사용하여 라이브러리에서 제공하는 UI 컴포넌트를 통합하면서 UI의 다른 부분을 마이그레이션할 수 있습니다.

### 6\. 마이그레이션 중 테스트 및 모니터링

마이그레이션 중에는 앱이 예상대로 작동하는지 확인하기 위해 테스트가 필수적입니다. [Compose의 테스트 라이브러리](https://developer.android.com/develop/ui/compose/testing)를 활용하여 새로운 컴포저블을 검증할 수 있습니다. 또한, 기존 XML 구현과 Compose를 비교하여 동등하거나 개선된 사용자 경험을 보장하기 위해 성능 프로파일링을 수행하는 것이 중요합니다.

-----

## 요약

Jetpack Compose로의 마이그레이션은 프로젝트의 범위와 목표에 따라 **점진적으로 또는 전체 재작성**으로 달성할 수 있습니다. [Compose의 상호운용성](https://developer.android.com/develop/ui/compose/migrate/interoperability-apis)을 활용하여 Compose와 XML을 혼용함으로써 원활한 전환을 할 수 있습니다. 컴포넌트 단위로 마이그레이션하든 화면 단위로 하든, 핵심은 위험을 최소화하면서 유지보수성 향상, 확장성, 현대적인 UI 경험과 같은 Compose의 장점을 극대화하는 전략을 채택하는 것입니다. 더 깊은 통찰력을 원한다면 다음 리소스들을 탐색해 보세요.

  * [안드로이드 공식 문서: 마이그레이션 전략](https://developer.android.com/develop/ui/compose/migrate/strategy)
  * [안드로이드 개발자 블로그: Sunflower 앱을 Jetpack Compose로 마이그레이션하기](https://medium.com/androiddevelopers/migrating-sunflower-to-jetpack-compose-f840fa3b9985) (영문)
  * [안드로이드 개발자 블로그: Jetpack Compose로 마이그레이션하기 — 상호운용성의 아름다운 이야기 [1부]](https://medium.com/androiddevelopers/migrating-to-jetpack-compose-an-interop-love-story-part-1-3693ca3ae981) (영문)
  * [안드로이드 개발자 블로그: Jetpack Compose로 마이그레이션하기 — 상호운용성의 아름다운 이야기 [2부]](https://medium.com/androiddevelopers/migrating-to-jetpack-compose-an-interop-love-story-part-2-370fdd978c33) (영문)

-----

## 💡 프로 팁: XML과 Jetpack Compose를 혼용하면 앱 크기에 영향이 있나요?

마이그레이션 중에 XML과 Jetpack Compose를 혼용하면 앱 크기에 영향을 미칠 수 있지만, 그 **효과는 비교적 미미합니다.** Jetpack Compose는 여러 라이브러리의 모음이며, 전체 R8 최적화를 사용할 경우 일반적으로 **최대 약 2MB** 정도의 크기를 추가합니다. 이는 메모리에 민감한 기기에서는 문제가 될 수 있지만, 현대의 안드로이드 기기들은 일반적으로 심각한 성능 문제 없이 이 추가 크기를 처리할 수 있습니다.

-----

## Q. XML에서 Compose로 마이그레이션할 때, 기존 뷰 기반 레이아웃 내부에 컴포저블을 어떻게 통합하며, 어떤 시나리오에서 이 접근 방식이 가장 유용한가요?

XML에서 Jetpack Compose로 마이그레이션할 때, 기존의 뷰(View) 기반 레이아웃 내부에 컴포저블(Composable)을 통합하는 것은 **점진적이고 안정적인 전환**을 위한 핵심 전략입니다. 이를 위해 안드로이드는 **`ComposeView`** 라는 강력한 상호운용성(interoperability) 컴포넌트를 제공합니다.

-----

### 1\. 기존 뷰 기반 레이아웃에 컴포저블을 통합하는 방법

#### 1.1. 핵심 컴포넌트: `ComposeView`

`ComposeView`는 안드로이드의 전통적인 `View`를 상속받은 특수한 뷰입니다. 따라서 다른 `Button`이나 `TextView`처럼 **XML 레이아웃에 배치하거나 코드에서 동적으로 추가**할 수 있습니다. `ComposeView`의 주된 역할은 자신의 내부에 Jetpack Compose로 만들어진 UI, 즉 **컴포지션(Composition)을 호스팅하는 컨테이너**가 되는 것입니다.

#### 1.2. 방법 1: XML 레이아웃에 `ComposeView` 선언하기

가장 일반적인 방법으로, 기존 XML 파일의 원하는 위치에 `<ComposeView>` 태그를 추가합니다.

**1단계: XML 레이아웃에 `ComposeView` 추가**

```xml
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="이것은 기존의 XML 뷰입니다." />

    <androidx.compose.ui.platform.ComposeView
        android:id="@+id/my_compose_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp" />

</LinearLayout>
```

**2단계: 코드에서 `setContent` 호출**
Activity나 Fragment의 코드에서 `findViewById` 또는 ViewBinding을 사용하여 `ComposeView`의 참조를 얻은 후, `setContent` 메서드를 호출하여 내부에 표시할 `@Composable` 함수를 지정합니다.

```kotlin
// MainActivity.kt
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val composeView = findViewById<ComposeView>(R.id.my_compose_view)
    composeView.setContent {
        // 여기에 Compose로 만든 UI를 정의합니다.
        MyComposableFeature(text = "Compose가 여기에 있어요!")
    }
}

@Composable
fun MyComposableFeature(text: String) {
    Text(
        text = text,
        color = Color.Blue,
        style = MaterialTheme.typography.headlineSmall
    )
}
```

#### 1.3. 방법 2: 코드에서 `ComposeView` 동적 추가하기

XML을 수정하지 않고, 프로그래밍 방식으로 `ComposeView`를 생성하여 기존 `ViewGroup`(예: `LinearLayout`)에 추가할 수도 있습니다.

```kotlin
// MainActivity.kt
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val existingLayout = findViewById<LinearLayout>(R.id.my_linear_layout)

    // ComposeView를 동적으로 생성
    val myComposeView = ComposeView(this).apply {
        setContent {
            MyComposableFeature(text = "동적으로 추가된 Compose UI")
        }
    }

    // 기존 레이아웃에 추가
    existingLayout.addView(myComposeView)
}
```

-----

### 2\. 이 접근 방식이 가장 유용한 시나리오

`ComposeView`를 통해 XML 레이아웃 내에 컴포저블을 통합하는 방식은 다음과 같은 시나리오에서 매우 유용합니다.

#### 2.1. 점진적인 마이그레이션 (Gradual Migration) 🚶‍♂️

  * 전체 앱을 한 번에 재작성하는 대신, **화면의 일부 또는 특정 컴포넌트부터** Compose로 전환하고 싶을 때 이상적입니다. 예를 들어, 안정적으로 잘 작동하는 복잡한 XML 화면에서 위험성이 적은 작은 부분(예: 프로필 카드, 버튼 그룹)부터 `ComposeView`로 교체해 나갈 수 있습니다. 이는 마이그레이션의 위험을 줄이고 팀이 점진적으로 Compose에 적응할 수 있게 해줍니다.

#### 2.2. 새로운 기능 또는 컴포넌트 추가 ✨

  * 기존에 XML로 만들어진 화면에 **새로운 기능을 추가**해야 할 때, 해당 기능의 UI를 Compose로 작성하는 것이 더 빠르고 효율적일 수 있습니다. 새로운 기능을 `ComposeView`를 통해 기존 화면에 쉽게 통합할 수 있습니다.

#### 2.3. 동적인 UI 또는 복잡한 애니메이션 구현 🎬

  * 화면의 특정 부분이 **상태에 따라 UI가 크게 변하거나, 복잡하고 인터랙티브한 애니메이션**이 필요한 경우, 이는 명령형 방식의 기존 뷰 시스템보다 선언적인 Compose로 구현하는 것이 훨씬 쉽습니다. 전체 화면은 XML로 유지하되, 이 동적인 부분만 `ComposeView`로 구현하여 Compose의 장점을 최대한 활용할 수 있습니다.

#### 2.4. 재사용 가능한 Compose 컴포넌트 활용 🧩

  * 디자인 시스템에 따라 버튼, 입력 필드, 카드 등 재사용 가능한 UI 컴포넌트를 Compose로 미리 만들어 두었다면, `ComposeView`를 사용하여 이러한 **공통 컴포저블을 아직 XML로 남아있는 여러 레거시 화면에 일관되게 적용**할 수 있습니다.

#### 2.5. (응용) 기존 `RecyclerView` 아이템에 Compose 적용

  * 기존에 `RecyclerView`를 사용하고 있는 화면에서, 각 목록 아이템의 뷰만 Compose로 구현하고 싶을 때 `ViewHolder` 내에 `ComposeView`를 배치하여 사용할 수 있습니다. `onBindViewHolder`에서 각 아이템 데이터에 맞게 `composeView.setContent`를 호출하는 방식입니다. (단, 새로운 목록을 만들 때는 `LazyColumn`/`LazyRow` 사용이 권장됩니다.)

-----

### 3\. 결론

`ComposeView`는 전통적인 뷰 시스템과 Jetpack Compose를 연결하는 핵심적인 **상호운용성 도구**입니다. 이를 통해 개발자는 기존의 안정적인 XML 기반 프로젝트를 한 번에 모두 재작성하는 위험을 감수하는 대신, **점진적이고 전략적으로 Compose를 도입**할 수 있습니다. 새로운 기능을 추가하거나 복잡한 UI를 구현하는 등 Compose의 장점이 극대화되는 부분부터 적용해 나감으로써, 낮은 위험으로 앱을 현대화하고 개발 생산성을 높일 수 있습니다.


## Q. 안드로이드 앱을 Jetpack Compose로 마이그레이션할 때 화면 단위 방식과 컴포넌트 단위 방식의 장단점은 무엇인가요?

안드로이드 앱을 XML에서 Jetpack Compose로 마이그레이션할 때, **화면 단위(Screen-by-Screen)** 방식과 **컴포넌트 단위(Component-by-Component)** 방식은 각각 뚜렷한 장단점을 가집니다. 프로젝트의 현재 상태, 팀의 경험, 그리고 최종 목표에 따라 적절한 전략을 선택하거나 두 가지를 혼합하여 사용해야 합니다.

-----

### 1\. 화면 단위(Screen-by-Screen) 마이그레이션

#### 1.1. 접근 방식

하나의 전체 화면(주로 하나의 `Activity` 또는 `Fragment`에 해당)을 통째로 Jetpack Compose로 다시 작성하는 전략입니다. 마이그레이션이 완료된 화면은 XML 레이아웃 없이 오직 `@Composable` 함수들로만 구성됩니다.

#### 1.2. 장점

  * **👍 아키텍처의 일관성:**
    마이그레이션된 화면 내에서는 온전히 Compose의 상태 관리 패턴(`ViewModel`, `StateFlow`, `@Composable` 함수 등)을 일관되게 적용할 수 있습니다. 해당 화면 내에서 기존 뷰 시스템과의 상호운용성으로 인한 복잡성을 고민할 필요가 없습니다.
  * **👍 명확한 분리 및 진행 상황 추적:**
    마이그레이션이 완료된 화면과 레거시 XML 화면 간의 경계가 명확합니다. "전체 20개 화면 중 5개 화면 마이그레이션 완료"와 같이 진행 상황을 추적하고 관리하기가 용이합니다.
  * **👍 Compose의 이점 극대화:**
    해당 화면에서는 Compose의 모든 장점(개발 속도 향상, 강력한 애니메이션, 코드 간소화, 대화형 미리보기 등)을 온전히 누릴 수 있습니다.
  * **👍 집중적인 노력:**
    팀은 한 번에 하나의 논리적 단위(화면)에 노력을 집중할 수 있어, 계획 수립과 테스트가 더 명확하고 수월해집니다.

#### 1.3. 단점

  * **👎 복잡한 화면의 높은 초기 비용:**
    만약 크고 복잡한 화면부터 마이그레이션을 시작한다면, 해당 화면 하나를 전환하는 데 매우 높은 초기 비용과 시간이 소요될 수 있습니다. 이는 가시적인 결과물이 나오기까지의 기간을 길게 만들 수 있습니다.
  * **👎 높은 리스크:**
    마이그레이션 과정에서 버그가 발생하면 해당 화면 전체의 기능에 영향을 미칠 수 있으며, 만약 그 화면이 핵심적인 사용자 흐름의 일부라면 리스크가 큽니다.
  * **👎 일시적인 사용자 경험(UX) 불일치:**
    마이그레이션 기간 동안 사용자는 두 가지 스타일의 UI를 경험하게 될 수 있습니다. Compose로 전환된 화면은 기존 XML 화면과 애니메이션, 터치 피드백 등에서 미묘한 차이를 보여 일시적으로 통일되지 않은 경험을 줄 수 있습니다.
  * **👎 공유 컴포넌트 문제:**
    만약 해당 화면이 아직 XML로 남아있는 공유 컴포넌트를 사용해야 한다면, `AndroidView` 컴포저블을 사용하여 XML 뷰를 Compose 내에 삽입해야 하므로 상호운용성으로 인한 복잡성이 추가됩니다.

-----

### 2\. 컴포넌트 단위(Component-by-Component) 마이그레이션

#### 2.1. 접근 방식

앱 전체에서 공통적으로 사용되는 재사용 가능한 UI 요소(예: 버튼, 카드, 다이얼로그, 커스텀 입력 필드 등)를 먼저 식별합니다. 이 개별 컴포넌트들을 Compose로 다시 만든 후, `ComposeView`를 사용하여 기존 XML 레이아웃에서 해당 컴포넌트들을 점진적으로 교체해 나가는 전략입니다.

#### 2.2. 장점

  * **👍 낮은 리스크 및 점진적 적용:**
    한 번에 하나의 버튼이나 카드처럼 작은 단위를 변경하므로, 핵심 기능이 손상될 위험이 크게 줄어듭니다. 매우 안정적으로 마이그레이션을 진행할 수 있습니다.
  * **👍 빠르고 가시적인 개선:**
    공통으로 사용되는 하나의 컴포넌트를 개선하면, 해당 컴포넌트를 사용하는 여러 화면의 모양과 느낌을 한 번에 빠르게 개선할 수 있습니다.
  * **👍 재사용성 촉진:**
    이 전략은 팀이 처음부터 재사용 가능한 컴포넌트 라이브러리를 Compose로 구축하도록 유도합니다. 이는 훌륭한 Compose 개발의 핵심 원칙 중 하나입니다.
  * **👍 팀의 학습 곡선 완화:**
    팀원들이 전체 화면이라는 큰 부담을 갖기 전에, 작고 관리 가능한 작업을 통해 Compose를 학습하고 적응하는 데 매우 좋은 방법입니다.

#### 2.3. 단점

  * **👎 상호운용성 오버헤드:**
    앱 전반에 걸쳐 `ComposeView`를 많이 사용하게 됩니다. 이는 동일 화면 내에 기존 뷰 코드와 Compose 코드가 혼재하게 만들어, 관리하고 디버깅하기 더 복잡할 수 있습니다.
  * **👎 화면 내 아키텍처 불일치:**
    하나의 화면이 서로 다른 상태 관리 로직을 가질 수 있습니다 (XML 뷰를 위한 기존 방식 + Compose 컴포넌트를 위한 새로운 방식). 이는 단일 화면의 코드 흐름을 이해하기 어렵게 만들 수 있습니다.
  * **👎 성능 고려 사항:**
    각 `ComposeView`는 약간의 오버헤드를 가집니다. 단일 화면에 여러 개의 개별 `ComposeView`를 사용하는 것은 해당 화면 전체를 하나의 통합된 Compose 트리로 구성하는 것보다 성능이 떨어질 수 있습니다.
  * **👎 완전한 Compose 전환까지의 긴 여정:**
    이 접근 방식은 매우 점진적이므로, 특정 화면 하나에서 XML을 완전히 제거하기까지 오랜 시간이 걸릴 수 있습니다.

-----

### 3\. 어떤 전략을 선택해야 하는가? (요약 비교)

| | 화면 단위 마이그레이션 | 컴포넌트 단위 마이그레이션 |
| :--- | :--- | :--- |
| **적합한 경우** | • 새로 만드는 화면</br>• 디자인 전면 개편이 필요한 화면</br>• 독립성이 높은 단순한 화면 | • 크고 복잡한 레거시 앱</br>• 디자인 시스템을 먼저 업데이트할 때</br>• 낮은 리스크로 점진적 개선을 원할 때 |
| **장점** | 아키텍처 일관성, Compose 장점 극대화 | 낮은 리스크, 빠른 부분 개선, 팀 학습 용이 |
| **단점** | 높은 초기 비용, 높은 리스크 | 상호운용성 복잡성, 화면 내 아키텍처 불일치 |

**하이브리드 접근 방식:**
실제로는 많은 팀이 두 전략을 혼합하여 사용합니다. 먼저 공통 컴포넌트(버튼, 텍스트 스타일 등)를 Compose로 만들어 디자인 시스템을 구축한 다음, 신규 화면이나 마이그레이션 우선순위가 높은 화면부터 화면 단위로 전환하는 방식이 효과적일 수 있습니다.

-----

### 4\. 결론

두 전략 중 정답은 없습니다. **화면 단위 마이그레이션**은 특정 화면을 완전히 현대화하여 Compose의 이점을 빠르게 누릴 수 있지만 초기 비용과 리스크가 큽니다. 반면, **컴포넌트 단위 마이그레이션**은 안정적이고 점진적이지만 상호운용성으로 인한 복잡성이 따릅니다. 따라서 프로젝트의 규모, 기존 코드의 상태, 팀의 Compose 숙련도, 그리고 비즈니스 목표를 종합적으로 고려하여 가장 적합한 전략을 선택하거나 두 가지를 현명하게 조합해야 합니다.