# `Box`란 무엇인가요?

**`Box`** 는 Jetpack Compose의 기본적인 레이아웃 컴포넌트로, 공통 부모 내에 **여러 자식 컴포저블을 겹쳐 쌓을 수 있게** 합니다. `Box`는 자식들을 자신의 경계에 상대적으로 배치하며, 오버레이 효과, 정렬 제어, 그리고 레이어링을 가능하게 합니다. 이로 인해 `Box`는 배경, 이미지 위의 아이콘, 또는 떠다니는(플로팅) UI 요소가 필요한 많은 시나리오에서 특히 유용합니다.

### `Box` 작동 방식

`Box`는 기본적으로 자식들을 **좌상단 모서리 정렬**로 배열하지만, `contentAlignment` 파라미터를 사용하여 정렬을 사용자 정의할 수 있습니다. 추가적으로, `Modifier` 프로퍼티는 크기, 패딩, 배경, 클릭 상호작용의 사용자 정의를 허용합니다.

자식들을 순차적으로 정렬하는 `Column`이나 `Row`와 달리, `Box`는 자식들을 스택과 같은 방식으로 **겹칩니다.** 이는 UI 컴포넌트를 레이어링하는 데 이상적입니다.

-----

## 사용 예시

다음 예시는 `Box`를 사용하여 이미지 위에 텍스트를 오버레이하는 방법을 보여줍니다.

```kotlin
@Composable
fun ImageWithOverlay() {
    Box(
        modifier = Modifier.size(200.dp),
        contentAlignment = Alignment.BottomCenter // 모든 자식을 하단 중앙에 배치
    ) {
        Image(
            painter = painterResource(id = R.drawable.skydoves_image),
            contentDescription = "배경 이미지"
        )
        Text(
            text = "Hello, skydoves!",
            color = Color.White,
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.5f)) // 반투명 배경 추가
                .padding(8.dp)
        )
    }
}
```

![compose-box](./screenshots/compose-box.png)

이 예시에서:

  * `Box`는 `Image`와 `Text`가 모두 자신의 경계 내에 배치되도록 보장합니다.
  * `contentAlignment = Alignment.BottomCenter`는 모든 내부 콘텐츠를 `Box`의 하단 중앙에 배치합니다.
  * `Text`에는 더 나은 가독성을 위해 반투명 배경이 주어졌습니다.

-----

## 주요 특징

`Box`는 UI 디자인에 다목적으로 사용될 수 있는 두 가지 주요 기능을 제공합니다. 첫째, 단일 레이아웃 내에 여러 자식을 쌓을 수 있게 하여 UI 요소를 오버레이하는 데 유용합니다. 둘째, 부모 수준에서는 `contentAlignment`를 통해, 개별 자식에 대해서는 `Modifier.align()`를 통해 정렬을 제어할 수 있습니다.

## 요약: `Box`

`Box`는 공통 부모 내에 UI 요소들을 쌓을 수 있게 해주는 유용하면서도 간단한 레이아웃 컴포저블입니다. 콘텐츠를 오버레이하거나, 배경 효과를 만들거나, 요소 정렬을 제어하는 데 특히 유용합니다.

-----

## 💡 프로 팁: `BoxWithConstraints`란 무엇인가요?

**`BoxWithConstraints`** 는 컴포지션 중에 **부모의 레이아웃 제약 조건에 접근**할 수 있게 해주는 고급 레이아웃 API입니다. 일반 `Box`와 달리, 개발자가 사용 가능한 공간과 크기 제약 조건에 따라 동적으로 UI 결정을 내릴 수 있게 하여, 반응형 디자인과 적응형 레이아웃에 유용합니다.

### `BoxWithConstraints` 작동 방식

`BoxWithConstraints`를 사용하면, 컴포저블은 `maxWidth`, `maxHeight`, `minWidth`, `minHeight`와 같은 프로퍼티를 제공하는 `Constraints` 스코프를 받습니다. 이 값들은 컴포저블에 사용 가능한 크기를 나타내며, 주어진 제약 조건에 따라 UI를 조정할 수 있게 합니다.

이는 UI 요소가 화면 크기, 윈도우 제약 조건, 또는 부모 레이아웃 치수에 따라 동적으로 적응해야 하는 상황에서 `BoxWithConstraints`를 특히 유용하게 만듭니다.

### 사용 예시

다음 예시는 `BoxWithConstraints`를 사용하여 사용 가능한 너비에 따라 텍스트 크기를 변경하는 방법을 보여줍니다.

```kotlin
@Composable
fun ResponsiveText() {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        // maxWidth를 사용하여 동적으로 텍스트 크기 결정
        val textSize = if (maxWidth < 300.dp) 14.sp else 20.sp
        Text(
            text = "Hello, skydoves!",
            fontSize = textSize
        )
    }
}
```

