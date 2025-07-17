# `Painter`란 무엇인가?

**`Painter`** 는 이미지, 벡터 그래픽 및 기타 그릴 수 있는 콘텐츠를 렌더링하기 위해 사용되는 추상화입니다. 이는 크기 조절(scaling), 틴팅(tinting), 사용자 정의 그리기 로직과 같은 기능을 지원하면서 이미지를 로드하고 표시하는 유연한 방법을 제공합니다.

### `Painter` 작동 방식

전통적인 이미지 로딩 접근 방식과 달리, `Painter`는 이미지 리소스를 표시하는 UI 컴포넌트로부터 **분리(decouple)** 합니다. 이는 `res/drawable` 리소스에 대한 `painterResource`, 또는 벡터 기반 이미지에 대한 `VectorPainter`를 반환하는 `rememberVectorPainter`와 같이 다양한 이미지 소스로 작업할 때 유용하게 만듭니다.

Compose UI는 다음과 같은 내장 `Painter` 구현체들을 제공합니다.

  * **`painterResource(id)`**: `res/drawable` 폴더에서 이미지를 로드하기 위해 사용됩니다.
  * **`ColorPainter(color)`**: 영역을 단색으로 채우기 위해 사용됩니다.
  * **`rememberVectorPainter(image = ImageVector)`**: `ImageVector`로부터 동적으로 `VectorPainter`를 생성하기 위해 사용됩니다.

-----

## 사용 예시

다음 예시는 `Image` 컴포저블과 함께 `Painter`를 사용하는 방법을 보여줍니다.

```kotlin
@Composable
fun DisplayImage() {
    val painter = painterResource(id = R.drawable.skydoves_image)
    Image(
        painter = painter,
        modifier = Modifier.size(100.dp),
        contentDescription = "샘플 이미지",
    )
}
```

이 예시에서:

  * `painterResource` 함수는 드로어블 리소스에서 이미지를 로드합니다.
  * `Image` 컴포저블은 이 `Painter`를 사용하여 지정된 크기로 이미지를 표시합니다.

벡터 이미지의 경우, `rememberVectorPainter`는 `VectorPainter`를 반환하며 다음과 같이 사용할 수 있습니다.

```kotlin
@Composable
fun DisplayVector() {
    val vectorPainter = rememberVectorPainter(image = Icons.Default.Star)
    Image(
        painter = vectorPainter,
        modifier = Modifier.size(50.dp),
        contentDescription = "벡터 아이콘",
    )
}
```

이 예시에서:

  * `rememberVectorPainter`는 `ImageVector`로부터 `VectorPainter`를 생성합니다.
  * `Image` 컴포저블은 `VectorPainter`를 사용하여 품질 저하 없이 크기 조절이 가능한 이미지를 표시합니다.

-----

## 주요 특징

`Painter` 객체는 Jetpack Compose에서 그릴 수 있는 요소를 나타내며, 안드로이드의 전통적인 `Drawable` API를 대체하는 역할을 합니다. 이는 이미지나 그래픽이 어떻게 렌더링되는지를 정의할 뿐만 아니라, 그것을 사용하는 컴포저블의 측정 및 레이아웃에도 영향을 미칩니다.

