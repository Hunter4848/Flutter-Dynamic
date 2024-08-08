package everspin.id.flutterdynamic

import android.app.Application
import kr.co.everspin.eversafe.EversafeHelper

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        /*val additionalInfo: MutableMap<String, Any> = HashMap()
        additionalInfo["serverPublicKeyHashes"] = arrayOf("GA/yTHehfqs/IC17YLaBXtmq1AUW1W5xNiqahH69KwU=")
        EversafeHelper.getInstance().initialize("https://appsect.eversafe.id/eversafe", "35891CAC31EC7886", additionalInfo)*/

        //VPS
        EversafeHelper.getInstance().initialize("http://103.96.146.239:4443/eversafe", "AD40329BEA550DE3", null)
    }
}