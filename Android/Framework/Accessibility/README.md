# 접근성은 어떻게 보장하나요?

**접근성(Accessibility)** 은 시각, 청각 또는 신체적 장애와 같이 장애가 있는 사람을 포함하여 **모든 사람이 애플리케이션을 사용할 수 있도록 보장**하는 것입니다. 접근성 기능을 구현하면 사용자 경험을 향상시키고 WCAG(웹 콘텐츠 접근성 지침)와 같은 글로벌 접근성 표준을 준수할 수 있습니다.

### 콘텐츠 설명(Content Descriptions) 활용

콘텐츠 설명은 UI 요소에 대한 텍스트 레이블을 제공하여, TalkBack과 같은 **스크린 리더**가 시각 장애가 있는 사용자에게 해당 요소를 음성으로 안내할 수 있게 합니다. 버튼, 이미지, 아이콘과 같이 상호작용 가능하거나 정보를 제공하는 요소에는 `android:contentDescription` 속성을 사용하세요. 만약 요소가 장식용이고 스크린 리더가 무시해야 한다면, `android:contentDescription`을 `null`로 설정하거나 `View.IMPORTANT_FOR_ACCESSIBILITY_NO`를 사용하세요.

```xml
<ImageView
    android:id="@+id/profile_image"
    android:src="@drawable/user_avatar"
    android:contentDescription="@string/user_profile_picture" />

<ImageView
    android:id="@+id/decorative_divider"
    android:src="@drawable/divider_line"
    android:importantForAccessibility="no" />
```

### 동적 글꼴 크기(Dynamic Font Sizes) 지원

앱이 기기 설정에서 사용자가 지정한 글꼴 크기 환경설정을 존중하도록 보장해야 합니다. 텍스트 크기에는 **`sp` 단위**를 사용하여 접근성 설정에 따라 자동으로 크기가 조정되도록 하세요.

```xml
<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@string/welcome_message"
    android:textSize="16sp" />
```

### 포커스 관리 및 내비게이션(Focus Management and Navigation)

특히 사용자 정의 뷰(custom views), 다이얼로그, 양식(forms)에서 포커스 동작을 올바르게 관리해야 합니다. 키보드 및 D-패드 사용자를 위한 논리적인 내비게이션 경로를 정의하려면 `android:nextFocusDown`, `android:nextFocusUp` 및 관련 속성을 사용하세요. 또한, 스크린 리더로 앱을 테스트하여 요소 간에 포커스가 자연스럽게 이동하는지 확인하세요.

### 색상 대비 및 시각적 접근성(Color Contrast and Visual Accessibility)

저시력 또는 색각 이상이 있는 사용자의 가독성을 향상시키려면 텍스트와 배경색 간에 충분한 대비를 제공해야 합니다. Android Studio의 **접근성 스캐너(Accessibility Scanner)** 와 같은 도구는 앱의 색상 대비를 평가하고 최적화하는 데 도움이 될 수 있습니다.

### 사용자 정의 뷰(Custom Views)와 접근성

사용자 정의 뷰를 만들 때는 `AccessibilityDelegate`를 구현하여 스크린 리더가 사용자 정의 UI 컴포넌트와 상호작용하는 방식을 정의해야 합니다. `onInitializeAccessibilityNodeInfo()` 메서드를 재정의하여 사용자 정의 요소에 의미 있는 설명과 상태를 제공하세요.

```kotlin
class MyCustomView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    init {
        accessibilityDelegate = object : AccessibilityDelegate() {
            override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(host, info)
                // 이 뷰에 대한 접근성 정보 설정 (예: 역할, 설명, 상태)
                info.className = MyCustomView::class.java.name
                info.contentDescription = "사용자 정의 설명"
                // info.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK) // 클릭 가능하면 추가
            }
        }
    }
}

class CustomView(context: Context) : View(context) {
    init {
        importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_YES
        setAccessibilityDelegate(object : AccessibilityDelegate() {
            override fun onInitializeAccessibilityNodeInfo(host: View, info: Accessibilit\
yNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(host, info)
                info.text = "Custom component description"
            }
        })
    }
}
```

