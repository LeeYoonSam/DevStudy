# 윈도우(Window)란 무엇인가?

**윈도우(Window)** 는 화면에 표시되는 액티비티(Activity)나 다른 UI 컴포넌트의 **모든 뷰(View)를 담는 컨테이너**를 나타냅니다. 이는 뷰 계층 구조(View hierarchy)에서 최상위 요소이며, 애플리케이션의 UI와 디스플레이(화면) 사이의 다리 역할을 합니다.

모든 액티비티, 다이얼로그(Dialog), 또는 토스트(Toast) 메시지는 `Window` 객체에 연결되어 있으며, 이 `Window` 객체는 자신이 포함하는 뷰들에 대한 레이아웃 파라미터, 애니메이션, 그리고 전환(transition) 효과를 제공합니다.

### 윈도우(Window)의 주요 특징

`Window` 클래스는 다음과 같은 몇 가지 주요 기능을 제공합니다.

* **데코 뷰 (DecorView):**
    `Window`는 뷰 계층의 루트 뷰(root view) 역할을 하는 `DecorView`를 포함합니다. `DecorView`는 일반적으로 상태 표시줄(status bar), 내비게이션 바(navigation bar), 그리고 앱의 콘텐츠 영역을 포함합니다.
* **레이아웃 파라미터 (Layout Parameters):**
    윈도우는 크기, 위치, 가시성과 같은 레이아웃 파라미터를 사용하여 뷰가 어떻게 배열되고 표시되는지를 정의합니다. 이러한 파라미터는 프로그래밍 방식으로 사용자 정의할 수 있습니다.
* **입력 처리 (Input Handling):**
    윈도우는 입력 이벤트(예: 터치 제스처, 키 입력)를 처리하고 이를 적절한 뷰로 전달(dispatch)합니다.
* **애니메이션 및 전환 (Animations and Transitions):**
    윈도우는 화면을 열거나 닫거나, 화면 간 전환 시 애니메이션을 지원합니다.
* **시스템 장식 (System Decorations):**
    윈도우는 상태 표시줄이나 내비게이션 바와 같은 시스템 UI 요소를 표시하거나 숨길 수 있습니다.

### 윈도우 관리 (Window Management)

윈도우는 **`WindowManager`** 에 의해 관리됩니다. `WindowManager`는 윈도우의 추가, 제거 또는 업데이트를 담당하는 시스템 서비스입니다. 이를 통해 서로 다른 윈도우(예: 앱 윈도우, 시스템 다이얼로그, 알림)가 기기에서 올바르게 공존하고 상호작용하도록 보장합니다.

### 윈도우(Window)의 일반적인 사용 사례

* **액티비티 윈도우 사용자 정의:**
    `getWindow()` 메서드를 사용하여 액티비티의 윈도우 동작을 수정할 수 있습니다. 예를 들어, 상태 표시줄을 숨기거나 배경을 변경할 수 있습니다.
    ```kotlin
    // 예시: Figure 120. systemUiVisibility.kt
    // 상태 표시줄 숨기기 (전체 화면 플래그 사용)
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    // 윈도우 배경을 검은색으로 변경
    window.setBackgroundDrawable(ColorDrawable(Color.BLACK))
    ```
* **다이얼로그 생성:**
    다이얼로그는 전용 윈도우를 사용하여 구현되므로 다른 UI 요소 위에 떠 있는 형태로 표시될 수 있습니다.
* **오버레이 사용:**
    윈도우는 `TYPE_APPLICATION_OVERLAY`를 통해 시스템 수준 기능이나 헤드업 알림과 같은 오버레이를 만드는 데 사용될 수 있습니다.
* **멀티 윈도우 모드 처리:**
    안드로이드는 분할 화면이나 PIP(Picture-in-Picture) 모드와 같은 기능을 활성화하기 위해 다중 윈도우를 지원합니다.

### 요약

