package com.aai.onestop

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aai.core.processManager.model.NodeCode
import com.aai.core.processManager.model.OSPEnvironment
import com.aai.core.sdk.OSPOptions
import com.aai.core.sdk.OSPProcessCallback
import com.aai.core.sdk.OSPSdk
import com.aai.onestop.network.HeaderCallback
import com.aai.onestop.network.HttpUrlConnectionClient
import com.aai.onestop.network.NetRequest
import com.aai.onestop.network.NetWorkCallback
import com.aai.selfie.SelfieNode
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var btnGetToken: Button
    private lateinit var btnStartFlow: Button
    private lateinit var tvSdkToken: TextView
    private lateinit var etKey: EditText
    private lateinit var etJourneyId: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        btnGetToken = findViewById(R.id.btnGetToken)
        tvSdkToken = findViewById(R.id.tvSdkToken)
        btnStartFlow = findViewById(R.id.btnStartFlow)
        etKey = findViewById(R.id.etKey)
        etJourneyId = findViewById(R.id.etJourneyId)
        btnGetToken.setOnClickListener {
            getSDKToken()
        }

        btnStartFlow.setOnClickListener {
            val token = tvSdkToken.text.toString()
            if (token.isEmpty()) {
                Toast.makeText(this@MainActivity, "sdkToken is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            OSPSdk.instance.init(
                OSPOptions(
                    context = MyApp.getInstance(),
                    sdkToken = token,
                )
            )
                .environment(OSPEnvironment.SANDBOX)
                .registerNode(NodeCode.SELFIE, SelfieNode())
                .registerCallback(object : OSPProcessCallback {
                    override fun onError(errorCode: String?, transId: String) {

                    }

                    override fun onEvent(
                        transId: String,
                        eventName: String,
                        params: MutableMap<String, String>?
                    ) {

                    }

                    override fun onExit(nodeCode: String, transId: String) {
                        Toast.makeText(this@MainActivity, "exit", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFinish(status: Boolean, transId: String) {
                        if (status) {
                            Toast.makeText(
                                this@MainActivity,
                                "You have completed this process",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onReady() {

                    }

                })
                .startFlow(this@MainActivity)
        }
    }

    private fun getSDKToken() {
        val apiKey = etKey.text.toString()
        val id = etJourneyId.text.toString()
        if (apiKey.isEmpty() || id.isEmpty()) {
            Toast.makeText(this, "Please enter a valid value", Toast.LENGTH_SHORT).show()
            return
        }
        val request = NetRequest(
            url = "https://sandbox-oop-api.advai.net/intl/openapi/sdk/v2/trans/start",
            method = "POST",
            headers = mutableMapOf(
                "X-ADVAI-KEY" to apiKey,
                "journeyId" to id
            )
        )
        HttpUrlConnectionClient.instance.sendRequest(
            request,
            netWorkCallback = object : NetWorkCallback {
                override fun onSuccess(response: String) {
                    val json = JSONObject(response)
                    if (json.has("data")) {
                        val jsonData = json.getJSONObject("data")
                        if (jsonData.has("sdkToken")) {
                            val sdkToken = jsonData.getString("sdkToken")
                            tvSdkToken.text = sdkToken
                        }
                    }
                }

                override fun onError(code: String, message: String) {
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                }
            },
        )
    }

}