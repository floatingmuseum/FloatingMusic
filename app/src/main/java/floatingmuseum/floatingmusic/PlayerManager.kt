package floatingmuseum.floatingmusic

import android.media.MediaPlayer


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
}