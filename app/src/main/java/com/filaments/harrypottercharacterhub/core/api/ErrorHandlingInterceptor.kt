package com.filaments.harrypottercharacterhub.core.api

import com.filaments.harrypottercharacterhub.utils.StringProvider
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException
import retrofit2.HttpException

/**
 * Created by prasildas
 */
class ErrorHandlingInterceptor(private val stringProvider: StringProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            val response = chain.proceed(chain.request())
            if (!response.isSuccessful) {
                val errorMessage =  stringProvider.serverError()
                throw CustomAppException.IOError(errorMessage, response.code)
            }
            response
        }catch (e: IOException) {
            throw CustomAppException.IOError(stringProvider.networkError(), null)
        } catch (e: HttpException) {
            throw CustomAppException.ServerError(stringProvider.serverError())
        } catch (e: Exception) {
            throw CustomAppException.UnknownError(stringProvider.unknownError())
        }
    }
}
