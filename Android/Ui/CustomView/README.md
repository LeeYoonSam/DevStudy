# 사용자 정의 뷰(Custom View)는 어떻게 구현하나요?

여러 화면에 걸쳐 재사용해야 하는 특정 모양과 동작을 가진 UI 컴포넌트를 디자인할 때 **사용자 정의 뷰(Custom View)** 구현은 필수적입니다. 사용자 정의 뷰를 사용하면 개발자는 애플리케이션 전체의 일관성과 유지보수성을 보장하면서 시각적 표현과 상호작용 로직을 모두 맞춤 설정할 수 있습니다. 사용자 정의 뷰를 만들면 복잡한 UI 로직을 캡슐화하고, 재사용성을 향상시키며, 프로젝트 내 여러 계층의 구조를 단순화할 수 있습니다.

애플리케이션이 표준 UI 컴포넌트로는 달성할 수 없는 고유한 디자인 요소를 요구한다면, 사용자 정의 뷰 구현이 필요해집니다. 안드로이드 개발에서는 다음 단계를 따라 XML을 사용하여 사용자 정의 뷰를 만들 수 있습니다.

### 1. 사용자 정의 뷰 클래스 생성하기

먼저, 기본 뷰 클래스(`View`, `ImageView`, `TextView` 등)를 확장(상속)하는 새 클래스를 정의합니다. 그런 다음, 아래에서 보시는 것처럼 구현하려는 사용자 정의 동작에 따라 `onDraw()`, `onMeasure()`, `onLayout()`과 같은 필요한 생성자와 메서드를 재정의(오버라이드)합니다.

**CustomCircleView.kt**
```kotlin
class CustomCircleView @JvmOverloads constructor( // @JvmOverloads 어노테이션 사용
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0 // 기본 스타일 속성
) : View(context, attrs, defStyle) { // View 클래스 상속

    override fun onDraw(canvas: Canvas) { // 그리기 로직 재정의
        super.onDraw(canvas)
        val paint = Paint().apply {
            color = Color.RED // 원의 색상을 빨간색으로 설정
            style = Paint.Style.FILL // 채우기 스타일로 설정
        }
        // 중앙에 빨간색 원 그리기
        canvas.drawCircle(width / 2f, height / 2f, width / 4f, paint)
    }
}
```

### 2. XML 레이아웃에서 사용자 정의 뷰 사용하기

사용자 정의 뷰 클래스를 만든 후에는 XML 레이아웃 파일에서 직접 참조할 수 있습니다. 사용자 정의 클래스의 전체 패키지 이름이 XML 태그로 사용되도록 해야 합니다. 또한, 아래 예시처럼 XML에서 정의할 수 있는 사용자 정의 속성을 뷰에 전달할 수도 있습니다 (다음 단계 참조).

**layout.xml**
```xml
<com.example.CustomCircleView android:layout_width="100dp"
    android:layout_height="100dp"
    android:layout_gravity="center"/>
```

### 3. 사용자 정의 속성 추가하기 (선택 사항)

`res/values` 폴더에 새 `attrs.xml` 파일을 만들어 사용자 정의 속성을 정의합니다. 이를 통해 아래 코드처럼 XML 레이아웃에서 뷰의 속성을 맞춤 설정할 수 있습니다.

