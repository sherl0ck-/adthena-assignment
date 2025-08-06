import StoreItemRegistry._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

import java.time.LocalDate
import java.util.Currency

class ShoppingCartTest extends AnyFunSuite with Matchers with TableDrivenPropertyChecks {

  val GBP          = Currency.getInstance("GBP")
  val registry     = StoreItemRegistry.default
  val shoppingDate = LocalDate.of(2025, 8, 6)

  val scenarios = Table(
    ("description", "promotions", "items", "shoppingDate", "expectedFinal"),
    (
      "no promotions applied",
      Nil,
      Map(Apples -> 1, Bread -> 1),
      shoppingDate,
      BigDecimal("1.80"),
    ),
    (
      "expired promotion not applied",
      List(PercentageDiscount(Apples, 10.0, LocalDate.of(2025, 8, 1), LocalDate.of(2025, 8, 5))),
      Map(Apples -> 3),
      shoppingDate,
      BigDecimal("3.00"),
    ),
    (
      "percentage discount applied",
      List(PercentageDiscount(Apples, 10.0, LocalDate.of(2025, 8, 4), LocalDate.of(2025, 8, 10))),
      Map(Apples -> 2),
      shoppingDate,
      BigDecimal("1.80"), // 2 * 1.00 - 0.20
    ),
    (
      "buyXgetY discount not applied (not enough items)",
      List(BuyXGetYDiscount(Soup, 2, Bread, 50.0)),
      Map(Soup -> 1, Bread -> 1),
      shoppingDate,
      BigDecimal("1.45"),
    ),
    (
      "buyXgetY discount applied twice",
      List(BuyXGetYDiscount(Soup, 2, Bread, 50.0)),
      Map(Soup -> 4, Bread -> 2),
      shoppingDate,
      BigDecimal("3.40"), // 4*0.65 + 2*0.80 - 0.80
    ),
    (
      "buyXgetX discount",
      List(BuyXGetYDiscount(Apples, 3, Apples, 100.0)),
      Map(Apples -> 4),
      shoppingDate,
      BigDecimal("3.00"),
    ),
  )

  forAll(scenarios) { (description, promotions, items, shoppingDate, expectedFinal) =>
    test(s"ShoppingCart - $description") {
      println(s"ShoppingCart - $description")
      val store = Store(
        promotions = promotions,
        storeItemRegistry = registry,
        currency = Currency.getInstance("GBP"),
      )

      val cart = ShoppingCart(items, store, shoppingDate)
      ReceiptPrinter.print(cart, true)

      cart.finalPrice shouldBe expectedFinal
    }
  }
}
