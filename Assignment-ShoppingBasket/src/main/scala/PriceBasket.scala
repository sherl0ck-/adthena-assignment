import java.util.Currency

object PriceBasket extends App {
  if (args.isEmpty) {
    println("Usage: PriceBasket item1 item2 ...")
    sys.exit(1)
  }

  val cart = ShoppingCart.fromArgs(args)

  println("Cart contents (for debugging):")
  cart.items.foreach { case (item, qty) =>
    println(f"${item.name} x$qty → £${item.price * qty}%.2f")
  }
  println("-----------------------------")

  val total              = cart.total
  val totalAfterDiscount = cart.finalPrice

}
