# JUnit in Eclipse #

If you try to run the unit tests from Eclipse IDE you will see this kind of error:
> `Error: ShouldNotReachHere()`
It means you need to edit the Run Configuration and in the Classpath tab, remove the Bootstrap Entries, namely Android2.x.

# Wat is tested #

Included in the unit tests are
  * Testing the Compass Model determines the correct yaw and pitch for different vectors.