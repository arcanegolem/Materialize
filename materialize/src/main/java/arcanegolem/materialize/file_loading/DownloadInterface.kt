package arcanegolem.materialize.file_loading

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

internal interface DownloadInterface {
  @Streaming
  @GET
  suspend fun downloadFile(@Url url: String): ResponseBody
}

internal fun DownloadInterface(
  headers: HashMap<String,String>? = null
) = Retrofit.Builder().baseUrl("https://www.google.com").client(
  OkHttpClient.Builder().addInterceptor(DownloadInterceptor(headers)).build()
).build().create(DownloadInterface::class.java)