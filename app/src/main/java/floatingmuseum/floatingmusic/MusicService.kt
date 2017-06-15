package floatingmuseum.floatingmusic

import android.app.Service
import android.content.Intent
import android.media.MediaMetadata
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import org.jetbrains.anko.mediaSessionManager

/**
 * Created by Floatingmuseum on 2017/6/15.
 */
class MusicService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        initMediaSession()
    }

    private fun initMediaSession() {
        val sessionManager = mediaSessionManager
        val session = MediaSessionCompat(this,"")
        val controls = session.controller.transportControls
//        session.setQueue()
//        session.setQueueTitle()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }
}