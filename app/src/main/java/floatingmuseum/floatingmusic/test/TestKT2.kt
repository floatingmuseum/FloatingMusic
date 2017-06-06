package floatingmuseum.floatingmusic.test


/**
 * Created by Floatingmuseum on 2017/6/2.
 */
class TestKT2(){
    lateinit var test1:TestKT
    fun testMethod(){
//        val test1 = TestKT.Inner()
//        test1.innerFun()
        val test = TestKT()
        var list = arrayListOf(1,2,3)
        TestUtil.swap(list,1,2)
        list.swap(1,2)
        test.setListener(object : TestKT.Listener {
            override val year: Int
                get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

            override fun method1() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun method2() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        val b = object : TestKT.Listener {
            override val year = 5

            override fun method2() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun method1() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
        val a = object {
            var age:Int=0
            var year:Int=2
        }
        a.age+a.year
        foo().x
        var t = TestKT.create()
    }

    private fun foo()= object {
        val x: String = "x"
    }
}