**attrs.xml**
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="CustomCircleView"> <attr name="circleColor" format="color"/> <attr name="circleRadius" format="dimension"/> </declare-styleable>
</resources>
```

사용자 정의 뷰 클래스에서는 아래 코드처럼 `context.obtainStyledAttributes()`를 사용하여 생성자 내에서 사용자 정의 속성을 검색합니다.

**CustomCircleView.kt (업데이트)**
```kotlin
class CustomCircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    var circleColor: Int = Color.RED // 원 색상의 기본값
    var circleRadius: Float = 50f   // 원 반지름의 기본값

    init { // 초기화 블록
        // 생성자 파라미터에 따라 속성 가져오기
        when {
            attrs != null && defStyle != 0 -> getAttrs(attrs, defStyle)
            attrs != null -> getAttrs(attrs)
        }
    }

    private fun getAttrs(attrs: AttributeSet) {
        // AttributeSet에서 사용자 정의 속성 값 가져오기
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomCircleView)
        try {
            setTypeArray(typedArray)
        } finally {
            typedArray.recycle() // TypedArray 사용 후 반드시 재활용
        }
    }

    private fun getAttrs(attrs: AttributeSet, defStyle: Int) {
        // AttributeSet과 기본 스타일 속성으로 사용자 정의 속성 값 가져오기
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomCircleView, defStyle, 0)
        try {
            setTypeArray(typedArray)
        } finally {
            typedArray.recycle() // TypedArray 사용 후 반드시 재활용
        }
    }

    private fun setTypeArray(typedArray: TypedArray) {
        // TypedArray에서 실제 속성 값 읽어오기
        circleColor = typedArray.getColor(R.styleable.CustomCircleView_circleColor, Color.RED)
        circleRadius = typedArray.getDimension(R.styleable.CustomCircleView_circleRadius, 50f)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint().apply {
            color = circleColor // 사용자 정의 색상 사용
            style = Paint.Style.FILL
        }
        // 사용자 정의 반지름 사용 (뷰 크기 기반으로 재계산될 수 있음)
        val radiusToDraw = if (circleRadius > width / 2f || circleRadius > height / 2f) {
            Math.min(width / 2f, height / 2f) // 뷰 크기를 넘지 않도록
        } else {
            circleRadius
        }
        canvas.drawCircle(width / 2f, height / 2f, radiusToDraw, paint)
    }
}
```

이제 XML 파일에서 사용자 정의 속성을 사용할 수 있습니다.

```xml
<com.example.CustomCircleView
    xmlns:app="http://schemas.android.com/apk/res-auto" android:layout_width="100dp"
    android:layout_height="100dp"
    app:circleColor="@color/blue" app:circleRadius="30dp"/>
