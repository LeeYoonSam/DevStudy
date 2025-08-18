# Kotlin 컴파일러 버전 (Kotlin Compiler Version)

## 버전 호환성

Compose Compiler는 **특정 Kotlin 버전과의 엄격한 호환성**이 요구됩니다. 이는 개발 시 가장 먼저 확인해야 할 중요한 요소입니다.

## 버전 검사 우회

### suppressKotlinVersionCompatibilityCheck

버전 검사를 우회할 수 있는 컴파일러 매개변수가 제공됩니다:

```kotlin
suppressKotlinVersionCompatibilityCheck
```

### 주의사항

버전 검사 우회 시 다음 위험이 있습니다:

- **컴파일 오류 발생 가능성 증가**
- **Kotlin 컴파일러 백엔드 호환성 문제**
- **개발자가 모든 위험성 부담**

> 💡 이 매개변수는 실험적인 Kotlin 릴리스에 대한 Compose 테스트 목적으로 추가된 것으로 추정됩니다.

## 요약

- Compose Compiler는 특정 Kotlin 버전과의 엄격한 호환성이 필요하며, 이는 안정적인 개발을 위한 핵심 요구사항입니다. 버전 검사 우회는 가능하지만 실험적 목적 외에는 권장되지 않으며, 개발자가 모든 위험을 감수해야 합니다.