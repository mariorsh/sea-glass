package io.github.mariorsh.seaglass.sample
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
fun main() = application { Window(onCloseRequest = ::exitApplication, title = "Sea Glass 0.2.0") { SampleApp() } }
