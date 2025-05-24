# 캔버스(Canvas)란 무엇이며 어떻게 활용하나요?

**캔버스(Canvas)** 는 **사용자 정의 그리기(custom drawing)** 를 위한 핵심 컴포넌트입니다. 이는 화면이나 비트맵(Bitmap)과 같은 다른 그리기 표면(drawing surfaces)에 직접 그래픽을 렌더링하기 위한 인터페이스를 제공합니다. 캔버스는 개발자에게 그리기 과정에 대한 완전한 제어권을 부여하여 사용자 정의 뷰, 애니메이션 및 시각 효과를 만드는 데 일반적으로 사용됩니다.

### 캔버스(Canvas) 작동 방식

`Canvas` 클래스는 모양, 텍스트, 이미지 및 기타 콘텐츠를 그릴 수 있는 **2D 그리기 표면**을 나타냅니다. 이는 그려지는 콘텐츠가 어떻게 보여야 하는지(색상, 스타일, 선 굵기 등)를 정의하는 `Paint` 클래스와 긴밀하게 상호작용합니다. 사용자 정의 뷰(View)의 `onDraw()` 메서드를 재정의(override)하면 `Canvas` 객체가 전달되어 무엇을 그릴지 정의할 수 있게 됩니다.

다음은 뷰에서 기본적인 사용자 정의 그리기를 하는 예시입니다.

**CustomView.kt**
```kotlin
class CustomView(context: Context) : View(context) {
    private val paint = Paint().apply { // Paint 객체 초기화
        color = Color.BLUE         // 색상을 파란색으로 설정
        style = Paint.Style.FILL   // 채우기 스타일로 설정
    }

    override fun onDraw(canvas: Canvas) { // 그리기 로직
        super.onDraw(canvas)
        // 뷰의 중앙에 반지름 100f의 파란색 원을 그림
        canvas.drawCircle(width / 2f, height / 2f, 100f, paint)
    }
}
```
이 예시에서 `onDraw()` 메서드는 `Canvas` 객체를 사용하여 사용자 정의 뷰의 중앙에 파란색 원을 그립니다.

### 캔버스(Canvas)의 일반적인 작업

캔버스는 다음과 같은 다양한 그리기 작업을 허용합니다.

* **모양 (Shapes):** `drawCircle()`, `drawRect()`, `drawLine()`과 같은 메서드를 사용하여 원, 사각형, 선과 같은 모양을 그릴 수 있습니다.
* **텍스트 (Text):** `drawText()` 메서드는 지정된 좌표와 모양으로 텍스트를 렌더링합니다.
* **이미지 (Images):** `drawBitmap()`을 사용하여 이미지를 렌더링합니다.
* **사용자 정의 경로 (Custom Paths):** `Path` 객체와 `drawPath()`를 조합하여 복잡한 모양을 그릴 수 있습니다.

### 변환 (Transformations)

캔버스는 크기 조절(scaling), 회전(rotating), 이동(translating)과 같은 변환을 지원합니다. 이러한 작업은 캔버스의 좌표계를 수정하여 복잡한 장면을 더 쉽게 그릴 수 있도록 합니다.

* **이동 (Translation):** `canvas.translate(dx, dy)`를 사용하여 캔버스 원점을 새 위치로 이동시킵니다.
* **크기 조절 (Scaling):** `canvas.scale(sx, sy)`를 사용하여 특정 비율로 그리기를 확대/축소합니다.
* **회전 (Rotation):** `canvas.rotate(degrees)`를 사용하여 지정된 각도만큼 캔버스를 회전시킵니다.

이러한 변환은 누적되며 이후의 모든 그리기 작업에 영향을 미칩니다. (`save()`와 `restore()`를 사용하여 변환 상태를 관리할 수 있습니다.)

### 사용 사례

캔버스는 다음과 같이 고급 사용자 정의 그래픽이 필요한 시나리오에서 특히 유용합니다.