`Window` 클래스는 안드로이드에서 앱의 뷰와 UI 요소를 위한 최상위 컨테이너를 제공하는 필수적인 구성 요소입니다. 이는 앱이 화면, 시스템 UI, 사용자 입력과 상호작용하는 방식을 사용자 정의할 수 있게 하며, `WindowManager`에 의한 관리는 안드로이드 환경과의 원활한 통합을 보장합니다.

---

## Q. 단순한 레이아웃을 가진 액티비티가 화면에 표시될 때 화면에는 몇 개의 윈도우가 존재하며, 그것들은 무엇인가요?

단순한 레이아웃을 가진 액티비티(Activity) 하나가 화면에 표시될 때, 사용자가 보는 전체 화면을 구성하기 위해 일반적으로 **최소 3개 이상의 주요 윈도우(Window)** 가 존재하게 됩니다. 이들은 다음과 같습니다.

### 1. 애플리케이션의 메인 윈도우 (Activity's Main Window) - 1개

* **설명:** 현재 실행 중인 액티비티 자체를 위한 윈도우입니다. 이 윈도우는 액티비티의 `setContentView()`를 통해 설정된 레이아웃과 그 안의 모든 뷰(View)들을 담고 있는 최상위 컨테이너 역할을 합니다.
* **내용:**
    * **데코 뷰(DecorView):** 해당 윈도우의 루트 뷰로, 실제 앱 콘텐츠가 그려지는 영역과 시스템이 그리는 영역(예: 타이틀 바 - 테마에 따라 다름)을 포함합니다.
    * **앱 콘텐츠 영역:** 개발자가 정의한 레이아웃(XML 또는 코드로 생성)이 이 영역에 그려집니다.

### 2. 시스템 UI 윈도우 (System UI Windows) - 일반적으로 2개 이상

안드로이드 시스템은 애플리케이션 윈도우 외에도 자체적인 UI 요소들을 표시하기 위해 별도의 윈도우들을 사용합니다.

* **상태 표시줄 윈도우 (Status Bar Window):**
    * **설명:** 화면 상단에 위치하며 시간, 배터리 상태, Wi-Fi 신호, 알림 아이콘 등을 표시하는 영역입니다. 이는 시스템 UI의 일부로, 앱 윈도우와는 별개의 윈도우 레이어에서 관리될 수 있습니다.
    * **개수:** 일반적으로 1개.

* **내비게이션 바 윈도우 (Navigation Bar Window):**
    * **설명:** 화면 하단(또는 제스처 내비게이션의 경우 가장자리)에 위치하며 뒤로 가기, 홈, 최근 앱과 같은 시스템 내비게이션 컨트롤을 제공합니다. 이 또한 시스템 UI의 일부로 별도의 윈도우 레이어에서 관리될 수 있습니다.
    * **개수:** 일반적으로 1개. (기기 설정이나 상태에 따라 숨겨지거나 다를 수 있음)

### 3. (상황에 따라) 추가될 수 있는 윈도우

위의 필수적인 윈도우들 외에도 다음과 같은 윈도우들이 상황에 따라 존재할 수 있습니다.

* **입력 방식 편집기 (IME) 윈도우 / 키보드 윈도우:**
    * 사용자가 `EditText` 등에 포커스를 맞춰 키보드가 올라오면, 이 키보드 역시 자신만의 윈도우를 가집니다. "단순한 레이아웃이 표시될 때"라는 초기 상태에서는 보통 보이지 않습니다.
* **토스트(Toast) 메시지, 다이얼로그(Dialog), 팝업 윈도우(PopupWindow):**
    * 앱이 이러한 요소들을 표시하면, 각각은 자신만의 윈도우를 가질 수 있습니다. 질문은 "단순한 레이아웃"이므로, 액티비티 초기 표시 시에는 이런 것들이 없다고 가정합니다.
