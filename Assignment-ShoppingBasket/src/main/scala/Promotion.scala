import java.time.LocalDate

trait Promotion {
  def apply(items: Map[StoreItem, Int]): Option[(BigDecimal, String)]
  def description: String

  def isValidOn(date: LocalDate): Boolean = true

}

trait DateBoundPromotion extends Promotion {
  val startDate: LocalDate
  val endDate: LocalDate
  override def isValidOn(date: LocalDate): Boolean =
    date.isAfter(startDate) && date.isBefore(endDate)
}

case class PercentageDiscount(
  targetItem: StoreItem,
  percentage: Double,
  startDate: LocalDate,
  endDate: LocalDate,
) extends DateBoundPromotion {
  def apply(items: Map[StoreItem, Int]): Option[(BigDecimal, String)] =
    items.get(targetItem) match {
      case Some(qty) =>
        val priceReduction = targetItem.price * qty * (percentage / 100)
        Some((priceReduction, description))
      case None => None
    }

  override def description: String = s"${targetItem.name} $percentage% off"
}

case class BuyXGetYDiscount(
  buyItem: StoreItem,
  buyQuantity: Int,
  getItem: StoreItem,
  discountPercentage: Double,
) extends Promotion {

  def apply(items: Map[StoreItem, Int]): Option[(BigDecimal, String)] = {

    val buyCount = items.getOrElse(buyItem, 0)
    val getCount = items.getOrElse(getItem, 0)

    val nEligibleDiscounts = buyCount / buyQuantity
    val nDiscountedItems   = math.min(getCount, nEligibleDiscounts)

    if (nDiscountedItems > 0)
      Some((getItem.price * (discountPercentage / 100) * nDiscountedItems, description))
    else None

  }

  def description: String = s"Buy $buyQuantity $buyItem, get $getItem at $discountPercentage% off"
}
