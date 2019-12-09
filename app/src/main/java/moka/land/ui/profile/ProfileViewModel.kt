package moka.land.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollographql.apollo.AboutMokaQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.exception.ApolloHttpException
import com.apollographql.apollo.exception.ApolloNetworkException
import moka.land.modules.awaitEnqueue

typealias Profile = AboutMokaQuery.AsUser
typealias Repository = AboutMokaQuery.AsRepository

class ProfileViewModel(
    private var apolloClient: ApolloClient) : ViewModel() {

    var loading = MutableLiveData<Boolean>()
    var profile = MutableLiveData<Profile>()
    var pinnedRepository = MutableLiveData<List<Repository>>()

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
                ?.map { it.node() as Repository }
        }
        catch (e: ApolloNetworkException) {
        }
        catch (e: ApolloHttpException) {
        }
        finally {
            loading.value = false
        }
    }

    suspend fun loadPinnedRepository() {

    }

}
