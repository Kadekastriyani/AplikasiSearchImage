package com.D121211035.SearchImage.network.model

data class PixabayResponse(
    val hits: List<Hit>,
    val total: Int,
    val totalHits: Int
)