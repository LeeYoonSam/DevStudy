# @DisallowComposableCalls

`@DisallowComposableCalls`는 함수 내에서 **Composable 함수의 호출이 발생하는 것을 방지**하기 위해 사용됩니다. 이 어노테이션은 Composable 함수를 안전하게 호출할 수 없는 Composable 함수의 **인라인 람다 매개변수**에서 유용하게 사용될 수 있습니다. 주로 **recomposition 마다 호출되면 안 되는 람다식**에 가장 적합하게 사용됩니다.

## 사용 예시: remember 함수

이 어노테이션에 대한 예시는 Compose Runtime의 일부인 `remember` 함수에서 찾아볼 수 있습니다. `remember` 함수는 `calculation` 블록에 의해 제공된 값을 기억합니다. 이 `calculation` 블록은 **최초의 composition 단계에서만 수행**되며, 이후의 모든 recomposition 단계에서는 항상 이미 계산된 값을 반환합니다.

```kotlin
@Composable
inline fun <T> remember(calculation: @DisallowComposableCalls () -> T): T = 
    currentComposer.cache(false, calculation)
```

## 동작 원리

`@DisallowComposableCalls` 어노테이션 덕분에 `calculation` 람다식에서 Composable 함수 호출이 금지됩니다. 만약 Composable 함수 호출이 허용된다면:
- Composable 함수의 노드 방출 시 슬롯 테이블에서 공간을 차지
- 람다가 더 이상 호출되지 않으므로 첫 composition 단계 후에 삭제됨

## 적용 범위

`@DisallowComposableCalls`는 **조건부로 호출되는 인라인 람다**에서 가장 적합하게 사용됩니다. 인라인 람다는:
- 구현 세부사항으로서 작동
- Composable 함수처럼 "활성화"되어 있지 않아야 함

### 특별한 점

인라인 람다는 상위 호출 컨텍스트의 Composable 기능을 "상속"한다는 점에서 특별합니다:
- 예: `forEach`의 람다는 `@Composable`로 마킹되어있지 않지만, `forEach` 자체가 Composable 함수 내에서 호출되면 Composable 함수를 호출 가능
- 이는 `forEach`와 같은 API에서는 예상되는 동작이지만, `remember`와 같은 API에서는 바람직하지 않음

## 전파성

`@DisallowComposableCalls`로 마킹된 인라인 람다 내부에서 또 다른 인라인 람다를 호출하는 경우:
- 컴파일러는 해당 람다도 `@DisallowComposableCalls`로 표시해야 함
- 일반적인 클라이언트 프로젝트에서는 자주 사용되지 않음
- Compose UI가 아닌 다른 사용 사례에 Jetpack Compose를 사용하는 경우 관련성이 높아질 수 있음
  - Compose Runtime에 대한 자체 클라이언트 라이브러리 작성 시 필요
  - Compose Runtime 제약조건 준수 필요

## 요약

`@DisallowComposableCalls`는 Jetpack Compose의 중요한 어노테이션으로, 주로 다음과 같은 상황에서 사용됩니다:
1. Composable 함수 내의 특정 람다에서 다른 Composable 함수 호출을 제한
2. `remember`와 같이 최초 composition에서만 실행되어야 하는 함수의 안전성 보장
3. 불필요한 recomposition 방지 및 메모리 누수 예방

이 어노테이션은 Compose의 성능 최적화와 안정성을 위한 핵심 도구이며, 특히 커스텀 Compose 라이브러리 개발 시 중요하게 고려해야 할 요소입니다.