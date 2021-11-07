package tk.atna.shortlyapp.data.datasource.api

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import okhttp3.Request
import okio.Timeout
import org.koin.core.component.KoinComponent
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import tk.atna.shortlyapp.data.model.ServerResponse
import tk.atna.shortlyapp.domain.model.ApiException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ApiExceptionCallAdapterFactory(private val gson: Gson) : CallAdapter.Factory(), KoinComponent {

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        val rawType = getRawType(returnType)
        if (rawType != Call::class.java) return null
        val callType = getParameterUpperBound(0, returnType as ParameterizedType)
        return ExceptionAdapter(gson, callType)
    }

    private class ExceptionAdapter(
        private val gson: Gson,
        private val type: Type
    ) : CallAdapter<Type, Call<Type>> {

        override fun responseType() = type

        override fun adapt(call: Call<Type>) = ExceptionCall(gson, call)
    }

    private class ExceptionCall<T>(
        private val gson: Gson,
        realCall: Call<T>
    ) : CallProxy<T>(realCall) {

        override fun enqueue(callback: Callback<T>) {
            realCall.enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    callback.onFailure(call, t)
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    if (response.isSuccessful) {
                        callback.onResponse(call, response)
                    } else {
                        val exception = createApiException(response)
                        if (exception == null) {
                            callback.onResponse(call, response)
                        } else {
                            callback.onFailure(call, exception)
                        }
                    }
                }
            })
        }

        private fun createApiException(response: Response<*>): ApiException? {
            return try {
                val body = response.errorBody() ?: return null
                val bean: ServerResponse<*>? = gson.fromJson(body.string(), ServerResponse::class.java)
                if (bean?.ok == false) ApiException(bean.errorMessage, response) else null
            } catch (ignored: JsonSyntaxException) {
                null
            } catch (ignored: IllegalArgumentException) {
                null
            }
        }

        override fun execute(): Response<T> {
            val response = super.execute()
            if (response.isSuccessful) return response
            val exception = createApiException(response)
            if (exception != null) throw exception
            return response
        }

        override fun clone() = ExceptionCall(gson, realCall.clone())
    }

    private abstract class CallProxy<T>(protected val realCall: Call<T>) : Call<T> {
        override fun enqueue(callback: Callback<T>) = realCall.enqueue(callback)
        override fun isExecuted() = realCall.isExecuted
        override fun isCanceled() = realCall.isCanceled
        override fun cancel() = realCall.cancel()
        override fun execute(): Response<T> = realCall.execute()
        override fun request(): Request = realCall.request()
        override fun clone(): Call<T> = realCall.clone()
        override fun timeout(): Timeout = realCall.timeout()
    }
}