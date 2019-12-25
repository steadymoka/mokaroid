package moka.land.ui.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollographql.apollo.AboutMokaQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.GetRepositoryQuery
import com.apollographql.apollo.MyRepositoriesQuery
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.exception.ApolloHttpException
import com.apollographql.apollo.exception.ApolloNetworkException
import kotlinx.coroutines.delay
import moka.land.base.log
import moka.land.modules.awaitEnqueue
import moka.land.util.NotNullMutableLiveData

typealias Profile = GetRepositoryQuery.AsUser
typealias Repository = GetRepositoryQuery.Repository

enum class Error {
    CONNECTION, SERVER, NOPE
}

class RepositoryViewModel(
    private var apolloClient: ApolloClient) : ViewModel() {

    var loading = NotNullMutableLiveData(true)

    var error = MutableLiveData<Error>()

    var repository = MutableLiveData<Repository>()

    //

    suspend fun loadRepository(name: String) {
        try {
            loading.value = true

            log("name: ${name}")
            val query = GetRepositoryQuery(name)
            repository.value = (apolloClient.query(query).awaitEnqueue()
                .search()
                .edges()
                ?.getOrNull(0)
                ?.node() as Profile)
                .repository()

            error.value = Error.NOPE
        }
        catch (e: ApolloNetworkException) {
            error.value = Error.CONNECTION
        }
        catch (e: ApolloHttpException) {
            error.value = Error.SERVER
        }
        finally {
            loading.value = false
        }
    }

}
