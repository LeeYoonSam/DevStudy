# Composable 함수 타입 (Composable function types)

`@Composable 어노테이션`은 **컴파일 시점에 함수의 타입을 효과적으로 변경**합니다. 함수의 구문(syntax)적 관점에서, Composable 함수의 타입은 `@Composable (T) -> A` 입니다. 여기서 A는 Unit일 수도 있고, 함수가 값을 반환하는 경우(예를 들어 remember) 다른 타입일 수도 있습니다. 개발자들은 Kotlin에서 일반적인 람다를 선언하는 것처럼 Composable 람다를 선언할 수 있습니다.

```kotlin
// This can be reused from any Composable tree
val textComposable: @Composable (String) -> Unit = {
  Text(
    text = it,
    style = MaterialTheme.typography.subtitle1
  )
}

@Composable
fun NamePlate(name: String, lastname: String) {
  Column(modifier = Modifier.padding(16.dp)) {
    Text(
      text = name,
      style = MaterialTheme.typography.h6
    )
    textComposable(lastname)
  }
}
```


또한, Composable 함수는 `@Composable Scope.() -> A`와 같은 형태의 타입을 가질 수 있는데, 이는 특정 Composable로만 정보 범위를 지정하는 데 자주 사용됩니다.

```kotlin
inline fun Box(
  ...,
  content: @Composable BoxScope.() -> Unit
) {
  // ...
  Layout(
    content = { BoxScopeInstance.content() },
    measurePolicy = measurePolicy,
    modifier = modifier
  )
}
```

언어의 관점에서 볼 때, **`타입`은 컴파일러에 정보를 제공하여 빠른 정적 검증을 수행하고, 때로는 편리한 코드를 생성하며, 런타임에 의해 활용되는 데이터 사용 방식을 제한 및 정제하기 위하여 존재**합니다. `@Composable 어노테이션`은 **런타임 시 Composable 함수의 유효성을 검사하고 사용하는 방법을 변경하는데, 이것이 바로 Composable 함수가 Kotlin의 표준 함수와 다른 타입으로 간주되는 이유**입니다.

## 요약

- `@Composable` 어노테이션은 함수의 타입을 변경하는 **타입 수식어(type modifier)** 입니다. 이는 컴포저블 함수가 일반 함수와 다르다는 것을 컴파일러와 런타임에 알려, 상태 관리나 리컴포지션과 같은 Compose의 고유한 동작을 가능하게 합니다. 컴포저블 람다는 `@Composable (T) -> Unit`과 같은 타입으로 변수에 할당될 수 있습니다.
* `@Composable` 어노테이션은 일반 코틀린 함수의 타입을 **`@Composable (T) -> A`** 와 같은 **특별한 컴포저블 함수 타입**으로 변경하는 역할을 합니다.
* 이 타입 시스템 덕분에, 컴포저블 람다를 변수에 할당하거나 파라미터로 전달하는 등 일반 함수처럼 다룰 수 있습니다.
* 또한, `BoxScope`와 같이 특정 **스코프(Scope)를 가진 컴포저블 함수 타입** (`@Composable Scope.() -> Unit`)도 정의하여, 특정 컨텍스트에서만 사용할 수 있는 기능을 제공할 수 있습니다.
* 결론적으로, `@Composable`을 별도의 타입으로 취급하는 것은 컴파일러가 코드를 정적으로 검증하고 Compose 런타임이 함수를 특별한 방식으로(예: 리컴포지션) 처리하도록 하는 근간이 됩니다.