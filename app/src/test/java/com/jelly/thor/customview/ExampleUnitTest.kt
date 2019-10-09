package com.jelly.thor.customview

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    var a = 1
    private val aaa
        get() = a++
    @Test
    fun addition_isCorrect() {
        println("$aaa")
        println("$aaa")
        println("$aaa")
        println("$aaa")
        println("$aaa")
        assertEquals(4, 2 + 2)
    }
}
