# `derivedStateOf`의 목적과 작동 방식은 무엇인가요?

`derivedStateOf`는 **하나 이상의 상태(State) 객체로부터 파생된 값을 계산**하는 컴포저블 API입니다. 이는 의존하는 상태 중 하나가 변경될 때만 파생된 값이 재계산되도록 보장하여, 반응형 상태 관계를 관리하는 효과적인 도구입니다.

`derivedStateOf`의 핵심 기능 중 하나는, 의존하는 상태들이 빈번하게 업데이트되더라도 **계산된 최종 값 자체가 변경될 때만 리컴포지션을 트리거**하여 리컴포지션을 최적화하는 것입니다. 이는 빈번한 상태 업데이트가 있는 시나리오에서 성능을 향상시키고 불필요한 리컴포지션을 피하는 데 특히 유용합니다.

`derivedStateOf`는 중복 리컴포지션을 방지하도록 설계되었지만, 약간의 연산 오버헤드를 발생시킵니다. 따라서 리컴포지션 방지가 매우 중요한 상황에서, 추가 비용보다 이점이 더 클 때 신중하게 사용해야 합니다.

-----

## 언제 `derivedStateOf`를 사용해야 하나요?

  * **파생 데이터 (Derived Data):** 필터링된 리스트나 조합된 텍스트와 같이 기존 상태들로부터 값을 계산해야 할 때.
  * **리컴포지션 방지:** 파생된 값이 자주 변경되는 상태에 의존하지만, 파생된 값 자체가 변경될 때만 리컴포지션을 원할 때.

-----

## `derivedStateOf` 사용 방법

아래는 검색어에 따라 아이템 목록을 필터링하는 실용적인 예시입니다.

```kotlin
@Composable
fun DerivedStateExample(items: List<String>, searchQuery: String) {
    // searchQuery나 items가 변경될 때만 remember 블록이 재실행됨
    val filteredItems by remember(searchQuery, items) {
        // derivedStateOf는 내부의 계산 결과(필터링된 리스트)가
        // 이전과 다를 때만 이 State를 읽는 컴포저블의 리컴포지션을 트리거함
        derivedStateOf {
            items.filter { it.contains(searchQuery, ignoreCase = true) }
        }
    }

    Column {
        Text("검색 결과:")
        filteredItems.forEach { item ->
            Text(item)
        }
    }
}
```

이 예시에서:

  * `filteredItems`는 `items`와 `searchQuery`로부터 파생됩니다.
  * `derivedStateOf`는 `searchQuery`나 `items`가 변경될 때만 리스트가 다시 계산되도록 보장합니다. (정확히는 `remember`가 이를 보장하며, `derivedStateOf`는 그 결과 리스트가 이전과 다를 때만 리컴포지션을 유발합니다.)

-----

## `derivedStateOf`는 어떻게 작동하나요?

1.  람다 블록 내에서 접근하는 상태들을 관찰합니다.
2.  관찰하는 상태 중 하나라도 변경되면 새로운 값을 계산합니다.
3.  새롭게 계산된 값이 이전 값과 **다를 경우에만** 리컴포지션을 트리거합니다.

-----

## 사용 시 유의할 핵심 사항

  * 항상 **`remember`와 함께 `derivedStateOf`를 사용**하여 리컴포지션 간에 파생된 상태가 유지되도록 보장하세요.
  * `derivedStateOf` 블록 안에 무거운 계산을 넣지 마세요. 부드러운 성능을 보장하기 위해 계산을 효율적으로 유지하세요.
  * 불필요한 리컴포지션을 피하는 것이 매우 중요한 경우에만 아껴서 사용하세요.

-----

## 고급 예시: 실시간으로 파생 상태 계산하기

```kotlin
@Composable
fun RealTimeDerivedStateExample() {
    var text by remember { mutableStateOf("") }
    // isInputValid는 text 상태로부터 파생됨
    val isInputValid by remember {
        derivedStateOf { text.length >= 5 }
    }

    Column {
        TextField(value = text, onValueChange = { text = it })
        if (isInputValid) { // isInputValid 값이 true에서 false로, 또는 그 반대로 바뀔 때만 리컴포지션
            Text("유효한 입력입니다")
        } else {
            Text("입력은 최소 5자 이상이어야 합니다")
        }
    }
}
```

