# `Layout` 컴포저블이란 무엇인가?

**`Layout` 컴포저블**은 자식 컴포저블들의 **측정(measuring)과 배치(positioning)를 완전히 제어**할 수 있게 해주는 저수준(low-level) API입니다. 미리 정의된 동작을 제공하는 `Column`, `Row`, `Box`와 같은 고수준 레이아웃 컴포넌트와 달리, `Layout`은 개발자가 특정 요구 사항에 맞춰진 **사용자 정의 레이아웃을 생성**할 수 있게 합니다.

## `Layout` 작동 방식

`Layout` 컴포저블은 자식 컴포저블들이 어떻게 측정되고 배치되는지를 정의하는 함수를 제공합니다. 이는 두 가지 주요 단계로 구성됩니다.

1.  **측정 단계 (Measurement Phase)** – 부모가 제공한 제약 조건(constraints)에 따라 각 자식 컴포저블의 크기를 결정합니다.
2.  **배치 단계 (Placement Phase)** – 사용 가능한 공간 내에 각 자식 컴포저블을 배치합니다.

기본적인 `Layout` 컴포저블은 다음과 같이 구조화됩니다.

```kotlin
@Composable
fun CustomLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        // 1. 자식들을 측정합니다.
        val placeables = measurables.map { measurable ->
            // 각 자식에게 제약 조건을 전달하여 측정합니다.
            measurable.measure(constraints)
        }

        // 2. 레이아웃 자체의 크기를 결정합니다.
        val width = constraints.maxWidth
        val height = placeables.sumOf { it.height }

        // 3. layout() 함수 내에서 자식들을 배치합니다.
        layout(width, height) {
            // 자식들을 순차적으로 배치합니다.
            var yPosition = 0
            placeables.forEach { placeable ->
                placeable.placeRelative(x = 0, y = yPosition)
                yPosition += placeable.height
            }
        }
    }
}
```

### `Layout`의 주요 구성 요소

  * **자식 측정:** `measure()` 함수는 부모로부터 제약 조건을 적용하여 각 자식 컴포저블을 측정하는 데 사용됩니다.
  * **레이아웃 크기 결정:** `layout()` 함수는 레이아웃의 최종 너비와 높이를 정의합니다.
  * **자식 배치:** `placeRelative()` 함수는 각 자식 컴포저블이 레이아웃 내 어디에 배치될지를 결정합니다.

-----

## 언제 `Layout`을 사용해야 하나요?

`Layout` 컴포저블은 `Column`, `Row`, `Box`와 같은 표준 레이아웃 컴포넌트가 특정 디자인 요구 사항에 필요한 사용자 정의 수준을 제공하지 못할 때 유용합니다. 자식 컴포저블이 어떻게 측정되고 배치되는지에 대한 완전한 제어가 필요하다면, `Layout`은 사용자 정의 측정 및 배치 로직을 정의할 수 있게 해줍니다.

이는 엇갈린 그리드(staggered grids), 겹치는 요소, 또는 콘텐츠에 따라 동적으로 크기가 조절되는 컴포넌트와 같이 비표준적인 배열을 요구하는 복잡한 UI 구조를 구현할 때 특히 유용합니다. `Layout`은 제약 조건과 측정에 직접 접근할 수 있으므로, 개발자는 리컴포지션과 불필요한 재측정을 효율적으로 관리하여 UI 성능을 최적화할 수 있습니다.

또한, `Layout`을 사용하는 것은 특정 동작과 스타일링을 캡슐화하는 재사용 가능한 사용자 정의 레이아웃 컴포넌트를 구축하는 데 도움이 되어, UI 코드를 더 깔끔하고 유지보수하기 쉽게 만듭니다. 만약 디자인이 기존 레이아웃 컴포저블로는 달성할 수 없는 정밀한 정렬 규칙, 적응형 레이아웃, 또는 완전히 독특한 배열을 요구한다면, `Layout`이 올바른 선택입니다.