이 예시에서:

  * `BoxWithConstraints` 컴포저블은 사용 가능한 수평 공간을 결정하는 `maxWidth`를 제공합니다.
  * 만약 `maxWidth`가 300.dp보다 작으면 텍스트 크기는 14.sp로 설정되고, 그렇지 않으면 20.sp로 증가합니다.
  * 이 접근 방식은 텍스트가 사용 가능한 화면 너비에 따라 동적으로 적응하도록 보장합니다.

### 주요 특징

`BoxWithConstraints`는 컴포지션 스코프 내에서 레이아웃 제약 조건에 접근할 수 있게 하여 반응형 디자인을 가능하게 합니다. 개발자가 사용 가능한 공간에 따라 동적으로 조정되는 UI 요소를 만드는 데 도움을 줍니다. 실시간 제약 조건 값을 제공하므로, 화면 크기에 따라 조건부로 레이아웃을 변경하거나, 타이포그래피를 조정하거나, 심지어 UI 컴포넌트를 재배열하는 데 사용될 수 있습니다.

그러나 `BoxWithConstraints`는 일반 `Box`에 비해 추가적인 오버헤드를 발생시키며 직접적인 대체재로 사용되어서는 안 됩니다. 이는 내부적으로 **`SubcomposeLayout`** 을 사용하여 구현되기 때문입니다.

```kotlin
// BoxWithConstraints 내부 구현 (개념적 표현)
@Composable
fun BoxWithConstraints(
    // ...
    content: @Composable BoxWithConstraintsScope.() -> Unit
) {
    // ...
    SubcomposeLayout(modifier) { constraints ->
        val scope = BoxWithConstraintsScopeImpl(this, constraints)
        // subcompose를 통해 content를 측정 단계에서 컴포즈함
        val measurables = subcompose(Unit) { scope.content() }
        // ...
    }
}
```

따라서 레이아웃 제약 조건에 접근하는 것이 필수적인 시나리오에 가장 적합하며, 불필요한 성능 비용을 피하기 위해 필요할 때만 사용해야 합니다.

### 요약: `BoxWithConstraints`

`BoxWithConstraints`는 부모 제약 조건에 따라 동적인 UI 조정을 허용하는 레이아웃 컴포저블입니다. UI 요소가 다양한 화면 크기나 레이아웃 제약에 적응해야 하는 반응형 디자인을 만드는 데 특히 유용합니다. `BoxWithConstraints`를 활용함으로써, 개발자는 자신의 컴포저블이 다양한 기기 구성에 걸쳐 유연하고 적응성 있게 유지되도록 보장할 수 있습니다.

-----

## Q. 어떤 시나리오에서 `Column`이나 `Row` 대신 `Box`를 사용하는 것을 선호하며, `Box`는 자식 컴포저블을 어떻게 다르게 처리하나요?

`Box`는 자식 컴포저블들을 Z축으로 **겹쳐 쌓아야 할 때** 사용하며, `Column`(수직)이나 `Row`(수평)처럼 순차적으로 배치하지 않는다는 점에서 다릅니다. 따라서 **이미지 위에 텍스트나 아이콘을 올리는 오버레이 효과**를 만들거나, 단일 자식 컴포저블을 **컨테이너 내 특정 위치(예: 중앙, 하단 등)에 정렬**하고자 할 때 `Box`를 사용하는 것이 더 적합합니다.

-----

## 1. `Box`가 자식 컴포저블을 처리하는 방식 (Column/Row와의 차이점)

`Box`, `Column`, `Row`는 Jetpack Compose의 가장 기본적인 레이아웃 컴포저블이지만, 자식들을 배치하는 방식에 근본적인 차이가 있습니다.

  * **`Column` 및 `Row` (순차적 레이아웃):**

      * 이들의 주된 목적은 자식들을 **순서대로 하나씩 나열**하는 것입니다.
      * **`Column`** 은 자식들을 **수직**으로 위에서 아래로 배치합니다.
      * **`Row`** 는 자식들을 **수평**으로 왼쪽에서 오른쪽으로 배치합니다.
      * 기본적으로 자식들이 서로 겹치지 않습니다.

  * **`Box` (중첩/겹침 레이아웃):**

      * `Box`의 주된 목적은 자식들을 **겹쳐서 쌓는 것**입니다. 마치 여러 장의 종이를 같은 위치에 포개어 놓는 것과 같습니다.
      * 기본적으로 `Box` 내에 선언된 모든 자식들은 `Box`의 **좌상단(top-start)** 을 기준으로 그려집니다.
      * Z축 순서(어떤 것이 더 위에 그려질지)는 코드에 **선언된 순서**에 따라 결정됩니다. 즉, 나중에 선언된 컴포저블이 더 위에 그려집니다.

