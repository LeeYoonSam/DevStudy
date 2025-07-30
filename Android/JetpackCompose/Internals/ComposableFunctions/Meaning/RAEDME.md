# Composable 함수의 의미 (The meaning of Composable functions)

Composable 함수는 Jetpack Compose의 가장 기본이 되는 요소이며, Composable 트리 구조를 작성하는 데 사용됩니다. 여기서 저자는 의도적으로 `트리(trees)`라고 이야기하는데, 그 이유는 Compose Runtime(컴포즈 런타임)이 Composable 함수를 메모리에서 큰 트리의 일원인 하나의 노드(node)로서 이해하고 나타낼 수 있기 때문입니다.

@Composable 어노테이션 달아놓는 것만으로 어떠한 표준 코틀린 함수든 Composable 함수로 만들 수 있습니다. 가령, 아래와 같은 예시를 살펴볼 수 있습니다.

```kotlin
@Composable
fun NamePlate(name:String) {
  // Our composable code 
}
```

@Composable 어노테이션을 사용함으로써, 우리는 컴파일러에게 이 함수가 본질적으로 데이터를 하나의 노드(node)로 변환하여 Composable 트리(tree)에 기재하겠다는 의도를 전달합니다. 

즉, 우리가 Composable 함수를 `@Composable (Input) -> Unit`와 같은 형태로 볼 때, 입력값은 데이터가 되고, 출력은 일반적으로 우리가 생각하는 함수의 반환 값이 아니라, 트리에 요소를 삽입하기 위해 기재된 일련의 동작(action)이라고 볼 수 있습니다. 우리는 이 동작을 함수 실행의 부수 효과(side effect)로 발생한다고 말할 수 있습니다.

> 함수에서 입력(input)을 받고 Unit을 반환하면 해당 함수 내에서 input을 어떤 방식으로든 소비하고 있을 가능성이 높다고 볼 수 있습니다.

이러한 동작은 일반적으로 Compose 관용어로 `방출(emitting)`이라고 알려져 있습니다. Composable 함수는 실행될 때 함수의 구현 정보를 방출하며 이는 Composition 처리 과정 중에 발생합니다.

**Composable 함수를 구성한다**라는 용어가 심심찮게 등장할 텐데, 접하실 때마다 이를 단순히 **실행한다(executing)**와 동일한 의미로 해석하시면 되겠습니다.


![composable-function-emits](./screenshots/composable-function-emits.png)
그림 1. Composable function emits image

Composable 함수를 실행하는 **유일한 목적**은 `트리의 인메모리 표현(in-memory representation)을 만들거나 업데이트하는 것`입니다. 

Composable 함수는 읽은 데이터가 변경될 때마다 다시 실행되므로 나타내는 메모리 구조를 항상 최신 상태로 유지합니다. 따라서, 트리를 최신 상태로 유지하기 위해 트리에 새 노드를 삽입하거나, 제거 및 교체하고 위치를 옮길 수도 있습니다. 또한, Composable 함수는 트리로부터 상태(state)를 읽거나 쓸 수도 있습니다.

## 요약
* `@Composable` 어노테이션이 붙은 함수는 Jetpack Compose UI를 구성하는 기본 단위로, **값을 반환하는 대신 UI 노드(node)를 '방출(emit)'하여 메모리 내의 UI 트리를 만들거나 업데이트**하는 것을 유일한 목적으로 합니다.
* **컴포저블 함수를 구성한다**는 것은 단순히 **함수를 실행한다**는 의미입니다.
* 컴포저블 함수는 자신이 읽는 **데이터(상태)가 변경될 때마다 자동으로 다시 실행(리컴포지션)** 되어, UI 트리를 항상 최신 상태로 유지합니다.