```

### 4. 레이아웃 측정 처리하기 (선택 사항)

사용자 정의 뷰가 치수를 측정하는 방식을 제어하고 싶다면, 특히 표준 뷰와 다르게 동작하는 경우, 아래에서 볼 수 있듯이 `onMeasure()` 메서드를 재정의합니다.

**onMeasure.kt**
```kotlin
override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val desiredSize = 200 // 원하는 기본 크기 (예시)

    // MeasureSpec을 고려하여 최종 크기 결정
    val width = resolveSize(desiredSize, widthMeasureSpec)
    val height = resolveSize(desiredSize, heightMeasureSpec)

    // 측정된 크기 설정
    setMeasuredDimension(width, height)
}
```

💡 **추가 팁:** 애플리케이션의 요구 사항이나 특정 디자인 명세에 맞는 재사용 가능하고 특화된 컴포넌트가 필요할 때 사용자 정의 뷰를 만드는 것이 필수적입니다. 사용자 정의 뷰는 애니메이션, 콜백, 사용자 정의 속성 및 기타 고급 기능을 통합하여 기능과 사용자 경험을 향상시킬 수 있습니다. 사용자 정의 뷰 구축에 대한 이해를 심화하려면 GitHub에서 사용할 수 있는 다양한 예제와 구현을 탐색해 보세요. 실제 사용 사례를 관찰하면 귀중한 통찰력과 영감을 얻을 수 있습니다. 사용자 정의 뷰 구현의 실제 예로는 GitHub의 [ElasticViews](https://github.com/skydoves/ElasticViews)와 [ProgressView](https://github.com/skydoves/ProgressView)를 살펴보세요. 이러한 프로젝트는 재사용 가능하고 동적인 사용자 정의 뷰를 만드는 실용적인 접근 방식을 보여줍니다. GitHub의 [CircleImageView](https://github.com/hdodenhof/CircleImageView)도 살펴볼 가치가 있습니다.

### 요약

XML을 통해 안드로이드에서 사용자 정의 뷰를 구현하면 UI 디자인에 유연성을 부여합니다. 사용자 정의 뷰 시스템과 캔버스(Canvas)를 사용하여 다양한 맞춤형 위젯을 만들 수 있습니다. 자세한 내용은 [사용자 정의 뷰에 대한 안드로이드 개발자 문서](https://developer.android.com/guide/topics/ui/custom-components?hl=ko)를 참조하세요.

---

## Q. XML 레이아웃에서 하위 호환성을 보장하면서 사용자 정의 뷰에 사용자 정의 속성을 효율적으로 적용하려면 어떻게 해야 하나요?

사용자 정의 뷰(Custom View)를 만들 때 XML 레이아웃에서 속성을 설정하고, 이를 뷰 코드 내에서 효율적으로 읽어오면서 동시에 다양한 안드로이드 버전 및 테마 환경에서 일관되게 작동하도록 하위 호환성을 보장하는 것은 매우 중요합니다.

### 1. 서론: 효율성과 호환성의 중요성

사용자 정의 속성을 사용하면 XML 레이아웃에서 뷰의 동작이나 모양을 유연하게 변경할 수 있습니다. 이 속성들을 효율적으로 읽고 적용하는 것은 성능에 영향을 미치며, 다양한 스타일 및 테마 속성과의 상호작용을 올바르게 처리하는 것은 하위 호환성 및 일관된 사용자 경험을 위해 필수적입니다.

### 2. 사용자 정의 속성 정의 (`attrs.xml`)

먼저, `res/values/attrs.xml` 파일에 `<declare-styleable>` 태그를 사용하여 사용자 정의 뷰가 사용할 속성들을 정의합니다.

```xml
<resources>
    <declare-styleable name="MyCustomView">
        <attr name="customTitleText" format="string" />
        <attr name="customTitleColor" format="color" />
        <attr name="customPadding" format="dimension" />
        <attr name="customMode">
            <enum name="mode_one" value="0" />
            <enum name="mode_two" value="1" />
        </attr>
    </declare-styleable>