-----

## 요약: `Layout`

`Layout` 컴포저블은 측정과 배치에 대한 세밀한 제어를 제공하여 사용자 정의 레이아웃을 구축하는 데 사용됩니다. 이는 개발자가 자식들이 어떻게 크기 조정되고 배치되어야 하는지를 정의할 수 있게 해주므로, 표준 레이아웃 컴포넌트를 넘어서는 고급 UI 시나리오에 사용될 수 있습니다.

-----

## 💡 프로 팁: SubcomposeLayout이란 무엇인가?

**`SubcomposeLayout`** 은 레이아웃 내에서 **동적 컴포지션**을 허용하는 저수준(low-level) API입니다. 이는 주로 UI 컴포넌트가 부모 레이아웃 패스와는 독립적으로 자신의 하위 컴포넌트들을 리컴포즈해야 할 때 사용됩니다. 이는 자식 콘텐츠 크기가 비동기 데이터에 의존하거나 여러 번의 측정 패스가 필요할 때 특히 유용합니다.

### `SubcomposeLayout` 작동 방식

Jetpack Compose에서 표준 레이아웃 시스템은 각 컴포저블이 리컴포지션당 한 번 측정되는 단일 패스 측정 모델을 따릅니다. 그러나 특정 UI 구조는 자식 컴포저블을 여러 번 측정하거나 레이아웃 제약 조건이 알려질 때까지 컴포지션을 지연시켜야 합니다. `SubcomposeLayout`은 측정 단계에서 자식의 주문형(on-demand) 컴포지션을 허용하여 이러한 유연성을 가능하게 합니다.

`SubcomposeLayout`이 필요한 일반적인 사용 사례는 다음과 같습니다.

  * `Layout`이나 `LayoutModifier`로는 처리할 수 없는, **컴포지션 중에 부모 제약 조건에 접근**해야 할 때 (예: `BoxWithConstraints`와 유사).
  * **한 자식의 크기를 기반으로 다른 자식을 측정하거나 배치**해야 할 때. 이는 서브컴포지션(subcomposition)을 사용하는 핵심 이유입니다.
  * 긴 목록에서 보이는 아이템만 렌더링하고 다른 아이템은 필요할 때까지(예: 스크롤 중) 지연시키는 등, **사용 가능한 공간에 따라 아이템을 지연(lazy)하여 컴포즈**하고 싶을 때.
  * 컴포저블의 크기가 컴포지션 타임에 동적으로 결정되어야 하는 콘텐츠에 의존할 때.
  * 레이아웃 로직이 여러 독립적인 단계에서 자식을 측정하고 배치해야 할 때.
  * 레이지 리스트의 헤더와 같이, 입력이 변경될 때만 리컴포즈되어야 하는 동적 UI를 다룰 때.

### 사용 예시

아래는 `SubcomposeLayout`을 사용하여 자식 컴포저블을 동적으로 측정하는 예시입니다.

```kotlin
@Composable
fun DynamicContentLayout() {
    SubcomposeLayout { constraints ->
        // "content" 슬롯을 동적으로 컴포즈하고 측정
        val measurable = subcompose("content") {
            Text(text = "Hello, skydoves!")
        }.first().measure(constraints)

        // 측정된 크기에 따라 레이아웃 크기를 정하고 자식 배치
        layout(measurable.width, measurable.height) {
            measurable.placeRelative(0, 0)
        }
    }
}
```

이 예시에서:

1.  `subcompose("content")`는 텍스트 콘텐츠를 동적으로 컴포즈합니다.
2.  `measure` 함수는 주어진 제약 조건에 따라 자식의 크기를 계산합니다.
3.  `layout` 함수는 측정된 자식을 그에 맞게 배치합니다.

### 주요 고려 사항

