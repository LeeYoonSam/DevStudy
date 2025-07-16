# Arrangement와 Alignment의 차이점은 무엇인가요?

Jetpack Compose에서 **`Arrangement`** 와 **`Alignment`** 는 모두 레이아웃 내에서 UI 요소의 위치를 지정하는 데 사용되지만, 서로 다른 목적을 수행합니다. 이 둘의 차이점을 이해하는 것은 UI 컴포넌트를 효과적으로 구성하는 데 도움이 됩니다.

-----

## Arrangement란 무엇인가? (주축 방향의 자식들 간 배치)

**`Arrangement`** 는 `Row`나 `Column`과 같이 단일 방향으로 아이템을 배열하는 레이아웃 내에서, **여러 자식 컴포저블들의 간격과 분배**를 제어합니다. 이는 레이아웃의 **주축(main axis)** 을 따라 자식들이 어떻게 배치되는지를 결정합니다.

예를 들어, `Row`에서 `Arrangement`는 아이템들이 **수평으로** 어떻게 간격을 둘지를 정의하는 반면, `Column`에서는 아이템들이 **수직으로** 어떻게 간격을 둘지를 정의합니다.

### Arrangement 예시

```kotlin
@Composable
fun RowWithArrangement() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween // 주축(수평) 방향의 배치 설정
    ) {
        Text(text = "Hello")
        Text(text = "skydoves")
    }
}
```

이 예시에서:

  * `Arrangement.SpaceBetween`은 두 `Text` 컴포저블이 사용 가능한 너비에 걸쳐 균등한 간격으로 배치되도록 보장합니다.
  * 첫 번째 `Text`는 시작점에 위치하고, 두 번째 `Text`는 끝점에 위치하며, 그 사이의 공간은 분배됩니다.

-----

## Alignment란 무엇인가? (교차축 방향의 정렬)

**`Alignment`** 는 자식 컴포저블들이 부모 내에서 **교차축(cross axis)** 을 따라 어떻게 배치될지를 결정합니다. `Box`, `Row`, `Column`과 같은 레이아웃에서 요소들이 컨테이너의 경계에 상대적으로 어떻게 정렬되는지를 제어하는 데 사용됩니다.

  * **`Row` (주축: 수평)에서, `Alignment`는 수직 정렬에 영향을 줍니다.**
  * **`Column` (주축: 수직)에서, `Alignment`는 수평 정렬에 영향을 줍니다.**
  * **`Box`에서, `Alignment`는 수평 및 수직 정렬 모두에 영향을 줍니다.**

### Alignment 예시

```kotlin
@Composable
fun ColumnWithAlignment() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally // 교차축(수평) 방향의 정렬 설정
    ) {
        Text(text = "Hello")
        Text(text = "skydoves")
    }
}
```

이 예시에서:

  * `horizontalAlignment = Alignment.CenterHorizontally`는 두 `Text` 요소가 `Column` 내에서 중앙 정렬되도록 보장합니다.
  * `Column`은 여전히 자식들을 수직으로 쌓지만, 이제 그들은 수평축을 따라 중앙에 정렬됩니다.

-----

## 주요 차이점: 주축 vs. 교차축

**`Arrangement`** 는 여러 자식 요소들을 **주축**을 따라 배치하는 데 사용됩니다 (`Row`에서는 수평, `Column`에서는 수직). 반면에, **`Alignment`** 는 개별 요소들을 부모 내에서 **교차축**을 따라 배치하는 데 사용됩니다.

이들을 올바르게 사용하는 것은 Jetpack Compose에서 효율적이고 잘 구조화된 레이아웃을 구축하는 데 필수적입니다. `Arrangement`와 `Alignment`는 다른 목적을 수행하기 때문에 어느 것을 사용해야 할지 결정할 때 혼동을 일으킬 수 있습니다. 그들의 명확한 역할을 이해하면 더 나은 레이아웃 결정을 내리고 불필요한 복잡성을 피하는 데 도움이 됩니다.

-----

## 요약

`Arrangement`와 `Alignment` 모두 UI 요소의 위치를 지정하는 데 도움이 되지만, 다른 축에서 작동하며 다른 목적을 가집니다. **`Arrangement`** 는 **여러 아이템**이 **주축**을 따라 어떻게 간격을 둘지를 제어하는 반면, **`Alignment`** 는 **아이템들**이 부모 컨테이너 내에서 **교차축**을 따라 어떻게 위치할지를 결정합니다.

-----

## Q. 아이템들이 화면 전체에 걸쳐 균등한 간격으로 배치되고 상단에 정렬되어야 하는 `Row`를 상상해 보세요. 어떤 `Arrangement`와 `Alignment`의 조합을 사용하고, 그 이유는 무엇인가요?

