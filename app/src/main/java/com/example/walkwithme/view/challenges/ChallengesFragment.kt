package com.example.walkwithme.view.challenges

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.walkwithme.R

class ChallengesFragment : Fragment(), ChallengesViewInterface {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_challenges, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {}

}