* **사용자 정의 뷰 (Custom Views):** 표준 위젯으로는 달성할 수 없는 고유한 UI 컴포넌트 그리기.
* **게임 (Games):** 정밀한 제어가 필요한 게임 그래픽 렌더링.
* **차트 및 다이어그램 (Charts and Diagrams):** 사용자 정의 형식으로 데이터 시각화.
* **이미지 처리 (Image Processing):** 프로그래밍 방식으로 이미지 수정 또는 결합.

### 요약

캔버스는 화면에 사용자 정의 그래픽을 렌더링하는 유연하고 유용한 방법을 제공합니다. 모양, 텍스트, 이미지 그리기를 위한 메서드와 변환 기능을 활용하여 개발자는 풍부한 시각적 및 맞춤형 경험을 만들 수 있습니다. 고급 그래픽 기능이 필요한 사용자 정의 뷰를 만드는 데 널리 사용됩니다.

---

## Q. AndroidX 라이브러리에서 지원하지 않는 복잡한 모양이나 UI 요소를 렌더링하기 위한 사용자 정의 뷰는 어떻게 만드나요? 어떤 Canvas 메서드와 API를 사용하게 되나요?

AndroidX 라이브러리에서 기본적으로 제공하지 않는 매우 복잡한 모양이나 독특한 상호작용을 가진 UI 요소를 구현해야 할 때, 개발자는 직접 **사용자 정의 뷰(Custom View)** 를 만들어야 합니다. 이를 위해 안드로이드의 2D 그래픽 엔진의 핵심인 **캔버스(Canvas)** 와 관련 API들을 적극적으로 활용하게 됩니다.

### 1. 사용자 정의 뷰 생성 단계 (핵심 요약)

복잡한 모양이나 UI 요소를 렌더링하기 위한 사용자 정의 뷰를 만드는 기본적인 단계는 다음과 같습니다.

1.  **`View` 클래스 확장:** 새로운 클래스를 만들고 `android.view.View` 또는 필요한 경우 특정 뷰(예: `ImageView` 등)를 상속합니다. 완전히 새로운 그리기를 할 때는 주로 `View`를 직접 상속합니다.
2.  **생성자(Constructor) 구현:** XML 레이아웃에서 사용하거나 코드에서 직접 생성할 수 있도록 필요한 생성자(일반적으로 `Context`만 받는 생성자, `Context`와 `AttributeSet`을 받는 생성자 등)를 구현합니다.
3.  **`onMeasure(int widthMeasureSpec, int heightMeasureSpec)` 재정의:** 뷰의 크기를 결정합니다. 부모로부터 전달된 `MeasureSpec`을 기반으로 뷰가 차지할 너비와 높이를 계산하고, `setMeasuredDimension()`을 호출하여 최종 크기를 설정합니다.
4.  **`onDraw(Canvas canvas)` 재정의 (가장 중요):** 실제 그리기가 이루어지는 곳입니다. 이 메서드에 전달되는 `Canvas` 객체를 사용하여 원하는 모양, 텍스트, 이미지 등을 그립니다.
5.  **(선택 사항) `onLayout(boolean changed, int left, int top, int right, int bottom)` 재정의:** `ViewGroup`을 상속받아 자식 뷰들을 배치해야 하는 경우가 아니라면, 단일 커스텀 뷰에서는 기본 구현으로 충분할 때가 많습니다.
6.  **(선택 사항) `onTouchEvent(MotionEvent event)` 등 이벤트 처리 메서드 재정의:** 사용자 상호작용(터치, 제스처 등)을 처리해야 할 경우 구현합니다.
7.  **(선택 사항) 사용자 정의 속성(Custom Attributes) 처리:** XML에서 뷰의 속성을 설정할 수 있도록 `attrs.xml`에 속성을 정의하고 생성자에서 이를 읽어와 사용합니다.

### 2. `Canvas` 및 관련 API 활용법 (복잡한 모양 및 UI 요소 렌더링)

