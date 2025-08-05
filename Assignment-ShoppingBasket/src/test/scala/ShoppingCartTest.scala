import StoreItemRegistry.{ Apples, Bread, Milk, Soup }
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import java.time.LocalDate
import java.util.Currency

class ShoppingCartTest extends AnyFunSuite with Matchers {

  test("ShoppingCart placeholder: configurable store, registry, date, promotions, items") {
    // Custom StoreItem
    case object Bananas extends StoreItem {
      val name  = "Bananas"
      val price = BigDecimal(0.40)
    }

    // Custom Registry
    val customRegistry: StoreItemRegistry = StoreItemRegistry.default.add(List(Bananas))

    // Custom Promotions
    val customPromotions = List(
      PercentageDiscount(Bananas, 15.0, LocalDate.of(2025, 8, 1), LocalDate.of(2025, 8, 31)),
      BuyXGetYDiscount(Soup, 3, Milk, 100.0), // free Milk for 3 soups
    )

    // Custom Store
    val store = Store(
      promotions = customPromotions,
      storeItemRegistry = customRegistry,
      currency = Currency.getInstance("GBP"),
    )

    // Custom items
    val items: Map[StoreItem, Int] = Map(
      Bananas -> 4,
      Soup    -> 3,
      Milk    -> 1,
    )

    // Custom date for shopping
    val shoppingDate = LocalDate.of(2025, 8, 15)

    // Shopping cart
    val cart = ShoppingCart(items, store, shoppingDate)

    ReceiptPrinter.print(cart, true)
    println("Placeholder test output:")

    // No assertions yet — this is a template for testing full configurability
  }

  test("ShoppingCart calculates total and applies promotions correctly") {
    val store = Store(
      promotions = List(
        PercentageDiscount(Apples, 10.0, LocalDate.of(2025, 8, 4), LocalDate.of(2025, 8, 10)),
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
    val cart = ShoppingCart(items, store, LocalDate.of(2025, 8, 6))

    // Assert raw total
    cart.total shouldBe BigDecimal("4.10") // 2*1.00 + 2*0.65 + 1*0.80

    // Assert final price
    val finalPrice = cart.finalPrice
    finalPrice shouldBe BigDecimal("3.50") // With ~10% off apples -20p and 50% off bread -40p
  }

  test("ShoppingCart with no promotions returns full total") {
    val store = Store(
      promotions = Nil,
      StoreItemRegistry.default,
      Currency.getInstance("GBP"),
    )

    val items: Map[StoreItem, Int] = Map(
      Apples -> 1,
      Bread  -> 1,
    )

    val cart = ShoppingCart(items, store, LocalDate.of(2025, 8, 11))

    cart.total shouldBe BigDecimal("1.80") // 1.00 + 0.80
    cart.finalPrice shouldBe BigDecimal("1.80")
  }

  test("ShoppingCart with expired promotions does not apply discounts") {
    val store = Store(
      promotions = List(
        PercentageDiscount(Apples, 10.0, LocalDate.of(2025, 8, 1), LocalDate.of(2025, 8, 6)),
      ),
      StoreItemRegistry.default,
      Currency.getInstance("GBP"),
    )

    val items: Map[StoreItem, Int] = Map(Apples -> 3)

    val cart = ShoppingCart(items, store, LocalDate.of(2025, 8, 7))

    cart.total shouldBe BigDecimal("3.00")
    cart.finalPrice shouldBe BigDecimal("3.00") // no discount applied
  }

  test("ShoppingCart using custom registry includes additional item") {
    case object Bananas extends StoreItem {
      val name  = "Bananas"
      val price = BigDecimal(0.70)
    }

    val customRegistry: StoreItemRegistry = StoreItemRegistry.default.add(List(Bananas))

    val store = Store(
      promotions = Nil,
      storeItemRegistry = customRegistry,
      Currency.getInstance("GBP"),
    )

    val items: Map[StoreItem, Int] = Map(Bananas -> 2, Milk -> 1)

    val cart = ShoppingCart(items, store, LocalDate.of(2025, 8, 6))

    cart.total shouldBe BigDecimal("2.70") // 2*0.70 + 1.30
    cart.finalPrice shouldBe BigDecimal("2.70")
  }

  test("BuyXGetYDiscount is not applied if buy quantity is insufficient") {
    val store = Store(
      promotions = List(BuyXGetYDiscount(Soup, 2, Bread, 50.0)),
      StoreItemRegistry.default,
      Currency.getInstance("GBP"),
    )

    val items: Map[StoreItem, Int] = Map(Soup -> 1, Bread -> 1)

    val cart = ShoppingCart(items, store, LocalDate.of(2025, 8, 6))

    cart.total shouldBe BigDecimal("1.45")      // 0.65 + 0.80
    cart.finalPrice shouldBe BigDecimal("1.45") // no discount
  }

  test("Multiple BuyXGetYDiscounts applied if buy quantity allows") {
    val store = Store(
      promotions = List(BuyXGetYDiscount(Soup, 2, Bread, 50.0)),
      StoreItemRegistry.default,
      Currency.getInstance("GBP"),
    )

    val items: Map[StoreItem, Int] = Map(Soup -> 4, Bread -> 2)

    val cart = ShoppingCart(items, store, LocalDate.of(2025, 8, 6))

    cart.total shouldBe BigDecimal("4.20")      // 4*0.65=2.60 + 2*0.80=1.60
    cart.finalPrice shouldBe BigDecimal("3.40") // 2x 50% off Bread = -0.80
  }
}