위 예시에서:

  * 입력의 유효성(`isInputValid`)은 `text` 상태로부터 파생됩니다.
  * UI는 모든 텍스트 변경 시가 아니라, `isInputValid`의 값(`true` 또는 `false`)이 변경될 때만 리컴포지션됩니다.

-----

## 잘못된 사용법

두 개의 Compose 상태 객체를 결합할 때, "상태를 파생"시키고 있다는 이유만으로 항상 `derivedStateOf`를 사용해야 한다고 가정하는 것은 흔한 실수입니다. 많은 경우, 이는 아래 예시와 같이 불필요한 오버헤드를 유발하며 필요하지 않습니다.

```kotlin
// 사용하지 마세요: derivedStateOf의 잘못된 사용법
var firstName by remember { mutableStateOf("") }
var lastName by remember { mutableStateOf("") }

// 이것은 비효율적입니다!
val fullNameBad by remember { derivedStateOf { "$firstName $lastName" } } 
// 이것이 더 효율적입니다.
val fullNameCorrect = "$firstName $lastName" 
```

이 예시에서, `fullName`은 `firstName`과 `lastName`이 변경될 때마다 업데이트되며, 이는 예상된 동작입니다. 이 경우 과도한 리컴포지션이 발생하지 않으므로, 계산을 `derivedStateOf`로 감싸는 것은 불필요한 복잡성과 오버헤드만 추가합니다. `derivedStateOf`는 의존성이 업데이트될 때가 아니라, **파생된 값의 결과가 변경될 때만** 재계산함으로써 명확한 성능 이점이 있을 때만 사용하세요.

-----

## 요약

`derivedStateOf`는 반응형이고, 최적화되었으며, 파생된 상태를 만들기 위한 부작용 핸들러 API 중 하나입니다. 이는 리컴포지션이 필요할 때만 발생하도록 보장하여 UI를 더 효율적으로 만들고 코드를 더 선언적으로 만듭니다. `derivedStateOf`를 사용하면 Compose 애플리케이션에서 성능과 명확성을 유지하면서 복잡한 상태 관계를 효과적으로 관리할 수 있습니다.

-----

## Q. 어떤 종류의 상황에서 `derivedStateOf`를 사용하겠습니까? 또한, 다른 상태 변수로부터 값을 계산하는 경우라도 언제 `derivedStateOf` 사용을 피해야 하나요?

`derivedStateOf`는 **자주 변경되는 하나 이상의 상태(State)로부터 파생된 계산 결과가 그보다 훨씬 덜 자주 변경될 때** 사용하여 불필요한 리컴포지션을 방지하는 데 사용합니다.

반대로, 파생된 값의 변경 빈도가 원본 상태의 변경 빈도와 동일하다면, `derivedStateOf`는 불필요한 오버헤드만 추가하므로 사용을 피해야 합니다.

-----

### 1. `derivedStateOf`를 사용해야 하는 상황 ✅

핵심은 **원본 상태는 자주 변경되지만, 그로부터 파생된 최종 결과는 드물게 변경되는 경우**입니다. `derivedStateOf`는 이 최종 결과가 실제로 변경되었을 때만 리컴포지션을 트리거하여 성능을 최적화합니다.

#### 1.1. 목록 스크롤에 따른 UI 상태 변경

  * **시나리오:** `LazyColumn`을 스크롤할 때, 특정 위치(예: 첫 번째 아이템)를 지나야만 "맨 위로 가기" 버튼이 나타나도록 하고 싶을 때.
  * **분석:**
      * **원본 상태:** 스크롤 위치(`listState.firstVisibleItemIndex`)는 사용자가 스크롤하는 동안 매 순간 매우 빈번하게 변경됩니다.
      * **파생 상태:** 버튼의 표시 여부(`listState.firstVisibleItemIndex > 0`)는 `true` 또는 `false`라는 두 가지 값만 가집니다. 이 값은 사용자가 첫 아이템을 지나 스크롤을 시작하는 순간(`false` → `true`)과 다시 맨 위로 돌아오는 순간(`true` → `false`)에만 변경됩니다.
  * **사용 이유:** `derivedStateOf`를 사용하면, 스크롤 위치가 계속 바뀌더라도 버튼의 표시 여부(`true`/`false`)가 바뀔 때만 리컴포지션이 발생하여 수많은 불필요한 UI 업데이트를 막을 수 있습니다.
    ```kotlin
    val listState = rememberLazyListState()
    val showButton by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    if (showButton) {
        ScrollToTopButton()
    }
    ```

