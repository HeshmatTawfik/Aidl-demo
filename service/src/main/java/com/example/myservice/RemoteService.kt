package com.example.myservice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Process
import android.util.Log

class RemoteService : Service(){
    private  val binder = object: IMyAidlInterface.Stub(){
        override fun getPid(): Int = Process.myPid()

        override fun sendMessage(msg: String?) {
            Log.i("RemoteService", "sendMessage: $msg")
        }

    }
    override fun onCreate() {
        super.onCreate()
    }
    override fun onBind(p0: Intent?): IBinder {
        return binder
    }
}