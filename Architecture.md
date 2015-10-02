# Model View Controller #

The application is centered on a model of the compass, called the CompassModel. This model does have some (idempotent) methods to extract the data in different ways (computing the yaw and strength, for example), but otherwise it is docile and observable.

The main activity sets up three views:
  * The OpenGL view
  * A text view for showing coordinates
  * A vibrator view, which vibrates when you align the phone to the field

The activity also sets up a controller whose job is to periodically sample the magnetometer and update the model.

> `  controller -> model -> view(s) `

# UI Thread #

Because updates to the model notify the view listeners in the same thread (using java.util.Observer), it is important that they run in the UI thread. I found that both the SensorManager and an android.os.Timer generated periodic callbacks in this thread. (The android.os.Timer was needed for a synthetic, test field source).

# OpenGL sink #

The OpenGL surface view has its own desynchronized update rate. Therefore it doesn't need to listen for changes to the model: a separate 'renderer' object is run periodically, and it polls the model.

# View adapter #

In order to display some textual information, I chose not to subclass TextView, but instead to write an adapter. The adapter listened to model events and then updated the text view directly.