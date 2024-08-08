package everspin.id.flutterdynamic

import android.util.Log
import everspin.id.flutterdynamic.model.MEncryptedRequest
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import kr.co.everspin.eversafe.EversafeHelper
import kr.co.everspin.eversafe.components.base64.Base64
import org.json.JSONObject

class MainActivity : FlutterActivity() {
    private val CHANNEL = "everspin.id.flutterdynamic/channel"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
            .setMethodCallHandler { call, result ->
                if (call.method == "nativeMethod") {
                    val username = call.argument<String>("username")
                    val password = call.argument<String>("password")
                    nativeMethod(username!!, password!!, result)
                } else {
                    result.notImplemented()
                }
            }
    }

    private fun nativeMethod(username: String, password: String, result: MethodChannel.Result) {
        object : EversafeHelper.GetVerificationTokenTask() {
            override fun onAction(p0: ByteArray?, verificationTokenAsBase64: String?, p2: Int) {
                Log.d(
                    "verification",
                    "verificationTokenAsBase64: $verificationTokenAsBase64"
                )
                EversafeHelper.getInstance().getEncryptionContext(200) { encryptionContext ->
                    val json = JSONObject().apply {
                        put("user_name", username)
                        put("user_password", password)
                    }
                    val bytes = json.toString().toByteArray()

                    val encryptedRequest = MEncryptedRequest(
                        payload = Base64.encodeBase64String(encryptionContext.encrypt(bytes)),
                        evToken = Base64.encodeBase64String(
                            encryptionContext.encrypt(encryptionContext.verificationToken)
                        ),
                        evEncDesc = Base64.encodeBase64String(encryptionContext.contextDescriptor)
                    )

                    // Kirimkan encryptedRequest ke Flutter
                    val response = mapOf(
                        "payload" to encryptedRequest.payload,
                        "evToken" to encryptedRequest.evToken,
                        "evEncDesc" to encryptedRequest.evEncDesc
                    )

                    Log.d("NativeMethod", "Sending response to Flutter: $response")
                    result.success(response)
                }
            }
        }.setTimeout(100000).execute()
    }

}
