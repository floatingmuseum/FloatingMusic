package floatingmuseum.floatingmusic

import android.media.MediaPlayer
import floatingmuseum.floatingmusic.utils.*
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import java.util.concurrent.TimeUnit


/**
 * Created by Floatingmuseum on 2017/6/2.
 */
class PlayerManager private constructor() {

    private val player = MediaPlayer()

    companion object {
        private val playerManager = PlayerManager()
        fun getInstance(): PlayerManager {
            return playerManager
        }
    }

    fun play(uri: String) {
        player.reset()
        player.setDataSource(uri)
        player.prepare()
        player.start()
    }

    fun pauseMusic() {
        if (player.isPlaying) {
            player.pause()
        } else {
            player.start()
        }
    }

    fun stopMusic() {
        if (player.isPlaying) {
            player.stop()
        }
    }

    fun replay() {
        if (player.isPlaying) {
            player.seekTo(0)
        }
    }

    fun setMusicListener() {
        Observable.just("")
                .compose(threadSwitch<String>())
                .subscribe(object :Observer<String>{
                    override fun onComplete() {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onSubscribe(d: Disposable?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onNext(t: String?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onError(e: Throwable?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                })

        Flowable.just("")
                .compose(flowableThreadSwitch<String>())
    }

    private fun sendProgress() :Int{
        return 2
    }

    interface MusicProgressListener {

    }
}

