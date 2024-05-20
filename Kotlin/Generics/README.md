# Generic

## 변성(Variance) — 공변성(Covariance)과 반공변성(Contravariance) 정의
타입 시스템의 Typing rule 또는 Type constructor(제네릭 등)는 다음 중 하나의 성질을 가진다.

### Covariant(공변성)
**서브타입의 순서가 보존**
- 좁은(more specific) 타입 ≤ 넓은(more generic) 타입 순서를 보존하는 경우
- Type constructor가 타입 A ≤ B에 대해 T<A> ≤ T<B>인 경우

### Contravariant(반공변성)
**서브타입의 순서가 반대로 보존**
- 타입의 순서가 좁은(more specific) 타입 ≥ 넓은(more generic) 인 경우
- Type constructor가 타입 A ≤ B에 대해 T<A> ≥ T<B>인 경우

### Bivariant
**넓은 타입으로도, 좁은 타입으로도 변환 가능**
- Covariant하면서 Contravariant한 경우
- Type constructor가 타입 A ≤ B에 대해 T<A> ≡ T<B>인 경우

### Invariant(무공변)
**타입을 변환할 수 없음**
- Covariant하지도, Contravariant하지도, Bivariant하지도 않은 경우

## kotlin-variance
슈퍼타입이 선언된 곳이면 어디든 하위타입으로 대체할 수 있습니다.

하지만 제네릭의 경우 항상 우리가 예상하는 방식으로 작동하는 것은 아닙니다. 
MutableList<Cow>는 List<Cow>의 서브타입이긴 하지만, MutableList<FarmAnimal>의 서브타입은 아닙니다.

약간의 마법을 사용하면 매개변수화된 유형을 필요한 하위 유형으로 세분화할 수 있습니다. 

그러기 위해서는 먼저 공변성(Covariance)과 반공병성(Contravariance)에 대해 배워야 합니다.

### [Covariance](./Covariance.kt)
계약: 동전을 넣으면 간식을 받을수 있는 자판기 설치

**기계 변경**
- 동전을 넣으면 트레일 믹스, 젤리 봉지, 캔디바 같은 간식을 랜덤으로 받음
- 동전을 넣으면 캔디바 간식을 받음
- 동전을 넣으면 캔디바, 장난감을 랜덤으로 받음

### Covariance and Substitution
- 동전을 넣으면 트레일 믹스, 젤리 봉지, 캔디바 같은 간식을 랜덤으로 받거나 캔디바만 받음 (O)
  - 동전 -> 간식 이 나오는것과 동일해서 계약은 유효
- 동전을 넣으면 캔디바, 장난감을 랜덤으로 받음 (X)
  - 계약 위반

### [Contravariance](./Contravariance.kt)
**기계 변경**
- 동전과 지폐를 모두 사용하는 기계
- 동전만 사용하는 기계

### Contravariance and Substitution
- 원래 자판기를 동전과 지폐를 모두 받을 수 있는 자판기로 교체 (O)
  - 여전히 동전을 받을 수 있었기 때문에 모든 것이 정상적으로 작동했습니다. 아이들은 동전만 가지고 있었기 때문에 실제로 지폐 자판기를 사용한 적은 없었지만, 자판기가 여전히 동전을 받을 수 있는 한 지폐 자판기가 있어도 아무런 문제가 없었습니다.
- 동전만 받는 기계로 교체 (X)
  - 더 이상 니켈, 동전 또는 다른 종류의 동전을 받지 않는 기계로 계약 위반

### 하위 유형을 하위 유형으로 만드는 것은 무엇인가요?
하위 유형은 상위 유형의 계약을 완전히 지원해야 합니다. 구체적으로 다음 세 가지 규칙을 준수해야 한다는 뜻입니다.
- 서브타입은 슈퍼타입과 동일한 공용 속성 및 함수를 모두 가져야 합니다.
- 함수 매개변수 유형은 상위 유형과 동일하거나 더 일반적인 유형이어야 합니다.
- 함수 반환 유형은 상위 유형과 동일하거나 상위 유형보다 더 구체적이어야 합니다.

