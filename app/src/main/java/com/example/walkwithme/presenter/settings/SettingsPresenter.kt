package com.example.walkwithme.presenter.settings

import com.example.walkwithme.R
import com.example.walkwithme.view.settings.SettingsViewInterface
import com.example.walkwithme.adapter.CategoryAdapter
import com.example.walkwithme.model.category.CategoryCard

class SettingsPresenter(
    private val settingsInterface: SettingsViewInterface
) {

    fun setCategoryRecyclerView() {
        val numberOfCategories = 6
        val names = arrayListOf("Cafe", "Church", "Fountain", "Museum", "Park", "Supermarket")
        val images = arrayListOf(
            settingsInterface.getDrawable(R.drawable.category_cafe),
            settingsInterface.getDrawable(R.drawable.category_church),
            settingsInterface.getDrawable(R.drawable.category_fountain),
            settingsInterface.getDrawable(R.drawable.category_museum),
            settingsInterface.getDrawable(R.drawable.category_park),
            settingsInterface.getDrawable(R.drawable.category_supermarket)
        )

        val dataList = ArrayList<CategoryCard>()

        for (i in 0 until numberOfCategories) {
            dataList.add(
                CategoryCard(
                    1, names[i], images[i]
                )
            )
        }

        val cardAdapter = CategoryAdapter(dataList)
        settingsInterface.configurateRecyclerView(cardAdapter)
    }

}