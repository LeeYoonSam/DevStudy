# 결론 (Conclusion)

이 장에서는 핵심 **Compose** 개념을 활용하여 **Compose UI** 외 시스템을 구축하는 방법을 살펴봤습니다. **사용자 정의 composition**은 실제로 쉽게 만나볼 수 없지만, 이미 **Kotlin/Compose** 환경에서 작업하고 있다면 갖춰둘 가치가 있는 훌륭한 도구입니다.

## 벡터 그래픽 Composition

**벡터 그래픽 composition**은 사용자 정의 **Composable** 트리를 **Compose UI**에 통합하는 좋은 예입니다. 동일한 원칙을 사용하여 **UI composition**의 상태, 애니메이션, **composition local**과 쉽게 상호작용을 할 수 있는 다른 사용자 정의 요소를 만들 수 있습니다.

## 독립적인 Composition

모든 **Kotlin** 플랫폼에서 **독립적인(standalone) composition**을 만들 수도 있습니다. 우리는 브라우저에서 **Kotlin/JS**를 통해 **Compose Runtime**을 기반으로 한 **DOM** 관리 라이브러리의 토이 버전을 만듦으로써 이를 탐구했습니다. 비슷한 방식으로, **Compose Runtime**은 이미 Android 분야가 아닌 프로젝트에서 UI 트리를 조작하는 데 사용되고 있습니다(예: **Mosaic**, Jake Wharton의 CLI에 대한 접근).

## 실험과 피드백

**Compose**로 자신만의 아이디어를 실험하고, `#compose` **Kotlin Slack** 채널에서 **Compose** 팀에 피드백을 제공할 것을 권합니다! 그들의 주요 목표는 여전히 **Compose UI**로 정의되어 있지만, 다른 분야에서의 **Compose** 활용에 대해 알아가는 것에 흥미를 느끼고 있습니다.

## 요약

- **Compose Runtime**을 활용하면 **Compose UI** 외부에서도 다양한 시스템을 구축할 수 있습니다
- **사용자 정의 composition**은 **Kotlin/Compose** 환경에서 강력한 도구가 될 수 있습니다
- **벡터 그래픽 composition**은 **Compose UI**와 통합하는 실용적인 예시입니다
- **독립적인 composition**을 통해 모든 **Kotlin** 플랫폼에서 **Compose Runtime**을 활용할 수 있습니다
- **Compose Runtime**은 Android 외부 프로젝트(예: **Mosaic**, CLI 도구)에서도 활용되고 있습니다
- **Compose** 팀은 다양한 분야에서의 **Compose** 활용에 관심이 있으며, 실험과 피드백을 환영합니다