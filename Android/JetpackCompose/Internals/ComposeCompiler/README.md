# 2. Compose 컴파일러 (The Compose compiler)

Jetpack Compose는 다양한 라이브러리들로 구성되어 있는데 이 책에서는 특히 **Compose 컴파일러 (이하 Compose Compiler), Compose 런타임 (이하 Compose Runtime), Compose UI 세 가지에 중점**을 둘 것입니다.


`Compose Compiler`와 `Runtime`은 Jetpack Compose의 핵심 요소입니다. 엄밀하게 말하자면, Compose UI는 Compose 아키텍처의 일부가 아닙니다. 그 이유는 **Runtime과 Compiler는 그 요구 사항을 충족하는 어떤 클라이언트 라이브러리에서든 사용될 수 있도록 포괄적으로 디자인되었기 때문**입니다. 

`Compose UI`는 **Runtime과 Compiler를 활용하는 클라이언트 중 하나**에 불과합니다. Compose UI와 관련해서 다른 클라이언트 라이브러리들도 있는데, 대표적으로 JetBrains에서 개발 중인 데스크톱 및 웹용 클라이언트 라이브러리가 있습니다. 그 말인 즉, Compose UI를 살펴보면 Compose가 Composable 트리의 런타임 인메모리 표현을 제공하는 방법과, 궁극적으로 해당 인메모리 표현을 어떻게 실제 요소로 구체화하는지를 이해하는 데 도움이 됩니다.

![compose-architecture](./Screenshots/compose-architecture.png)

Compose에 대한 위의 구조를 처음 접하시는 분은 위 그림이 나타내는 순서들이 약간 혼란스러울 수 있습니다. 지금까지 우리는 Compiler와 Runtime이 함께 동작하며 라이브러리의 모든 기능을 활성화한다는 것을 알아보았음에도 위 개념들에 익숙하지 않다면 아직까지 다소 추상적으로 느껴질 수 있습니다. `Compose Compiler`가 **Runtime의 요구 사항에 맞게 코드를 만드는 작업들, Runtime이 어떻게 동작하는지, 초기의 composition과 후속 recomposition은 언제 트리거(trigger)되는지, 트리의 인메모리 표현이 어떻게 공급되는지, 그 정보가 후속 recomposition에 어떻게 사용**되는지 등에 대한 더 자세한 설명들을 학습하게 될 겁니다. 이와 같은 Compose의 내부적인 개념을 이해하는 것은 라이브러리가 어떻게 동작하는지와 코딩할 때 본인이 무엇을 기대할 수 있는지에 대한 전반적인 감각을 기르는 데 도움이 됩니다.


이제 컴파일러를 이해하는 것부터 시작해 보도록 하겠습니다.

---

- [Compose 컴파일러 (The Compose compiler)](./Compiler/README.md)
- [Compose 어노테이션들 (Compose annotations)](./Annotations/README.md)
- [@Composable](./AnnotationComposable/README.md)
- [@ComposableCompilerApi](./AnnotationComposableCompilerApi/README.md)
- [@InternalComposeApi](./AnnotationInternalComposeApi/README.md)
- [@DisallowComposableCalls](./DisallowComposableCalls/README.md)
- [@ReadOnlyComposable](./ReadOnlyComposable/README.md)