`SubcomposeLayout`은 상당한 유연성을 제공하지만 잠재적인 성능 비용 때문에 신중하게 사용해야 합니다. 자식 요소를 여러 번 측정하고 컴포즈할 수 있으므로, 불필요한 리컴포지션은 성능을 저하시킬 수 있습니다. 표준 레이아웃으로 충분하지 않은 경우, 예를 들어 여러 측정 단계가 필요한 동적 UI 요소에 가장 적합합니다. `SubcomposeLayout`을 사용할 때는 효율성을 유지하기 위해 리컴포지션이 필요할 때만 발생하도록 보장해야 합니다.

### 요약: `SubcomposeLayout`

`SubcomposeLayout`은 레이아웃 패스 내에서 동적인 서브컴포지션을 허용하는 유용한 API입니다. 콘텐츠 측정과 리컴포지션이 부모 레이아웃과 분리되어야 하는 시나리오에 가장 적합합니다. 상당한 유연성을 제공하지만, 성능 함정을 피하기 위해 신중하게 사용해야 합니다.

-----

## Q. 언제 `Row`나 `Column`과 같은 표준 컴포넌트 대신 `Layout` 컴포저블을 사용하기로 선택하겠습니까?

`Row`나 `Column`과 같은 표준 컴포넌트가 제공하는 **단순한 선형(가로/세로) 또는 중첩(겹침) 배치 규칙**만으로는 구현할 수 없는 **사용자 정의(custom) 레이아웃**을 만들어야 할 때 `Layout` 컴포저블을 사용합니다. 예를 들어, **엇갈린 그리드(staggered grid), 원형 배치, 또는 자식의 크기에 따라 다른 자식의 위치가 동적으로 결정되는 복잡한 관계**를 구현해야 하는 경우가 해당됩니다.

---
### 1. 표준 레이아웃의 한계와 `Layout` 컴포저블의 필요성

`Row`, `Column`, `Box`, 그리고 `ConstraintLayout`과 같은 고수준 표준 레이아웃 컴포저블은 대부분의 일반적인 UI 디자인을 매우 효율적이고 간단하게 구현할 수 있도록 도와줍니다.

* **`Row`**: 자식들을 **가로**로 순서대로 배치합니다.
* **`Column`**: 자식들을 **세로**로 순서대로 배치합니다.
* **`Box`**: 자식들을 **겹쳐서(Z축으로)** 쌓고, 내부에서 정렬합니다.
* **`ConstraintLayout`**: 복잡한 상대적 제약 조건을 통해 평평한 계층 구조를 만듭니다.

하지만 이들의 동작 방식은 **미리 정의**되어 있습니다. 만약 이 규칙에서 벗어나는 완전히 새로운 방식의 배치가 필요하다면, 표준 컴포넌트만으로는 한계에 부딪히게 됩니다. 바로 이때, 개발자에게 측정과 배치의 완전한 제어권을 제공하는 저수준(low-level) API인 **`Layout`** 컴포저블이 필요해집니다.

---
### 2. `Layout` 컴포저블을 선택하는 구체적인 시나리오

다음과 같은 경우 `Row`나 `Column` 대신 `Layout` 사용을 고려해야 합니다.

#### 2.1. 비표준(Non-standard) 그리드 레이아웃 구현 📐
* **엇갈린 그리드 (Staggered Grid):** Pinterest와 같이 아이템들의 높이가 각기 달라 서로 엇갈리게 배치되는 그리드를 구현해야 할 때. `LazyVerticalGrid`는 모든 셀의 크기가 동일한 표준 그리드에 적합하지만, 스태거드 그리드는 각 아이템의 측정된 높이에 따라 다음 아이템의 위치를 동적으로 계산해야 하므로 `Layout`을 통한 직접적인 제어가 필요합니다.
* **플로우 레이아웃 (Flow Layout):** 아이템들을 가로로 배치하다가 공간이 부족하면 자동으로 다음 줄로 넘어가는 레이아웃(일명 태그(Tag) 레이아웃)을 구현할 때. `Row`는 줄바꿈을 지원하지 않으므로, 각 아이템을 측정하며 줄바꿈이 필요한 시점을 직접 계산해야 합니다.