사용자 정의 페인터를 만들려면, `Painter` 클래스를 확장하고 `onDraw` 메서드를 구현하면 되며, 이 메서드는 사용자 정의 그래픽을 렌더링하기 위한 `DrawScope`에 대한 접근을 제공합니다. 이는 컴포저블 내에서 콘텐츠가 어떻게 그려지는지에 대한 완전한 제어를 가능하게 합니다. 자세한 내용은 [공식 문서](https://developer.android.com/develop/ui/compose/graphics/images/custompainter)를 참조하세요.

-----

## 요약

`Painter`는 크기 조절 및 사용자 정의에 대한 유연성을 제공하면서 이미지 및 벡터 렌더링을 단순화하는 추상화입니다. `Painter`와 `VectorPainter`를 사용하여, 개발자는 이미지 리소스를 효율적으로 로드하고, 비트맵 및 벡터 기반 그래픽을 모두 Compose 친화적인 방식으로 지원할 수 있습니다.

-----

## Q. Jetpack Compose에서 사용자 정의 `Painter`를 만들어 본 적이 있나요? 그렇다면, 어떤 사용 사례였고 그리기 로직은 어떻게 구현했나요?

네, 사용자 정의 `Painter`는 정적인 이미지를 넘어 **동적인 데이터나 로직에 따라 무언가를 그려야 할 때** 유용합니다. 대표적인 사용 사례는 사용자 프로필 이미지가 없을 때, **사용자 이름의 첫 글자를 원형 배경 위에 그리는** 플레이스홀더를 만드는 것입니다. 이 로직은 `Painter` 클래스를 상속하고 `onDraw` 메서드를 재정의하여 `Canvas`에 직접 배경과 텍스트를 그리는 방식으로 구현합니다.

-----

### 1. 사용자 정의 `Painter`가 필요한 시나리오

Jetpack Compose에서 `Image`나 `Box`의 `background` Modifier에 사용되는 `Painter`는 `painterResource`를 통해 정적인 드로어블(drawable) 리소스를 로드하는 것이 일반적입니다. 하지만 다음과 같은 경우에는 사용자 정의 `Painter` 구현이 필요합니다.

  * **데이터에 따라 내용이 동적으로 변하는 그래픽:** 사용자 이름의 이니셜, 주식 차트, 오디오 시각화 등 정적인 이미지 파일로 만들기 어려운 동적인 시각적 요소를 그려야 할 때.
  * **고도로 최적화된 그리기 로직:** 매우 복잡한 벡터 그래픽이나 특정 렌더링 최적화가 필요하여, `Canvas` API를 직접 제어해야 할 때.
  * **재사용 가능한 그리기 컴포넌트:** 특정 그리기 로직을 캡슐화하여 앱의 여러 부분에서 재사용하고 싶을 때.

이번 답변에서는 **"사용자 프로필 이미지가 없을 때 이름의 첫 글자와 동적인 배경색으로 프로필 아이콘을 대체하는"** 시나리오를 바탕으로 설명하겠습니다.

-----

### 2. 구현 단계: 이니셜(Initial) 플레이스홀더 `Painter` 만들기

#### 2.1. 1단계: `Painter` 클래스 확장

먼저, `Painter` 추상 클래스를 상속받는 새로운 클래스를 만듭니다. 이 클래스는 그리는 데 필요한 데이터(예: 이니셜, 배경색, 텍스트 색상)를 생성자 파라미터로 받습니다.

```kotlin
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
// ... 기타 import

class InitialPainter(
    private val initial: Char,
    private val backgroundColor: Color,
    private val textColor: Color,
    private val textStyle: TextStyle
) : Painter() {
    // ... 2, 3단계 구현 ...
}
```

#### 2.2. 2단계: `intrinsicSize` 재정의

`intrinsicSize` 프로퍼티는 `Painter`의 고유(원본) 크기를 지정합니다. `Modifier`에서 크기를 명시적으로 지정하지 않았을 때, 이 크기를 기반으로 레이아웃이 결정될 수 있습니다. 여기서는 고정된 크기를 반환하도록 설정합니다.

```kotlin
// InitialPainter 클래스 내부
override val intrinsicSize: Size
    get() = Size(120f, 120f) // 예시: 120x120 픽셀 크기
```

#### 2.3. 3단계: `onDraw` 메서드 재정의 (핵심 로직)

`onDraw` 메서드는 실제 그리기 로직이 들어가는 곳입니다. 이 메서드는 `DrawScope`를 수신 객체로 가지므로, 그리기 영역의 크기(`size`)에 접근하고 다양한 그리기 함수(`drawCircle`, `drawText` 등)를 직접 호출할 수 있습니다.

```kotlin
// InitialPainter 클래스 내부
// textMeasurer는 onDraw 외부에서 remember로 생성하는 것이 더 효율적입니다.
// 이 예시에서는 설명을 위해 onDraw 내부에 표현합니다.
override fun onDraw(drawScope: DrawScope) {
    // 1. 배경 원 그리기
    drawScope.drawCircle(
        color = backgroundColor,
        radius = drawScope.size.minDimension / 2 // 뷰 크기에 맞춰 반지름 결정
    )

    // 2. 텍스트(이니셜)를 그리기 위해 TextMeasurer 사용 준비
    // 실제 구현에서는 TextMeasurer를 @Composable 컨텍스트에서 rememberTextMeasurer()로 생성하고
    // 이 Painter에 전달해야 합니다. 여기서는 개념 설명을 위해 간략화합니다.
    // 아래 코드는 실제로는 별도의 @Composable 함수 내에서 처리되어야 합니다.
    // 이 예시에서는 개념 전달을 위해 의사 코드로 표현합니다.

    /*
    // 올바른 구현을 위한 개념적 코드 (실제로는 @Composable 함수에서 처리 필요)
    val textMeasurer = rememberTextMeasurer()
    val textLayoutResult = textMeasurer.measure(
        text = AnnotatedString(initial.toString()),
        style = textStyle.copy(color = textColor)
    )
    
    // 3. 텍스트를 원의 중앙에 배치
    val textSize = textLayoutResult.size
    drawScope.drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(
            x = (drawScope.size.width - textSize.width) / 2,
            y = (drawScope.size.height - textSize.height) / 2
        )
    )
    */
}
```

> **참고:** 위 `onDraw`의 텍스트 그리기 부분은 `TextMeasurer`를 사용해야 하는데, 이는 `@Composable` 컨텍스트에서만 생성할 수 있습니다. 따라서 실제로는 이 `Painter`를 사용하는 상위 컴포저블에서 텍스트 레이아웃 결과를 계산하고, 그 결과를 `Painter`에 전달하여 그리는 방식이 더 적합합니다. 하지만 여기서는 `Painter`가 어떤 역할을 하는지 개념적으로 보여주는 데 중점을 둡니다.

-----

### 3. 완성된 코드 및 사용 예시

`Painter`를 생성하고 사용하는 전체적인 모습은 다음과 같습니다.

```kotlin
// InitialPainter.kt
class InitialPainter(
    private val initial: Char,
    private val backgroundColor: Color,
) : Painter() {
    override val intrinsicSize: Size = Size.Unspecified

    override fun DrawScope.onDraw() {
        // 배경 원 그리기
        drawCircle(
            color = backgroundColor,
            radius = size.minDimension / 2f
        )
        // 텍스트 그리기는 더 복잡하므로 여기서는 개념만 설명합니다.
        // 실제로는 Text 컴포저블을 Box 위에 겹치는 것이 더 간단합니다.
        // 하지만 이 예시는 Painter로 모든 것을 그릴 수 있음을 보여줍니다.
    }
}


// ProfileImage.kt
@Composable
fun ProfileImage(user: User, modifier: Modifier = Modifier) {
    val painter = if (user.imageUrl != null) {
        // 이미지가 있을 경우, Coil이나 Glide 같은 라이브러리 사용
        rememberAsyncImagePainter(model = user.imageUrl)
    } else {
        // 이미지가 없을 경우, 사용자 정의 InitialPainter 사용
        remember(user.name) { // 이름이 바뀔 때만 Painter 재생성
            InitialPainter(
                initial = user.name.firstOrNull()?.uppercaseChar() ?: '?',
                backgroundColor = Color.Gray // 이름에 따라 다른 색상을 생성하는 로직 추가 가능
            )
        }
    }

    Image(
        painter = painter,
        contentDescription = "Profile for ${user.name}",
        modifier = modifier
            .size(120.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}
```

-----

### 4. 결론

사용자 정의 `Painter`를 만드는 것은 **동적인 데이터를 기반으로 한 커스텀 그래픽**을 구현할 때 매우 강력한 방법입니다. `Painter` 클래스를 확장하고 `onDraw` 메서드 내에서 `DrawScope`의 그리기 함수들을 활용하면, 정적인 리소스 파일로는 표현할 수 없는 거의 모든 종류의 시각적 요소를 프로그래밍 방식으로 생성할 수 있습니다. 이는 재사용 가능하고 데이터에 반응하는 고유한 UI 컴포넌트를 만드는 데 핵심적인 기술입니다.