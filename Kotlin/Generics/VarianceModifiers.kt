class VarianceModifier {
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

  // class VendingMachine<T : Snack>(private val snack: T) {
  //   fun purchase(money: Coin): T = snack
  //   fun refund(snack: T): Coin = Dime()
  // }

  /**
   * refund() 함수 조건 미충족
   *  - 2. parameter: same as or more general than -> X
   */
  // class VendingMachine1<Snack>() {
  //   fun purchase(money: Coin): Snack
  //   fun refund(snack: Snack): Coin
  // }
  
  // class VendingMachine2<CandyBar>() {
  //   fun purchase(money: Coin): CandyBar
  //   fun refund(snack: CandyBar): Coin
  // }

  /**
   * 첫 번째 변성 수정자는 out으로 명명되며, 이 유형 매개 변수가 아웃 위치에만 표시되도록 Kotlin에 알리는 방식입니다. 
   * 유형 매개변수 T에 out 수정자를 추가해 보겠습니다.
   */
  class VendingMachine<out T : Snack>(private val snack: T) {
      fun purchase(money: Coin): T = snack
  }
  
  fun main() {
    /**
     * 이 간단한 변경만으로 목록 19.14의 코드를 오류 없이 컴파일할 수 있습니다. 
     * 이제 VendingMachine<CandyBar는 VendingMachine<Snack의 하위 유형이 됩니다!
     * 
     * type은 연관된 함수 결과 type과 공변성 입니다. 
     * 따라서 이 type parameter가 결과 type으로만 사용되도록 함으로써 VendingMachine type이 T와 공변성이 안전하다는 것을 알 수 있습니다.
     */
    val candyBarMachine: VendingMachine<CandyBar> = VendingMachine(CandyBar())
    val vendingMachine: VendingMachine<Snack> = candyBarMachine
  }
}