**Variance and Properties(변성 및 속성)**
간단하게 설명하기 위해 이 장에서는 주로 함수 매개변수 유형 및 반환 유형과 관련된 variance에 초점을 맞춥니다. 그러나 variance은 프로퍼티에도 여전히 적용됩니다.

- 함수 반환 유형과 마찬가지로 `val`로 선언된 프로퍼티를 재정의하고 더 구체적인 유형을 지정할 수 있습니다.
- `var`로 선언된 프로퍼티도 재정의할 수 있지만, 그 유형은 상위 유형에 선언된 것과 정확히 동일해야 하며 더 구체적이거나 더 일반적인 유형으로 대체할 수 없습니다.


## Variance Modifiers
위에서 살펴본 것처럼 VendingMachine<CandyBar>는 VendingMachine<Snack>의 계약을 완전히 만족하므로 그 서브타입이 될 수 있어야 합니다. 

그러나 타입 매개변수는 제네릭 타입의 본문 전체에서 여러 곳에 사용될 수 있습니다. 함수의 반환 타입, 함수의 매개변수, 프로퍼티의 타입 등으로 사용될 수 있습니다.

VendingMachine 인터페이스에 refund() 함수를 추가하면 어떻게 될지 생각해 봅시다.

```kotlin
class VendingMachine<T : Snack>(private val snack: T) {
    fun purchase(money: Coin): T = snack
    fun refund(snack: T): Coin = Dime()
}
```
- 이 경우 유형 매개변수 T는 함수 결과 유형과 함수 매개변수 유형으로 모두 나타납니다. 
- 이 버전의 VendingMachine<CandyBar>는 VendingMachine<Snack>의 계약을 만족할까요? 

### The out modifier
### The in modifier
Kotlin에는 `out` 수정자를 보완하는 `in`이라는 수정자가 포함되어 있습니다. 하지만 이를 살펴보기 전에 VendingMachine 클래스에서 상황을 바꿔 보겠습니다. 스낵 유형에 대한 유형 매개 변수 대신 돈 유형에 대한 유형 매개 변수를 사용하겠습니다.

- `T`가 인 포지션에서만 사용되더라도 이를 `in` variance 수정자를 사용하여 Kotlin에 선언해야 합니다.

- variance 수정자를 여러 유형 매개변수에 적용할 수 있다는 점을 알아두면 도움이 됩니다. 하지만 이 장의 나머지 부분에서는 예제를 쉽게 따라할 수 있도록 단일 유형 매개변수로 돌아가겠습니다.
- `in` 및 `out` 수정자가 variance을 만드는 방법을 살펴봤지만, `in`으로 선언된 유형 매개변수는 결과 유형으로 사용할 수 없고 `out`으로 선언된 유형 매개변수는 함수 매개변수 유형으로 사용할 수 없다는 단점도 살펴보았습니다. 하지만 때로는 이러한 절충안으로 해결되지 않을 때도 있습니다. 이러한 경우에도 유형 투영을 사용하여 variance이 주는 이점을 얻을 수 있습니다.


## Variance on Multiple Type Parameters
```kotlin
class VendingMachine<in T : Money, out R: Product>(private val product: R) {
    fun purchase(money: T): R = product
}
```

### Type Projections
- 지금까지 유형 매개변수에 변성 수정자를 사용하여 `VendingMachine<CandyBar>`를 `VendingMachine<Snack>`의 하위 유형으로 만들 수 있었습니다.

```kotlin
fun getSnackFrom(machine: VendingMachine<Snack>): Snack {
    return machine.purchase(Dime())
}
```

```kotlin
val candyBarMachine: VendingMachine<CandyBar> = VendingMachine(CandyBar())
getSnackFrom(candyBarMachine)
```
- 유형 매개변수에는 변성 수정자가 없으므로 VendingMachine<CandyBar>는 VendingMachine<Snack>의 서브타입이 아니므로 CandyBarMachine의 인스턴스로 함수를 호출할 수 없습니다.

