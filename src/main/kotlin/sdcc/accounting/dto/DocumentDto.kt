package sdcc.accounting.dto

import java.io.InputStream

data class DocumentDto(
        val id: Int?,
        val filename: String,
        val amount: Float,
        val description: String,
        val year: Int,
        val tag: String
)

data class DocumentFileDto(
    val filename: String,
    val size: Long,
    val file: InputStream
)
