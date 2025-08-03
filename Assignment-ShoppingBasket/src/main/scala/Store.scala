import StoreItemRegistry.{Bread, Milk}

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
        PercentageDiscount(Bread, 20.0, LocalDate.now().minusDays(5), LocalDate.now().plusDays(2)),
        BuyXGetYDiscount(Bread, 2, Milk, 10.0),
      ),
      storeItemRegistry = StoreItemRegistry.default,
    )
}