```kotlin
fun getSnackFrom(machine: VendingMachine<out Snack>): Snack {
    return machine.purchase(Dime())
}
```
- 이렇게 변경하면 코드가 성공적으로 컴파일

### 공통 적인 타입 클래스
**Original Generic Class**
```kotlin
class VendingMachine<T: Product>() {
  fun purchase (money: Money): T = ...
  fun refund (product: T): Money = ...
}
```

**Effective Parameterized Type**
```kotlin
class VendingMachine<Snack>() {
  fun purchase (money: Money): Snack = ...
  fun refund (product: Snack): Money = ...
}
```

### Out-Projections

```kotlin
class VendingMachine<out T : Product>(private val product: T) {
    fun purchase(money: Money): T = product
}
```

**Effective Out-Projected Type**
```kotlin
class VendingMachine<out Snack>() {
  fun purchase (money: Money): Snack = ...
  fun refund (product: Nothing): Money = ...
}
```

### In-Projections
```kotlin
fun getRefundFrom(machine: VendingMachine<in CandyBar>): Coin {
    return machine.refund(CandyBar())
}
```

**Effective In-Projected Type**
```kotlin
class VendingMachine<in Snack>() {
  fun purchase (money: Money): Any? = ...
  fun refund (product: Snack): Money = ...
}
```

### Star Projections
다음은 tune()이라는 함수가 포함된 업데이트된 버전의 VendingMachine입니다. 보시다시피 이 새로운 함수는 함수 매개변수나 반환 유형으로 유형 매개변수를 전혀 사용하지 않습니다.


```kotlin
class VendingMachine<T : Snack>(private val snack: T) {
    fun purchase(money: Coin): T = snack
    fun refund(snack: T): Coin = Dime()
    fun tune() = println("All tuned up!")
}
```

이러한 경우 말 그대로 VendingMachine에서 생성된 모든 종류의 유형을 허용하는 함수가 필요할 수 있습니다. 
Kotlin에서는 별표 투영이라는 특별한 종류의 유형 투영을 사용하여 이를 쉽게 수행할 수 있습니다. 별 투영을 만들려면 변성 수정자를 사용하는 대신 별표 *(즉, "별")를 유형 인수 대신 사용하기만 하면 됩니다. 
예를 들어, 다음은 유형 인자에 관계없이 모든 종류의 자동판매기를 허용하는 함수입니다.

```kotlin
fun service(machine: VendingMachine<*>) {
    print("Tuning up $machine... ")
    machine.tune()
}
```

이 함수는 유형 인수에 관계없이 모든 종류의 자동판매기로 호출할 수 있습니다.

```kotlin
service(VendingMachine(CandyBar()) // Works with VendingMachine<CandyBar>
service(VendingMachine(TrailMix()) // Works with VendingMachine<TrailMix>
service(VendingMachine(GummyBears()) // Works with VendingMachine<GummyBears>
```

별 투영은 원래 매개변수화된 유형과 비슷해 보이지만
- 유형 매개변수가 인 위치에 사용된 모든 위치는 Nothing 유형으로 대체됩니다.
- 유형 매개변수가 아웃 위치에서 사용된 곳에서는 유형 매개변수의 상한으로 대체됩니다. 상한값이 지정되지 않은 경우 기본적으로 Any?

**Effective Star-Projected Type**
```kotlin
class VendingMachine<*>() {
  fun purchase (money: Money): Product = ...
  fun refund (product: Nothing): Money = ...
}
```

## 표준 라이브러리의 변성
이제 공변성, 반공변성, 변성 수정자 및 유형 투영에 대해 모두 배웠으므로 표준 라이브러리의 특정 유형이 왜 그런 식으로 작동하는지 더 잘 이해할 수 있습니다. 이 장의 시작은 MutableList<Cow>가 List<Cow>의 서브타입이지만 MutableList<FarmAnimal>의 서브타입이 아니라는 사실을 제시하는 것으로 시작했습니다. 

