# Compose 런타임 (Compose Runtime)

**Compose 런타임**은 Jetpack Compose의 근본적인 구성 요소로, **프로그래밍 모델과 상태 관리의 핵심 엔진 역할**을 합니다. 비록 Jetpack Compose가 내부 구성 요소에 대한 깊은 지식 없이도 직관적이고 원활하게 작동하도록 설계되었지만, Compose 런타임 API를 이해하는 것은 **성능 및 메모리 관리를 최적화**하는 데 매우 중요합니다.

Compose 런타임은 내부적으로 상태를 관리하여 수동 개입의 필요성을 줄여줍니다. 그러나 사용하는 API가 내부적으로 어떻게 작동하는지에 대한 확실한 이해를 갖추면, 특히 상태 관리 및 부작용(side-effect) 처리를 다룰 때 의도치 않은 부작용을 최소화하면서 더 효율적인 애플리케이션을 만들 수 있습니다.

---

- [상태(State)란 무엇이며 어떻게 관리하나요?](./State/README.md)
- [상태 호이스팅(State Hoisting)으로 얻을 수 있는 장점은 무엇인가요?](./StateHoisting/README.md)
- [remember와 rememberSaveable의 차이점은 무엇인가요?](./Remember/README.md)
- [컴포저블 함수 내에서 코루틴 스코프를 안전하게 생성하는 방법](./CoroutineScope/README.md)
- [컴포저블 함수 내에서 부작용(Side-Effect)을 처리하는 방법](./SideEffects/README.md)
- [rememberUpdatedState의 목적과 작동 방식은 무엇인가요?](./RememberUpdatedState/README.md)
- [produceState의 목적과 작동 방식은 무엇인가요?](./ProduceState/README.md)
- [snapshotFlow란 무엇인가?](./SnapshotFlow/README.md)
- [derivedStateOf의 목적과 작동 방식은 무엇인가요?](./DerivedState/README.md)