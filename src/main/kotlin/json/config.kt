package json

data class config(
    val firebase: Firebase,
    val keys: Keys,
    val token: String,
    val prefix: String
)