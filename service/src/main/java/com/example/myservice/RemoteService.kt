package com.example.myservice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Process
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class RemoteService : Service(){
    companion object{
        const val action = "MessageReceived"
        const val message = "message"

    }
    private  val binder = object: IMyAidlInterface.Stub(){
        override fun getPid(): Int = Process.myPid()

        override fun sendMessage(msg: String?) {
            messageReceived(msg)
        }

    }

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }

    private fun messageReceived(msg:String?){
        val intent = Intent(action)
        intent.putExtra(message, msg)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
}