### 접근성 테스트(Testing for Accessibility)

Android Studio의 **접근성 스캐너(Accessibility Scanner)** 및 **레이아웃 검사기(Layout Inspector)** 와 같은 도구를 사용하여 접근성 문제를 식별하고 수정하세요. 이러한 도구는 앱이 보조 기술에 의존하는 사용자에게 접근 가능한지 확인하는 데 도움이 됩니다.

### 요약

안드로이드 애플리케이션에서 접근성을 보장하는 것은 콘텐츠 설명 제공, `sp` 단위를 사용한 동적 글꼴 크기 지원, 내비게이션을 위한 포커스 관리, 적절한 색상 대비 보장, 사용자 정의 뷰에 대한 접근성 지원 추가 등을 포함합니다. 안드로이드 도구를 활용하고 철저히 테스트함으로써 모든 사용자에게 포용적이고 접근 가능한 애플리케이션을 구축할 수 있습니다. 접근성에 대한 자세한 내용은 아래 공식 문서를 확인하세요.

  * [앱 접근성 향상](https://developer.android.com/guide/topics/ui/accessibility/apps?hl=ko)
  * [앱 접근성 개선 원칙](https://developer.android.com/guide/topics/ui/accessibility/principles?hl=ko)
  * [앱 접근성 테스트](https://developer.android.com/guide/topics/ui/accessibility/testing?hl=ko)

---

## Q. 동적 글꼴 크기를 지원하기 위한 몇 가지 모범 사례는 무엇이며, 텍스트 크기에 `dp` 대신 `sp` 단위를 사용하는 것이 선호되는 이유는 무엇인가요?

사용자가 시스템 설정에서 글꼴 크기를 변경했을 때 앱의 텍스트도 함께 조절되도록 하는 동적 글꼴 크기 지원은 접근성의 매우 중요한 부분입니다. 이를 올바르게 구현하기 위한 몇 가지 모범 사례와 `dp` 대신 `sp` 단위를 사용하는 이유를 설명드리겠습니다.

### 1. 텍스트 크기에 `dp` 대신 `sp` 단위를 사용하는 것이 선호되는 이유

안드로이드에서 UI 요소의 크기를 지정하는 단위에는 여러 가지가 있지만, 텍스트 크기에는 `sp`를, 그 외 레이아웃 치수(너비, 높이, 여백 등)에는 주로 `dp`를 사용합니다.

* **`dp` (Density-independent Pixels, 밀도 독립형 픽셀):**
    * 화면의 물리적 밀도(dpi, dots per inch)에 기반한 추상 단위입니다.
    * 다양한 화면 밀도를 가진 기기에서 UI 요소가 일관된 물리적 크기를 갖도록 보장합니다 (예: 고밀도 화면에서는 1dp가 더 많은 픽셀로, 저밀도 화면에서는 더 적은 픽셀로 변환됨).
    * 주로 레이아웃의 너비, 높이, 패딩, 마진 등 일반적인 UI 요소의 크기를 지정하는 데 사용됩니다.
    * **하지만 `dp`는 사용자가 시스템 설정에서 변경한 '글꼴 크기' 환경설정을 반영하지 않습니다.**

* **`sp` (Scale-independent Pixels, 배율 독립형 픽셀):**
    * `dp`와 유사하게 화면 밀도에 따라 크기가 조절됩니다.
    * **가장 큰 차이점은 `sp` 단위는 사용자가 기기의 접근성 설정에서 지정한 '글꼴 크기' 환경설정에도 영향을 받는다는 것입니다.**
    * 즉, 사용자가 시스템 글꼴 크기를 '크게'로 설정하면 `sp`로 지정된 텍스트는 더 커지고, '작게'로 설정하면 더 작아집니다.

결론적으로, **텍스트 크기에 `sp` 단위를 사용하는 주된 이유는 사용자의 글꼴 크기 선호도를 존중하여 접근성을 향상시키기 위함입니다.** `dp`는 이러한 사용자 설정을 반영하지 않으므로 텍스트 크기에는 부적합합니다.

### 2. 동적 글꼴 크기를 지원하기 위한 모범 사례

1.  **모든 텍스트 크기에 `sp` 단위 사용:**
    * 가장 기본적인 원칙입니다. XML 레이아웃 파일에서 `android:textSize` 속성 값을 `sp` 단위로 지정합니다 (예: `"16sp"`).
    * 코드에서 프로그래밍 방식으로 텍스트 크기를 설정할 때도 `sp` 단위를 기준으로 설정해야 합니다. 예를 들어, `TextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeInSp)`를 사용합니다.

2.  **다양한 글꼴 크기로 철저히 테스트:**
    * 개발 중 기기나 에뮬레이터의 '설정 > 디스플레이 > 글꼴 크기' (또는 접근성 메뉴)에서 글꼴 크기를 최소, 기본, 크게, 최대로 변경해가며 앱의 UI가 어떻게 보이는지 반드시 테스트해야 합니다.
    * **확인 사항:**
        * **텍스트 잘림(Truncation/Clipping):** 텍스트가 너무 커져서 컨테이너 밖으로 잘리거나, 말줄임표(`...`)로 표시될 때 중요한 정보가 가려지지 않는지 확인합니다.
        * **레이아웃 깨짐(Layout Breaking):** UI 요소들이 서로 겹치거나, 정렬이 틀어지거나, 화면 밖으로 벗어나는 등의 레이아웃 문제가 발생하는지 확인합니다.
        * **가독성(Readability):** 모든 글꼴 크기에서 텍스트가 명확하게 읽히는지 확인합니다.

3.  **유연한 레이아웃 설계:**
    * 텍스트를 담는 `TextView`의 너비나 높이에 대해, 텍스트 내용에 따라 크기가 조절될 수 있도록 `wrap_content`를 적절히 사용합니다.
    * `ConstraintLayout`을 사용할 때는 유연한 제약 조건(예: 체인, 가이드라인, 퍼센티지, `wrap_content`와 제약조건 조합)을 활용하여 텍스트 크기 변화에 잘 대응하도록 합니다.
    * 텍스트를 포함하는 컨테이너에 고정된 높이(fixed height)를 지정하는 것을 피해야 합니다. 텍스트가 커질 공간이 필요합니다.
    * 제한된 공간에서는 `TextView`의 `minLines`, `maxLines` 속성과 `ellipsize` 속성을 함께 사용하여 텍스트가 넘칠 때를 대비하되, 중요한 정보가 잘리지 않도록 주의합니다.

4.  **텍스트 크기에 기반한 고정된 치수 사용 지양:**
    * 기본 글꼴 크기를 가정하고 UI 요소의 크기를 고정된 픽셀 값으로 설정하지 마세요. 레이아웃은 다양한 텍스트 크기를 수용할 수 있어야 합니다.

5.  **줄 간격(Line Spacing) 관리:**
    * 사용자 정의 줄 간격이 필요하다면 `android:lineSpacingExtra` 또는 `android:lineSpacingMultiplier` 속성을 사용하되, 이 값들도 글꼴 크기 변화에 따라 적절히 보이는지 테스트합니다. 큰 글꼴에서 줄 간격이 너무 좁아 가독성을 해치지 않도록 합니다.

6.  **아이콘과 텍스트의 조화:**
    * 아이콘과 텍스트 레이블이 함께 사용될 경우, 텍스트 레이블의 크기가 변해도 아이콘과의 정렬이 틀어지거나 아이콘을 가리지 않도록 레이아웃을 구성합니다. 벡터 드로어블(Vector Drawable)과 같이 크기 조절이 용이한 아이콘 사용을 고려합니다.

7.  **충분한 터치 영역 크기 확보:**
    * 텍스트 크기가 작아지더라도, 해당 텍스트를 포함하는 버튼이나 클릭 가능한 요소의 전체 터치 영역은 최소한의 크기(Material Design 권장 48dp x 48dp)를 유지하도록 합니다.

8.  **중요 정보의 말줄임 처리 주의:**
    * 텍스트가 말줄임표(`...`)로 표시될 때, 특히 큰 글꼴 크기에서 중요한 정보가 숨겨지지 않도록 주의해야 합니다. 필요하다면 긴 정보를 표시하는 다른 방법(예: "더보기" 기능)을 고려합니다.

9.  **텍스트 크기 조절 제한 (극히 드문 경우):**
    * 매우 예외적으로, 극단적으로 큰 글꼴 크기가 특정 중요 레이아웃을 완전히 깨뜨리고 다른 디자인 해결책이 없을 경우에만, 특정 `TextView`에 대해 프로그래밍 방식으로 글꼴 크기 조절의 상한선을 둘 수 있습니다. 하지만 이는 접근성 원칙에 어긋나므로 최후의 수단으로만 고려해야 하며, 가급적 완전히 적응형 레이아웃을 목표로 해야 합니다.

이러한 모범 사례들을 따르면 사용자의 글꼴 크기 선호도에 관계없이 모든 사용자에게 일관되고 접근 가능한 앱 경험을 제공하는 데 도움이 됩니다.

---

## Q. 개발자는 보조 기술에 의존하는 사용자를 위해 적절한 포커스 관리 및 내비게이션을 어떻게 보장할 수 있으며, 접근성 문제를 테스트하는 데 도움이 되는 도구는 무엇인가요?

안드로이드 앱 개발 시, 스크린 리더(예: TalkBack), 스위치 제어, 키보드 등 보조 기술에 의존하는 사용자를 위해 적절한 포커스(focus) 관리와 직관적인 내비게이션(navigation)을 보장하는 것은 매우 중요합니다. 이는 모든 사용자가 앱의 모든 기능을 동등하게 이용할 수 있도록 하는 핵심 요소입니다.

### 1. 적절한 포커스 관리 및 내비게이션 보장 방법

#### 1.1. 논리적인 포커스 순서 정의

* **자연스러운 순서 (기본):** 기본적으로 포커스는 XML 레이아웃 파일에 요소가 배치된 순서(일반적으로 위에서 아래로, 왼쪽에서 오른쪽으로 - LTR 언어 기준)대로 이동합니다. 이 자연스러운 순서가 사용자에게 논리적인지 확인하는 것이 중요합니다.
* **명시적 포커스 순서 지정:**
    * `android:nextFocusForward` (API 26 이상): 사용자가 앞으로 탐색할 때(예: 키보드의 Tab 키) 다음에 포커스를 받을 요소를 명시적으로 지정합니다. 이는 이전의 방향별 속성보다 더 견고할 수 있습니다.
    * `android:nextFocusDown`, `android:nextFocusUp`, `android:nextFocusLeft`, `android:nextFocusRight`: 특정 방향으로 이동 시 포커스를 받을 요소를 명시적으로 정의합니다. 복잡한 2D 내비게이션(예: D-패드 사용)에 유용합니다. 예측 가능하고 명확한 경로를 만들기 위해 신중하게 사용해야 합니다.
* **관련 요소 그룹화:**
    * `ViewGroup`에 `android:focusable="true"` 속성을 설정하고, 필요한 경우 `android:focusableInTouchMode="true"`를 추가하여 관련된 항목 그룹을 하나의 포커스 단위처럼 동작하게 할 수 있습니다.
    * `ViewGroup`의 `android:descendantFocusability` 속성(`blocksDescendants`, `beforeDescendants`, `afterDescendants`)을 사용하여 하위 요소들의 포커스 처리 방식을 제어할 수 있습니다.

#### 1.2. 명확한 포커스 표시

* 현재 포커스를 받은 요소는 시각적으로 명확하게 구별되어야 합니다 (예: 뚜렷한 하이라이트, 테두리, 배경색 변경). 안드로이드는 기본 포커스 하이라이트를 제공하지만, 가시성을 높이거나 브랜드 일관성을 위해 사용자 정의할 수 있으며, 이때 색상 대비 요구 사항을 충족해야 합니다.
* 명확한 사용자 정의 대안을 제공하지 않는 한, 기본 포커스 하이라이트를 제거하거나 가리지 않도록 주의해야 합니다.

#### 1.3. 사용자 정의 뷰(Custom View)의 접근성

* 사용자 정의 뷰를 만드는 경우, 기본적으로 접근성 포커스나 동작을 처리하지 않을 수 있습니다.
* **포커스 가능하게 만들기:** `view.isFocusable = true` (코드에서) 또는 XML에서 `android:focusable="true"`를 설정합니다. 터치 모드에서도 포커스를 받게 하려면 `isFocusableInTouchMode`도 설정합니다.
* **`AccessibilityDelegateCompat` 구현:** `onInitializeAccessibilityNodeInfo()` 메서드를 재정의하여 다음 정보를 제공합니다.
    * 적절한 클래스 이름 (`info.className`).
    * 의미 있는 콘텐츠 설명 (`info.contentDescription`).
    * 클릭 동작(`info.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK)`), 포커스 동작 등 필요한 접근성 액션.
    * 뷰의 현재 상태 (체크됨, 선택됨, 비활성화됨 등).
* **접근성 이벤트 전송:** 뷰의 상태가 변경되거나 특정 동작이 수행될 때 `ViewCompat.performAccessibilityAction()` 또는 `View.announceForAccessibility()` 등을 사용하여 적절한 접근성 이벤트를 전송합니다.
* **키보드 입력 처리:** 사용자 정의 뷰가 상호작용 가능하다면 `onKeyDown()`, `onKeyUp()` 등을 통해 키보드 입력을 적절히 처리해야 합니다.

#### 1.4. 스크린 리더 호환성 (예: TalkBack)

* **콘텐츠 설명(Content Description):** 모든 상호작용 요소(버튼, 사용자 정의 컨트롤 등)와 중요한 이미지에는 간결하고 명확한 `android:contentDescription`을 제공해야 합니다. 순수하게 장식용인 요소는 `android:contentDescription="@null"`로 설정하거나 `android:importantForAccessibility="no"`로 지정하여 스크린 리더가 무시하도록 합니다.
* **콘텐츠 그룹화:** 스크린 리더가 하나의 단위로 함께 읽어야 하는 관련된 정보들은 `ViewGroup`으로 묶고, 해당 `ViewGroup`에 `android:screenReaderFocusable="true"` (API 28+) 또는 `android:focusable="true"`와 적절한 `contentDescription`을 설정합니다. (단, `android:focusable="true"`는 키보드 내비게이션에도 영향을 줄 수 있으므로 주의).
* **음성 안내(Announcements):** 중요한 동적 변경 사항(예: "장바구니에 항목 추가됨", "로딩 완료")은 `View.announceForAccessibility(CharSequence)`를 사용하여 스크린 리더 사용자에게 알려줍니다.
* **라이브 리전(Live Regions):** 내용이 동적으로 변경되어 사용자에게 알려야 하는 뷰(예: 오류 메시지, 타이머, 실시간 점수판)에는 `android:accessibilityLiveRegion` 속성(`polite` 또는 `assertive`)을 사용합니다.

#### 1.5. 모달 및 다이얼로그 처리

* 다이얼로그나 모달 창이 나타나면 포커스가 해당 창 내부로 이동해야 합니다.
* 다이얼로그/모달이 닫힐 때까지 포커스는 그 내부에 갇혀 있어야 합니다 (즉, 사용자가 탭 키 등으로 다이얼로그 뒤의 요소로 이동할 수 없어야 함).
* 다이얼로그/모달이 닫히면 포커스는 원래 다이얼로그/모달을 열었던 요소나 그 근처의 합리적인 요소로 돌아가야 합니다.

#### 1.6. 키보드 접근성

* 모든 상호작용 요소는 키보드만으로도 접근하고 조작할 수 있어야 합니다 (Tab/Shift+Tab으로 이동, Enter/Space로 활성화).
* 앱이 Android TV나 게임 컨트롤러와 함께 사용될 수 있다면 D-패드 내비게이션을 테스트해야 합니다.

### 2. 접근성 문제 테스트에 도움이 되는 도구

1.  **접근성 스캐너 (Accessibility Scanner - Google 제공 앱):**
    * 기기 내에서 직접 앱 화면을 스캔하여 접근성 개선을 위한 제안 사항을 제공합니다.
    * 콘텐츠 설명 누락, 터치 영역 크기 부족, 낮은 색상 대비율 등 일반적인 문제를 확인하고, 문제 해결을 위한 관련 문서 링크도 제공합니다.

2.  **TalkBack (톡백 - 안드로이드 내장 스크린 리더):**
    * **수동 테스트:** 설정 > 접근성 메뉴에서 TalkBack을 활성화한 후, 스와이프 제스처와 두 번 탭하기 등을 사용하여 앱을 탐색합니다. 이는 시각 장애 사용자의 입장에서 앱을 경험하는 가장 좋은 방법입니다.
    * **확인 사항:** 모든 콘텐츠가 음성으로 안내되는가? 읽는 순서가 논리적인가? 상호작용 요소가 명확히 설명되고 조작 가능한가? 포커스 이동이 예측 가능한가?

3.  **Android Studio 도구:**
    * **레이아웃 검사기 (Layout Inspector):** 뷰의 접근성 관련 속성(예: 콘텐츠 설명)을 보여줄 수 있습니다.
    * **Lint 검사:** Android Studio에 내장된 Lint 도구는 XML 레이아웃이나 Java/Kotlin 코드에서 일부 일반적인 접근성 문제(예: `ImageView`에 `contentDescription` 누락)를 자동으로 검사하고 경고를 표시합니다.
    * **Compose UI 미리보기의 접근성 검사:** Jetpack Compose를 사용하는 경우, UI 미리보기에서 접근성 정보를 표시하고 잠재적인 문제를 강조할 수 있습니다.

4.  **키보드 전용 내비게이션 테스트:**
    * 실제 키보드를 연결하거나 에뮬레이터에서 키보드 입력을 활성화한 후, Tab, Shift+Tab, 방향키, Enter, Space 키만을 사용하여 모든 상호작용 요소를 탐색하고 조작해 봅니다.

5.  **색상 대비 검사 도구:**
    * WCAG 지침에 따라 색상 대비율을 확인할 수 있는 다양한 온라인 도구나 브라우저 확장 프로그램이 있습니다. 접근성 스캐너 앱도 이 기능을 일부 제공합니다.

6.  **수동 검토 및 사용자 피드백:**
    * 앱의 UI와 사용자 흐름을 접근성 관점에서 직접 검토합니다.
    * 가능하다면, 실제 장애를 가진 사용자로부터 피드백을 받는 것이 매우 중요합니다.

이러한 방법들을 통해 개발자는 보조 기술 사용자도 앱의 모든 기능을 원활하게 사용할 수 있도록 포커스 관리와 내비게이션을 개선하고, 관련 문제들을 효과적으로 테스트하고 수정할 수 있습니다.