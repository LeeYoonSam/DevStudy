# View and ViewGroups
- [안드로이드에서 View 란?](#안드로이드에서-view-란)
- [Custom View 만들기](#custom-view-만들기)
- [ViewGroups 이란 무엇이며 View와 어떻게 다른지?](#viewgroups-이란-무엇이며-view와-어떻게-다른지)
- [ConstraintLayout 이란?](#constraintlayout-이란)
- [ViewTreeObserver가 무엇인지?](#viewtreeobserver가-무엇인지)

---

## [안드로이드에서 View 란?](https://blog.mindorks.com/create-your-own-custom-view)
- 앱의 레이아웃을 디자인하기 위해 제공하는 구성요소
- 뷰 내부에 일부 요소를 포함할 직사각형 영역으로 이해
- 모든 UI 구성 요소의 슈퍼 클래스

### ViewGroup
- View 여러개가 모인것을 ViewGroup 이라고 한다.
- 최상위 ViewGroup 은 부모이고 그 아래에 이쓴 모든 뷰 및 기타 뷰 그룹은 자식이다.

## Custom View 만들기
- 안드로이드에서 제공하지 않아서 Custom View 나 재사용 가능한 View 를 만들어야 할때 사용

### Custom Views type
- Custom Views: 모든 것을 그리는 것
- Custom View Groups: 기존 위젯을 사용하여 inflate 가능한 xml 파일을 형성하는 것

### Custom View 를 사용해야 하는 이유
- UI 구성 요소를 재사용
- Android 에서 제공하지 않는 새로운 interaction 추가

### Android 는 어떻게 UI를 그리는지?
> onMeasure() -> onLayout() -> onDraw()

- onMeasure: 위에서 아래로 UI 크기를 측정하는 곳에서 호출되고, 부모 컨테이너를 사용, onMeasure() 에서 child 는 부모가 제공한 제약 조건을 받는다.
- onLayout(): 위젯의 위치를 플로팅하기 위해 호출
- onDraw(): UI 렌더링

### Custom View 만드는 단계
1. 기존 위젯 클래스 확장(예: Button, TextView)
2. View 클래스 확장(예: View)

```kotlin
class MyCustomView : View
```
- 적어도 하나의 생성자를 재정의 해야 한다.

```kotlin
class MyCustomView(context: Context) : View(context)
```
- draw 를 위해 기본 activity 컨텍스트가 필요합니다.

```kotlin
class MyCustomView(context: Context, attrs: AttributeSet) : View(context, attrs)
```
- XML 에서 새 View 인스턴스 생성

```kotlin
class MyCustomView(context: Context, attrs: AttributeSet, defstyleAttr:Int) : View(context, attrs, defstyeAttr)
```
- 디자인 속성 사용

```kotlin
class MyCustomView(context: Context, attrs: AttributeSet, defstyleAttr:Int, defStyleRes:Int) : View(context, attrs, defstyeAttr,defStyleRes)
```
- theme 속성 사용
<br>

**invalidate(): View 에게 업데이트해야 함을 알림.**

### onMeasure()
```kotlin
override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    size = Math.min(measuredWidth, measuredHeight)
    setMeasuredDimension(newsize, newsize)
}
```
- onMeasure()에서 widthMeasureSpec 및 heightMeasureSpec을 매개변수로 얻습니다. 이 매개변수는 측정 사양의 크기 및 모드로 구성됩니다.

```kotlin
int mode = MeasureSpec.getMode(widthMeasureSpec);
int size = MeasureSpec.getSize(widthMeasureSpec);
```

**MeasureSpec Type**
- MeasureSpec.Exactly : view가 layout_width = "100dp"와 같이 XML에 지정된 정확한 크기여야 함을 지정합니다.
- MeasureSpec.AT_MOST : wrap_content를 사용하는 동안 뷰가 최대일 수 있음을 지정합니다.
- MeasureSpec.UNSPECIFIED : 뷰가 원하는 만큼의 공간을 차지할 수 있습니다.

## ViewGroups 이란 무엇이며 View와 어떻게 다른지?

### View
- View 객체는 Android에서 UI(User Interface) 요소의 기본 빌딩 블록
- View는 사용자의 행동에 반응하는 간단한 사각형 상자입니다.
- 예를들면 EditText, Button, CheckBox 등입니다.
- View는 모든 UI 클래스의 기본 클래스인 android.view.View 클래스를 참조합니다.

### ViewGroups
- ViewGroup은 보이지 않는 컨테이너입니다. 
- View 및 ViewGroup을 보유합니다.
- 예를 들어 LinearLayout은 Button(View) 및 기타 레이아웃을 포함하는 ViewGroup입니다.
- ViewGroup은 레이아웃의 기본 클래스입니다.

## [ConstraintLayout 이란?](https://blog.mindorks.com/using-constraint-layout-in-android-531e68019cd)
- ConstraintLayout은 단순하고 표현력이 뛰어나며 유연한 레이아웃 시스템과 Android Studio Designer 도구에 내장된 강력한 기능을 결합합니다. 
- 다양한 화면 크기와 변화하는 장치 방향에 자동으로 적응하는 반응형 사용자 인터페이스 레이아웃을 더 쉽게 만들 수 있습니다.

### ConstraintLayout 이 필요한 이유?
- 복잡한 화면 디자인을 더 쉽게 처리 할수 있고 레이아웃의 성능을 향상 시킨다.
- 중첩된 레이아웃이 있는 복잡한 화면 디자인도 빠르게 처리할 수 있다.
- 단일 레이아웃 인스턴스로 이전 레이아웃의 많은 기능을 달성할 수 있는 유연성 수준을 제공
- 평면 또는 얕은 레이아웃 계층 구조를 설계할 수 있다.
- 레이아웃이 덜 복잡해지고 런타임 시 사용자 인터페이스 렌더링 성능이 향상된다.
- 다양한 Android 기기 화면 크기를 처리하기 위해 구현되었다.

### 참고
[Learn Android ConstraintLayout - Part 1 - Everything you need to know](https://youtu.be/P2XHNDHI4PQ)
[Learn Android ConstraintLayout - Part 2 - Guideline, Barrier and Group](https://youtu.be/tUn_eGBr1n4)

## ViewTreeObserver가 무엇인지?
- 뷰 트리 옵저버는 뷰 트리의 전역 변경 사항을 알릴 수 있는 리스너를 등록하는 데 사용됩니다. 
- 전역 이벤트에는 전체 트리의 레이아웃, 드로잉 패스의 시작, 터치 모드 변경이 포함되지만 이에 국한되지는 않습니다. 
- ViewTreeObserver는 뷰 계층 구조에서 제공하는 것처럼 애플리케이션에서 인스턴스화해서는 안 됩니다. 
