package floatingmuseum.floatingmusic

import android.media.MediaPlayer
import android.text.format.DateUtils
import com.orhanobut.logger.Logger
import floatingmuseum.floatingmusic.entity.MusicInfo
import floatingmuseum.floatingmusic.utils.flowableThreadSwitch
import floatingmuseum.floatingmusic.utils.formatMilliseconds
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


/**
 * Created by Floatingmuseum on 2017/6/2.
 */
class PlayerManager private constructor() : MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    private var player = MediaPlayer()
    private var nextPlayer: MediaPlayer? = null
    private var currentPlayMode = PLAY_MODE_REPEAT_LIST
    private var currentPlayingState = PLAY_STATE_STOP
    private var originalList = ArrayList<MusicInfo>()
    private var playingList = ArrayList<MusicInfo>()
    private var currentMusicInfo: MusicInfo? = null
    private var listener: MusicListener? = null
    private var progressDisposable: Disposable? = null
    private var musicIndex = 0

    init {
        player.setOnPreparedListener(this)
        player.setOnCompletionListener(this)
    }

    companion object {
        const val PLAY_STATE_PLAYING = 0
        const val PLAY_STATE_PAUSE = 1
        const val PLAY_STATE_STOP = 2

        const val PLAY_MODE_REPEAT_ONE = 3
        const val PLAY_MODE_REPEAT_LIST = 4
        const val PLAY_MODE_PLAY_LIST = 5
        const val PLAY_MODE_SHUFFLE = 6

        private val playerManager = PlayerManager()

        fun getInstance(): PlayerManager {
            return playerManager
        }
    }

    fun play(info: MusicInfo) {
        player.reset()
        player.setDataSource(info.uri)
        setMusicInfo(info)
        player.prepareAsync()
    }

    private fun startPlay() {
        player.start()
        currentPlayingState = PLAY_STATE_PLAYING
        startRefreshProgress()
    }

    fun pause() {
        if (player.isPlaying) {
            currentPlayingState = PLAY_STATE_PAUSE
            listener?.onMusicPause(currentMusicInfo!!)
            player.pause()
            stopRefreshProgress()
        }
    }

    fun resume() {
        currentPlayingState = PLAY_STATE_PLAYING
        listener?.onMusicResume(currentMusicInfo!!)
        startPlay()
    }

    fun stop() {
        if (player.isPlaying) {
            player.reset()
        }
    }

    override fun onPrepared(mp: MediaPlayer?) {
        listener?.onMusicPrepared(currentMusicInfo!!)
//        createNextMediaPlayer()
        startPlay()
    }

    override fun onCompletion(mp: MediaPlayer?) {
        Logger.d("歌曲索引onCompletion:" + playNextPreviousByUser)
//        if (playNextPreviousByUser) {
//            playNextPreviousByUser = false
//            return
//        }
        playNext()
    }

    fun playPrevious() {
//        playNextPreviousByUser = true
        if (musicIndex == 0) {
            musicIndex = originalList.lastIndex
        } else {
            musicIndex--
        }
        Logger.d("歌曲索引previous:" + musicIndex)
        listener?.onMusicChanged(originalList[musicIndex])
        play(originalList[musicIndex])
    }

    var playNextPreviousByUser = false

    fun playNext() {
//        playNextPreviousByUser = true
        if (musicIndex == originalList.lastIndex) {
            musicIndex = 0
        } else {
            musicIndex++
        }
        Logger.d("歌曲索引next:" + musicIndex)
        listener?.onMusicChanged(originalList[musicIndex])
        play(originalList[musicIndex])
    }

    fun replay() {
        if (player.isPlaying) {
            player.seekTo(0)
        }
    }

    private fun setMusicInfo(info: MusicInfo) {
        for (index in originalList.indices) {
            val item = originalList[index]
            if (info.id == item.id) {
                musicIndex = index
                Logger.d("歌曲索引setMusicInfo:" + musicIndex)
                currentMusicInfo = item
                return
            }
        }
    }

    fun startRefreshProgress() {
        stopRefreshProgress()
        progressDisposable = Flowable.interval(0, 1000, TimeUnit.MILLISECONDS)
                .compose(flowableThreadSwitch<Long>())
                .subscribe { sendProgress() }

    }

    fun stopRefreshProgress() {
        if (progressDisposable != null && progressDisposable!!.isDisposed) {
            progressDisposable!!.dispose()
        }
    }

    fun refreshMusicList(newMusicList: ArrayList<MusicInfo>) {
        originalList.clear()
        originalList.addAll(newMusicList)
    }

    fun setMusicListener(listener: MusicListener) {
        this.listener = listener
    }

    fun changePlayMode() {
        if (currentPlayMode == PLAY_MODE_REPEAT_LIST) {
            currentPlayMode = PLAY_MODE_REPEAT_ONE
        } else if (currentPlayMode == PLAY_MODE_REPEAT_ONE) {
            currentPlayMode = PLAY_MODE_PLAY_LIST
        } else if (currentPlayMode == PLAY_MODE_PLAY_LIST) {
            currentPlayMode = PLAY_MODE_SHUFFLE
        } else {
            currentPlayMode = PLAY_MODE_REPEAT_LIST
        }
        listener?.onPlayModeChanged(currentPlayMode)
    }

    fun getPlayMode(): Int = currentPlayMode
    fun getPlayState(): Int = currentPlayingState

    fun getMusicInfo(): MusicInfo? = currentMusicInfo

    fun sendProgress() {
//        Logger.d("歌曲进度:" + currentMusicInfo?.title + "..." + player.currentPosition + "..." + player.duration + "..." + formatMilliseconds(player.currentPosition.toLong()) + "..." + formatMilliseconds(player.duration.toLong()))
        currentMusicInfo?.progress = player.currentPosition.toLong()
        listener?.onMusicProgress(currentMusicInfo!!)
    }

    fun createNextMediaPlayer() {
        var nextMusicIndex = 0
        if (musicIndex != originalList.lastIndex) {
            nextMusicIndex = musicIndex + 1
        }
        val nextMusic = originalList[nextMusicIndex]
        nextPlayer = MediaPlayer()
        nextPlayer?.setDataSource(nextMusic.uri)
        nextPlayer?.prepare()
        player.setNextMediaPlayer(nextPlayer)
    }
}

