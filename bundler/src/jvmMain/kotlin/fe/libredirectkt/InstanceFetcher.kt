package fe.libredirectkt

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InstanceFetcher(
    private val client: HttpClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    private val rawGithubContentHost = Url("https://raw.githubusercontent.com")
    private val rawLibRedirectUrl = buildUrl {
        takeFrom(rawGithubContentHost)
        appendPathSegments("libredirect")
    }

    suspend fun fetchConfig(): String = withContext(dispatcher) {
        val response = client.get(url = buildUrl {
            takeFrom(rawLibRedirectUrl)
            appendPathSegments("libredirect", "master", "src", "config.json")
        })
        response.bodyAsText()
    }

    suspend fun fetchInstances(): String = withContext(dispatcher) {
        val response = client.get(url = buildUrl {
            takeFrom(rawLibRedirectUrl)
            appendPathSegments("instances", "main", "data.json")
        })
        response.bodyAsText()
    }
}
