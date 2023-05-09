package sdcc.accounting.payload.request

import org.jetbrains.annotations.NotNull
import java.sql.Blob
import java.time.Year

data class DocumentRequest(

    @NotNull
    var id: Int,

    @NotNull
    var email: String,

    @NotNull
    var tag: String,

    @NotNull
    var amount: Int,

    @NotNull
    var file: Blob,

    @NotNull
    var year: Year,

    var description: String
)