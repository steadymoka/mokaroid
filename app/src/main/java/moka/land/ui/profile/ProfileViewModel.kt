package moka.land.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.AboutMokaQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.MyRepositoriesQuery
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.exception.ApolloNetworkException
import kotlinx.coroutines.delay
import moka.land.modules.awaitEnqueue
import moka.land.util.NotNullMutableLiveData
import moka.land.util.addValues


typealias Profile = AboutMokaQuery.AsUser
typealias Pinned = AboutMokaQuery.AsRepository
typealias Organizer = AboutMokaQuery.Node2
typealias Repository = MyRepositoriesQuery.Node1

enum class Error {
    CONNECTION, SERVER, NOPE
}

class ProfileViewModel(
    private var apolloClient: ApolloClient) : ViewModel() {

    var loading = NotNullMutableLiveData(true)

    var error = MutableLiveData<Error>()

    var profile = MutableLiveData<Profile>()

    var pinnedList = MutableLiveData<List<Pinned>>()

    var organizerList = MutableLiveData<List<Organizer>>()

    var myRepositoryList = NotNullMutableLiveData(arrayListOf<Repository>())

    var selectedTab = NotNullMutableLiveData(Tab.Overview)

    private var endCursorOfMyRepositories: String? = null

    //

    suspend fun loadProfileData() {
        try {
            delay(1000) // fixme : for place holder check
            val query = AboutMokaQuery()
            val res = apolloClient.query(query).awaitEnqueue()

            profile.value = res
                .search()
                .edges()
                ?.getOrNull(0)
                ?.node() as? Profile ?: return

            pinnedList.value = (res
                .search()
                .edges()
                ?.getOrNull(0)
                ?.node() as Profile)
                .pinnedItems()
                .edges()
                ?.map { it.node() as Pinned }

            organizerList.value = (res
                .search()
                .edges()
                ?.getOrNull(0)
                ?.node() as Profile)
                .organizations()
                .nodes()
                ?.map { it }

            error.value = Error.NOPE
        }
        catch (e: ApolloNetworkException) {
            error.value = Error.CONNECTION
        }
        catch (e: ApolloException) {
            error.value = Error.SERVER
        }
        finally {
            loading.value = false
        }
    }

    suspend fun loadRepositories() {
        try {
            if (endCursorOfMyRepositories == "LAST") {
                return
            }

            loading.value = true
            val query = MyRepositoriesQuery(Input.optional(endCursorOfMyRepositories))

            val repositories = (apolloClient.query(query).awaitEnqueue()
                .search()
                .edges()
                ?.getOrNull(0)
                ?.node() as MyRepositoriesQuery.AsUser)
                .repositories()
                .apply {
                    endCursorOfMyRepositories = pageInfo().endCursor() ?: "LAST"
                }
                .edges()
                ?.map {
                    it.node() as Repository
                }

            loading.value = false
            myRepositoryList.addValues(repositories as ArrayList<Repository>)
            error.value = Error.NOPE
        }
        catch (e: ApolloNetworkException) {
            loading.value = false
            error.value = Error.CONNECTION
        }
        catch (e: ApolloException) {
            loading.value = false
            error.value = Error.SERVER
        }
    }

    suspend fun reloadRepositories() {
        endCursorOfMyRepositories = null
        loadRepositories()
    }

}
