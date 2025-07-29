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
- [프리뷰(Preview)는 어떻게 작동하며 어떻게 처리하나요?](./Preview/README.md)
- [Compose UI 컴포넌트 또는 화면에 대한 유닛 테스트는 어떻게 작성하나요?](./Test/README.md)
- [스크린샷 테스트란 무엇이며, 개발 중 UI 일관성을 보장하는 데 어떻게 도움이 되나요?](./TestScreenshot/README.md)
- [Jetpack Compose에서 접근성을 보장하는 방법](./Accessibility/README.md)

---

## 용어

1. **중간 표현 (IR, Intermediate Representation)**

    중간 표현(IR)은 컴파일러가 컴파일 과정에서 소스 코드를 나타내기 위해 사용하는 추상적인 코드 구조입니다. 이는 소스 코드와 대상 머신 코드 사이의 다리 역할을 하며, 플랫폼에 독립적인 최적화, 코드 분석, 그리고 효율적인 코드 생성을 가능하게 합니다.

2.  **슬롯 테이블 (Slot table)**
    
    슬롯 테이블(Slot table)은 Jetpack Compose에서 컴포지션(Composition) 단계 동안 UI 요소의 상태를 저장하고 관리하는 데 사용되는 데이터 구조입니다. 이는 UI 컴포넌트, 그 관계, 그리고 연관된 상태를 효율적으로 추적하여, 상태 변경에 영향을 받는 요소만 업데이트함으로써 최적화된 리컴포지션을 가능하게 합니다.

3.  **갭 버퍼 (Gap buffer)**
    
    갭 버퍼(Gap buffer)는 텍스트 편집기에서 동적인 문자 시퀀스를 효율적으로 관리하기 위해 흔히 사용되는 데이터 구조입니다. 이는 '갭(gap)'을 가진 연속적인 메모리 블록을 유지하여, 필요할 때만 요소를 이동시켜 빈번한 수정에 대한 오버헤드를 줄이고 빠른 삽입 및 삭제를 가능하게 합니다.

4.  **부작용 (Side-effects)**
    
    부작용(Side-effects)은 프로그래밍에서 값을 반환하는 것 이상의 작업을 수행하여, 프로그램의 상태에 영향을 미치거나 외부 환경과 상호작용하는 행위입니다. 예시로는 변수 수정, 네트워크 요청, 또는 UI 업데이트 등이 있으며, 이는 함수의 범위를 벗어나 프로그램 동작에 영향을 줄 수 있습니다.

5.  **슬롯 테이블(Slot table)**
    
    슬롯 테이블(Slot table)은 Jetpack Compose에서 컴포지션(Composition) 단계 동안 UI 요소의 상태를 저장하고 관리하는 데 사용되는 데이터 구조입니다. 이는 UI 컴포넌트, 그 관계, 그리고 연관된 상태를 효율적으로 추적하여, 상태 변경에 영향을 받는 요소만 업데이트함으로써 최적화된 리컴포지션을 가능하게 합니다.

6.  **JIT (Just-in-Time) 컴파일**
    
    JIT(실시간 컴파일)는 바이트코드가 실행 직전에 동적으로 머신 코드로 변환되는 런타임 과정입니다. 이를 통해 런타임 환경은 실제 실행 패턴을 기반으로 코드를 최적화하여, 자주 사용되는 코드 경로의 성능을 향상시킬 수 있습니다.

7.  **R8 최적화**
    
    R8 최적화는 안드로이드를 위한 코드 축소 및 최적화 도구로, 사용되지 않는 코드 제거, 메서드 인라이닝, 그리고 상수 폴딩 및 람다 그룹화와 같은 고급 최적화를 적용하여 APK 크기를 줄이고 런타임 성능을 향상시킵니다. 또한 보안을 강화하기 위해 코드를 난독화하고, 바이트코드를 더 효율적인 실행 형태로 변환합니다.

