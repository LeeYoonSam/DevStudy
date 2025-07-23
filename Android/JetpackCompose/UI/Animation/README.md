# Jetpack Compose에서 시각적 애니메이션을 구현하는 방법

Jetpack Compose는 UI 상태 간의 부드럽고 시각적으로 매력적인 전환을 가능하게 하는 **선언적 애니메이션 시스템**을 제공합니다. 내장 API를 사용하여 개발자는 컴포저블 가시성, 콘텐츠 변경, 크기 조정 및 속성 전환을 쉽게 애니메이션화할 수 있습니다. 이러한 애니메이션은 최적의 성능을 유지하면서 사용자 경험을 향상시킵니다.

-----

### `AnimatedVisibility`를 사용한 나타남 및 사라짐 효과

`AnimatedVisibility`는 컴포저블이 **진입 및 퇴장 전환**을 애니메이션화하도록 허용합니다. 기본적으로 나타날 때는 페이드인(fade-in) 및 확장(expansion) 효과를, 사라질 때는 페이드아웃(fade-out) 및 축소(shrink) 효과를 적용합니다. `EnterTransition` 및 `ExitTransition`을 사용하여 사용자 정의 전환을 정의할 수도 있습니다.

```kotlin
@Composable
fun AnimatedVisibilityExample() {
    var isVisible by remember { mutableStateOf(true) }

    Column {
        Button(onClick = { isVisible = !isVisible }) {
            Text("가시성 토글")
        }

        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Blue)
            )
        }
    }
}
```

  * `Box`는 나타날 때 페이드인하며 확장되고, 사라질 때 페이드아웃하며 축소됩니다.
  * `EnterTransition`과 `ExitTransition`은 애니메이션 효과를 사용자 정의할 수 있게 합니다.

-----

### `Crossfade`를 사용한 부드러운 전환

`Crossfade`는 두 컴포저블 간의 전환을 페이드 효과로 애니메이션화하여, 컴포넌트 전환이나 상태 변경에 이상적입니다.

```kotlin
@Composable
fun CrossfadeExample() {
    var selectedScreen by remember { mutableStateOf("Home") }

    Column {
        Row {
            Button(onClick = { selectedScreen = "Home" }) { Text("홈") }
            Button(onClick = { selectedScreen = "프로필" }) { Text("프로필") }
        }

        Crossfade(targetState = selectedScreen, label = "screen_crossfade") { screen ->
            when (screen) {
                "Home" -> Text("홈 화면", fontSize = 24.sp)
                "Profile" -> Text("프로필 화면", fontSize = 24.sp)
            }
        }
    }
}
```

  * "홈"과 "프로필" 사이를 전환할 때, 콘텐츠가 부드럽게 크로스페이드됩니다.
  * 탭 내비게이션 및 화면/이미지/컴포넌트 전환에 유용합니다.

-----

### `AnimatedContent`를 사용한 콘텐츠 변경 애니메이션

`AnimatedContent`는 레이아웃을 유지하면서 서로 다른 콘텐츠 상태 간을 부드럽게 전환합니다.

```kotlin
@Composable
fun AnimatedContentExample() {
    var count by remember { mutableStateOf(0) }

    Column {
        Button(onClick = { count++ }) { Text("증가") }

        AnimatedContent(targetState = count, label = "count_animation") { targetCount ->
            Text(text = "카운트: $targetCount", fontSize = 24.sp)
        }
    }
}
```

  * `count`가 업데이트될 때마다 `Text`의 전환이 부드럽게 애니메이션화됩니다.
  * 다양한 애니메이션 스펙(animation specs)으로 사용자 정의할 수 있습니다.

-----

### `animate*AsState`를 사용한 속성 애니메이션

`animate*AsState` 함수들은 **단일 값을 부드럽게 애니메이션화**하도록 설계된 가장 간단한 애니메이션 API입니다. 목표 값만 지정하면 API가 현재 상태에서 목표 상태로의 전환을 부드러운 애니메이션으로 자동으로 처리합니다. 다양한 `animate*AsState` 함수들은 `animateDpAsState`, `animateFloatAsState`, `animateIntAsState`, `animateOffsetAsState`, `animateSizeAsState`, `animateValueAsState` 등 여러 값 타입을 지원하여 유연하고 직관적인 애니메이션을 가능하게 합니다.

아래 예시는 컴포넌트의 크기를 동적으로 조정하는 `animateDpAsState`를 보여줍니다.

