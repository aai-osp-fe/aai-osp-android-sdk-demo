package com.aai.onestop.network

interface HeaderCallback {

    fun onGetHeaders(map: Map<String,List<String>>)

}