왜 그럴까요?
- MutableList는 List 인터페이스를 확장하기 때문에 MutableList<Cow>는 List<Cow>의 서브타입입니다. 이는 일반적인 인터페이스 상속입니다.
- MutableList<Cow>는 요소를 읽고 수정할 수 있으므로 유형 매개변수가 인-위치와 아웃-위치 모두에 나타나기 때문에 MutableList<FarmAnimal>의 서브타입이 아닙니다. 따라서 변성 수정자를 가질 수 없습니다. 이는 VendingMachine과 유사합니다.

지금까지 살펴본 바와 같이 Kotlin의 컬렉션 유형은 일반적으로 읽기 전용 유형(예: List)과 변경 가능 유형(예: MutableList)의 두 가지 유형으로 제공됩니다. 읽기 전용 유형은 유형 매개 변수에 out 수정자가 있으므로 List<Cow>는 List<FarmAnimal>의 하위 유형이 됩니다. 그러나 가변 유형에는 변성 수정자가 없으므로 MutableList<Cow>는 MutableList<FarmAnimal>의 하위 유형이 되지 않습니다. 하지만 이제 아시다시피, 유형 투영을 사용하여 이 문제를 해결할 수 있습니다!

## 배운 내용
- 공분산이 유형과 함수 반환 유형 간의 관계를 설명하는 방법.
- 공분산이 유형과 해당 함수 매개변수 유형 간의 관계를 설명하는 방법.
- 유형 매개변수에 분산 수정자를 사용하여 선언 사이트 분산을 만드는 방법.
- 유형 인수의 분산 수정자를 사용하여 사용 사이트 분산을 생성하는 방법.
- 표준 라이브러리의 컬렉션 유형이 분산 수정자를 사용하는 방법.

## 요약
- Generic 을 만들때 주의할 3가지 확인사항
  1. class 는 같은 함수와 속성을 포함해야 한다.
  2. 매개변수 타입은 같거나 더 일반적인(상위) 타입이어야 한다.
  3. 반환 타입은 같거나 더 자세한(하위) 타입이어야 한다.
- 변성 수정자 out 으로 out-position(반환 타입) 에서만 사용하도록 알린다.
- 변성 수정자 in 으로 in-position(매개변수 타입) 에서만 사용하도록 알린다.
- 타입 투영 - 2가지를 같이 사용해야 할 경우 in, out 을 제거 해야 한다.
  - 별도의 함수를 만들어서 매개변수로 클래스를 받고 in, out 이 필요하다면 각각 함수를 만들어서 사용
    - 이때 사용하지 않는 다른 함수들은 out - 매개변수(Nothing), in - 반환타입(Any?) 로 변경된다.
  - 매개변수도 없고 리턴타입도 없는 경우 스타 투영을 사용한다 (VendingMachine<*>)
    - 이때 매개변수 - Nothing, 반환타입 - 최상위 타입 으로 변경된다.


## References
- [Covariance and contravariance (computer science)](https://en.wikipedia.org/wiki/Covariance_and_contravariance_(computer_science))
- [리스코프 치환 원칙](https://ko.wikipedia.org/wiki/%EB%A6%AC%EC%8A%A4%EC%BD%94%ED%94%84_%EC%B9%98%ED%99%98_%EC%9B%90%EC%B9%99)
- [kotlin-variance](https://typealias.com/start/kotlin-variance/)
- [타입 시스템에서의 변성(Variance) — 공변성(Covariance)과 반공변성(Contravariance)](https://driip.me/d875a384-3fb9-471b-a53b-b3ca52f8238e)
- [제네릭의 공변성(Covariance) 및 반공변성(Contravariance)](https://learn.microsoft.com/ko-kr/dotnet/standard/generics/covariance-and-contravariance)