package floatingmuseum.floatingmusic.test

import com.orhanobut.logger.Logger

/**
 * Created by Floatingmuseum on 2017/6/6.
 */
class Example {
    var d: String by Delegate()
    fun test1() {
        1.sum(2)
        1 sum (2)
    }

    /**
     * 可变数量的参数
     */
    fun test2(vararg nums: Int): Int {
        val result = nums.sum()
        return result
    }

    /**
     * 局部函数
     */
    fun test3() {
        val outer = 10
        //        test3_1()
        fun test3_1(inner: Int) {
            Logger.d("inner+outer=" + (inner + outer))
        }
        test3_1(20)
    }

    fun test4() {
        val numbers: MutableList<Int> = mutableListOf(1, 2, 3)
        val readOnlyView: List<Int> = numbers
        println(numbers)        // 输出 "[1, 2, 3]"
        numbers.add(4)
        println(readOnlyView)   // 输出 "[1, 2, 3, 4]"
//        readOnlyView.clear()    // -> 不能编译
        val strings = hashSetOf("a", "b", "c", "c")
        assert(strings.size == 3)
    }

    fun test5() {
        //正序1234
        for (i in 1..4) {

        }

        //倒序4321
        for (i in 4 downTo 1) {

        }

        //13
        for (i in 1..4 step 2) {

        }

        //42
        for (i in 4 downTo 1 step 2) {

        }

        //123
        for (i in 1 until 4) {

        }
        var result = 5.rangeTo(10)
        var list = mutableListOf(1, 2, 3, 4)
        list.filter { it.equals(1) }
                .forEach { list.remove(it) }
        var arr = arrayOf(1, 2, 3, 4, 5)
    }

    fun test6() {
        val a: String = "abc"
        Logger.d(a.length)

        val b: String? = "cba"
//        Logger.d(b.length)
        Logger.d(b!!.length)//如果不惧怕得到一个空指针可以使用双叹号
        if (b != null) {
            //在非空判断内可以使用length,非空判断外提示可能有异常
            Logger.d(b.length)
        }
        //这样可以使用,如果不为空打印b长度,为空打印null
        Logger.d(b?.length)

//        val c: Int = if (b != null) b.length else -1
        //简写 如果 ?: 左侧表达式非空，elvis 操作符就返回其左侧表达式，否则返回右侧表达式。 请注意，当且仅当左侧为空时，才会对右侧表达式求值。
        val c = b?.length ?: -1

        //过滤集合中的null
        val nullableList: List<Int?> = listOf(1, 2, null, 4)
        val intList: List<Int> = nullableList.filterNotNull()

        //防止类型转换异常,如果a不能转换为Int,则返回null
        val aInt:Int? = a as? Int
    }

    infix fun Int.sum(num: Int) = this + num
}

/**
 * 他们是成员函数或扩展函数
 * 他们只有一个参数
 * 他们用 infix 关键字标注
 *
 * 中缀表示法
 * 1 sum(2)
 * 或者
 * 1.sum(2)
 *
 * 结果3
 */
infix fun Int.sum(num: Int): Int {
    return this + num
}