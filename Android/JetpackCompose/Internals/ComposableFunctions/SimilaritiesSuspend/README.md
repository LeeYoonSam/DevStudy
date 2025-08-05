# Suspend 함수와의 유사성 (Similarities with suspend functions)

Kotlin의 `suspend` 함수는 다른 suspend 함수에서만 호출될 수 있으므로, suspend 함수 또한 호출 컨텍스트를 필요로합니다. 이는 suspend 함수 끼리만 묶일 수 있도록 보장하며, **Kotlin 컴파일러가 모든 계산 과정에 걸쳐 런타임 환경을 주입하고 전달할 기회를 제공**합니다.


이 런타임 환경은 매개변수 목록의 끝에 `Continuation`라는 추가 매개변수로서 각 suspend 함수에 추가됩니다. 이 매개변수는 암시적인 것이므로, 개발자들이 이에 대해 알아야 할 필요가 없습니다. Continuation은 언어 차원에서 몇 가지 새롭고 강력한 기능을 사용할 수 있게 합니다.


Kotlin의 코루틴 시스템에서 Continuation은 일종의 콜백과 같습니다. **Continuation은 프로그램에게 어떻게 실행을 계속 이어서 할지 알려주는 역할**을 합니다.


아래와 같은 코드가 있다고 가정해보겠습니다.

```kotlin
suspend fun publishTweet(tweet: Tweet): Post = ...
```


위의 코드는 Kotlin 컴파일러에 의해 아래와 같이 변경됩니다.

```kotlin
fun publishTweet(tweet: Tweet, callback: Continuation<Post>): Unit
```


Continuation은 Kotlin 런타임이 프로그램의 **다양한 중단점에서 실행을 일시 중단하고 재개하는 데 필요한 모든 정보**를 담고 있습니다. 이는 호출 컨텍스트를 요구하는 것이 실행 트리 전반에 걸쳐 암시적인 방법으로 추가 정보를 전달하는 수단으로 사용될 수 있는 또 다른 좋은 예 입니다. 이 정보를 바탕으로 런타임은 고급 언어 기능을 활성화할 수 있습니다.

마찬가지로 `@Composable`도 언어적 기능으로 이해할 수 있습니다. Composable 함수는 표준 Kotlin 함수를 재시작(restartable) 가능하고, 상태(state) 등으로부터 반응할 수 있도록 만듧니다.


이 시점에서 왜 Jetpack Compose 팀은 원하는 동작을 구현하기 위해 suspend를 사용하지 않았는가 라는 합리적인 질문을 던져볼 수 있습니다. 비록 Composable과 suspend가 가져가는 패턴은 매우 유사하지만, 두 기능은 언어 수준에서 완전히 다른 기능을 가능하게 합니다.


**Continuation 인터페이스**는 실행을 중단하고 재개하는 것에 매우 구체적이므로, 콜백 인터페이스로 모델링되어 있으며, Kotlin은 **실행 지점 간 점프를 수행하고, 다양한 중단점을 조율하고, 중단점 사이에서 데이터를 공유하는 등 필요한 모든 시스템과 함께 기본적인 구현을 생성**합니다. 그러나, Compose의 사용 사례는 이와 매우 다릅니다. **Compose의 목표는 런타임에서 다양한 방법으로 최적화될 수 있는 대규모의 함수 호출 그래프에서 인메모리 표현을 생성하는 것**이기 때문입니다.


Composable 함수와 suspend 함수 사이의 유사점을 이해하고 나면, `함수 컬러링(function coloring)`이라는 아이디어가 흥미롭게 다가오실 수 있습니다.


## 요약
* **`@Composable` 함수**는 코틀린의 **`suspend` 함수**와 유사한 패턴을 가집니다. 둘 다 **특정 호출 컨텍스트(calling context)** 를 필요로 하므로, 같은 종류의 함수 내에서만 호출될 수 있습니다.
* 이러한 유사성은 컴파일러가 두 함수 유형을 변환하는 방식에서 비롯됩니다. 컴파일러는 `suspend` 함수에는 숨겨진 `Continuation` 파라미터를, `@Composable` 함수에는 숨겨진 `Composer` 파라미터를 **암묵적으로 주입**합니다.
* 주입된 `Continuation`과 `Composer`는 각각 코루틴 런타임 및 Compose 런타임과 상호작용하는 통로 역할을 합니다.
* 그러나 두 기능의 **목적은 근본적으로 다릅니다.** `suspend` 함수는 실행을 **일시 중단하고 재개**하는 비동기 흐름 제어를 위한 것이고, `@Composable` 함수는 **UI 트리의 인메모리 표현을 생성하고 업데이트**하기 위한 것입니다.