-----

## 2. `Box`를 사용하는 것이 더 적합한 시나리오

이러한 "겹침"과 "정렬"의 특성 때문에, 다음과 같은 시나리오에서는 `Column`이나 `Row` 대신 `Box`를 사용하는 것이 훨씬 더 효율적이고 직관적입니다.

### 2.1. UI 요소 겹치기 (오버레이 효과) 🖼️

  * **시나리오:** 이미지 위에 재생 버튼 아이콘을 올리거나, 사용자 프로필 사진 위에 텍스트(이름)를 표시하거나, 특정 콘텐츠 영역 위에 로딩 스피너(`CircularProgressIndicator`)를 띄우거나, 아이콘 위에 알림 개수를 나타내는 작은 배지(badge)를 붙여야 할 때.
  * **이유:** `Column`이나 `Row`는 이러한 요소들을 옆이나 아래에 배치할 뿐, 겹치게 만들 수 없습니다. `Box`는 이러한 레이어링(layering)을 위해 설계된 가장 기본적인 컴포넌트입니다.
    ```kotlin
    Box {
        Image(painter = painterResource(id = R.drawable.profile_pic), contentDescription = null)
        Text(
            text = "skydoves",
            modifier = Modifier.align(Alignment.BottomCenter) // BoxScope 내에서만 사용 가능한 align
        )
    }
    ```

### 2.2. 단일 자식을 특정 위치에 정렬하기 🎯

  * **시나리오:** 하나의 아이템(예: 아이콘, 버튼, 텍스트)을 컨테이너의 정중앙, 하단 중앙, 우상단 등 특정 위치에 간단하게 배치하고 싶을 때.
  * **이유:** `Box`는 `contentAlignment` 파라미터를 제공하여 그 안의 모든 자식들을 한 번에 정렬할 수 있습니다. 예를 들어 `Box(contentAlignment = Alignment.Center)`는 자식을 정중앙에 배치합니다. 이는 `Column`이나 `Row`에서 `Arrangement`나 `Spacer`를 사용하여 정렬을 구현하는 것보다 훨씬 간결하고 명확한 방법입니다.

### 2.3. 다른 컴포저블의 배경으로 사용하기 🎨

  * **시나리오:** 특정 컴포저블(예: `Text`)에 배경을 추가하고 싶지만, 배경의 크기나 모양이 콘텐츠와 정확히 일치하지 않아야 할 때.
  * **이유:** `Box` 안에 배경 역할을 할 컴포저블과 실제 콘텐츠 역할을 할 컴포저블을 차례로 넣어 겹치게 만들 수 있습니다.
    ```kotlin
    Box {
        // 배경 역할을 하는 Box
        Box(
            Modifier
                .matchParentSize() // 아래 Text 크기에 맞춰짐
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
        )
        Text(text = "Hello", modifier = Modifier.padding(16.dp))
    }
    ```

### 2.4. `matchParentSize()` Modifier 사용하기

  * **시나리오:** 자식 컴포저블이, `Box` 내 다른 자식들에 의해 결정된 `Box`의 최종 크기를 그대로 채우도록 만들고 싶을 때.
  * **이유:** `Modifier.matchParentSize()`는 `BoxScope` 내에서만 사용할 수 있는 특별한 Modifier입니다. 이는 `Box`가 자식들을 모두 측정한 후 결정된 최종 크기를 그대로 자식에게 적용하게 해줍니다. 이는 위 2.3 예시처럼 콘텐츠 위에 딱 맞는 배경을 만들 때 매우 유용합니다.

-----

## 3. 결론

선택 기준은 간단합니다.

  * **`Column` / `Row`**: 자식들을 **순차적으로** (세로 또는 가로로) 나열해야 할 때 사용합니다.
  * **`Box`**: 자식들을 **겹쳐 쌓거나**, 특정 기준에 맞춰 **정렬**해야 할 때 사용합니다.

`Box`는 `Column`과 `Row`만큼이나 기본적인 레이아웃 구성 요소이며, 이 세 가지를 적절히 조합하면 거의 모든 종류의 2D UI 레이아웃을 효과적으로 만들 수 있습니다.


## Q. `Box`의 `contentAlignment` 파라미터는 개별 자식에 `Modifier.align()`를 사용하는 것과 어떻게 다른가요? 둘을 함께 사용할 수 있나요?

