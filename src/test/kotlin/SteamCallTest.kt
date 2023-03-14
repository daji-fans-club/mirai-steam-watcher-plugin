import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlin.test.Test
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.example.mirai.plugin.SteamCall
import org.example.mirai.plugin.SteamResponse

/**
 * @author reimia
 */
internal class SteamCallTest {
    val steamApiKey = "your-steam-key"
    private val steamCall = SteamCall(HttpClient())

    @OptIn(ExperimentalSerializationApi::class)
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
        val steamResponse = json.decodeFromString<SteamResponse>(s)
        println(steamResponse)
    }

    @Test
    fun steamRequest2() {
        val list = listOf(76561198212300964, 76561198871064283)
        val response =
            runBlocking { steamCall.steamRequest(list) }
        println(response)
    }
}