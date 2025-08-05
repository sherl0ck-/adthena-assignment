object ReceiptPrinter {

  def print(cart: ShoppingCart, debug: Boolean = false): Unit = {
    if (debug) {
      cart.items.foreach { case (item, qty) =>
        println(f"${item.name} x$qty → £${item.price * qty}%.2f")
      }
    }

    cart.printPrice("Subtotal", cart.total)
    cart.printDiscountsReceipt()
    cart.printPrice("Total price", cart.finalPrice)
  }

}