```kotlin
@Composable
fun AnimateAsStateExample() {
    var isExpanded by remember { mutableStateOf(false) }

    val boxSize by animateDpAsState(
        targetValue = if (isExpanded) 200.dp else 100.dp,
        animationSpec = tween(durationMillis = 500),
        label = "size_animation"
    )

    Column {
        Button(onClick = { isExpanded = !isExpanded }) {
            Text("크기 토글")
        }

        Box(
            modifier = Modifier
                .size(boxSize)
                .background(Color.Green)
        )
    }
}
```

  * `animateDpAsState`는 Dp 값을 500밀리초에 걸쳐 보간(interpolate)합니다.
  * 토글 시 부드러운 크기 전환을 보장합니다.

-----

### `animateContentSize`를 사용한 크기 변경 처리

`animateContentSize`는 레이아웃 크기 변경을 자동으로 애니메이션화하여, 수동 애니메이션 콜백의 필요성을 없애줍니다.

```kotlin
@Composable
fun AnimateContentSizeExample() {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Button(
            onClick = { expanded = !expanded }
        ) { 
            Text("확장/축소") 
        }

        Box(
            modifier = Modifier
                .background(Color.Red)
                .animateContentSize() // 크기 변경을 자동으로 애니메이션화
                .padding(16.dp)
        ) {
            Text(
                text = if (expanded) "더 많은 콘텐츠를 가진 확장된 텍스트" else "짧은 텍스트",
                fontSize = 18.sp
            )
        }
    }
}
```

  * 콘텐츠가 변경될 때 `Box`의 크기가 부드럽게 조절됩니다.
  * 추가적인 애니메이션 로직이 필요 없으며, 크기 전환이 자동으로 발생합니다.

-----

### 요약

Jetpack Compose는 원활한 구현을 가능하게 하는 유용한 애니메이션 API들을 제공하여, XML 기반 애니메이션에 비해 훨씬 쉽게 만들어 줍니다.

  * **`AnimatedVisibility`**: 컴포저블의 나타남과 사라짐을 애니메이션화합니다.
  * **`Crossfade`**: UI 상태 간의 부드러운 전환을 제공합니다.
  * **`AnimatedContent`**: 콘텐츠 업데이트를 동적으로 애니메이션화합니다.
  * **`animate*AsState`**: 크기, 불투명도, 위치와 같은 값들을 보간합니다.
  * **`animateContentSize`**: 추가 로직 없이 크기 변경을 자동으로 애니메이션화합니다.

-----

## Q. 콘텐츠에 따라 크기가 변하는 텍스트 블록에 대해 부드러운 확장/축소 효과를 어떻게 만들겠습니까?

콘텐츠에 따라 크기가 변하는 텍스트 블록의 부드러운 확장/축소 효과는 **`Modifier.animateContentSize()`** 를 사용하여 가장 쉽게 구현할 수 있습니다. 이 Modifier를 텍스트를 감싸는 컨테이너에 적용하면, 내부 콘텐츠의 크기가 변경될 때마다 컨테이너의 크기 변화를 **자동으로 부드럽게 애니메이션화**해 줍니다.

-----

## 1. 해결책: `Modifier.animateContentSize()` 사용

Jetpack Compose는 이러한 일반적인 UI 요구사항을 위해 매우 간단하고 강력한 해결책을 제공합니다. 바로 `Modifier.animateContentSize()` 입니다.

이 Modifier는 컴포저블의 콘텐츠 크기가 변경될 때마다 그 크기 변화를 감지하여, 이전 크기에서 새로운 크기로 부드럽게 전환되는 애니메이션을 자동으로 적용합니다. 개발자가 직접 애니메이션 상태(`animate*AsState` 등)를 관리하거나 복잡한 로직을 작성할 필요가 없습니다.

-----

## 2. 구현 예시 코드 📜

다음은 "더보기/접기" 기능이 있는 텍스트 블록에 `animateContentSize`를 적용하여 부드러운 확장/축소 효과를 구현하는 예시입니다.

```kotlin
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ExpandableText() {
    // 1. 확장/축소 상태를 관리하는 State 변수
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.LightGray.copy(alpha = 0.5f))
            .clickable { isExpanded = !isExpanded } // Column 전체를 클릭하여 상태 토글
    ) {
        Text(
            text = "Jetpack Compose는 안드로이드에서 UI를 빌드하는 현대적인 툴킷입니다. " +
                    "선언적 접근 방식을 사용하여 더 적은 코드로 더 직관적이고 강력한 UI를 만들 수 있습니다. " +
                    "이 텍스트 블록은 콘텐츠 길이에 따라 크기가 변하며 부드럽게 애니메이션됩니다.",
            // 2. isExpanded 상태에 따라 최대 줄 수 변경
            maxLines = if (isExpanded) Int.MAX_VALUE else 2,
            overflow = TextOverflow.Ellipsis, // 내용이 넘칠 경우 말줄임표(...) 표시
            modifier = Modifier
                // 3. 이 Modifier가 마법을 부리는 부분입니다!
                .animateContentSize()
                .padding(16.dp)
        )
    }
}
```

