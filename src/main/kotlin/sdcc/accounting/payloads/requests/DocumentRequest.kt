package sdcc.accounting.payloads.requests

import org.jetbrains.annotations.NotNull
import java.sql.Blob
import java.time.Year

data class DocumentRequest(
    @NotNull
    private val id: Int,
    @NotNull
    private val tag: String,
    @NotNull
    private val amount: Int,
    @NotNull
    private val file: Blob,
    @NotNull
    private val year: Year,
    private val description: String
) {
    fun getId() = id
    fun getTag() = tag
    fun getAmount() = amount
    fun getFile() = file
    fun getYear() = year
    fun getDescription() = description
}