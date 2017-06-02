package floatingmuseum.floatingmusic

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.orhanobut.logger.Logger
import com.wanjian.cockroach.Cockroach

/**
 * Created by Floatingmuseum on 2017/5/31.
 */
class App : Application() {

    companion object {
        lateinit var context: App
    }

    init {
        context = this
    }

    override fun onCreate() {
        super.onCreate()
        initCockroach()
    }

    private fun initCockroach() {

        Cockroach.install { thread, throwable ->
            // handlerException内部建议手动try{  你的异常处理逻辑  }catch(Throwable e){ } ，以防handlerException内部再次抛出异常，导致循环调用handlerException

            Handler(Looper.getMainLooper()).post {
                try {
                    throwable.printStackTrace()
                    Logger.e("AndroidRuntime--->CockroachException:$thread<---$throwable")
//                        ToastUtil.show("Exception Happend\n" + thread + "\n" + throwable.toString())
                    //                        throw new RuntimeException("..."+(i++));
                } catch (e: Throwable) {

                }
            }
        }
    }
}