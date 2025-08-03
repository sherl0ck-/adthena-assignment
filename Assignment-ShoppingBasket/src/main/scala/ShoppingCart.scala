import java.time.LocalDate

case class ShoppingCart(items: Map[StoreItem, Int], store: Store) {

  val shoppingDate = LocalDate.now()

  def total: BigDecimal = {
    val total = items.map { case (item, qty) => item.price * qty }.sum
    println(s"Subtotal: ${CurrencyFormatter.format(total)(store.currency)}")
    total
  }

  def applyDiscounts: List[(BigDecimal, String)] =
    store.promotions.filter(_.isValidOn(shoppingDate)).flatMap(_.apply(items))

  def finalPrice: BigDecimal = {
    val appliedDiscounts = applyDiscounts

    if (appliedDiscounts.isEmpty) {
      println("(No offers available)")
    }

    appliedDiscounts.foreach { case (amount, description) =>
      println(s"$description: ${CurrencyFormatter.format(amount)(store.currency)}")
    }

    val totalDiscount      = appliedDiscounts.map(_._1).sum
    val totalAfterDiscount = total - totalDiscount
    println(s"Total price: ${CurrencyFormatter.format(totalAfterDiscount)(store.currency)}")

    totalAfterDiscount
  }
}

object ShoppingCart {
  def fromArgs(args: Array[String]): ShoppingCart =
    fromArgs(args, Store.default)
  def fromArgs(args: Array[String], store: Store): ShoppingCart = {
    val itemCounts: Map[StoreItem, Int] = args
      .flatMap(arg => StoreItemRegistry.default.get(arg))
      .groupBy(identity)
      .view
      .mapValues(_.length)
      .toMap

    ShoppingCart(itemCounts, store)
  }
}
