object PriceBasket extends App {
  if (args.isEmpty) {
    println("Usage: PriceBasket item1 item2 ...")
    sys.exit(1)
  }

  val cart = ShoppingCart.fromArgs(args)
  ReceiptPrinter.print(cart)
}
