# Jetpack Compose

안드로이드 팀이 [**Jetpack Compose 1.0 안정 버전**](https://android-developers.googleblog.com/2021/07/jetpack-compose-announcement.html)을 발표한 이후, 운영 환경(production)에서의 채택이 빠르게 가속화되었습니다. 2023년, Google은 Jetpack Compose로 빌드된 125,000개 이상의 앱이 구글 플레이 스토어에 성공적으로 게시되었다고 보고했습니다. 오늘날 Jetpack Compose는 널리 사용되는 UI 툴킷이 되었으며, 개발자가 더 높은 생산성과 효율성으로 레이아웃을 **선언적으로(declaratively)** 생성할 수 있게 해줍니다.

일부 회사는 계속해서 XML 기반 레이아웃에 의존하고 있지만, XML에서 Compose로 전환하는 것은 하룻밤 만에 이루어지는 작업이 아닙니다. 결과적으로, 많은 개발자들은 여전히 두 접근 방식에 모두 익숙해야 할 필요가 있습니다. 그러나 최근 몇 년간 생태계가 상당히 성숙해지고, 일반적인 과제들에 대한 강력한 커뮤니티 지원과 해결책이 제공되면서 대부분의 새로운 프로젝트는 Jetpack Compose를 채택하는 경향이 있습니다.

Jetpack Compose는 코드 재사용성 향상 및 Lifecycle, Navigation, Hilt와 같은 기존 안드로이드 프레임워크와의 원활한 호환성과 같은 주요 이점을 제공합니다. 또한 코루틴 및 다양한 커뮤니티 주도 확장 기능에 대한 향상된 지원을 포함합니다. 이러한 이점에도 불구하고, 개발자는 성능을 최적화하고 비효율성을 방지하기 위해 **리컴포지션(recomposition)** 과 같은 Jetpack Compose의 내부 메커니즘에 대한 깊은 이해를 얻어야 합니다.

이 책은 세 가지 주요 부분인 **Compose 기본(Compose Fundamentals)**, **Compose 런타임(Compose Runtime)**, **Compose UI**로 구성되어 있으며, 각각 Jetpack Compose의 내부 동작 방식(under the hood)의 다른 측면에 초점을 맞춥니다. 어떤 순서로든 이 섹션들을 탐색할 수 있지만, 체계적인 학습 경험을 위해 순차적으로 읽도록 내용이 설계되었습니다.

이 책은 가능한 모든 Jetpack Compose 관련 인터뷰 질문을 다루기보다는, **Compose에 대한 견고한 기초 및 심화 이해를 구축하는 것**을 목표로 합니다. **"숙련자를 위한 프로 팁"** 섹션에서는 핵심 Compose API의 내부 구현을 깊이 파고들어, 여러분이 더 깊은 전문성을 개발하는 데 도움을 줄 것입니다. 일부 개념이 처음에는 복잡해 보이더라도 위축되지 마세요. 명확해질 때까지 다시 방문하여 살펴보세요.

이 질문들은 Jetpack Compose에 대한 여러분의 이해를 향상시키고 면접을 자신감 있게 준비하는 데 도움이 되도록 설계되었습니다.

---

- [Compose Fundamentals](./Fundamentals/README.md)