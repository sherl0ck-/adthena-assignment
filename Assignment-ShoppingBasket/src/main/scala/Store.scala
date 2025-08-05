import StoreItemRegistry.{ Apples, Bread, Soup }

import java.time.LocalDate
import java.util.Currency

case class Store(
  promotions: List[Promotion],
  storeItemRegistry: StoreItemRegistry,
  currency: Currency,
)

object Store {
  val default: Store =
    Store(
      currency = Currency.getInstance("GBP"),
      promotions = List(
        PercentageDiscount(Apples, 10.0, LocalDate.now().minusDays(1), LocalDate.now().plusDays(5)),
        BuyXGetYDiscount(Soup, 2, Bread, 50.0),
      ),
      storeItemRegistry = StoreItemRegistry.default,
    )
}
