package ru.skillbranch.skillarticles.data.remote.interceptors

import dagger.Lazy
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import ru.skillbranch.skillarticles.data.local.PrefManager
import ru.skillbranch.skillarticles.data.remote.RestService
import ru.skillbranch.skillarticles.data.remote.req.RefreshReq

//lazyApi инъектиться только при первом обращении
class TokenAuthenticator(val prefManager: PrefManager, val lazyApi: Lazy<RestService>) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {

        return if (response.code == 401) {

            val res = lazyApi.get().refreshAccessToken(RefreshReq(prefManager.refreshToken)).execute()
            return if (res.isSuccessful) {

                val bearer = "Bearer ${res.body()!!.accessToken}"

                prefManager.accessToken = bearer
                prefManager.refreshToken = res.body()!!.refreshToken
                response.request.newBuilder()
                    .header("Authorization", bearer)
                    .build()

            } else null

        } else null
    }
}