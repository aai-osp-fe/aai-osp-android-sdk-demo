package com.aai.onestop.network

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.security.SecureRandom
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager

class HttpUrlConnectionClient private constructor() : NetWorkClient {

    private val tag = "HttpUrlConnectionClient: "

    private val mainThreadExecutor = MainThreadExecutor()

    private val executorService = Executors.newCachedThreadPool()

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            HttpUrlConnectionClient()
        }
    }

    override fun sendRequest(
        request: NetRequest,
        netWorkCallback: NetWorkCallback,
        onHeaderCallback: HeaderCallback?
    ) {
        executorService.submit {
            var connection: HttpURLConnection? = null
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, arrayOf<TrustManager>(IgnoreSSLTrust()), SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
            try {
                HttpURLConnection.setFollowRedirects(false)
                val url = URL(request.url)
                connection = url.openConnection() as HttpURLConnection
                connection.instanceFollowRedirects = false

                // set method
                connection.requestMethod = request.method

                // set headers
                connection.setRequestProperty("Accept", "application/json")
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("Accept-Encoding", "UTF-8")
                request.headers?.let {
                    for ((key, value) in it) {
                        connection.setRequestProperty(key, value.toString())
                    }
                }

                connection.doInput = true
                if (request.method == NetMethod.POST) {
                    request.requestBody?.let {
                        WriteRequestBody().write(connection, it)
                    }
                }

                // 获取响应
                val responseCode = connection.responseCode
                handleHeaders(connection.headerFields, onHeaderCallback)
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    reader.close()
                    val responseData = response.toString()

                    mainThreadExecutor.execute {
                        netWorkCallback.onSuccess(responseData)
                    }
                } else {
                    val errorMessage = "HTTP Error: $responseCode"
                    mainThreadExecutor.execute {
                        netWorkCallback.onError(responseCode.toString(), errorMessage)
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                mainThreadExecutor.execute {
                    netWorkCallback.onError("UN_KNOWN", "Network Error: ${e.message}")
                }
            } finally {
                connection?.disconnect()
            }
        }
    }

    private fun handleHeaders(map: Map<String, List<String>>, onHeaderCallback: HeaderCallback?) {
        mainThreadExecutor.execute {
            onHeaderCallback?.onGetHeaders(map)
        }
    }

}
