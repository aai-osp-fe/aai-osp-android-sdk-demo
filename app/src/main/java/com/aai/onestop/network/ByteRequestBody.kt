package com.aai.onestop.network

import java.io.File

data class ByteRequestBody(
    val key: String,
    val value: String? = null,
    val bytes: ByteArray? = null,
    val fileName: String,
    val contentType: String,
    val file: File? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ByteRequestBody

        if (key != other.key) return false
        if (value != other.value) return false
        if (bytes != null) {
            if (other.bytes == null) return false
            if (!bytes.contentEquals(other.bytes)) return false
        } else if (other.bytes != null) return false
        if (fileName != other.fileName) return false
        if (contentType != other.contentType) return false
        if (file != other.file) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + (value?.hashCode() ?: 0)
        result = 31 * result + (bytes?.contentHashCode() ?: 0)
        result = 31 * result + fileName.hashCode()
        result = 31 * result + contentType.hashCode()
        result = 31 * result + (file?.hashCode() ?: 0)
        return result
    }
}