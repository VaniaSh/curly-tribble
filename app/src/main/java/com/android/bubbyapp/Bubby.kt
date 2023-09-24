package com.android.bubbyapp

import android.app.Application
import com.google.firebase.FirebaseApp

class Bubby : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
    }
}