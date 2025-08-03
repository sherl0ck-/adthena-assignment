import java.util.Currency

object CurrencyFormatter {

  def format(amount: BigDecimal)(implicit currency: Currency): String = {
    val fractionDigits = currency.getDefaultFractionDigits
    if (fractionDigits == 2 && amount < 1) {
      val subunit = f"${amount * 100}%.0f"
      CurrencyContext
        .subunitFor(currency)
        .map(symbol => s"$subunit$symbol")
        .get
    } else {
      f"${currency.getSymbol}$amount%.2f"
    }
  }
}

object CurrencyContext {
  // Constants
  val GBP: Currency = Currency.getInstance("GBP")
  val EUR: Currency = Currency.getInstance("EUR")
  val USD: Currency = Currency.getInstance("USD")

  val default: Currency = GBP

  private val metadata: Map[String, String] = Map(
    "GBP" -> "p",
    "EUR" -> "c",
    "USD" -> "¢",
  )

  def subunitFor(currency: Currency): Option[String] = metadata.get(currency.getCurrencyCode)
}
