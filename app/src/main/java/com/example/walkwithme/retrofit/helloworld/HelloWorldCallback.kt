package com.example.walkwithme.retrofit.helloworld

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HelloWorldCallback(private val context: Context) : Callback<HelloWorldResponse> {
    override fun onResponse(
        call: Call<HelloWorldResponse>,
        response: Response<HelloWorldResponse>
    ) {
        if (response.isSuccessful) {
            val toast = Toast.makeText(
                context,
                response.body()!!.value,
                Toast.LENGTH_LONG
            )
            toast.setMargin(0f, 0.1f)
            toast.show()
        } else {
            Toast.makeText(
                context,
                "Something went wrong...",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onFailure(call: Call<HelloWorldResponse>, t: Throwable) {
        Toast.makeText(context, "${t.message}", Toast.LENGTH_LONG)
            .show()
    }
}