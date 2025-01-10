# Ui
- [안드로이드에서 View 란?](#안드로이드에서-view-란)
- [Custom View 만들기](#custom-view-만들기)
- [ViewGroups 이란 무엇이며 View와 어떻게 다른지?](#viewgroups-이란-무엇이며-view와-어떻게-다른지)
- [LayoutInflater와 LayoutViewpagerWithPreviewIndicatorBinding.inflate()에 대한 설명](#layoutinflater와-layoutviewpagerwithpreviewindicatorbindinginflate에-대한-설명)
- [ConstraintLayout 이란?](#constraintlayout-이란)
- [ViewTreeObserver가 무엇인지?](#viewtreeobserver가-무엇인지)
- [include, merge, ViewStub 차이](#include-merge-viewstub-차이)
- [FragmentContainerView 란?](#fragmentcontainerview-란)
- [Glide 에서 리사이즈를 처리하지 않게 하는 방법 2가지](#glide-에서-리사이즈를-처리하지-않게-하는-방법-2가지)
- [Bitmap-prepareToDraw](#bitmap-preparetodraw)

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


## LayoutInflater와 LayoutViewpagerWithPreviewIndicatorBinding.inflate()에 대한 설명
### LayoutInflater란?
LayoutInflater는 Android에서 XML 레이아웃 파일을 메모리 상의 View 객체로 변환하는 역할을 합니다. 즉, XML로 정의된 UI를 실제 화면에 보여줄 수 있는 객체로 만들어주는 것이죠.

### 주요 기능:
- XML 레이아웃 파일을 읽어들여 View 객체 생성
- 생성된 View 객체에 대한 참조 반환

### 사용 이유:
- 코드에서 직접 UI를 구성하는 것보다 XML 레이아웃 파일을 사용하여 UI를 디자인하는 것이 더 효율적이고 가독성이 좋기 때문입니다.
- 런타임에 동적으로 View를 생성하고 추가할 수 있습니다.

### LayoutViewpagerWithPreviewIndicatorBinding.inflate()
이 메소드는 View Binding 라이브러리를 사용하여 생성된 바인딩 클래스의 메소드입니다. View Binding은 XML 레이아웃 파일의 View를 직접 참조할 수 있도록 해주는 기능으로, findViewById를 사용하지 않고도 View에 접근할 수 있게 합니다.

### 기능:
- 지정된 레이아웃 파일을 inflate하여 바인딩 객체를 생성합니다.
- 생성된 바인딩 객체를 통해 레이아웃 파일 내의 모든 View에 직접 접근할 수 있습니다.

### 매개변수:
- inflater: LayoutInflater 객체
- parent: 부모 ViewGroup (일반적으로 null)
- attachToRoot: 생성된 View를 부모 ViewGroup에 바로 붙일지 여부

### attachToRoot 파라미터
- true: 생성된 View를 부모 ViewGroup에 바로 붙입니다. 이 경우, 부모 ViewGroup의 레이아웃 파라미터가 적용됩니다.
- false: 생성된 View를 부모 ViewGroup에 붙이지 않고, 단순히 View 객체만 반환합니다. 이 경우, 생성된 View의 레이아웃 파라미터를 직접 설정해야 합니다.

### 예시:
```kotlin
val inflater = LayoutInflater.from(context)
val binding = LayoutViewpagerWithPreviewIndicatorBinding.inflate(inflater, null, false)

// 생성된 View를 ViewGroup에 추가
val container = findViewById<ViewGroup>(R.id.container)
container.addView(binding.root)
```

위 예시에서:
- LayoutInflater.from(context)를 통해 LayoutInflater 객체를 생성합니다.
- inflate() 메소드를 호출하여 레이아웃 파일을 inflate하고, 바인딩 객체를 생성합니다.
- attachToRoot를 false로 설정하여 생성된 View를 바로 부모 ViewGroup에 붙이지 않고, 직접 container ViewGroup에 추가합니다.

### 왜 attachToRoot를 false로 설정하는가?
- 유연성: 생성된 View를 원하는 ViewGroup에 추가하고, 레이아웃 파라미터를 자유롭게 설정할 수 있습니다.
- 재사용성: 생성된 View를 다른 곳에서도 재사용할 수 있습니다.

### 결론
LayoutInflater는 XML 레이아웃 파일을 View 객체로 변환하는 중요한 역할을 하며, View Binding과 함께 사용하면 더욱 효율적이고 안전하게 UI를 구성할 수 있습니다. attachToRoot 파라미터는 생성된 View를 어떻게 사용할지에 따라 적절하게 설정해야 합니다.


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


## [include, merge, ViewStub 차이]

### include
> 재사용하고 싶은 컴포넌트를 레이아웃에 넣을 때, include 를 사용한다.

ex) Toolbar 와 같이 재사용하는 컴포넌트를 적용할때 유용

### merge
> merge는 include 태그와 함께 더미뷰를 생성해 준다. include 됐을 때, merge 태그는 무시되고 그 자식 View 들을 태그의 부모 View 에 추가한다.

- 레이아웃을 그릴 때 depth 가 깊어 질수록 성능은 저하된다. merge 태그를 사용해 depth 를 줄여 성능을 개선할 수 있다.

**activity_main.xml**
```xml
<LinearLayout
	xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="mathc_parent">
    
    <include
    	layout="@layout/merge_button"/>
   
</LinearLayout>
```

**merge_button.xml**
```xml
<merge xmlns:android="http://schemas.android.com/apk/res/android">

    <Button
        android:id="@+id/btnAdd"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="add"/>

    <Button
        android:id="@+id/btnDelete"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="delete"/>

</merge>
```

### ViewStub
> 레이아웃에 사용 빈도가 매우 낮은 복잡한 뷰가 필요한 때가 있습니다. 종류(예: 항목 세부정보, 진행률 표시기 또는 실행취소 메시지)에 상관없이 필요할 때만 뷰를 로드하여 메모리 사용을 줄이고 렌더링 속도를 높일 수 있습니다.
리소스 로드를 지연하는 것은 나중에 앱에 필요할 수도 있는 복잡한 뷰가 있을 때 사용할 중요한 기법입니다. 복잡하고 거의 사용되지 않는 뷰의 `ViewStub`를 정의하여 이 기법을 구현할 수 있습니다.
ViewStub 는 사이즈가 0인 더미 뷰이다. 기능은 include 와 동일하고 `setVisibility()` 또는 `inflate` 되었을 때, view 를 그리기 시작한다. 즉 `lazy include` 라고 생각하면 된다.

```
주의: viewstub 선언할 때, inflate 된 후 선언해야 한다. 그렇지 않으면 NullPointerException 이 발생한다.
```

### 정리
정리
- 뷰의 재활용 및 좀 더 나은 구조의 레이아웃을 그릴 수 있는 3가지 태그에 대해 알아봤습니다.

- **include** - 뷰의 재활용을 통해 유지보수의 이점이 있습니다.

- **viewStub** - 레이아웃에 사용 빈도가 매우 낮은 복잡한 뷰가 필요한 때, 자주 사용하지 않은 뷰를 그릴때 지정해 주면 불필요하게 레이아웃을 그릴 필요가 없어 성능 개선에 도움이 됩니다.

- **merge** - 레이아웃을 그릴 때 depth 가 깊어 질수록 성능은 저하됩니다. merge 태그를 사용해 depth 를 줄여 성능을 개선할 수 있습니다.

### 참고
- https://black-jin0427.tistory.com/180
- https://velog.io/@woga1999/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-xml-%ED%83%9C%EA%B7%B8-include-merge-ViewStub-%EC%B0%A8%EC%9D%B4

## FragmentContainerView 란?
FragmentContainerView는 프래그먼트용으로 특별히 설계된 맞춤형 레이아웃입니다. FrameLayout을 확장하여 Fragment Transaction을 안정적으로 처리할 수 있으며, Fragment 동작과 조정하는 추가 기능도 있습니다. 

FragmentContainerView는 일반적으로 액티비티의 xml 레이아웃에서 설정되는 프래그먼트의 컨테이너로 사용해야 합니다.

```xml
<androidx.fragment.app.FragmentContainerView
xmlns:android="http://schemas.android.com/apk/res/android"
xmlns:app="http://schemas.android.com/apk/res-auto"
android:id="@+id/fragment_container_view"
android:layout_width="match_parent"
android:layout_height="match_parent">
</androidx.fragment.app.FragmentContainerView>
```

FragmentContainerView는 `android:name` 속성을 사용하여 프래그먼트를 추가하는 데 사용할 수도 있습니다. 

FragmentContainerView는 다음과 같은 일회성 작업을 수행합니다.

- Fragment의 새 인스턴스를 만듭니다. 
- Fragment.onInflate 호출 
- FragmentTransaction을 실행하여 적절한 FragmentManager에 Fragment를 추가합니다.

FragmentManager.findFragmentByTag를 사용하여 추가된 프래그먼트를 검색할 수 있도록 하는 `android:tag`를 선택적으로 포함할 수 있습니다.

```xml
<androidx.fragment.app.FragmentContainerView
xmlns:android="http://schemas.android.com/apk/res/android"
xmlns:app="http://schemas.android.com/apk/res-auto"
android:id="@+id/fragment_container_view"
android:layout_width="match_parent"
android:layout_height="match_parent"
android:name="com.example.MyFragment"
android:tag="my_tag">
</androidx.fragment.app.FragmentContainerView>
```
- FragmentContainerView는 Fragment 사용 사례 이외의 다른 ViewGroups(FrameLayout, LinearLayout 등)의 대체물로 사용되어서는 안 됩니다.

- FragmentContainerView는 Fragment의 Fragment.onCreateView에서 반환된 View만 허용합니다. 다른 View를 추가하려고 하면 IllegalStateException이 발생합니다.

- 17 이상의 API용 FragmentContainerView에서는 레이아웃 애니메이션 및 전환이 비활성화됩니다. 그렇지 않으면 FragmentTransaction.setCustomAnimations를 통해 애니메이션을 수행해야 합니다. animateLayoutChanges가 true로 설정되거나 setLayoutTransition이 직접 호출되면 UnsupportedOperationException이 발생합니다.

- 종료 애니메이션을 사용하는 프래그먼트는 FragmentContainerView에 대해 다른 모든 Fragment보다 먼저 그려집니다. 이렇게 하면 기존 프래그먼트가 보기 상단에 표시되지 않습니다.

### 참고
- [FragmentContainerView](https://developer.android.com/reference/androidx/fragment/app/FragmentContainerView)

## Glide 에서 리사이즈를 처리하지 않게 하는 방법 2가지
1. Target.SIZE_ORIGINAL 사용
```
.override(view.context.getDisplayInfo().widthPixels, Target.SIZE_ORIGINAL)
```
- Target.SIZE_ORIGINAL 을 사용해서 실제 이미지의 height 에 맞춰서 세팅하도록 설정

- 일반적으로는 큰 이슈가 없을것으로 예상되나, 큰 이미지를 여러개 보여줘야 한다면 GPU 렌더링시 문제 발생

2. CustomTarget 을 사용해서 비트맵을 직접 사용
```
.into(object : CustomTarget<Bitmap>() {
    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
        resource.run {
            prepareToDraw()
            view.setImageBitmap(resource)
        }
    }

    override fun onLoadCleared(placeholder: Drawable?) {
        view.setImageDrawable(placeholder)
    }
})
```

- 비트맵을 로드하기전 prepareToDraw() 를 사용해서 이미지 디코딩된 캐시를 활용
- override 로 사이즈를 조절하지 않고 받은 비트맵을 그대로 사용
- scaleType 등이 적용되면 이미지가 작게 보일수 있음
- android:adjustViewBounds="true" 를 적용해서 종횡비에 맞게 뷰크기를 결정

## Bitmap-prepareToDraw
그리는 데 사용되는 비트맵과 연결된 캐시를 만듭니다.

Build.VERSION_CODES.N부터 이 호출은 Bitmap이 아직 업로드되지 않은 경우 RenderThread의 GPU에 대한 비동기 업로드를 시작합니다. 하드웨어 가속을 사용하면 비트맵이 렌더링되기 위해 GPU에 업로드되어야 합니다. 이것은 기본적으로 비트맵을 처음 그릴 때 수행되지만 비트맵의 크기에 따라 프로세스에 몇 밀리초가 걸릴 수 있습니다. Bitmap을 수정하고 다시 그릴 때마다 다시 업로드해야 합니다.

이 메서드를 미리 호출하면 사용되는 첫 번째 프레임에서 시간을 절약할 수 있습니다. 예를 들어 디코딩된 비트맵이 표시될 때 이미지 디코딩 작업자 스레드에서 이것을 호출하는 것이 좋습니다. 이 메서드를 호출하기 전에 Bitmap을 미리 그리기 수정하는 것이 좋습니다. 그러면 캐시되고 업로드된 사본을 다시 업로드하지 않고도 재사용할 수 있습니다.