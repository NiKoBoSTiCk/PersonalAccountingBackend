package sdcc.accounting.dto

import java.io.File

data class DocumentDto(
        val id: Int?,
        val filename: String,
        val amount: Int,
        val description: String,
        val year: Int,
        val tag: String
)

data class DocumentFileDto(
        val filename: String,
        val file: File
)