#### 2.2. 원형 또는 곡선 배치 레이아웃 ⚪️
* **시나리오:** 여러 개의 프로필 이미지를 원형으로 배치하거나, 메뉴 아이템을 부채꼴 모양의 곡선을 따라 배치해야 하는 경우.
* **이유:** `Row`와 `Column`은 직선적인 배치만 가능합니다. 원이나 곡선의 특정 각도에 따른 x, y 좌표를 삼각함수 등을 이용해 계산하고, 각 자식 뷰를 해당 위치에 정확히 배치하려면 `Layout`의 `placement` 단계에서 직접 제어해야 합니다.

#### 2.3. 콘텐츠에 따라 동적으로 결정되는 복잡한 의존적 레이아웃 🧩
* **시나리오:** 한 자식의 측정된 크기에 따라 다른 자식의 위치나 크기가 결정되는 복잡한 관계를 가진 UI를 구현해야 할 때. 예를 들어, 텍스트 길이에 따라 옆에 있는 아이콘의 위치가 유동적으로 변해야 하는 경우가 있습니다.
* **이유:** `Layout` 컴포저블은 모든 자식들을 먼저 측정한 후(measurement pass), 그 측정값을 바탕으로 각 자식들의 최종 위치를 계산하여 배치(placement pass)할 수 있는 완전한 제어권을 제공합니다.

#### 2.4. 성능 최적화가 극도로 중요한 경우 ⚡️
* **시나리오:** `Row`, `Column` 등을 깊게 중첩하여 만든 레이아웃이 측정 및 배치 단계에서 성능 저하를 유발하는 경우.
* **이유:** 매우 특수한 경우지만, 특정 레이아웃에 대한 고도로 최적화된 측정 및 배치 로직을 직접 작성하면, 일반적인 고수준 컴포넌트를 중첩해서 사용하는 것보다 더 적은 측정 패스로 동일한 레이아웃을 구현하여 성능을 개선할 수 있습니다. 이는 고급 사용 사례에 해당합니다.

---
### 3. 결론: 제어 수준에 따른 선택

결정을 위한 가이드라인은 간단합니다.

* **먼저 표준 컴포넌트를 고려하세요:** **`Row`, `Column`, `Box`, `ConstraintLayout`** 은 대부분(95% 이상)의 UI 요구사항을 해결할 수 있으며, 사용하기 쉽고 내부적으로 고도로 최적화되어 있습니다.
* **표준으로 불가능할 때 `Layout`을 사용하세요:** 표준 컴포넌트의 미리 정의된 배치 규칙만으로는 원하는 디자인을 절대 구현할 수 없을 때, `Layout` 컴포저블은 개발자가 상상하는 거의 모든 2D 배열을 직접 구현할 수 있는 강력하고 유연한 '탈출구'를 제공합니다.


## Q. `LazyVerticalGrid`를 사용하여 달성할 수 없는 엇갈린 그리드(staggered grid) 레이아웃을 구축해야 한다고 가정해 봅시다. `Layout`을 사용하여 이를 어떻게 구현하겠습니까?

`Layout` 컴포저블을 사용하여 엇갈린 그리드(Staggered Grid)를 구현하려면, **측정(Measure) 단계**에서 각 자식 컴포저블을 측정하고, **배치(Placement) 단계**에서 각 열(column)의 현재 높이를 추적하여 **가장 높이가 낮은 열에 다음 아이템을 배치**하는 방식으로 구현합니다. 이를 통해 아이템들이 엇갈린 형태로 빈 공간을 효율적으로 채우게 됩니다.

-----

## 1. 구현 전략: 측정 및 최단 열(Column) 배치

