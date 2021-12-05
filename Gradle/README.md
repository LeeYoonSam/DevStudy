# Gradle
- [Kotlin DSL 이란?](#kotlin-dsl-이란)

---

## Kotlin DSL 이란?
> DSL이란 Domain Specific Language의 약어로 특정 분야에 최적화된 프로그래밍 언어를 뜻한다. 상용구 코드를 최소화 하기 위해 명령형 코드 대신 선언적 코드 형식을 따른다.
Kotlin DSL은 코틀린의 언어적인 특징으로 가독성이 좋고 간략한 코드를 사용하여 Gradle 스크립팅을 하는 것을 목적으로 하는 DSL이다.

- 빌드 스크립트에서 사용하는 객체, 함수, 속성들은 Gradle API와 적용한 plugin API에서 가져온다.

### Kotlin DSL을 사용하는 이유는?

**장점**
- 컴파일 타임에 에러 확인
- 코드 탐색
- 자동 완성
- 구문 강조
- IDE의 지원으로 향상된 편집환경
- 소스코드와 동일한 언어의 사용

**단점**
- 빌드 캐시가 Invalidation 되거나 클린 빌드시에 Groovy DSL보다 느리다.
- Java8이상에서 동작
- 새로운 라이브러리 버전 Inspection 기능 미지원