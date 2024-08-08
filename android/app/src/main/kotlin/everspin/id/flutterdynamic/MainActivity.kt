package everspin.id.flutterdynamic

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity: FlutterActivity() {
    private val CHANNEL = "everspin.id.flutterdynamic/channel"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
            .setMethodCallHandler { call, result ->
                if (call.method == "nativeMethod") {
                    val response = nativeMethod()
                    result.success(response)
                } else {
                    result.notImplemented()
                }
            }
    }

    private fun nativeMethod(): String {
        // Tambahkan logika native Kotlin Anda di sini
        return "Hello from native Android!"
    }
}
