ThisBuild / scalaVersion := "2.13.16"
ThisBuild / version      := "0.1.0"

lazy val root = (project in file("."))
  .settings(
    name                                       := "Assignment-ShoppingBasket",
    assembly / mainClass                       := Some("PriceBasket"),
    assembly / assemblyJarName                 := "shopping-basket.jar",
    libraryDependencies += "org.scalatest"     %% "scalatest"       % "3.2.18"   % Test,
    libraryDependencies += "org.scalatestplus" %% "scalacheck-1-15" % "3.2.11.0" % Test,
  )
