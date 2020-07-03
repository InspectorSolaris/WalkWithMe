package com.example.walkwithme.model.genetic

data class Genotype<T, V>(
    val genes: List<T>,
    val fitness: V? = null
)