#### 1.2. 사용자 입력에 따른 유효성(Validation) 검사

  * **시나리오:** 비밀번호 입력 필드에 8자 이상을 입력해야만 "사용 가능" 메시지가 표시되도록 할 때.
  * **분석:**
      * **원본 상태:** `TextField`의 텍스트 값은 사용자가 키를 누를 때마다 변경됩니다.
      * **파생 상태:** 입력이 유효한지 여부(`text.length >= 8`)는 `true` 또는 `false` 값만 가집니다. 이 값은 텍스트 길이가 7자에서 8자가 될 때, 그리고 8자에서 7자가 될 때만 변경됩니다.
  * **사용 이유:** 매 키 입력마다 리컴포지션이 일어나는 것을 방지하고, 유효성 상태가 실제로 변경될 때만 관련 UI를 업데이트하여 효율성을 높입니다.

#### 1.3. 복잡한 필터링 또는 그룹화 결과

  * **시나리오:** 대규모의 작업 목록(`allTasks`)이 있고, 사용자가 선택한 필터(`"완료"`, `"미완료"`)에 따라 필터링된 목록을 보여줄 때.
  * **분석:**
      * **원본 상태:** `allTasks` 목록이나 `filter` 상태가 변경될 수 있습니다.
      * **파생 상태:** 필터링된 결과 목록(`filteredTasks`).
  * **사용 이유:** 만약 `allTasks` 목록에 새로운 아이템이 추가되었지만, 그 아이템이 현재 필터 조건과 맞지 않는다면, 필터링된 결과 목록 자체는 변경되지 않습니다. 이때 `derivedStateOf`는 결과가 동일함을 인지하고 UI의 리컴포지션을 건너뜁니다.

-----

### 2. `derivedStateOf` 사용을 피해야 하는 상황 ❌

다른 상태 변수로부터 값을 계산하는 경우라도, **`derivedStateOf`를 사용하는 것이 오히려 비효율적인** 경우가 있습니다.

#### 2.1. 단순한 상태 조합 또는 변환

  * **시나리오:** `firstName` 상태와 `lastName` 상태를 조합하여 `fullName`을 만드는 경우.
    ```kotlin
    var firstName by remember { mutableStateOf("홍") }
    var lastName by remember { mutableStateOf("길동") }

    // 👎 불필요한 사용법
    val fullNameBad by remember { derivedStateOf { "$firstName $lastName" } }

    // 👍 올바르고 효율적인 사용법
    val fullNameCorrect = "$firstName $lastName"
    ```
  * **피해야 하는 이유:**
    `fullName`의 값은 `firstName` 또는 `lastName`이 변경될 때마다 **항상 함께 변경**됩니다. 파생된 값의 변경 빈도가 원본 상태의 변경 빈도와 동일하므로, 리컴포지션을 건너뛸 기회가 전혀 없습니다. 이 경우 `derivedStateOf`는 내부적으로 이전 값과 새 값을 비교하는 추가적인 오버헤드만 발생시킬 뿐 아무런 이점을 제공하지 않습니다.

#### 2.2. 계산 비용이 매우 저렴한 경우

  * 파생 값을 계산하는 비용이 매우 저렴하다면(예: 간단한 `Boolean` 플래그 계산), `derivedStateOf`를 사용하여 얻는 리컴포지션 방지 이점보다 `derivedStateOf` 자체를 유지하고 비교하는 오버헤드가 더 클 수 있습니다.

-----

### 3. 결론: 핵심 판단 기준

`derivedStateOf`의 사용 여부를 결정하는 핵심 질문은 다음과 같습니다.

> **"계산 결과가 원본이 되는 상태들보다 훨씬 덜 자주 변경되는가?"**

  * **예 (YES):** 원본 상태는 빈번하게 바뀌지만 최종 결과는 가끔 바뀐다면, `derivedStateOf`는 불필요한 리컴포지션을 막아주는 훌륭한 최적화 도구입니다.
  * **아니요 (NO):** 원본 상태가 바뀔 때마다 최종 결과도 거의 항상 바뀐다면, `derivedStateOf`를 사용하지 말고 일반 `val` 변수로 직접 계산하는 것이 더 효율적입니다.