이 시나리오에서는 **`horizontalArrangement = Arrangement.SpaceBetween`** 과 **`verticalAlignment = Alignment.Top`** 의 조합을 사용합니다. `Arrangement.SpaceBetween`은 아이템들을 `Row`의 전체 너비에 걸쳐 **수평으로 균등하게 분배**하고, `Alignment.Top`은 각 아이템을 `Row`의 **수직 상단에 정렬**시켜주기 때문입니다.

-----

## 1. 해결책: `Arrangement.SpaceBetween`과 `Alignment.Top`의 조합

`Row` 컴포저블 내에서 아이템들을 화면 전체 너비에 걸쳐 균등한 간격으로 배치하고, 동시에 모든 아이템을 상단에 정렬하려면 다음과 같이 `horizontalArrangement`와 `verticalAlignment` 파라미터를 함께 사용해야 합니다.

-----

## 2. 각 파라미터의 역할과 이유

### 2.1. `horizontalArrangement` (수평 배치) ↔️

  * **역할:** `Row`의 **주축(main axis)인 수평 방향**으로 자식 컴포저블들을 어떻게 분배하고 간격을 둘지 결정합니다.
  * **`Arrangement.SpaceBetween`을 사용하는 이유:** 이 값은 첫 번째 자식은 시작점에, 마지막 자식은 끝점에 배치한 후, 나머지 자식들 **사이에(between) 동일한 간격**을 부여합니다. 이는 "화면 전체에 걸쳐 균등한 간격으로 배치"라는 요구사항을 가장 잘 만족시키는 옵션 중 하나입니다.
  * **다른 옵션:**
      * **`Arrangement.SpaceEvenly`:** 모든 자식들 **주변에 동일한 공간**을 부여합니다 (양 끝에도 공간이 생김).
      * **`Arrangement.SpaceAround`:** 각 자식이 양옆으로 동일한 공간을 갖도록 분배합니다 (양 끝의 공간은 아이템 사이 공간의 절반이 됨).

### 2.2. `verticalAlignment` (수직 정렬) ↕️

  * **역할:** `Row`의 **교차축(cross axis)인 수직 방향**으로 자식 컴포저블들을 어떻게 정렬할지를 결정합니다.
  * **`Alignment.Top`을 사용하는 이유:** 이 값은 `Row` 컨테이너의 **상단**을 기준으로 모든 자식들을 정렬합니다. 만약 자식들의 높이가 서로 다르더라도, 모든 자식의 상단이 `Row`의 상단에 맞춰지게 됩니다. 이는 "상단에 정렬"이라는 요구사항을 직접적으로 만족시킵니다.
  * **다른 옵션:**
      * **`Alignment.CenterVertically`:** 수직으로 중앙 정렬합니다 (기본값).
      * **`Alignment.Bottom`:** 하단에 정렬합니다.

-----

## 3. 코드 예시

```kotlin
@Composable
fun EvenlySpacedTopAlignedRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth() // Row가 화면 전체 너비를 차지하도록 설정
            .height(100.dp) // Row의 높이를 지정하여 수직 정렬을 명확히 볼 수 있게 함
            .background(Color.LightGray),
        // 수평 배치: 자식들 사이에 균등한 간격
        horizontalArrangement = Arrangement.SpaceBetween,
        // 수직 정렬: 모든 자식을 상단에 맞춤
        verticalAlignment = Alignment.Top
    ) {
        // 높이가 다른 자식들을 배치하여 정렬 효과 확인
        Text(text = "아이템 1", modifier = Modifier.background(Color.Red))
        Text(text = "아이템 2", fontSize = 24.sp, modifier = Modifier.background(Color.Green))
        Text(text = "아이템 3", modifier = Modifier.background(Color.Blue))
    }
}
```

**실행 결과:**

  * 3개의 `Text` 컴포저블은 `Row`의 너비 전체에 걸쳐 균등한 간격으로 배치됩니다 ("아이템 1"은 맨 왼쪽, "아이템 3"은 맨 오른쪽).
  * 높이가 다른 세 `Text` 모두 자신의 상단 경계가 `Row`의 상단 경계에 맞춰져 정렬됩니다.

-----

## 4. 결론

`Row` 컴포저블을 사용할 때, **`Arrangement`** 는 주축인 **수평 방향의 아이템 간 간격과 분배**를 책임지고, **`Alignment`** 는 교차축인 **수직 방향의 정렬**을 책임집니다. 이 두 가지 개념을 명확히 구분하고 조합하면, 원하는 대부분의 2차원 레이아웃을 효과적으로 구성할 수 있습니다.


