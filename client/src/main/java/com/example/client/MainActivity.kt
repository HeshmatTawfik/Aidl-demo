package com.example.client

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import com.example.client.databinding.ActivityMainBinding
import com.example.myservice.IMyAidlInterface

class MainActivity : AppCompatActivity(), ServiceConnection {
    private val tag = MainActivity::class.java.simpleName
    private var isConnected = false
   private lateinit var binding: ActivityMainBinding
    private var remoteService: IMyAidlInterface? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.connectBt.setOnClickListener {
            if (isConnected){
                disconnectFromService()
            }
            else {
                connectToRemoteService()
            }
        }

        binding.sendBt.setOnClickListener{
            sendMessage()
        }
    }

    private fun sendMessage() {
        val msg = binding.msgEditText.text.toString()
        val id = android.os.Process.myPid()
        remoteService?.sendMessage("$id: $msg")
        binding.msgEditText.setText("")
    }

    private fun connectToRemoteService() {
        val intent = Intent("aidldemo")
        val pack = IMyAidlInterface::class.java.`package`
        pack?.let {
            intent.setPackage(pack.name)
            this.applicationContext?.bindService(
                intent, this, Context.BIND_AUTO_CREATE
            )
        }
    }

    private fun disconnectFromService(){
        applicationContext.unbindService(this)
        isConnected = false
        remoteService = null
        Log.i(tag, "disconnecting from service")
        binding.textView.text = getString(R.string.disconnected)
        binding.connectBt.text = getString(R.string.connect)
        binding.msgEditText.visibility = View.GONE
        binding.sendBt.visibility = View.GONE

    }

    override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
        isConnected = true
        Log.i(tag, "onServiceConnected: connected to the service")
        remoteService = IMyAidlInterface.Stub.asInterface(binder)
        binding.textView.text = getString(R.string.service_pid,remoteService?.pid)
        binding.connectBt.text = getString(R.string.disconnect)
        binding.msgEditText.visibility = View.VISIBLE
        binding.sendBt.visibility = View.VISIBLE
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        disconnectFromService()
    }
}