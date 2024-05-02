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

## [kotlin-variance](./Sample.kt)
슈퍼타입이 선언된 곳이면 어디든 하위타입으로 대체할 수 있습니다.

하지만 제네릭의 경우 항상 우리가 예상하는 방식으로 작동하는 것은 아닙니다. 
MutableList<Cow>는 List<Cow>의 서브타입이긴 하지만, MutableList<FarmAnimal>의 서브타입은 아닙니다.

약간의 마법을 사용하면 매개변수화된 유형을 필요한 하위 유형으로 세분화할 수 있습니다. 

그러기 위해서는 먼저 공변성(Covariance)과 반공병성(Contravariance)에 대해 배워야 합니다.

### Covariance
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

### Contravariance
**기계 변경**
- 동전과 지폐를 모두 사용하는 기계
- 동전만 사용하는 기계

### Contravariance and Substitution
- 원래 자판기를 동전과 지폐를 모두 받을 수 있는 자판기로 교체 (O)
  - 여전히 동전을 받을 수 있었기 때문에 모든 것이 정상적으로 작동했습니다. 아이들은 동전만 가지고 있었기 때문에 실제로 지폐 자판기를 사용한 적은 없었지만, 자판기가 여전히 동전을 받을 수 있는 한 지폐 자판기가 있어도 아무런 문제가 없었습니다.
- 동전만 받는 기계로 교체 (X)
  - 더 이상 니켈, 동전 또는 다른 종류의 동전을 받지 않는 기계로 계약 위반

### Variance Modifiers
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



## References
- [Covariance and contravariance (computer science)](https://en.wikipedia.org/wiki/Covariance_and_contravariance_(computer_science))
- [리스코프 치환 원칙](https://ko.wikipedia.org/wiki/%EB%A6%AC%EC%8A%A4%EC%BD%94%ED%94%84_%EC%B9%98%ED%99%98_%EC%9B%90%EC%B9%99)
- [kotlin-variance](https://typealias.com/start/kotlin-variance/)
- [타입 시스템에서의 변성(Variance) — 공변성(Covariance)과 반공변성(Contravariance)](https://driip.me/d875a384-3fb9-471b-a53b-b3ca52f8238e)
- [제네릭의 공변성(Covariance) 및 반공변성(Contravariance)](https://learn.microsoft.com/ko-kr/dotnet/standard/generics/covariance-and-contravariance)