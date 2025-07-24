# Compose UI

Compose UI는 `Box`, `Column`, `Row`, `MaterialTheme`과 같이 UI 관련 API를 포함합니다. 이는 레이아웃, 그리기, 입력을 포함하여 기기와 상호작용하는 데 필요한 UI 라이브러리 모음을 제공하며, [`Compose Material`](https://developer.android.com/jetpack/androidx/releases/compose-material), `Compose Foundation`, [`Compose UI`](https://developer.android.com/jetpack/androidx/releases/compose-ui)와 같은 여러 라이브러리를 포함합니다.

Jetpack Compose UI는 주로 네이티브 안드로이드를 위해 설계되었으며 Compose 런타임 위에서 작동합니다. 플랫폼에 독립적인 Compose 컴파일러 및 Compose 런타임과 달리, Compose UI는 이러한 라이브러리들의 클라이언트 역할을 합니다. 크로스플랫폼 개발을 위해, JetBrains는 [코틀린 멀티플랫폼](https://kotlinlang.org/docs/multiplatform.html)을 사용하여 안드로이드, iOS, WebAssembly (Wasm), 데스크톱을 지원하도록 Compose UI를 확장하는 [**Compose Multiplatform**](https://www.jetbrains.com/compose-multiplatform/)을 제공합니다. Compose 채택이 계속 증가함에 따라 Compose Multiplatform 생태계도 확장되고 있지만, 이 책은 특히 네이티브 안드로이드용 Compose UI에 초점을 맞춥니다.

`Modifier`와 같은 핵심 API들은 성능에 직접적인 영향을 미치고 UI 레이아웃이 얼마나 효율적이고 우아하게 구축되는지를 결정하기 때문에, Compose UI의 핵심 구성 요소를 이해하는 것은 매우 중요합니다. 이 카테고리는 여러분이 매일 사용하는 Compose UI API에 대한 더 깊은 이해를 얻는 데 도움이 될 것입니다. 그러나 이 책은 가능한 모든 Jetpack Compose 관련 인터뷰 질문을 상세히 다루기보다는, Compose UI에 대한 견고한 기초를 다지는 데 필수적인 근본적인 개념에 초점을 맞춥니다.

---

- [Modifier란 무엇인가?](./Modifier/README.md)
- [Layout 컴포저블이란 무엇인가?](./Layout/README.md)
- [Box란 무엇인가요?](./Box/README.md)
- [Arrangement와 Alignment의 차이점은 무엇인가요?](./ArrangementAlignment/README.md)
- [Painter란 무엇인가?](./Painter/README.md)
- [네트워크에서 이미지를 로드하는 방법](./LoadImages/README.md)
- [UI 버벅임(jank)을 피하면서 수백 개의 아이템을 리스트로 효율적으로 렌더링하려면 어떻게 해야 할까요?](./EfficientlyRender/README.md)
- [레이지 리스트(Lazy Lists)로 페이지네이션(Pagination)을 구현하는 방법](./Pagination/README.md)
- [Canvas란 무엇인가?](./Canvas/README.md)
- [graphicsLayer Modifier를 사용해 본 적이 있나요?](./GraphicsLayer/README.md)
- [Jetpack Compose에서 시각적 애니메이션을 구현하는 방법](./Animation/README.md)
- [화면 간 내비게이션(이동)은 어떻게 하나요?](./Navigation/README.md)