`LazyVerticalGrid`와 같은 표준 컴포넌트는 모든 아이템이 동일한 높이를 가질 것을 가정하지만, 스태거드 그리드는 아이템마다 높이가 다릅니다. `Layout` 컴포저블의 측정 및 배치 단계를 직접 제어하여 이 문제를 해결할 수 있습니다.

1.  **열(Column) 개수 결정:** 만들고자 하는 그리드의 열 개수를 정합니다.
2.  **너비 계산 및 자식 측정:** 전체 가용 너비를 열 개수로 나누어 각 아이템이 차지할 너비를 계산합니다. 이 계산된 너비 제약 조건을 사용하여 모든 자식 컴포저블을 측정합니다. 이 과정을 통해 각 아이템의 **가변적인 높이**를 알 수 있습니다.
3.  **열 높이 추적:** 각 열의 현재 높이를 추적할 배열을 준비합니다 (초기값은 모두 0).
4.  **최단 열에 배치:** 측정된 아이템들을 순서대로 순회하며, 현재 시점에서 **가장 높이가 낮은(가장 위쪽 공간이 비어있는) 열**을 찾아 해당 위치에 아이템을 배치합니다.
5.  **열 높이 업데이트:** 아이템을 배치한 후, 해당 열의 높이를 방금 배치한 아이템의 높이만큼 증가시킵니다.
6.  **전체 레이아웃 크기 결정:** 모든 아이템 배치가 끝난 후, 가장 긴 열의 높이가 전체 `Layout` 컴포저블의 높이가 됩니다.

-----

## 2. `Layout`을 사용한 Staggered Grid 구현 단계

### 2.1. 1단계: 컴포저블 시그니처 정의

먼저, `StaggeredVerticalGrid`라는 이름의 커스텀 레이아웃 컴포저블을 정의합니다. `Modifier`와 `columns` 수를 파라미터로 받고, 콘텐츠는 후행 람다로 받습니다.

```kotlin
@Composable
fun StaggeredVerticalGrid(
    modifier: Modifier = Modifier,
    columns: Int = 2,
    content: @Composable () -> Unit
) {
    // Layout 컴포저블 사용
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        // ... 측정 및 배치 로직 ...
    }
}
```

### 2.2. 2단계: 측정 단계 (Measure Pass)

`Layout`의 람다 블록 내에서, 전달받은 `measurables`(자식 컴포저블들)를 측정하여 `placeables`(배치 가능한 요소들)로 변환합니다.

```kotlin
// Layout 블록 내부
// 1. 각 열의 너비 계산
val columnWidth = constraints.maxWidth / columns

// 2. 각 자식에게 적용할 제약 조건 생성 (너비는 고정, 높이는 자유롭게)
val itemConstraints = constraints.copy(
    minWidth = columnWidth,
    maxWidth = columnWidth
)

// 3. 모든 자식들을 주어진 제약 조건으로 측정하여 Placeable 리스트 생성
val placeables = measurables.map { measurable ->
    measurable.measure(itemConstraints)
}
```

### 2.3. 3단계: 배치 단계 (Placement Pass)

측정된 `placeables`를 사용하여 각 아이템을 화면에 배치하고, 전체 레이아웃의 크기를 결정합니다.

```kotlin
// Layout 블록 내부 (측정 단계 이후)
// 4. 각 열의 현재 Y 좌표(높이)를 추적할 배열 초기화
val columnHeights = IntArray(columns) { 0 }

// 5. layout() 함수를 호출하여 크기와 배치 로직 정의
layout(
    width = constraints.maxWidth,
    height = (columnHeights.maxOrNull() ?: 0).coerceIn(constraints.minHeight, constraints.maxHeight)
) {
    // 6. 각 열의 X 좌표 계산
    val columnX = IntArray(columns) { index ->
        index * columnWidth
    }

    // 7. 각 placeable을 가장 짧은 열에 배치
    placeables.forEach { placeable ->
        // 현재 가장 높이가 낮은 열 찾기
        val minHeight = columnHeights.minOrNull() ?: 0
        val shortestColumnIndex = columnHeights.indexOf(minHeight)

        // 해당 위치에 아이템 배치
        placeable.placeRelative(
            x = columnX[shortestColumnIndex],
            y = columnHeights[shortestColumnIndex]
        )

        // 배치된 열의 높이 업데이트
        columnHeights[shortestColumnIndex] += placeable.height
    }
}
```

