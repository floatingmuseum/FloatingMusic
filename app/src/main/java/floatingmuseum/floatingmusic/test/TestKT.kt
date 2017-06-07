package floatingmuseum.floatingmusic.test

import java.util.*


/**
 * Created by Floatingmuseum on 2017/6/2.
 */
open class TestKT {
    inner class Inner {

        fun innerFun() = name
    }

    var name: String = ""
    val age: Int
        get() = 5
    var count = 0
        set(value) {
            if (value >= 0) {
                field = value
            }
        }

    fun Any?.saySome(): String {
        if (this == null) {
            return "none"
        } else {
            return toString()
        }
    }

    fun setListener(listener: Listener) {}

    interface Listener {
        val year: Int
        fun method1()
        fun method2() {
            println("哈?")
        }
    }

    interface Callback {
        fun method2() {
            println("蛤?")
        }
    }


    class cc(override val year: Int) : Listener, Callback {
        override fun method1() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun method2() {
            super<Listener>.method2()
            super<Callback>.method2()
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

    companion object Factory {
        fun create(): TestKT = TestKT()

    }
}

fun <T> ArrayList<T>.swap(index1: Int, index2: Int) {
    val temp = this[index1]
    this[index1] = this[index2]
    this[index2] = temp
}