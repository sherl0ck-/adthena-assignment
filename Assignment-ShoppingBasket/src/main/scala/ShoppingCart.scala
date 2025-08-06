import java.time.LocalDate

case class ShoppingCart(
  items: Map[StoreItem, Int],
  store: Store,
  shoppingDate: LocalDate = LocalDate.now(),
) {

  def total: BigDecimal = items.map { case (item, qty) => item.price * qty }.sum

  def printPrice(priceName: String, price: BigDecimal): Unit =
    println(s"$priceName: ${CurrencyFormatter.format(price)(store.currency)}")

  private lazy val appliedDiscounts: List[(BigDecimal, String)] =
    store.promotions.filter(_.isValidOn(shoppingDate)).flatMap(_.apply(items))

  def printDiscountsReceipt(): Unit = {
    if (appliedDiscounts.isEmpty) {
      println("(No offers available)")
    }

    appliedDiscounts.foreach { case (amount, description) =>
      println(s"$description: ${CurrencyFormatter.format(amount)(store.currency)}")
    }
  }

  def finalPrice: BigDecimal = {
    val totalDiscount = appliedDiscounts.map(_._1).sum
    total - totalDiscount
  }
}

object ShoppingCart {
  def fromArgs(args: Array[String]): ShoppingCart =
    fromArgs(args, Store.default)
  def fromArgs(args: Array[String], store: Store): ShoppingCart = {
    val (valid, invalid) = args.partition(arg => store.storeItemRegistry.get(arg).isDefined)
    if (invalid.nonEmpty)
      println(s"Warning: unknown items ignored: ${invalid.mkString(", ")}")

    val itemCounts: Map[StoreItem, Int] = valid
      .flatMap(arg => store.storeItemRegistry.get(arg))
      .groupBy(identity)
      .view
      .mapValues(_.length)
      .toMap

    ShoppingCart(itemCounts, store)
  }
}
