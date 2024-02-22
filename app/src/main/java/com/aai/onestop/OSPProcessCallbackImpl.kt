package com.aai.onestop

import android.content.Context
import android.widget.Toast
import com.aai.core.sdk.OSPProcessCallback

class OSPProcessCallbackImpl(private val context: Context): OSPProcessCallback {
    override fun onError(errorCode: String?, transId: String) {

    }

    override fun onEvent(
        transId: String,
        eventName: String,
        params: MutableMap<String, String>?
    ) {

    }

    override fun onExit(nodeCode: String, transId: String) {
        Toast.makeText(context, "exit", Toast.LENGTH_SHORT).show()
    }

    override fun onFinish(status: Boolean, transId: String) {
        if (status) {
            Toast.makeText(
                context,
                "You have completed this process",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onReady() {

    }

}