* **시스템 오버레이 윈도우:**
    * 다른 앱 위에 그리기 권한을 가진 앱(예: Facebook Messenger의 챗 헤드, 화면 녹화 앱의 컨트롤 버튼)이나 시스템 자체의 특정 알림(예: 헤드업 알림)은 별도의 오버레이 윈도우를 사용할 수 있습니다.

### 결론

따라서, **아주 기본적인 상황에서 단순한 레이아웃을 가진 액티비티 하나가 화면에 표시될 때, 최소한 다음과 같은 윈도우들이 존재합니다:**

1.  **애플리케이션의 메인 윈도우 (1개)**
2.  **상태 표시줄 윈도우 (1개)**
3.  **내비게이션 바 윈도우 (1개)**

이로 인해 **일반적으로 최소 3개의 윈도우**가 관여하여 사용자가 보는 화면을 구성한다고 할 수 있습니다. `WindowManager`는 이러한 모든 윈도우들의 Z-순서(화면에 쌓이는 순서), 크기, 위치 등을 관리합니다.

---

## 💡 프로 팁: WindowManager란 무엇인가?

**WindowManager(윈도우 매니저)** 는 화면에서 윈도우의 **배치, 크기, 모양을 관리**하는 안드로이드의 시스템 서비스입니다. 이는 애플리케이션과 안드로이드 시스템 간의 윈도우 관리 인터페이스 역할을 하여, 앱이 윈도우를 생성, 수정 또는 제거할 수 있게 합니다. 안드로이드의 윈도우는 전체 화면 액티비티부터 떠다니는 오버레이에 이르기까지 무엇이든 될 수 있습니다.

### WindowManager의 주요 책임

WindowManager는 시스템 내 윈도우 계층 구조를 관리하는 책임을 집니다. 이는 윈도우가 Z-순서(레이어링) 및 다른 시스템 윈도우와의 상호작용에 따라 올바르게 표시되도록 보장합니다. 예를 들어, 윈도우에 대한 포커스 변경, 터치 이벤트, 애니메이션을 처리합니다.

### 일반적인 사용 사례

* **사용자 정의 뷰 추가:** 개발자는 WindowManager를 사용하여 플로팅 위젯이나 시스템 오버레이와 같이 앱의 표준 액티비티 외부에서 사용자 정의 뷰를 표시할 수 있습니다.
* **기존 윈도우 수정:** 애플리케이션은 기존 윈도우의 속성(예: 크기 조절, 위치 변경, 투명도 변경)을 업데이트할 수 있습니다.
* **윈도우 제거:** `removeView()` 메서드를 사용하여 프로그래밍 방식으로 윈도우를 제거할 수 있습니다.

### WindowManager 사용하기

WindowManager 서비스는 `Context.getSystemService(Context.WINDOW_SERVICE)`를 통해 접근합니다.

아래는 WindowManager를 사용하여 화면에 플로팅 뷰를 추가하는 예시입니다.

**WindowManager를 사용하여 플로팅 뷰 추가하기**
```kotlin
val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

val floatingView = LayoutInflater.from(context).inflate(R.layout.floating_view, null)

val params = WindowManager.LayoutParams(
    WindowManager.LayoutParams.WRAP_CONTENT, // 너비
    WindowManager.LayoutParams.WRAP_CONTENT, // 높이
    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, // 윈도우 타입: 다른 앱 위에 표시되는 오버레이
    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,      // 윈도우 플래그: 포커스를 받지 않음
    PixelFormat.TRANSLUCENT                           // 픽셀 포맷: 투명 배경 가능
)
// params.gravity = Gravity.TOP or Gravity.START // 위치 지정 (예: 좌상단)
// params.x = 0
// params.y = 0

// 뷰를 윈도우에 추가
windowManager.addView(floatingView, params)
```
이 예시에서:
* `TYPE_APPLICATION_OVERLAY`는 뷰가 다른 앱 위에 표시될 수 있도록 허용합니다.
* `FLAG_NOT_FOCUSABLE`과 같은 플래그는 특별히 지정하지 않는 한 윈도우가 사용자 입력과 상호작용하지 않도록 만듭니다.

