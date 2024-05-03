class Contravariance {
  interface Money

  open class Coin : Money
  class Nickel : Coin()
  class Dime : Coin()
  class Quarter : Coin()

  interface Bill : Money
  class OneDollar : Bill
  class FiveDollar : Bill
  class TenDollar : Bill

  interface Product
  interface Snack: Product
  interface Toy: Product

  class CandyBar : Snack
  class Car : Toy


  // open class VendingMachine {
  //   open fun purchase(money: Coin): Snack = randomSnack()
  // }

  /**
   * 매개변수 유형을 Coin 에서 Money로 변경하려고 하면 컴파일러 오류가 발생
   *
   * 에러가 발생하는 이유
   * - Kotlin에서는 클래스 또는 인터페이스에 매개 변수 유형만 다르다면 동일한 이름을 가진 함수를 여러 개 가질 수 있습니다.
   * - 이 기능을 지원하기 위해 Kotlin에서는 하위 유형에서 함수 매개변수 유형을 변경하는 것을 허용하지 않습니다.
   */
  // class AnyMoneyVendingMachine : VendingMachine() {
  //   override fun purchase(money: Money): Snack = randomSnack()
  // }

  /*
   * 이 문제를 해결하는 한 가지 방법은 단순히 오버라이드 키워드를 제거하는 것인데,
   * 이는 함수를 재정의하는 대신 과부하가 걸리는 것을 의미합니다.
   *
   * 이 작업을 수행하면 두 개의 purchase() 함수가 생성되며,
   * 각 함수는 AnyMoneyVendingMachine과 VendingMachine에 각각 하나의 바디를 갖게 된다는 점을 기억하세요.
   */
  // class AnyMoneyVendingMachine : VendingMachine() {
  //   fun purchase(money: Money): Snack = randomSnack()
  // }

  /**
   * 따라서 Kotlin의 오버로드 기능이 방해가 되고 있습니다. 하지만 오버로딩은 fun 키워드로 선언된 함수에만 적용되므로 이 문제를 해결하려면 다음과 같이
   * purchase()를 함수에서 함수 유형이 있는 속성으로 변경하면 됩니다.
   */
  // open class VendingMachine {
  //   open val purchase: (Coin) -> Snack = { randomSnack() }
  // }

  // class AnyMoneyVendingMachine : VendingMachine() {
  //   override val purchase: (Coin) -> Snack = { randomSnack() }
  // }

  // class AnyMoneyVendingMachine : VendingMachine() {
  //   override val purchase: (Money) -> Snack = { randomSnack() }
  // }

  /**
   * 일반적으로 서브타입은 다른 클래스를 확장하는 클래스, 다른 인터페이스를 확장하는 인터페이스 또는 인터페이스를 구현하는 클래스라고 생각합니다. 
   * 이 세 가지 경우 모두 Kotlin의 유형 시스템은 서브클래스가 위의 세 가지 규칙을 따르도록 합니다. 
   * 그러나 매개변수화된 유형(예: VendingMachine<Snack> 및 VendingMachine<CandyBar>)의 경우 한 유형이 다른 유형의 하위 유형임을 명시적으로 선언할 수 없습니다.
   * 
   * 이를 보여주기 위해 VendingMachine을 제네릭 클래스로 변환해 보겠습니다. 
   * 이 변경의 일부로 purchase() 함수가 호출될 때 반환되는 스낵인 스낵 생성자 매개 변수를 추가하겠습니다.
   * 
   * 따라서 VendingMachine<CandyBar>는 VendingMachine<Snack>의 계약을 완전히 만족하므로,
   * 그 하위 유형 중 하나가 되어도 안전합니다. 
   * 하지만 이런 일이 자동으로 발생하는 것은 아닙니다. 
   * 
   * class VendingMachine<Snack>() {
   *  fun purchase(money: Coin): Snack
   * }
   * 
   * class VendingMachine<CandyBar>() {
   *  fun purchase (money: Coin): CandyBar
   * }
   * 
   * 1. class: same functions/properties
   * 2. parameter: same as or more general than
   * 3. return: same as or more specific than
   * 
   * 예를 들어 VendingMachine<Snack>으로 선언된 변수에 이를 할당하려고 하면 컴파일러 오류가 발생합니다.
   */
  class VendingMachine<T : Snack>(private val snack: T) {
    fun purchase(money: Coin): T = snack
  }

  /**
   * 예를 들어 VendingMachine<Snack>으로 선언된 변수에 이를 할당하려고 하면 컴파일러 오류가 발생합니다.
   */
  fun some() {
    val candyBarMachine: VendingMachine<CandyBar> = VendingMachine(CandyBar())
    val vendingMachine: VendingMachine<Snack> = candyBarMachine
  }
}
