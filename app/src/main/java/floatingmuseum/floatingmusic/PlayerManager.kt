package floatingmuseum.floatingmusic

import android.media.MediaPlayer
import floatingmuseum.floatingmusic.entity.MusicInfo
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
class PlayerManager private constructor() : MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    override fun onPrepared(mp: MediaPlayer?) {
        startPlay()
    }

    override fun onCompletion(mp: MediaPlayer?) {
        playNext()
    }

    private val player = MediaPlayer()
    private var currentPlayMode = PLAY_MODE_REPEAT_LIST
    private var currentPlayingState = PLAY_STATE_STOP
    private var currentMusicList = ArrayList<MusicInfo>()
    private var currentMusicInfo: MusicInfo? = null

    init {
        player.setOnPreparedListener(this)
        player.setOnCompletionListener(this)
    }

    companion object {
        const val PLAY_MODE_REPEAT_ONE = "repeatOne"
        const val PLAY_MODE_REPEAT_LIST = "repeatList"
        const val PLAY_MODE_SHUFFLE = "SHUFFLE"

        const val PLAY_STATE_PLAYING = 0
        const val PLAY_STATE_PAUSE = 1
        const val PLAY_STATE_STOP = 2

        private val playerManager = PlayerManager()

        fun getInstance(): PlayerManager {
            return playerManager
        }
    }

    fun play(uri: String) {
        player.reset()
        player.setDataSource(uri)
        player.prepareAsync()
    }

    private fun startPlay() {
        player.start()
        currentPlayingState = PLAY_STATE_PLAYING
    }

    fun pause() {
        if (player.isPlaying) {
            player.pause()
        }
    }


    fun resume() {
        startPlay()
    }

    fun stop() {
        if (player.isPlaying) {
            player.stop()
        }
    }

    fun playPrevious() {}

    fun playNext() {}

    fun replay() {
        if (player.isPlaying) {
            player.seekTo(0)
        }
    }

    fun refreshMusicList(newMusicList: ArrayList<MusicInfo>) {
        currentMusicList.clear()
        currentMusicList.addAll(newMusicList)
    }

    fun setMusicListener() {

    }

    fun setPlayMode(mode: String) {
        when (mode) {
            PLAY_MODE_REPEAT_LIST -> currentPlayMode = PLAY_MODE_REPEAT_LIST
            PLAY_MODE_REPEAT_ONE -> currentPlayMode = PLAY_MODE_REPEAT_ONE
            PLAY_MODE_SHUFFLE -> currentPlayMode = PLAY_MODE_SHUFFLE
        }
    }

    fun getPlayMode(): String = currentPlayMode
    fun getPlayState(): Int = currentPlayingState

    fun hasMusicInfo(): Boolean = currentMusicInfo == null

    fun sendProgress(): Int {
        return 2
    }

    interface MusicProgressListener {

    }
}