`onDraw(Canvas canvas)` 메서드 내에서 `Canvas` 객체와 `Paint` 객체를 주로 사용하여 복잡한 그래픽을 구현합니다.

#### 2.1. `Paint` 객체: 그리기의 스타일과 속성 정의 (필수 동반자)

`Canvas`가 "무엇을 어디에 그릴지"를 결정한다면, `Paint` 객체는 "**어떻게** 그릴지" (색상, 스타일, 선 굵기, 텍스트 크기, 앤티에일리어싱 등)를 정의합니다.

* **주요 `Paint` 속성 및 메서드:**
    * `setColor(int color)`: 그리기 색상을 설정합니다.
    * `setStyle(Paint.Style style)`: 채우기(`FILL`), 외곽선만 그리기(`STROKE`), 또는 둘 다(`FILL_AND_STROKE`)를 설정합니다.
    * `setStrokeWidth(float width)`: 선이나 외곽선의 굵기를 설정합니다.
    * `setAntiAlias(boolean aa)`: 가장자리를 부드럽게 처리할지(앤티에일리어싱) 설정합니다. (일반적으로 `true`로 설정)
    * `setTextSize(float textSize)`: 텍스트 크기를 설정합니다.
    * `setTypeface(Typeface typeface)`: 사용자 정의 글꼴을 설정합니다.
    * `setShader(Shader shader)`: 그라데이션(`LinearGradient`, `RadialGradient`, `SweepGradient`)이나 비트맵 패턴(`BitmapShader`)과 같은 셰이더를 적용합니다.
    * `setAlpha(int alpha)`: 투명도를 설정합니다 (0-255).
    * `setXfermode(Xfermode xfermode)`: 픽셀 혼합 모드(PorterDuff 모드)를 설정하여 다양한 시각 효과를 낼 수 있습니다.

#### 2.2. 기본 도형 그리기 (`Canvas` 메서드)

`Canvas`는 다양한 기본 도형을 그리는 메서드를 제공합니다.

* `canvas.drawColor(int color)`: 캔버스 전체를 특정 색으로 채웁니다.
* `canvas.drawLine(float startX, float startY, float stopX, float stopY, Paint paint)`: 선을 그립니다.
* `canvas.drawRect(float left, float top, float right, float bottom, Paint paint)`: 사각형을 그립니다.
* `canvas.drawRoundRect(RectF rect, float rx, float ry, Paint paint)`: 둥근 모서리 사각형을 그립니다.
* `canvas.drawCircle(float cx, float cy, float radius, Paint paint)`: 원을 그립니다.
* `canvas.drawOval(RectF oval, Paint paint)`: 타원을 그립니다.
* `canvas.drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)`: 호(arc)를 그립니다.

#### 2.3. 복잡한 모양 그리기 (`Path` 객체 활용)

AndroidX 라이브러리에서 제공하지 않는 임의의 복잡한 모양은 `Path` 객체를 사용하여 정의하고 그립니다.

1.  **`Path` 객체 생성:** `val path = Path()`
2.  **경로 구성 (메서드 체이닝 가능):**
    * `path.moveTo(float x, float y)`: 현재 그리기 시작점을 지정된 좌표로 이동시킵니다.
    * `path.lineTo(float x, float y)`: 현재 점에서 지정된 점까지 직선을 추가합니다.
    * `path.quadTo(float x1, float y1, float x2, float y2)`: 2차 베지어 곡선을 추가합니다 (제어점 1개, 끝점 1개).
    * `path.cubicTo(float x1, float y1, float x2, float y2, float x3, float y3)`: 3차 베지어 곡선을 추가합니다 (제어점 2개, 끝점 1개).
    * `path.arcTo(RectF oval, float startAngle, float sweepAngle)`: 타원의 일부인 호를 경로에 추가합니다.
    * `path.addRect()`, `path.addCircle()`, `path.addOval()`, `path.addRoundRect()`: 미리 정의된 모양을 경로에 추가합니다.
    * `path.close()`: 현재 하위 경로의 시작점으로 직선을 그려 경로를 닫습니다.
    * **(고급)** `Path.Op`를 사용하여 여러 경로를 결합(UNION, DIFFERENCE, INTERSECT 등)할 수 있습니다.