</resources>
```
위 예시에서는 `MyCustomView`라는 이름으로 네 가지 사용자 정의 속성(`customTitleText`, `customTitleColor`, `customPadding`, `customMode`)을 정의했습니다.

### 3. 사용자 정의 뷰 생성자에서 속성 효율적으로 읽어오기

정의된 속성들은 뷰의 생성자(constructor) 내에서 `AttributeSet` 파라미터를 통해 접근할 수 있습니다. 이 값들을 효율적으로 읽고 적용하며 하위 호환성을 유지하는 방법은 다음과 같습니다.

#### 3.1. 올바른 생성자 활용 및 `@JvmOverloads` (코틀린)

안드로이드 뷰는 일반적으로 여러 생성자를 가질 수 있습니다. XML에서 뷰를 인플레이트할 때 주로 사용되는 생성자는 `View(Context context, AttributeSet? attrs)`와 `View(Context context, AttributeSet? attrs, int defStyleAttr)`입니다. 하위 호환성과 테마 스타일링을 올바르게 지원하려면, 특히 `defStyleAttr`을 처리하는 생성자를 잘 활용해야 합니다.

코틀린에서는 `@JvmOverloads` 어노테이션을 사용하여 여러 생성자를 자동으로 생성할 수 있지만, **`defStyleAttr`의 기본값을 주의해서 설정해야 합니다.** 단순히 `0`으로 설정하면 테마에서 정의된 기본 스타일을 상속받지 못할 수 있습니다. 상속받는 기본 안드로이드 위젯이 있다면 해당 위젯의 기본 스타일 속성(예: `R.attr.buttonStyle` 등)을 `defStyleAttr`의 기본값으로 전달하는 것이 좋습니다.

```kotlin
// MyCustomView.kt
class MyCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0 // 기본 스타일 속성 (테마에서 가져올 스타일을 가리키는 속성 ID)
    // API 21+ 에서는 defStyleRes: Int = 0 파라미터를 추가로 가질 수 있음
) : View(context, attrs, defStyleAttr) {

    private var titleText: String? = null
    private var titleColor: Int = Color.BLACK
    private var customPaddingValue: Int = 0
    private var currentMode: Int = 0

    init {
        // 생성자에서 속성 읽어오기
        attrs?.let {
            // defStyleAttr과 defStyleRes (여기서는 0)를 전달하여 스타일 우선순위 처리
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.MyCustomView,
                defStyleAttr,
                0 // defStyleRes: 직접적인 스타일 리소스 (API 21+ 에서는 네 번째 생성자에서 받음)
            )
            try {
                applyAttributes(typedArray)
            } finally {
                typedArray.recycle() // 매우 중요! 사용 후 반드시 재활용
            }
        }
    }

    private fun applyAttributes(typedArray: TypedArray) {
        titleText = typedArray.getString(R.styleable.MyCustomView_customTitleText)
        titleColor = typedArray.getColor(R.styleable.MyCustomView_customTitleColor, Color.BLACK) // 기본값 제공
        customPaddingValue = typedArray.getDimensionPixelSize(R.styleable.MyCustomView_customPadding, 0) // 기본값 제공
        currentMode = typedArray.getInt(R.styleable.MyCustomView_customMode, 0) // 기본값 제공

        // 읽어온 속성값 적용
        // 예: setPadding(customPaddingValue, customPaddingValue, customPaddingValue, customPaddingValue)
        // invalidate() 또는 requestLayout() 호출 (필요시)
    }

    // ... onDraw, onMeasure 등 나머지 구현 ...
}
```

#### 3.2. `context.obtainStyledAttributes()` 메서드 활용

이 메서드는 XML 레이아웃에서 설정된 속성 값, 스타일에 정의된 값, 테마에 정의된 기본 스타일 속성(`defStyleAttr`), 그리고 직접 지정된 기본 스타일 리소스(`defStyleRes`)를 종합적으로 고려하여 최종 속성 값을 가져옵니다. 이것이 하위 호환성과 유연한 스타일링의 핵심입니다.

* **`attrs (AttributeSet?)`:** XML 태그에 직접 설정된 속성들입니다.
* **`R.styleable.MyCustomView`:** `attrs.xml`에 정의한 `<declare-styleable>`의 이름입니다.
* **`defStyleAttr (int)`:** 현재 테마에서 이 뷰 타입의 기본 스타일을 가리키는 속성입니다 (예: `R.attr.myCustomViewStyle`). 테마에 이 속성이 정의되어 있고 그 값이 0이 아니면, 해당 스타일 리소스의 값들이 기본값으로 사용됩니다.
* **`defStyleRes (int)`:** `defStyleAttr`이 0이거나 테마에서 해당 속성을 찾을 수 없을 때 사용될 직접적인 스타일 리소스 ID입니다 (예: `R.style.MyDefaultCustomViewStyle`).

#### 3.3. `TypedArray` 사용 및 재활용

`obtainStyledAttributes()`는 `TypedArray` 객체를 반환합니다. 이 객체를 통해 각 속성 값을 읽어올 수 있습니다.
**매우 중요한 점은 `TypedArray` 사용이 끝난 후에는 반드시 `typedArray.recycle()`을 호출하여 리소스를 해제해야 한다는 것입니다.** 그렇지 않으면 메모리 누수가 발생할 수 있습니다. `try-finally` 블록을 사용하여 항상 `recycle()`이 호출되도록 보장하는 것이 좋습니다.

#### 3.4. 코드 내 기본값 제공

`TypedArray`의 `getXXX()` 메서드를 사용하여 속성 값을 읽어올 때는 항상 두 번째 인자로 **기본값(default value)** 을 제공해야 합니다. 이는 XML 레이아웃이나 어떤 스타일에서도 해당 속성이 설정되지 않았을 경우 뷰가 예측 가능하게 동작하도록 보장합니다.

### 4. 속성 값 적용 우선순위 이해

안드로이드 시스템은 다음 우선순위에 따라 최종적으로 뷰에 적용될 속성 값을 결정합니다 (높은 우선순위부터):

1.  XML 레이아웃 파일에서 뷰 태그에 직접 설정된 속성 (`AttributeSet`을 통해 전달됨).
2.  XML 레이아웃 파일에서 뷰 태그의 `style` 속성을 통해 적용된 스타일 리소스.
3.  생성자에 전달된 `defStyleAttr` (기본 스타일 속성)에 의해 현재 테마에서 참조되는 스타일 리소스.
4.  생성자에 전달된 `defStyleRes` (기본 스타일 리소스)에 의해 직접 참조되는 스타일 리소스 (주로 `defStyleAttr`이 0이거나 해석되지 않을 때 사용됨).
5.  테마(Theme) 자체에 의해 해당 뷰 타입에 직접 적용되는 기본 스타일.
6.  `TypedArray.getXXX()` 메서드에 제공된 코드 상의 기본값 (위의 모든 소스에서 값을 찾지 못했을 경우).

### 5. 하위 호환성을 위한 추가 고려 사항

* **API 레벨에 따른 분기 처리:** 특정 속성이나 스타일이 특정 API 레벨 이상에서만 지원된다면, `Build.VERSION.SDK_INT`를 확인하여 코드 내에서 분기 처리를 하거나, 다른 리소스 디렉터리(예: `values-v21`)를 활용할 수 있습니다.
* **테마 및 스타일 상속 활용:** 기본 안드로이드 테마나 Material Components 테마의 속성들을 상속받고, 필요한 부분만 커스터마이징하여 일관성을 유지하고 코드 중복을 줄입니다.

### 6. 결론: 체계적인 접근의 중요성

사용자 정의 뷰에 속성을 효율적으로 적용하고 하위 호환성을 보장하려면, `attrs.xml`을 통한 명확한 속성 정의, 올바른 생성자 시그니처 사용, `obtainStyledAttributes()`의 파라미터(특히 `defStyleAttr`과 `defStyleRes`)에 대한 이해, 그리고 `TypedArray`의 올바른 사용 및 재활용이 필수적입니다. 이러한 체계적인 접근은 다양한 환경에서 뷰가 일관되고 안정적으로 작동하도록 만드는 데 기여합니다.

---

### 💡 숙련자를 위한 프로 팁: 사용자 정의 뷰의 기본 생성자에 `@JvmOverloads`를 사용할 때 왜 주의해야 하나요?

코틀린의 `@JvmOverloads` 어노테이션은 코틀린 함수나 클래스에 대해 여러 개의 오버로드된 메서드나 생성자를 자동으로 생성하여 코틀린과 자바 간의 상호운용성을 단순화하는 기능입니다. 이는 특히 코틀린의 기본 인수(default arguments)가 관련될 때 유용한데, 자바는 기본 인수를 네이티브하게 지원하지 않기 때문입니다.

`@JvmOverloads`를 사용하면 코틀린 컴파일러는 기본값을 가진 파라미터의 모든 가능한 조합을 나타내는 여러 메서드 또는 생성자 시그니처를 컴파일된 바이트코드에 생성합니다.

그러나 사용자 정의 뷰를 구현할 때 `@JvmOverloads`를 신중하게 사용하지 않으면, 의도치 않게 기본 뷰 스타일을 재정의하여 사용자 정의 뷰가 의도한 스타일링을 잃게 만들 수 있습니다. 이는 특히 `Button`이나 `TextView`와 같이 미리 정의된 스타일이 있는 안드로이드 뷰를 확장할 때 문제가 될 수 있습니다.

예를 들어, 사용자 정의 `TextInputEditText`를 다음과 같이 정의할 수 있습니다.

```kotlin
class ElasticTextInputEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0 // defStyleAttr의 기본값을 0으로 설정
) : TextInputEditText(context, attrs, defStyle) {
    // ...
}
```

이 예에서 `ElasticTextInputEditText`를 일반 `TextInputEditText`처럼 사용하면 예기치 않은 동작이나 잠재적인 손상이 발생할 수 있습니다. 이는 위 코드에서 볼 수 있듯이 `defStyle`(정확히는 `defStyleAttr`) 값이 `0`으로 재정의되어 사용자 정의 뷰가 의도한 스타일링을 잃게 되기 때문입니다.

뷰가 XML 파일에서 인플레이트될 때, 두 개의 파라미터를 갖는 생성자(`Context`, `AttributeSet`)가 호출되고, 이 생성자는 다시 세 개의 파라미터를 갖는 생성자를 호출합니다.

```java
// @JvmOverloads가 생성하는 자바 바이트코드에서의 해당 생성자 (개념적 표현)
public ElasticTextInputEditText(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0); // defStyleAttr에 0이 전달됨
}
```

세 번째 파라미터인 `defStyleAttr`은 사용자 정의 구현에서 기본적으로 `0`으로 설정되는 경우가 많습니다. 그러나 이 세 파라미터 생성자의 목적은 안드로이드 문서에 다음과 같이 설명되어 있습니다.

> XML에서 인플레이션을 수행하고 테마 속성에서 클래스별 기본 스타일을 적용합니다. View의 이 생성자는 하위 클래스가 인플레이트될 때 자체 기본 스타일을 사용할 수 있도록 합니다. 예를 들어, Button 클래스의 생성자는 이 버전의 슈퍼 클래스 생성자를 호출하고 defStyleAttr에 R.attr.buttonStyle을 제공합니다. 이를 통해 테마의 버튼 스타일이 모든 기본 뷰 속성(특히 배경)뿐만 아니라 Button 클래스의 속성도 수정할 수 있습니다.

`ElasticTextInputEditText`에 대해 적절한 `defStyleAttr` 값(예: `R.attr.editTextStyle`)을 생략하면(기본값 0으로 두면), 사용자 정의 뷰는 상속된 스타일 구성을 잃어 XML 인플레이션 중에 일관성 없거나 깨진 동작을 보일 수 있습니다.

`TextInputEditText`의 내부 구현을 살펴보면 아래 코드 조각에서 볼 수 있듯이 내부적으로 `R.attr.editTextStyle`을 `defStyleAttr`로 사용한다는 것을 알 수 있습니다.

```java
public class TextInputEditText extends AppCompatEditText {

