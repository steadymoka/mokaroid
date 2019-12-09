package moka.land.modules

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloMutationCall
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.api.cache.http.HttpCachePolicy.NETWORK_ONLY
import com.apollographql.apollo.exception.ApolloException
import com.facebook.stetho.okhttp3.StethoInterceptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import moka.land.BuildConfig
import moka.land.component.AuthManager
import moka.land.network.ServerInfo
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.module
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val CONNECT_TIMEOUT = 15L
private const val WRITE_TIMEOUT = 15L
private const val READ_TIMEOUT = 15L

val networkModule = module {

    single { Cache(androidApplication().cacheDir, 10L * 1024 * 1024) }

    single {
        Interceptor { chain ->
            val userAgent = System.getProperty("http.agent") + " std-android/${BuildConfig.VERSION_CODE}}"
            val apiKey = "Bearer ${AuthManager.apiKey}"

            chain
                .proceed(
                    chain
                        .request()
                        .newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("User-Agent", userAgent)
                        .addHeader("Authorization", apiKey)
                        .build()
                )
        }
    }

    single {
        OkHttpClient.Builder().apply {
            cache(get())
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            addInterceptor(get() as Interceptor)

            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                addNetworkInterceptor(StethoInterceptor())
            }
        }.build()
    }

    single {
        ApolloClient
            .builder()
            .serverUrl(ServerInfo.endPoint)
            .okHttpClient(get())
            .build()
    }

}

suspend fun <T> ApolloQueryCall<T>.awaitEnqueue(): T {
    return suspendCoroutine { continuation ->
        this
            .httpCachePolicy(NETWORK_ONLY)
            .enqueue(object : ApolloCall.Callback<T>() {

                override fun onFailure(e: ApolloException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(response: Response<T>) {
                    if (response.errors().isEmpty()) {
                        response.data()
                            ?.run {
                                continuation.resume(this)
                            }
                            ?: run {
                                continuation.resumeWithException(ApolloException("something wrong"))
                            }
                    }
                    else {
                        continuation.resumeWithException(
                            ApolloException(
                                response.errors()[0].message() ?: "something wrong"
                            )
                        )
                    }
                }
            })
    }
}

suspend fun <T> ApolloMutationCall<T>.awaitEnqueue(): T {
    return suspendCoroutine { continuation ->
        this
            .enqueue(object : ApolloCall.Callback<T>() {

                override fun onFailure(e: ApolloException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(response: Response<T>) {
                    if (response.errors().isEmpty()) {
                        response.data()
                            ?.run {
                                continuation.resume(this)
                            }
                            ?: run {
                                continuation.resumeWithException(ApolloException("something wrong"))
                            }
                    }
                    else {
                        continuation.resumeWithException(
                            ApolloException(
                                response.errors()[0].message() ?: "something wrong"
                            )
                        )
                    }
                }
            })
    }
}
