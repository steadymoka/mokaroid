package moka.land.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollographql.apollo.AboutMokaQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.MyRepositoriesQuery
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.exception.ApolloHttpException
import com.apollographql.apollo.exception.ApolloNetworkException
import moka.land.modules.awaitEnqueue
import moka.land.util.NotNullMutableLiveData

typealias Profile = AboutMokaQuery.AsUser
typealias Pinned = AboutMokaQuery.AsRepository
typealias Repository = MyRepositoriesQuery.Node1

class ProfileViewModel(
    private var apolloClient: ApolloClient) : ViewModel() {

    var loading = MutableLiveData<Boolean>()

    var profile = MutableLiveData<Profile>()

    var pinnedRepository = MutableLiveData<List<Pinned>>()

    var myRepository = NotNullMutableLiveData(arrayListOf<Repository>())

    var selectedTab = NotNullMutableLiveData(Tab.Overview)

    private var endCursorOfMyRepositories: String? = null

    //

    suspend fun loadProfileData() {
        try {
            loading.value = true

            val query = AboutMokaQuery()

            profile.value = apolloClient.query(query).awaitEnqueue()
                .search()
                .edges()
                ?.getOrNull(0)
                ?.node() as? Profile ?: return

            pinnedRepository.value = (apolloClient.query(query).awaitEnqueue()
                .search()
                .edges()
                ?.getOrNull(0)
                ?.node() as Profile)
                .pinnedItems()
                .edges()
                ?.map { it.node() as Pinned }
        }
        catch (e: ApolloNetworkException) {
        }
        catch (e: ApolloHttpException) {
        }
        finally {
            loading.value = false
        }
    }

    suspend fun loadRepositories() {
        try {
            loading.value = true

            val query = MyRepositoriesQuery(Input.optional(endCursorOfMyRepositories))

            val repositories = (apolloClient.query(query).awaitEnqueue()
                .search()
                .edges()
                ?.getOrNull(0)
                ?.node() as MyRepositoriesQuery.AsUser)
                .repositories()
                .apply {
                    endCursorOfMyRepositories = pageInfo().endCursor()
                }
                .edges()
                ?.map {
                    it.node() as Repository
                }

            val loadedRepositories = arrayListOf<Repository>()
            loadedRepositories.addAll(myRepository.value)

            if (null != repositories) {
                loadedRepositories.addAll(repositories)
            }
            myRepository.value = loadedRepositories
        }
        catch (e: ApolloNetworkException) {
        }
        catch (e: ApolloHttpException) {
        }
        finally {
            loading.value = false
        }
    }

    suspend fun reloadRepositories() {
        endCursorOfMyRepositories = null
        myRepository.value.clear()
        loadRepositories()
    }

}