-----

## 3. 작동 원리 및 장점

### 작동 원리

1.  `isExpanded`라는 `State`가 `false`에서 `true`로 변경됩니다.
2.  `Text` 컴포저블이 리컴포지션되고, `maxLines`가 2에서 `Int.MAX_VALUE`로 변경됩니다.
3.  이로 인해 `Text`가 필요로 하는 전체 높이(target size)가 커집니다.
4.  `Modifier.animateContentSize()`는 이 크기 변화를 감지하고, 현재 높이에서 목표 높이까지의 전환을 위한 애니메이션을 자동으로 시작합니다. 반대의 경우(축소)에도 동일하게 작동합니다.

### 장점

  * **단순성 ✨:** 단 하나의 Modifier를 추가하는 것만으로 복잡한 애니메이션 로직을 구현할 수 있습니다. `animate*AsState`나 `Animatable` API를 직접 사용할 필요가 없습니다.
  * **자동화 🤖:** 콘텐츠의 목표 크기가 변경될 때마다 애니메이션이 자동으로 트리거됩니다. 개발자가 애니메이션을 수동으로 시작하거나 관리할 필요가 없습니다.
  * **부드러움 💧:** 기본적으로 자연스러운 애니메이션 효과(`tween` 또는 `spring`)를 제공하며, 필요하다면 `animationSpec` 파라미터를 통해 애니메이션 동작을 세부적으로 조정할 수도 있습니다.

-----

## 4. (참고) `AnimatedContent`와의 비교

  * **`animateContentSize`**: **컨테이너의 크기 변화**를 애니메이션화하는 데 중점을 둡니다. 내부 콘텐츠 자체는 즉시 변경되고(예: 텍스트가 바로 전체 내용으로 바뀜), 그에 맞춰 컨테이너의 크기가 부드럽게 조절됩니다.
  * **`AnimatedContent`**: **콘텐츠 자체의 전환**을 애니메이션화합니다. 예를 들어, 짧은 텍스트가 페이드아웃(fade-out)되면서 동시에 긴 텍스트가 페이드인(fade-in)되는 효과와 함께 컨테이너 크기도 조절됩니다.

따라서, 단순히 부드러운 확장/축소 효과만 필요하다면 `animateContentSize`가 가장 간단하고 적합한 해결책입니다. 콘텐츠가 바뀌는 과정 자체에 더 정교한 효과를 주고 싶다면 `AnimatedContent`를 고려할 수 있습니다.


## Q. Canvas, Painter, 그리고 애니메이션을 사용하여 로딩 플레이스홀더를 위한 쉬머(shimmer) 애니메이션 효과를 어떻게 구현하겠습니까?

로딩 플레이스홀더를 위한 쉬머(shimmer) 애니메이션은 **`rememberInfiniteTransition`** 을 사용하여 애니메이션 값을 무한 반복시키고, 이 값을 사용하여 **`Brush.linearGradient`** 로 생성한 그라데이션의 위치를 동적으로 변경하여 구현합니다. 이 움직이는 그라데이션을 `Modifier.background`나 `Canvas`를 통해 플레이스홀더 UI 위에 그리면, 빛이 스쳐 지나가는 듯한 쉬머 효과를 만들 수 있습니다.

-----

## 1. 구현 전략: 움직이는 그라데이션 ✨

쉬머 효과의 핵심은 **움직이는 대각선 그라데이션**을 만드는 것입니다. 플레이스홀더의 배경 위에 반투명한 하이라이트 그라데이션을 겹치고, 이 그라데이션의 위치를 계속해서 이동시키는 애니메이션을 적용하면 빛이 표면을 훑고 지나가는 듯한 효과를 낼 수 있습니다.

이를 위해 다음과 같은 Compose API를 조합합니다.

1.  **`rememberInfiniteTransition`**: 무한 반복되는 애니메이션을 생성합니다.
2.  **`transition.animateFloat`**: 트랜지션 내에서 `Float` 값을 특정 범위 내에서 계속 애니메이션화합니다. 이 값을 그라데이션의 위치를 계산하는 데 사용합니다.
3.  **`Brush.linearGradient`**: 여러 색상으로 구성된 선형 그라데이션을 만듭니다. 우리는 이 그라데이션의 시작점과 끝점 위치를 애니메이션 값에 따라 계속 변경할 것입니다.
4.  **`Modifier.background` 또는 `Canvas`**: 생성된 그라데이션 브러시를 실제 UI에 그립니다.