    private final Rect parentRect = new Rect();
    private boolean textInputLayoutFocusedRectEnabled;

    public TextInputEditText(@NonNull Context context) {
        this(context, null);
    }

    public TextInputEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle); // 여기서 R.attr.editTextStyle를 사용
    }

    public TextInputEditText(
        @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // ...
    }
}
```

`TextInputEditText`의 구현에서 세 번째 파라미터(`defStyleAttr`)로 `androidx.appcompat.R.attr.editTextStyle`을 전달하는 것을 관찰할 수 있습니다. 이 문제를 해결하고 적절한 스타일링을 보장하려면, 사용자 정의 뷰의 생성자에서 `defStyleAttr` 파라미터의 기본값으로 `R.attr.editTextStyle`을 설정하여 수정할 수 있습니다.

**ElasticTextInputEditText.kt (수정)**
```kotlin
class ElasticTextInputEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.editTextStyle // 올바른 기본 스타일 속성 지정
) : TextInputEditText(context, attrs, defStyleAttr) {
    // 사용자 정의 구현
}
```

기본값으로 `androidx.appcompat.R.attr.editTextStyle`을 명시적으로 할당함으로써, 사용자 정의 뷰가 XML 인플레이션 중에 예상되는 기본 스타일을 상속받아 원래 `TextInputEditText`의 동작과 일관성을 유지하도록 보장합니다.