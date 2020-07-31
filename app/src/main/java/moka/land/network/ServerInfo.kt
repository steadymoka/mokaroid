package moka.land.network

object ServerInfo {

    private const val END_POINT = "https://api.github.com/graphql"

    val endPoint: String
        get() {
            return END_POINT
        }

}