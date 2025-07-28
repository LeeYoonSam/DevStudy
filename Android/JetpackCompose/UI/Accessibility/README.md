# Jetpack Compose에서 접근성을 보장하는 방법

[Jetpack Compose에서 접근성](https://developer.android.com/develop/ui/compose/accessibility)을 보장하는 것은 스크린 리더와 같은 **보조 기술**이 쉽게 해석하고 상호작용할 수 있는 UI 컴포넌트를 설계하는 것을 포함합니다. Compose는 선언적 UI 모델을 유지하면서 접근성 기능을 더 쉽게 구현할 수 있도록 유연한 API 세트를 제공합니다.

-----

## `semantics` Modifier

Compose 접근성 시스템의 핵심에는 **`semantics` Modifier**가 있습니다. 이는 UI 요소가 접근성 서비스에 의해 어떻게 해석되어야 하는지를 기술할 수 있게 해줍니다.

```kotlin
Modifier.semantics {
    contentDescription = "Send Button"
}
```

이 Modifier는 콘텐츠 설명, 역할, 또는 사용자 정의 작업과 같은 메타데이터를 보조 도구에 전달합니다. 그러나 `Text`, `Button`, `Icon`과 같은 대부분의 컴포저블은 이미 내장된 시맨틱을 가지고 있으므로, 수동 사용은 종종 사용자 정의 컴포넌트에만 필요합니다.

-----

## 이미지와 아이콘을 위한 `contentDescription`

`contentDescription` 파라미터는 `Image` 및 `Icon` 컴포넌트의 기본 접근성 파라미터입니다. 이는 시각적 콘텐츠에 대한 텍스트 컨텍스트를 제공합니다.

```kotlin
Icon(
    imageVector = Icons.Default.Send,
    contentDescription = "전송"
)
```

만약 이미지가 장식용이라면, `null`을 전달하여 접근성 서비스에서 제외시킬 수 있습니다.

```kotlin
Image(painter = painterResource(id = R.drawable.divider), contentDescription = null)
```

-----

## 그룹화된 콘텐츠를 위한 시맨틱 병합

여러 요소가 **하나의 논리적 단위**로 제시되어야 할 때, 접근성 목적으로 이들을 그룹화하기 위해 `Modifier.clearAndSetSemantics {}` 또는 `Modifier.semantics(mergeDescendants = true)`를 사용합니다.

```kotlin
Column(
    modifier = Modifier.semantics(mergeDescendants = true) {}
) {
    Text("항공편: NZ123")
    Text("출발: 오전 10:30")
}
```

이는 보조 기술이 콘텐츠를 단일 항목으로 읽도록 보장합니다.

-----

## 사용자 정의 접근성 작업 (Custom Accessibility Actions)

스크린 리더나 다른 도구를 사용하는 사용자를 위해 상호작용을 향상시키기 위해 사용자 정의 작업을 추가할 수 있습니다.

```kotlin
Modifier.semantics {
    onClick("북마크하려면 두 번 탭하세요") {
        // 클릭 처리
        true
    }
}
```

이는 보조 기술 사용자에게 더 설명적이고 인터랙티브한 UI 경험을 제공합니다.

-----

## 접근성 테스트

Compose UI 테스트와 함께 [**`Accessibility Scanner`**](https://support.google.com/accessibility/android/answer/6376570?hl=en) 또는 **`AccessibilityTestRule`** 을 사용하여 접근성 레이블, 역할, 계층 구조를 검증할 수 있습니다. Compose는 또한 테스트에서 시맨틱 단언(assertion)을 허용하여 적절한 접근성 동작을 보장합니다.

```kotlin
composeTestRule.onNodeWithContentDescription("전송").assertExists()
```

`onNodeWithContentDescription`을 사용하여, 테스트는 접근성 레이블이 올바르게 할당되었는지 확인합니다.

-----

## 요약

Jetpack Compose는 `semantics`, `contentDescription`, `mergeDescendants`와 같은 구조화된 API를 제공하여 개발자가 접근성 있는 UI를 구현하는 데 도움을 줍니다. 시각적 요소를 적절히 어노테이션하고 관련 콘텐츠를 그룹화함으로써, 보조 기술에 의존하는 사용자를 포함한 더 넓은 범위의 사용자가 애플리케이션을 사용할 수 있도록 보장할 수 있습니다.

-----

## Q. `semantics` Modifier의 목적은 무엇인가요?

`semantics` Modifier의 주 목적은 컴포저블에 **'의미(semantics)'를 부여**하여, 스크린 리더와 같은 **보조 기술**이나 **테스트 프레임워크**가 해당 UI 요소의 역할과 상태를 이해할 수 있도록 만드는 것입니다. 즉, 눈에 보이는 UI를 기계가 이해할 수 있는 정보(콘텐츠 설명, 역할, 상태 등)로 변환하는 역할을 합니다.

-----

### 1. `semantics` Modifier의 핵심 역할: UI에 의미 부여하기 💬

Jetpack Compose는 UI를 화면에 그리지만, 코드 자체만으로는 시스템의 보조 기술(예: TalkBack)이나 테스트 도구가 "이 네모난 파란색 영역이 '전송' 기능을 하는 버튼이다"라고 이해할 수 없습니다.

**`Modifier.semantics`** 는 바로 이 지점에서 사용됩니다. 이 Modifier는 컴포저블에 다음과 같은 **의미론적 정보(메타데이터)** 를 첨부하여, UI 트리와는 별개인 **시맨틱 트리(Semantics Tree)** 를 구성합니다.

  * **역할(Role):** 이 요소가 버튼인지, 이미지인지, 체크박스인지 등을 정의합니다.
  * **상태(State):** 체크박스가 선택되었는지, 스위치가 켜져 있는지 등의 현재 상태를 설명합니다.
  * **설명(Description):** 텍스트가 없는 요소(예: 아이콘 버튼)에 대해 "뒤로 가기 버튼"과 같은 설명을 제공합니다.
  * **작업(Action):** "활성화하려면 두 번 탭하세요"와 같이 사용자가 수행할 수 있는 작업에 대한 정보를 제공합니다.

이렇게 구성된 시맨틱 트리는 두 가지 주요 시스템에서 사용됩니다.

1.  **접근성 서비스 (Accessibility Services):** TalkBack과 같은 스크린 리더는 이 시맨틱 트리를 읽어 사용자에게 UI를 음성으로 설명해 줍니다.
2.  **테스트 프레임워크 (Testing Framework):** Compose UI 테스트(`onNodeWithText` 등)는 이 시맨틱 트리를 쿼리하여 특정 노드를 찾고, 그 상태를 검증합니다.

-----

### 2. 주요 사용 사례 및 속성

`semantics { ... }` 블록 내에서는 다양한 시맨틱 속성을 정의할 수 있습니다.

  * **`contentDescription`**: 요소의 목적을 설명하는 텍스트입니다. 특히 `Icon`이나 `Image`처럼 텍스트 콘텐츠가 없는 요소의 접근성을 위해 필수적입니다.
    ```kotlin
    Icon(
        imageVector = Icons.Default.Share,
        contentDescription = "공유하기", // TalkBack이 "공유하기 버튼" 등으로 읽음
        modifier = Modifier.semantics {
            // contentDescription은 Icon의 파라미터로 직접 전달하는 것이 일반적임
        }
    )
    ```
  * **`onClick`**: 클릭 작업에 대한 설명(레이블)을 제공하여, 보조 기술 사용자에게 어떤 동작이 일어날지 알려줍니다.
    ```kotlin
    
    Modifier.semantics {
        onClick(label = "프로필 수정", action = {
            // 실제 클릭 시 수행될 작업
            true
        })
    }
    ```
  * **`role`**: 요소의 역할을 명시적으로 지정합니다. (`Role.Button`, `Role.Image`, `Role.Checkbox` 등)
    ```kotlin
    Modifier.semantics { role = Role.Button }
    ```
  * **`testTag`**: 오직 테스트 코드에서만 이 컴포저블을 찾기 위한 고유한 식별자를 부여합니다.

-----

### 3. 왜 대부분의 경우 직접 사용하지 않는가? 🤔

`semantics` Modifier는 접근성의 기초이지만, 실제 개발에서 이를 직접 사용하는 경우는 상대적으로 드뭅니다. 그 이유는 **`Button`, `Text`, `Icon`, `Checkbox`와 같은 대부분의 고수준 Material 컴포저블들이 이미 내부에 적절한 `semantics`를 내장**하고 있기 때문입니다.

  * **`Button`:** `onClick` 파라미터가 있으면 자동으로 `Role.Button`과 클릭 작업 시맨틱이 추가됩니다.
  * **`Icon`, `Image`:** `contentDescription` 파라미터를 통해 시맨틱 설명을 쉽게 추가할 수 있습니다.
  * **`Checkbox`:** `checked` 및 `onCheckedChange` 파라미터를 통해 선택 상태와 역할 시맨틱이 자동으로 설정됩니다.

따라서 개발자는 **`Canvas`나 `Box`와 같은 저수준(low-level) API를 사용하여 완전히 새로운 사용자 정의 컴포넌트를 만들 때**, 또는 기존 컴포넌트의 기본 시맨틱 동작을 재정의(override)해야 할 때만 `Modifier.semantics`를 직접 사용하게 됩니다.

-----

### 4. 결론

`Modifier.semantics`는 Jetpack Compose에서 **접근성과 테스트 자동화의 근간을 이루는 매우 중요한 도구**입니다. 이는 시각적인 UI 요소에 기계가 이해할 수 있는 '의미'를 부여하는 역할을 합니다. 비록 대부분의 경우 고수준 컴포넌트들을 통해 암시적으로 사용되지만, 그 원리를 이해하는 것은 접근성이 뛰어난 사용자 정의 컴포넌트를 만들고 견고한 UI 테스트를 작성하는 데 필수적입니다.


## Q. Compose에서 UI 요소 그룹이 단일 접근성 노드처럼 동작하게 하려면 어떻게 해야 하나요?

UI 요소 그룹을 단일 접근성 노드처럼 동작하게 하려면, 해당 요소들을 감싸는 부모 컴포저블(예: `Row`, `Column`, `Box`)의 **`Modifier`** 에 **`semantics(mergeDescendants = true) { }`** 를 적용합니다. 이렇게 하면 Compose는 하위 자식들의 시맨틱 정보(예: `contentDescription`, `text`)를 하나로 병합하여 스크린 리더가 전체 그룹을 하나의 의미 있는 단위로 읽도록 만듭니다.

-----

### 1. 문제점: 분리된 접근성 포커스 😵

기본적으로 스크린 리더(예: TalkBack)는 화면의 각 컴포저블 요소를 개별적으로 인식하고 포커스를 이동시킵니다. 예를 들어, 아이콘과 텍스트로 구성된 리스트 아이템이 있다면 다음과 같이 동작합니다.

```kotlin
// 기본 동작
Row(modifier = Modifier.clickable { }) {
    Icon(imageVector = Icons.Default.Person, contentDescription = "사용자 프로필")
    Column {
        Text("skydoves")
        Text("온라인")
    }
}
```

스크린 리더 사용자가 이 영역을 탐색하면 포커스가 다음과 같이 분리되어 이동합니다.

1.  "사용자 프로필, 이미지" (포커스 1)
2.  "skydoves, 텍스트" (포커스 2)
3.  "온라인, 텍스트" (포커스 3)

이는 시각적으로는 하나의 단위처럼 보이는 정보가 음성으로는 분리되어 전달되므로 비효율적이고 혼란스러운 사용자 경험을 유발할 수 있습니다.

-----

### 2. 해결책: `semantics` Modifier와 `mergeDescendants` 속성 ✅

이러한 여러 개의 UI 요소를 접근성 관점에서 하나의 논리적 단위로 묶으려면 `semantics` Modifier의 `mergeDescendants` 속성을 `true`로 설정합니다.

  * **`Modifier.semantics(mergeDescendants = true) { }`**
      * **역할:** 이 Modifier가 적용된 컴포저블의 모든 자손(descendants)들의 시맨틱 정보를 수집하여 **하나의 노드로 병합**합니다.
      * **동작:** 자식들의 `text`나 `contentDescription` 값들이 순서대로 합쳐져 부모의 시맨틱 설명이 됩니다. 병합된 후, 기존 자식들의 개별 시맨틱 노드는 접근성 서비스에 더 이상 노출되지 않습니다.

-----

### 3. 구현 예시

위의 예시 코드에 `semantics` Modifier를 적용하여 개선해 보겠습니다.

```kotlin
Row(
    modifier = Modifier
        .clickable { }
        .semantics(mergeDescendants = true) { } // 자식들의 시맨틱 정보를 병합
) {
    Icon(imageVector = Icons.Default.Person, contentDescription = "사용자 프로필")
    Column {
        Text("skydoves")
        Text("온라인")
    }
}
```

이제 스크린 리더 사용자가 이 영역을 탐색하면, 전체 `Row`가 하나의 포커스 대상으로 잡히며 다음과 같이 **한 번에 읽어줍니다.**

> "사용자 프로필, skydoves, 온라인"

이를 통해 사용자는 분산된 정보가 아닌, 하나의 완전한 컨텍스트를 가진 정보를 얻게 되어 훨씬 더 나은 사용자 경험을 하게 됩니다.

-----

### 4. (참고) `clearAndSetSemantics`와의 관계

`Modifier.clearAndSetSemantics { ... }`는 더 강력한 API로, 모든 자식들의 시맨틱을 **완전히 제거**하고 개발자가 람다 블록 내에서 **새롭게 정의한 시맨틱 정보로 대체**합니다.

  * **`mergeDescendants = true`**: 자식들의 기존 시맨틱 정보를 **활용하여 병합**할 때 사용합니다. (가장 일반적인 경우)
  * **`clearAndSetSemantics`**: 자식들의 시맨틱 정보는 무시하고, 그룹 전체에 대해 **완전히 새로운 설명을 제공**하고 싶을 때 사용합니다. (예: 여러 `Box`로 만든 복잡한 커스텀 프로그레스 바에 대해 "진행률: 75%" 라는 단일 설명만 제공하고 싶을 때)

-----

### 5. 결론

Jetpack Compose에서 여러 UI 요소를 하나의 논리적 그룹으로 묶어 접근성을 향상시키고 싶을 때는, 해당 요소들을 감싸는 부모 레이아웃에 **`Modifier.semantics(mergeDescendants = true) { }`** 를 적용하는 것이 표준적이고 효과적인 방법입니다. 이는 스크린 리더 사용자에게 더 간결하고 의미 있는 정보를 전달하여 앱의 전반적인 사용성을 높여줍니다.