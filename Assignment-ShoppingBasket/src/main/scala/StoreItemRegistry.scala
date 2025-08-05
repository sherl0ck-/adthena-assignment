class StoreItemRegistry private (private val itemsByName: Map[String, StoreItem]) {
  def get(name: String): Option[StoreItem] = itemsByName.get(name.trim.capitalize)
  def add(items: List[StoreItem]): StoreItemRegistry = {
    val updatedItems = itemsByName ++ items.map(i => i.name -> i)
    new StoreItemRegistry(updatedItems)
  }
}

object StoreItemRegistry {
  case object Apples extends StoreItem {
    val name  = "Apples"
    val price = BigDecimal(1.00)
  }

  case object Bread extends StoreItem {
    val name  = "Bread"
    val price = BigDecimal(0.80)
  }

  case object Soup extends StoreItem {
    val name  = "Soup"
    val price = BigDecimal(0.65)
  }

  case object Milk extends StoreItem {
    val name  = "Milk"
    val price = BigDecimal(1.30)
  }

  val default = new StoreItemRegistry(
    List(Apples, Bread, Milk, Soup).map(i => i.name -> i).toMap,
  )
}