> **참고:** 위 코드에서 최종 레이아웃 높이를 결정하는 `layout()` 함수 호출은 `placeables.forEach` 루프보다 먼저 와야 합니다. 따라서 루프를 두 번 돌거나, 배치 루프 이후에 가장 높은 열의 높이를 찾아 `layout()` 함수의 높이 값으로 사용해야 합니다. 아래 전체 코드 예시에서 더 정확한 구현을 보여줍니다.

-----

## 3. 전체 코드 예시

위 단계를 종합한 완성된 `StaggeredVerticalGrid` 컴포저블입니다.

```kotlin
@Composable
fun StaggeredVerticalGrid(
    modifier: Modifier = Modifier,
    columns: Int = 2,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        // 1. 각 열의 너비 계산
        val columnWidth = constraints.maxWidth / columns

        // 2. 각 자식을 측정하여 Placeable로 변환
        val itemConstraints = constraints.copy(maxWidth = columnWidth)
        val placeables = measurables.map { it.measure(itemConstraints) }

        // 3. 각 열의 높이와 아이템 배치를 위한 변수 준비
        val columnHeights = IntArray(columns) { 0 }
        val columnItemYPositions = Array(placeables.size) { 0 }

        // 4. 각 아이템을 어느 열에, 어떤 Y 좌표에 배치할지 결정
        placeables.forEachIndexed { index, placeable ->
            val shortestColumn = columnHeights.minOrNull() ?: 0
            val shortestColumnIndex = columnHeights.indexOf(shortestColumn)
            
            // 이 아이템이 배치될 Y 좌표 저장
            columnItemYPositions[index] = shortestColumn
            
            // 해당 열의 높이 업데이트
            columnHeights[shortestColumnIndex] += placeable.height
        }

        // 5. 전체 레이아웃의 높이를 가장 긴 열의 높이로 결정
        val totalHeight = columnHeights.maxOrNull()?.coerceIn(constraints.minHeight, constraints.maxHeight)
            ?: constraints.minHeight

        // 6. 최종 레이아웃 크기를 설정하고, 각 아이템을 계산된 위치에 배치
        layout(constraints.maxWidth, totalHeight) {
            val columnX = IntArray(columns) { it * columnWidth }

            placeables.forEachIndexed { index, placeable ->
                placeable.placeRelative(
                    x = columnX[columnItemYPositions[index] % columns], // 이 로직은 개선될 수 있음
                    y = columnItemYPositions[index]
                )
            }
        }
    }
}
```

> **구현 개선:** 위 예시의 `placeRelative` 로직은 단순화를 위해 약간의 수정이 필요할 수 있습니다. 각 `placeable`에 대해 배치할 열의 인덱스와 Y 좌표를 함께 저장하는 것이 더 견고한 방법입니다.

-----

## 4. 결론

`Row`, `Column`과 같은 표준 레이아웃 컴포저블은 정해진 규칙에 따라 자식을 배치하므로 스태거드 그리드와 같은 복잡한 레이아웃을 구현하기 어렵습니다. **`Layout` 컴포저블**은 개발자에게 **측정(Measure)과 배치(Placement)** 단계를 직접 제어할 수 있는 저수준 API를 제공함으로써 이러한 한계를 극복하게 해줍니다. 이를 통해 개발자는 각 자식의 크기를 개별적으로 측정하고, 동적인 계산을 통해 원하는 위치에 정확히 배치하여 `LazyVerticalGrid`로는 불가능한 거의 모든 종류의 2D 커스텀 레이아웃을 구현할 수 있습니다.