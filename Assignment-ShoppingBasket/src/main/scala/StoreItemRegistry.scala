class StoreItemRegistry private (private val itemsByName: Map[String, StoreItem]) {
  def get(name: String): Option[StoreItem] = itemsByName.get(name.trim.capitalize)
  def add(items: List[StoreItem]): StoreItemRegistry = {
    val updatedItems = itemsByName ++ items.map(i => i.name -> i)
    new StoreItemRegistry(updatedItems)
  }
}

object StoreItemRegistry {
  val Apples = StoreItem("Apples", 1.00)
  val Bread  = StoreItem("Bread", 0.80)
  val Soup   = StoreItem("Soup", 0.65)
  val Milk   = StoreItem("Milk", 1.30)

  val default = new StoreItemRegistry(
    List(Apples, Bread, Milk, Soup).map(i => i.name -> i).toMap,
  )
}
