package com.example.walkwithme.view.request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.walkwithme.R
import com.example.walkwithme.retrofit.helloworld.Endpoints
import com.example.walkwithme.retrofit.helloworld.HelloWorldCallback
import com.example.walkwithme.retrofit.ServiceBuilder
import kotlinx.android.synthetic.main.fragment_request.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestFragment : Fragment() {

    private val request = ServiceBuilder.buildService(Endpoints::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_request, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setListeners()
    }

    private fun setListeners() {
        RequestButtonGet.setOnClickListener { requestGet() }
        RequestButtonGetId.setOnClickListener { requestGetId() }
        RequestButtonPost.setOnClickListener { requestPost() }
        RequestButtonPut.setOnClickListener { requestPut() }
        RequestButtonDelete.setOnClickListener { requestDelete() }
    }

    private fun requestGet() {
        val call = request.getHelloWorld()

        call.enqueue(object : Callback<ArrayList<String>> {
            override fun onResponse(
                call: Call<ArrayList<String>>,
                response: Response<ArrayList<String>>
            ) {
                if (response.isSuccessful) {
                    var message = ""

                    for (word in response.body()!!) {
                        message = "$message $word"
                    }

                    val toast = Toast.makeText(
                        requireContext(),
                        message,
                        Toast.LENGTH_LONG
                    )
                    toast.setMargin(0f, 0.1f)
                    toast.show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Something went wrong...",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ArrayList<String>>, t: Throwable) {
                Toast.makeText(requireContext(), "${t.message}", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun requestGetId() {
        val call = request.getHelloWorldId(RequestDataId.text.toString().toInt())
        call.enqueue(
            HelloWorldCallback(
                requireContext()
            )
        )
    }

    private fun requestPost() {
        val call = request.postHelloWorld(RequestDataValue.text.toString())
        call.enqueue(
            HelloWorldCallback(
                requireContext()
            )
        )
    }

    private fun requestPut() {
        val call = request.putHelloWorld(
            RequestDataId.text.toString().toInt(),
            RequestDataValue.text.toString()
        )
        call.enqueue(
            HelloWorldCallback(
                requireContext()
            )
        )
    }

    private fun requestDelete() {
        val call = request.deleteHelloWorld(RequestDataId.text.toString().toInt())
        call.enqueue(
            HelloWorldCallback(
                requireContext()
            )
        )
    }

}