### 제한 사항 및 권한

시스템 오버레이와 같은 특정 유형의 윈도우에는 `SYSTEM_ALERT_WINDOW`와 같은 특별한 권한이 필요합니다. 안드로이드 8.0 (API 레벨 26)부터 시스템은 보안상의 이유로 오버레이 윈도우에 대해 더 엄격한 제한을 부과합니다.

### 요약

WindowManager는 안드로이드에서 윈도우를 관리하기 위한 근본적인 API입니다. 이를 통해 개발자는 표준 액티비티 생명주기 외부에서 프로그래밍 방식으로 뷰를 추가, 업데이트 및 제거할 수 있습니다. WindowManager를 올바르게 사용하면 플로팅 위젯이나 오버레이와 같은 고급 기능을 구현할 수 있지만, 권한과 사용자 경험을 신중하게 고려해야 합니다.

---

## 💡 프로 팁: PopupWindow란 무엇인가?

**PopupWindow(팝업 윈도우)** 는 기존 레이아웃 위에 **떠다니는 팝업 뷰**를 표시하는 데 사용되는 UI 컴포넌트입니다. 일반적으로 화면을 덮고 사용자의 상호작용이 있어야 닫히는 다이얼로그(Dialog)와 달리, PopupWindow는 더 유연하며 화면의 특정 위치에 배치될 수 있어 메뉴나 툴팁과 같은 임시적 또는 상황별 UI 요소에 자주 사용됩니다. PopupWindow의 특징은 다음과 같습니다.

* 사용자 정의 가능한 떠다니는 뷰에 콘텐츠를 표시합니다.
* 화면을 어둡게 하거나 차단할 필요가 없어 팝업 뒤의 다른 UI 컴포넌트와 상호작용할 수 있습니다.
* 사용자 정의 레이아웃, 애니메이션, 닫기 동작을 포함할 수 있습니다.
* 원활한 사용자 경험을 위해 터치 기반 닫기 및 포커스 제어를 지원합니다.

아래 예시 코드처럼 PopupWindow를 생성하고 표시할 수 있습니다.

**PopupWindowExample.kt**
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 팝업을 위한 커스텀 레이아웃 인플레이트
        val popupView = layoutInflater.inflate(R.layout.popup_layout, null)

        // PopupWindow 생성
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT, // 너비
            ViewGroup.LayoutParams.WRAP_CONTENT, // 높이
            true // 포커스 가능하게 설정 (true여야 팝업 외부 터치 시 닫힘 등 가능)
        )

        // 팝업 외부를 클릭하면 팝업 닫기
        // (참고: setOutsideTouchable(true)와 setBackgroundDrawable(ColorDrawable())도 함께 설정해야 할 수 있음)
        popupWindow.isOutsideTouchable = true // 외부 터치 가능하게
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 설정해야 외부 터치 인식

        // 또는 팝업 뷰 자체에 터치 리스너 설정 (다른 방식)
        /*
        popupView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_OUTSIDE) { // API 11 이하에서는 이 방식이 어려움
                 popupWindow.dismiss()
                 true
            } else {
                 false
            }
        }
        */

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener { anchorView -> // anchorView는 클릭된 버튼
            // 버튼에 맞춰 PopupWindow 표시
            popupWindow.showAsDropDown(anchorView)
        }
    }
}
```

### 요약

PopupWindow는 안드로이드에서 떠다니는 UI 요소를 표시하기 위한 다목적 도구입니다. 사용자 정의 레이아웃을 사용하고 유연한 위치 지정을 지원하는 능력 덕분에 주 앱 흐름을 방해하지 않으면서 사용자 상호작용을 향상시키는 상황별 메뉴, 툴팁 및 임시 팝업을 만드는 데 이상적입니다.