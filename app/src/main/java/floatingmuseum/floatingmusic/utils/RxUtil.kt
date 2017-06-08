package floatingmuseum.floatingmusic.utils

import floatingmuseum.floatingmusic.MusicListener
import floatingmuseum.floatingmusic.entity.MusicInfo
import io.reactivex.FlowableTransformer
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Created by Floatingmuseum on 2017/6/7.
 */

private val observableTransFormer = ObservableTransformer<Any, Any> { upstream ->
    upstream.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

/**
 * Observable的线程切换
 */
fun <T> observableThreadSwitch(): ObservableTransformer<T, T> {
    return observableTransFormer as ObservableTransformer<T, T>
}

private val flowableTransFormer = FlowableTransformer<Any, Any> { upstream ->
    upstream.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

/**
 * Observable的线程切换
 */
fun <T> flowableThreadSwitch(): FlowableTransformer<T, T> {
    return flowableTransFormer as FlowableTransformer<T, T>
}

//fun <T> observableThreadSwitch(): ObservableTransformer<T, T> {
//    return ObservableTransformer { upstream ->
//        upstream.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//    }
//}
//
//fun <T> flowableThreadSwitch(): FlowableTransformer<T, T> {
//    return FlowableTransformer { upstream ->
//        upstream.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//    }
//}
