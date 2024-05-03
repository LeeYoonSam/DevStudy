interface Coin
interface Product
interface Snack: Product
interface Toy: Product

class Dime : Coin {}

class CandyBar : Snack {}
class Car : Toy {}

// open class VendingMachine {
//     open fun purchase(money: Coin): Snack = randomSnack()
// }
open class VendingMachine {
  open fun purchase(money: Coin): Product = randomSnack()
}

fun randomSnack(): Snack {
  return CandyBar()
}

// class CandyBarMachine : VendingMachine() {
//   override fun purchase(money: Coin): Snack = CandyBar()
// }

/**
 * CandyBarMachine -> VendingMachine 의 subtype
 * CandyBar -> Snack 의 subtype
 */
class CandyBarMachine : VendingMachine() {
  override fun purchase(money: Coin): CandyBar = CandyBar()
}

fun randomToyOrSnack(): Toy {
  return Car()
}

class ToyOrSnackMachine : VendingMachine() {
  override fun purchase(money: Coin): Product = randomToyOrSnack()
}

fun main() {
  /**
   * VendingMachine -> supertype
   * CandyBarMachine -> subtype
   */
  val machine: VendingMachine = CandyBarMachine()
  
  // val snack = purchaseSnackFrom(CandyBarMachine())

  val candyBarMachine: CandyBarMachine = CandyBarMachine()
  val candyBar: CandyBar = candyBarMachine.purchase(Dime()) // (O) return CandyBar == type CandyBar

  val vendingMachine: VendingMachine = CandyBarMachine()
  val snack: Snack = vendingMachine.purchase(Dime()) // (X) return Product > type Snack

}

fun purchaseSnackFrom(machine: VendingMachine) = machine.purchase(Dime())