`Box`의 **`contentAlignment`** 파라미터는 **`Box` 내부의 모든 자식**에게 적용되는 **기본 정렬 규칙**을 설정합니다. 반면, **`Modifier.align()`** 은 **개별 자식**에게 적용되어 `Box`의 기본 정렬을 **무시하고 자신만의 정렬을 지정**하는 데 사용됩니다. 네, 둘을 함께 사용할 수 있으며, 이때 **`Modifier.align()`이 `contentAlignment`보다 우선순위가 높습니다.**

-----

### 1. 역할과 적용 범위의 차이

#### `Box(contentAlignment = ...)`: 컨테이너 전체의 기본 정렬 📥

  * **역할:** `Box` 컴포저블 자체의 파라미터로, 내부에 포함된 모든 자식들에게 적용될 **기본적인 정렬 방식**을 지정합니다.
  * **적용 범위:** `Box`의 **모든 직속 자식**들에게 영향을 미칩니다. 만약 자식이 개별적으로 `Modifier.align()`을 지정하지 않았다면, 이 `contentAlignment` 값을 따르게 됩니다.
  * **비유:** 문서 편집기에서 전체 문서의 기본 정렬을 "가운데 정렬"로 설정하는 것과 같습니다.

#### `Modifier.align(...)`: 개별 자식의 예외적 정렬 🎯

  * **역할:** 개별 자식 컴포저블에 적용되는 `Modifier`로, 해당 자식 **하나에 대해서만** 정렬 방식을 지정합니다.
  * **적용 범위:** 이 `Modifier`가 적용된 **단 하나의 자식**에만 영향을 미칩니다. `Modifier.align()`은 `BoxScope` 내에서만 사용할 수 있는 특별한 스코프 지정 Modifier입니다.
  * **비유:** 전체 문서가 "가운데 정렬"로 설정되어 있더라도, 특정 한 문단만 "오른쪽 정렬"로 따로 지정하는 것과 같습니다.

-----

### 2. 함께 사용 시 우선순위

`contentAlignment`과 `Modifier.align()`이 함께 사용될 경우, **더 구체적인 지시인 `Modifier.align()`이 항상 우선**합니다.

  * 자식 컴포저블에 `Modifier.align()`이 **지정되어 있으면**, 부모 `Box`의 `contentAlignment` 값은 무시되고 `Modifier.align()`에 지정된 정렬을 따릅니다.
  * 자식 컴포저블에 `Modifier.align()`이 **지정되어 있지 않으면**, 부모 `Box`의 `contentAlignment` 값을 따릅니다.
  * 만약 부모 `Box`의 `contentAlignment`도 지정되어 있지 않다면, 기본값인 `Alignment.TopStart`(좌상단)가 적용됩니다.

-----

### 3. 구현 예시

아래 예시는 `contentAlignment`과 `Modifier.align()`을 함께 사용하여 각 자식을 다르게 정렬하는 방법을 보여줍니다.

```kotlin
@Composable
fun AlignmentExample() {
    Box(
        // 1. 기본 정렬은 'Center'(중앙)로 설정
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(300.dp)
            .background(Color.LightGray)
    ) {
        // 2. align이 없으므로 부모의 'Center' 정렬을 따름
        Text("저는 중앙에 있어요")

        // 3. align이 있으므로 부모의 정렬을 무시하고 'TopEnd'(우상단)에 배치됨
        Button(
            onClick = { },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Text("우상단")
        }

        // 4. align이 있으므로 부모의 정렬을 무시하고 'BottomStart'(좌하단)에 배치됨
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Check",
            modifier = Modifier.align(Alignment.BottomStart)
        )
    }
}
```

**결과:**

  * "저는 중앙에 있어요" 텍스트는 `Box`의 중앙에 위치합니다.
  * "우상단" 버튼은 `Box`의 오른쪽 상단 모서리에 위치합니다.
  * 체크 아이콘은 `Box`의 왼쪽 하단 모서리에 위치합니다.

-----

### 4. 결론: 언제 무엇을 사용해야 하는가?

  * **`Box(contentAlignment = ...)`**:
    `Box` 내부의 **대부분 또는 모든 자식들이 동일한 정렬 방식**을 가져야 할 때 사용합니다. 각 자식에게 Modifier를 일일이 추가할 필요가 없어 코드가 간결해집니다.

  * **`Modifier.align(...)`**:
    대부분의 자식들은 기본 정렬을 따르지만, **특정 자식 하나만 예외적으로 다른 위치**에 배치하고 싶을 때 사용합니다. 개별 요소에 대한 세밀한 제어를 제공합니다.

이 두 가지를 함께 사용하는 것은 매우 일반적이고 강력한 패턴입니다. `contentAlignment`으로 전체적인 정렬 규칙을 정하고, `Modifier.align()`으로 특정 요소들의 위치를 미세 조정함으로써 복잡하고 유연한 레이아웃을 효과적으로 구성할 수 있습니다.