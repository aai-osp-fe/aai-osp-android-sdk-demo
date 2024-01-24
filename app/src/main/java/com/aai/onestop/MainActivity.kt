package com.aai.onestop

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aai.core.OSPOptions
import com.aai.core.OSPProcessCallback
import com.aai.core.OSPSdk
import com.aai.core.processManager.model.NodeCode
import com.aai.core.processManager.model.UrlConst
import com.aai.onestop.network.HeaderCallback
import com.aai.onestop.network.HttpUrlConnectionClient
import com.aai.onestop.network.NetRequest
import com.aai.onestop.network.NetWorkCallback
import com.aai.selfie.SelfieNode

class MainActivity : AppCompatActivity() {

    private lateinit var btnGetToken: Button
    private lateinit var etSdkToken: EditText
    private lateinit var btnStartFlow: Button
    private lateinit var btnClearToken: Button
    private lateinit var tvSdkToken: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        btnGetToken = findViewById(R.id.btnGetToken)
        tvSdkToken = findViewById(R.id.tvSdkToken)
        btnClearToken = findViewById(R.id.btnClearToken)
        etSdkToken = findViewById(R.id.etSdkToken)
        btnStartFlow = findViewById(R.id.btnStartFlow)
        btnGetToken.setOnClickListener {
            getSDKToken()
        }
        etSdkToken.setText("https://uat-oop-client.advai.net/intl/openapi/hostLink/start?accountSdkToken=970e6f7a-8ca6-40a9-a25c-a355cee7de3d")
        etSdkToken.setText("https://dev-oop-client.advai.cn/intl/openapi/hostLink/start?accountSdkToken=f4827c9e-5242-4d90-ae24-640db8236258")
        btnClearToken.setOnClickListener {
            etSdkToken.setText("")

        }


        btnStartFlow.setOnClickListener {
            val token = tvSdkToken.text.toString()

            OSPSdk.instance.init(
                OSPOptions(
                    context = MyApp.getInstance(),
                    sdkToken = token,
                    processCallback = object : OSPProcessCallback {
                        override fun onReady() {
                            println("processCallback: onReady")
                        }

                        override fun onExit(nodeCode: String) {
                            println("processCallback: onExit(nodeCode: $nodeCode)")
                        }

                        override fun onFinish(status: Boolean) {
                            println("processCallback: onFinish(status: $status)")
                        }

                        override fun onError(message: String?) {
                            println("processCallback: onError(message: $message)")
                        }

                        override fun onEvent(
                            eventName: String,
                            params: MutableMap<String, String>?
                        ) {
                            println("processCallback: onEvent(eventName: $eventName, params: $params)")
                        }
                    }
                )
            )
            if (token.isEmpty()) return@setOnClickListener
            OSPSdk.instance
                .registerNode(NodeCode.SELFIE, SelfieNode())
                .startFlow(this@MainActivity)
        }
    }

    private fun getSDKToken() {
        val url = etSdkToken.text.toString()
        if (url.isEmpty()) {
            Toast.makeText(this, "你得输入一个URL才行", Toast.LENGTH_SHORT).show()
            return
        }
        val baseUrlMap = mutableMapOf(
            "sandbox" to "https://sandbox",
            "uat" to "https://uat",
            "production" to "https://production",
            "pre" to "https://pre",
            "dev" to "https://dev",
            "sg" to "https://sg",
            "id" to "https://id"
        )
        for ((key, value) in baseUrlMap) {
            if (url.startsWith(value)) {
                UrlConst.currentEvn = key
                break
            }
        }
        val request = NetRequest(
            url = etSdkToken.text.toString(),
            method = "GET",
        )
        HttpUrlConnectionClient.instance.sendRequest(request,
            netWorkCallback = object : NetWorkCallback {
                override fun onSuccess(response: String) {
                    println("sdkToken: Success: $response")
                }

                override fun onError(code: String, message: String) {
                    println("sdkToken: error: $message")
                }

            },
            onHeaderCallback = object : HeaderCallback {
                override fun onGetHeaders(map: Map<String, List<String>>) {
                    map["Location"]?.get(0)?.let {
                        val sdkToken = Uri.parse(it).getQueryParameter("sdkToken")
                        println("sdkToken = $sdkToken")
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "yes", Toast.LENGTH_SHORT).show()
                            tvSdkToken.text = sdkToken
                        }
                    }
                }
            }
        )
    }

}