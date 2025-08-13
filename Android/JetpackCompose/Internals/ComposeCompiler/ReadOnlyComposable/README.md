# @ReadOnlyComposable

## 개요
**@ReadOnlyComposable** 어노테이션은 Composable 함수가 composition에 쓰기 작업을 하지 않고 오직 읽기만 수행한다는 것을 명시합니다. 이 어노테이션은 함수 본문 내의 모든 중첩된 Composable 호출에도 적용되며, Compose Runtime이 불필요한 코드 생성을 방지하는데 도움을 줍니다.

## Composition 그룹과 동작 방식

### Composition 그룹의 특징
- 컴파일러는 Composable 함수 본문을 "그룹"으로 감싸서 Runtime에 방출
- 그룹은 Composable 함수에 대한 필수 정보를 composition에 제공
- 각 그룹은 소스 코드의 위치를 나타내는 고유한 키(key)를 보유

### 그룹의 활용
- Recomposition 시 데이터 재정의 및 정리 방법 결정
- Composable 함수의 고유성 유지
- 재시작 가능한 그룹(restartable groups)과 이동 가능한 그룹(movable groups) 지원

## 조건부 로직에서의 그룹 예시

```kotlin
if (condition) {
    Text("Hello")
} else {
    Text("World")
}
```

위 예시에서:
- 동일한 `Text` 함수지만 서로 다른 논리를 표현
- 각각 고유한 키를 가진 이동 가능한 그룹으로 처리
- 부모 그룹 내에서 재정렬 가능

## @ReadOnlyComposable 사용 사례

### Compose 라이브러리 내 주요 사용 예시
- **CompositionLocal 관련**:
  - 필드의 기본값
  - 위임 유틸리티
- **Material 라이브러리**:
  - Colors
  - Typography
  - isSystemInDarkTheme() 함수
- **Context 및 리소스 관련**:
  - LocalContext
  - 애플리케이션 리소스 획득 호출
  - LocalConfiguration

## 특징
- 프로그램 실행 시 대부분 한 번만 값 설정
- Composable 트리에서 일관된 값 유지
- Composition에 쓰기 작업이 없어 데이터 교체나 이동이 불필요

## 요약
@ReadOnlyComposable은 Compose에서 읽기 전용 작업만을 수행하는 Composable 함수를 최적화하기 위한 어노테이션입니다. 주로 CompositionLocal 값 접근, 테마 속성, 시스템 설정 등 한 번 설정되고 변경이 적은 값들을 다룰 때 사용됩니다. 이를 통해 불필요한 코드 생성을 방지하고 성능을 최적화할 수 있습니다.