## Q. 왜 `Row`에서 `horizontalAlignment`를 설정하는 것은 작동하지 않지만, `Column`에서는 작동하나요? 이러한 동작을 유발하는 Compose 레이아웃 규칙은 무엇인가요?

`Row`에서 `horizontalAlignment`가 작동하지 않는 이유는 **`Row`의 주축(main axis)이 수평 방향이기 때문**입니다. `Alignment` 관련 파라미터(예: `horizontalAlignment`, `verticalAlignment`)는 항상 **교차축(cross axis)** 의 정렬을 담당합니다.

`Column`의 주축은 수직이므로 교차축이 수평이 되어 `horizontalAlignment`가 작동하지만, `Row`의 주축은 수평이므로 교차축은 수직이 되어 `verticalAlignment`만 의미를 가집니다.

---
## 1. 핵심 규칙: 주축(Main Axis)과 교차축(Cross Axis)

Jetpack Compose의 단방향 레이아웃(`Row`, `Column`)은 **주축**과 **교차축**이라는 두 가지 축을 기준으로 자식들을 배치합니다. 이 두 축의 개념을 이해하는 것이 핵심입니다.

* **주축 (Main Axis):**
    자식 컴포저블들이 **순차적으로 놓이는 방향**입니다.
    * `Row`의 주축은 **수평(Horizontal)** 입니다.
    * `Column`의 주축은 **수직(Vertical)** 입니다.
* **교차축 (Cross Axis):**
    주축에 대해 **수직인 방향**입니다.
    * `Row`의 교차축은 **수직(Vertical)** 입니다.
    * `Column`의 교차축은 **수평(Horizontal)** 입니다.

그리고 이 두 축에 대한 제어는 서로 다른 파라미터가 담당합니다.
* **`Arrangement`**: **주축** 방향으로 자식들 **간의 간격과 분배**를 제어합니다.
* **`Alignment`**: **교차축** 방향으로 자식들을 **정렬**합니다.

---
## 2. Row에서의 동작 방식

`Row`는 자식들을 수평으로 나열하는 컴포저블입니다.
* **주축 = 수평(Horizontal):**
    자식들이 왼쪽에서 오른쪽으로 어떻게 배치될지, 그들 사이의 간격을 어떻게 둘지는 **`horizontalArrangement`** 파라미터로 제어합니다. (`Arrangement.Start`, `Arrangement.Center`, `Arrangement.SpaceBetween` 등)
* **교차축 = 수직(Vertical):**
    자식들이 `Row`의 높이 안에서 위, 중앙, 아래 중 어디에 위치할지는 **`verticalAlignment`** 파라미터로 제어합니다. (`Alignment.Top`, `Alignment.CenterVertically`, `Alignment.Bottom` 등)

따라서, `Row` 컴포저블의 시그니처에는 **`horizontalAlignment`라는 파라미터 자체가 존재하지 않습니다.** 수평 방향은 `Arrangement`의 역할이기 때문입니다.

---
## 3. Column에서의 동작 방식

`Column`은 자식들을 수직으로 나열하는 컴포저블입니다.
* **주축 = 수직(Vertical):**
    자식들이 위에서 아래로 어떻게 배치될지, 그들 사이의 간격은 **`verticalArrangement`** 파라미터로 제어합니다. (`Arrangement.Top`, `Arrangement.Center`, `Arrangement.SpaceBetween` 등)
* **교차축 = 수평(Horizontal):**
    자식들이 `Column`의 너비 안에서 왼쪽, 중앙, 오른쪽 중 어디에 위치할지는 **`horizontalAlignment`** 파라미터로 제어합니다. (`Alignment.Start`, `Alignment.CenterHorizontally`, `Alignment.End` 등)

따라서 `Column`에서는 교차축이 수평이므로 `horizontalAlignment` 파라미터가 존재하고 정상적으로 작동합니다.

---
## 4. 요약: 올바른 파라미터 사용

| 레이아웃 | 주축 (Arrangement 제어) | 교차축 (Alignment 제어) |
| :--- | :--- | :--- |
| **`Row`** | **수평** (`horizontalArrangement`) | **수직** (`verticalAlignment`) |
| **`Column`** | **수직** (`verticalArrangement`) | **수평** (`horizontalAlignment`) |

**결론적으로,** `Row`에서 수평 방향의 배치를 제어하고 싶다면 `horizontalAlignment`이 아닌 **`horizontalArrangement`** 를 사용해야 하며, `Column`에서 수직 방향의 배치를 제어하고 싶다면 `verticalAlignment`가 아닌 **`verticalArrangement`** 를 사용해야 합니다. `Alignment`는 항상 교차축에 대한 정렬을 담당한다는 규칙을 기억하는 것이 중요합니다.