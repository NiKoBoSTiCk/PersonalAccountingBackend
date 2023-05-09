package sdcc.accounting.payload.request

import org.jetbrains.annotations.NotNull

data class UserRequest(

    @NotNull
    var name: String,

    @NotNull
    var surname: String,

    @NotNull
    var email: String
)
