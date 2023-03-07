package com.dpicallo.tealiumcollectapp


import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.webkit.WebView
import androidx.test.core.app.ApplicationProvider
import com.dpicallo.tealiumcollectapp.databinding.FragmentSecondBinding

import com.tealium.core.*
import com.tealium.core.collection.*
import com.tealium.dispatcher.TealiumEvent
import com.tealium.dispatcher.TealiumView
import com.tealium.lifecycle.Lifecycle
import com.tealium.remotecommanddispatcher.RemoteCommands
import com.tealium.remotecommanddispatcher.remoteCommands
import com.tealium.remotecommands.RemoteCommand
import com.tealium.tagmanagementdispatcher.TagManagement


object TealiumHelper {
    private const val TAG = "TealiumHelper"
    const val TEALIUM_MAIN = "main"



    //var editor: SharedPreferences.Editor = pref.edit()

    /*val context =  ApplicationProvider.getApplicationContext<ApplicationHelper>()
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    sharedPreferences.edit().putBoolean("useOnlyOnWifi", false).commit();*/


    @JvmStatic
    fun initialize(application: Application) {
        WebView.setWebContentsDebuggingEnabled(true)

        var pref: SharedPreferences = application.applicationContext.getSharedPreferences("MODE_PRIVATE", 0) // 0 - for private mode


        val config = TealiumConfig(application,
            "accountName",
            "profileName",
            Environment.DEV).apply {
            //useRemoteLibrarySettings = true

            //MODULES
            modules.add(Modules.Lifecycle)
            //modules.add(Modules.VisitorService)

            //DISPATCHERS
            //dispatchers.add(Dispatchers.Collect)
            dispatchers.add(Dispatchers.RemoteCommands)
            dispatchers.add(Dispatchers.TagManagement)

            //COLLECTORS
            collectors.add(Collectors.Tealium)
            collectors.add(Collectors.Time)
            collectors.add(Collectors.App)
            collectors.add(Collectors.Device)
            collectors.add(Collectors.Connectivity)


        }


        //val firebaseRemoteCommand = FirebaseRemoteCommand(application)
        //config.overrideCollectUrl = "https://collect.tealiumiq.com/event"


        val webViewRemoteCommand = object : RemoteCommand("remoteCommand", "testing RCs") {
            override fun onInvoke(response: Response) {
                //Logger.dev("TealiumHelper", "ResponsePayload for webView RemoteCommand ${response.requestPayload}")
                val editor = pref.edit()
                editor.putString("adobeECID", response.requestPayload.getString("adobeEcid")); // Storing string
                editor.apply()


            }


        }
        Tealium.create(TEALIUM_MAIN, config) {
            // Remote Command Tag - requires TiQ
            //remoteCommands?.add(firebaseRemoteCommand)
            // JSON Remote Command - requires filename
            //remoteCommands?.add(firebaseRemoteCommand, "firebase.json")
            remoteCommands?.add(webViewRemoteCommand)
            //VISITOR MODULE
            /*events.subscribe(object : VisitorUpdatedListener {
                override fun onVisitorUpdated(visitorProfile: VisitorProfile) {
                    Logger.dev("--", "did update vp with $visitorProfile")
                }
            })*/
        }
    }

    @JvmStatic
    fun trackView(viewName: String, data: Map<String, Any>? = null) {
        val instance: Tealium? = Tealium[TEALIUM_MAIN]

        // Instance can be remotely destroyed through publish settings
        instance?.track(TealiumView(viewName, data))
    }

    @JvmStatic
    fun trackEvent(eventName: String, data: Map<String, Any>? = null) {
        val instance: Tealium? = Tealium[TEALIUM_MAIN]

        // Instance can be remotely destroyed through publish settings
        instance?.track(TealiumEvent(eventName, data))

    }

    @JvmStatic
    fun trackScreen(activity: Activity, screenName: String) {
        trackView("screen_view",
            mapOf(
                DataLayer.SCREEN_NAME to screenName,
                DataLayer.SCREEN_CLASS to activity.javaClass.simpleName))
    }
}