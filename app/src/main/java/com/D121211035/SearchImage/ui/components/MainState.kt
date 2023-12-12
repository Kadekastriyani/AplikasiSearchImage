package com.D121211035.SearchImage.ui.components

import com.D121211035.SearchImage.network.model.Hit

data class MainState(
    val isLoading:Boolean=false,
    val data:List<Hit> = emptyList(),
    val error:String=""
)
