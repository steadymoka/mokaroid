package moka.land.network

import moka.land.BuildConfig

object ServerInfo {

    private const val END_DEV_POINT = "https://api.github.com/graphql"
    private const val END_PROD_POINT = "https://api.github.com/graphql"

    val endPoint: String
        get() {
            if (BuildConfig.FLAVOR == "dev") {
                return END_DEV_POINT
            }
            else {
                return END_PROD_POINT
            }
        }

}