# Shopping basket assignment
Scala repo to price a basket of goods taking into account some special offers
### Installation
Standard sbt installation (I'm using 1.11.3) through brew/apt or universally through Coursier

### Compile and run tests
```sbt assembly && sbt test``` from the terminal or ```compile``` followed by ```test``` in IntelliJ or similar. 

### Running from command line
```sbt "run Apples Milk Bread"``` 
or ```./PriceBasket Apples Milk Bread``` (might require you to do ```chmod +x PriceBasket```) will output as expected. Note that currency might show as e.g. GBP or £ depending on the system's locale 

### Adding a new test case
You can add a new row in scenarios within ShoppingCartTest. 
Currently, there isn't a way to bulk import test.
