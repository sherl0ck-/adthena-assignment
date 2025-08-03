import StoreItemRegistry.{ Apples, Bread, Soup }
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import java.time.LocalDate
import java.util.Currency

class ShoppingCartTest extends AnyFunSuite with Matchers {

  test("ShoppingCart calculates total and applies promotions correctly") {
    // Define test store
    val store = Store(
      promotions = List(
        PercentageDiscount(Apples, 10.0, LocalDate.of(2025, 7, 31), LocalDate.of(2025, 8, 5)),
        BuyXGetYDiscount(Soup, 2, Bread, 50.0),
      ),
      StoreItemRegistry.default,
      Currency.getInstance("GBP"),
    )

    // Define items
    val items: Map[StoreItem, Int] = Map(
      Apples -> 2,
      Soup   -> 2,
      Bread  -> 1,
    )

    // Build cart
    val cart = ShoppingCart(items, store)
    println("Cart contents:")
    cart.items.foreach { case (item, qty) =>
      println(f"${item.name} x$qty → £${item.price * qty}%.2f")
    }
    println("-----------------")

    // Assert raw total
    cart.total shouldBe BigDecimal("4.10") // 2*1.00 + 2*0.65 + 1*0.80

    // Assert final price
    val finalPrice = cart.finalPrice
    finalPrice shouldBe BigDecimal("3.50") // With ~10% off apples -20p and 50% off bread -40p
  }
}