-----

## 2. 구현 단계

### 1단계: 무한 반복 애니메이션 생성

`rememberInfiniteTransition`을 사용하여 시간에 따라 계속 변하는 `Float` 값을 만듭니다. 이 값은 그라데이션의 이동 거리를 나타냅니다.

### 2단계: 그라데이션 브러시(Brush) 생성

애니메이션 값에 따라 시작(`start`) 및 끝(`end`) `Offset`이 변경되는 `Brush.linearGradient`를 생성합니다. 그라데이션 색상은 보통 `[반투명, 하이라이트, 반투명]` 형태로 구성하여 자연스러운 빛 효과를 냅니다.

### 3단계: Modifier를 사용한 쉬머 효과 적용 (간단한 방법)

가장 간단한 방법은 `Modifier.background(brush = ...)`를 사용하여 플레이스홀더 역할을 하는 `Box`나 `Spacer`에 직접 움직이는 그라데이션을 적용하는 것입니다.

-----

## 3. 전체 코드 예시 👨‍💻

아래는 위 전략을 사용하여 재사용 가능한 `ShimmerPlaceholder` 컴포저블을 만드는 전체 예시입니다.

```kotlin
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerPlaceholder(modifier: Modifier = Modifier) {
    // 1. 무한 반복 트랜지션 생성
    val transition = rememberInfiniteTransition(label = "shimmerTransition")
    
    // 2. Float 값을 애니메이션화. -1f -> 2f 범위로 이동하도록 설정
    //    (이 값은 그라데이션의 상대적 위치를 결정)
    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f, // 컴포넌트 크기보다 큰 값으로 설정하여 이동 효과를 냄
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000), // 1초 동안 이동
        ),
        label = "shimmerTranslateAnimation"
    )

    // 3. 그라데이션 색상 정의 (기본 색상 -> 하이라이트 -> 기본 색상)
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    // 4. 애니메이션 값에 따라 이동하는 LinearGradient 브러시 생성
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnimation - 500f, translateAnimation - 500f),
        end = Offset(translateAnimation, translateAnimation)
    )

    // 5. Box에 브러시를 배경으로 적용하여 쉬머 효과 구현
    Spacer(
        modifier = modifier
            .background(brush = brush)
    )
}

// 사용 예시
@Composable
fun LoadingUserCard() {
    Row(modifier = Modifier.padding(16.dp)) {
        ShimmerPlaceholder(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            ShimmerPlaceholder(
                modifier = Modifier
                    .height(24.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            ShimmerPlaceholder(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth(0.7f)
            )
        }
    }
}
```

-----

## 4. (참고) 사용자 정의 `Painter`로 캡슐화하기

질문에서 언급된 것처럼, 이 그리기 로직 전체를 재사용 가능한 **사용자 정의 `Painter`** 로 캡슐화할 수도 있습니다.

  * **이유:** 매우 복잡한 그리기 로직을 재사용하거나, `Image` 컴포저블의 `painter` 파라미터와 같이 `Painter` 객체를 직접 요구하는 곳에 사용해야 할 때 유용합니다.
  * **구현 방식:** `Painter` 클래스를 상속받는 클래스를 만들고, `onDraw` 메서드 내에서 위와 동일한 그라데이션 및 그리기 로직을 구현합니다. 애니메이션 값(`State<Float>`)은 `Painter`의 생성자를 통해 전달받아야 합니다.
  * **트레이드오프:** 이 특정 쉬머 효과의 경우, `Modifier`를 사용하는 것이 더 간단하고 직관적입니다. `Painter`로 만드는 것은 더 많은 상용구 코드를 필요로 하지만, 그리기 로직 자체의 재사용성을 극대화할 수 있습니다.

-----

## 5. 결론

Jetpack Compose에서 쉬머 애니메이션은 **`rememberInfiniteTransition`** 으로 시간에 따라 변하는 값을 만들고, 이 값을 **`Brush.linearGradient`** 의 위치 계산에 사용하여 움직이는 그라데이션을 생성하는 방식으로 구현됩니다. 이 그라데이션 브러시를 `Modifier.background`와 같은 Modifier에 적용하면, 어떤 컴포저블이든 쉽게 반짝이는 로딩 플레이스홀더로 만들 수 있습니다. 이는 사용자에게 데이터가 로드 중임을 시각적으로 명확하게 알려주는 효과적인 방법입니다.