# Compose 기본 (Compose Fundamentals)

Jetpack Compose는 세 가지 주요 구성 요소인 **Compose 컴파일러(Compose Compiler)**, **Compose 런타임(Compose Runtime)**, **Compose UI**를 기반으로 구축됩니다. `remember`, `LaunchedEffect`, `Box`, `Column`, `Row`와 같이 UI 화면을 빌드하는 데 사용되는 대부분의 API는 런타임 및 UI 계층에서 제공됩니다. 그러나 내부적으로 Jetpack Compose는 더 복잡하게 구조화되어 있으며, 단순히 라이브러리 의존성을 추가하는 것만으로 프로젝트에 원활하게 통합됩니다.

Compose 내부 동작에 대한 깊은 이해가 애플리케이션을 빌드하는 데 필수는 아니지만, 전체 아키텍처를 파악하고 있는 것은 Compose의 다양한 역할, 특히 **Compose의 렌더링 단계** 및 **선언적 UI 개발의 본질**과 같은 개념을 이해하는 데 상당히 도움이 될 수 있습니다.

이 섹션에서는 Jetpack Compose의 더 고급 측면 중 일부를 깊이 파고들며, 이는 처음에는 파악하기 어려울 수 있습니다. 만약 개념이 복잡하게 느껴진다면, 더 접근하기 쉬운 진입점을 제공하는 **카테고리 1: Compose 런타임** 또는 **카테고리 2: Compose UI**부터 시작한 후, 이러한 더 깊은 주제들을 탐색하는 것을 고려해 보세요.

---

- [Jetpack Compose의 구조는 무엇인가?](./Structure/README.md)
- [Compose 단계(Phase)란 무엇인가?](./ComposePhase/README.md)
- [Jetpack Compose가 선언형 UI 프레임워크인 이유](./DeclarativeUI/README.md)
- [리컴포지션(Recomposition)이란 무엇이며 언제 발생하나요? 또한, 앱 성능과는 어떻게 관련되나요?](./Recomposition/README.md)
- [컴포저블 함수는 내부적으로 어떻게 작동하나요?](./Composable/README.md)
- [Jetpack Compose의 안정성(Stability)과 성능의 관계](./Stability/README.md)
- [안정성 향상을 통해 Compose 성능을 최적화한 경험이 있나요?](./OptimizingPerformance/README.md)