3.  **경로 그리기:** `canvas.drawPath(Path path, Paint paint)` 메서드를 사용하여 구성된 경로를 캔버스에 그립니다.

#### 2.4. 텍스트 그리기

* `canvas.drawText(String text, float x, float y, Paint paint)`: 지정된 위치에 텍스트를 그립니다. (x, y는 일반적으로 텍스트의 왼쪽 하단 기준)
* `canvas.drawTextOnPath(String text, Path path, float hOffset, float vOffset, Paint paint)`: 정의된 경로를 따라 텍스트를 그립니다.
* 정확한 텍스트 배치를 위해 `Paint` 객체의 `getTextBounds()`나 `measureText()` 메서드로 텍스트의 크기를 측정해야 할 수 있습니다.

#### 2.5. 비트맵(이미지) 그리기

* `canvas.drawBitmap(Bitmap bitmap, float left, float top, Paint paint)`: 비트맵을 지정된 위치에 그립니다.
* `canvas.drawBitmap(Bitmap bitmap, Rect src, RectF dst, Paint paint)`: 비트맵의 특정 부분(src)을 지정된 사각 영역(dst)에 맞게 크기를 조절하여 그립니다.

#### 2.6. 캔버스 변환 (Transformations)

복잡한 배치, 회전, 크기 조절 등을 위해 캔버스 자체의 좌표계를 변환할 수 있습니다.

* `canvas.save()`: 현재 캔버스의 변환 상태(매트릭스, 클립 영역 등)를 저장합니다.
* `canvas.restore()`: 가장 최근에 `save()`로 저장된 상태로 캔버스를 복원합니다. 변환을 특정 부분에만 적용하고 원래 상태로 돌아올 때 필수적입니다.
* `canvas.translate(float dx, float dy)`: 캔버스의 원점을 이동시킵니다.
* `canvas.scale(float sx, float sy, float px, float py)`: 지정된 중심점(px, py)을 기준으로 캔버스를 확대/축소합니다.
* `canvas.rotate(float degrees, float px, float py)`: 지정된 중심점(px, py)을 기준으로 캔버스를 회전시킵니다.
* `canvas.skew(float sx, float sy)`: 캔버스를 기울입니다.
* `canvas.clipRect(...)`, `canvas.clipPath(...)`: 특정 사각형이나 경로 영역으로 그리기 영역을 제한(클리핑)합니다. 이 영역 밖에는 그려지지 않습니다.

### 3. 구현 시 고려 사항

* **`onDraw()` 내 객체 생성 최소화:** `onDraw()`는 매우 자주 호출될 수 있으므로, 이 메서드 내에서 `Paint`, `Path` 객체 등을 반복적으로 생성하는 것은 피해야 합니다. 멤버 변수로 선언하고 재사용하거나, 상태가 변경될 때만 업데이트합니다.
* **성능 최적화:** 불필요한 그리기를 피하고, 복잡한 계산은 `onDraw()` 외부에서 미리 수행합니다. 하드웨어 가속을 최대한 활용하도록 코드를 작성합니다.
* **접근성 고려:** 사용자 정의 뷰도 접근성을 고려해야 합니다. 필요한 경우 `contentDescription`을 설정하고, `AccessibilityDelegate`를 사용하여 스크린 리더 등에 적절한 정보를 제공합니다.

### 4. 결론

`Canvas`와 `Paint`, 그리고 `Path` 객체를 잘 활용하면 안드로이드 프레임워크에서 기본적으로 제공하지 않는 거의 모든 종류의 2D 그래픽과 UI 요소를 사용자 정의 뷰로 구현할 수 있습니다. 중요한 것은 각 API의 역할을 이해하고, 성능을 고려하며, 뷰의 생명주기에 맞춰 적절히 초기화하고 그리는 것입니다.