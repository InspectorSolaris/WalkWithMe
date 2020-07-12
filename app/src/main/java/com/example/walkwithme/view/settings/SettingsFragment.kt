package com.example.walkwithme.view.settings

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.walkwithme.R
import com.example.walkwithme.adapter.CategoryAdapter
import com.example.walkwithme.presenter.settings.SettingsPresenter
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment(), SettingsViewInterface {

    private var settingsPresenter: SettingsPresenter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_settings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPresenter()
    }

    private fun setPresenter() {
        settingsPresenter = SettingsPresenter(
            this
        ).apply {
            setCategoryRecyclerView()
        }
    }

    override fun getDrawable(resource: Int): Drawable? {
        return ContextCompat.getDrawable(requireContext(), resource)
    }

    override fun configurateRecyclerView(adapter: CategoryAdapter) {
        CategoryRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        CategoryRecyclerView.adapter = adapter
    }

}