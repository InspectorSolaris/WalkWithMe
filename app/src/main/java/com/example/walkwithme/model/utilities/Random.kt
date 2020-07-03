package com.example.walkwithme.model.utilities

import kotlin.random.Random

object Random {
    private val random = Random(0)

    fun randInt(
        l: Int,
        r: Int
    ): Int {
        return random.nextInt(l, r)
    }

    fun randDouble(
        l: Double = 0.0,
        r: Double = 1.0
    ): Double {
        return random.nextDouble(l, r)
    }

    fun<T> randBy(ts: List<T>, p: (T) -> Double): T {
        var sum = random.nextDouble(ts.map(p).sum())
        var i = -1

        while (sum > 0) {
            ++i
            sum -= p(ts[i])
        }

        return ts[i]
    }
}