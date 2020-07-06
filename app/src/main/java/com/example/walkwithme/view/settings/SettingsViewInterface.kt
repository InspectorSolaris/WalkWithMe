package com.example.walkwithme.view.settings

import android.graphics.drawable.Drawable
import com.example.walkwithme.adapter.CategoryAdapter

interface SettingsViewInterface {

    fun configurateRecyclerView(adapter: CategoryAdapter)
    fun getDrawable(resource: Int): Drawable?
}