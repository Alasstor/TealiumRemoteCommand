package com.dpicallo.tealiumcollectapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider

/**
 * Application Helper.
 */
class ApplicationHelper : Application() {


    override fun onCreate() {
        super.onCreate()
        TealiumHelper.initialize(this);

    }
}