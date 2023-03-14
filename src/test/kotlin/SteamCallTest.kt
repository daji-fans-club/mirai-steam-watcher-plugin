import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlin.test.Ignore
import kotlin.test.Test
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.example.mirai.plugin.GetPlayerSummariesResponse
import org.example.mirai.plugin.SteamCall

/**
 * @author reimia
 */
internal class SteamCallTest {
    private val steamApiKey = "your-steam-key"

    @OptIn(ExperimentalSerializationApi::class)
    @Ignore
    @Test
    fun steamRequest() {
        val client = HttpClient()
        val url =
            "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/"
        val s: String = runBlocking {
            client.get(url) {
                parameter("key", steamApiKey)
                parameter("steamids", "[76561198212300964, 76561198871064283]")
            }
                .body()
        }
        println(s)
        val json = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
        val steamResponse = json.decodeFromString<GetPlayerSummariesResponse>(s)
        println(steamResponse)
    }

    @Ignore
    @Test
    fun steamRequest2() {
        val list = listOf(76561198212300964, 76561198871064283)
        val response =
            runBlocking { SteamCall.getPlayerSummaries(list) }
        println(response)
    }
}