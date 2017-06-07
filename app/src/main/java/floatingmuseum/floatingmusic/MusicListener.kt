package floatingmuseum.floatingmusic

import floatingmuseum.floatingmusic.entity.MusicInfo

/**
 * Created by Floatingmuseum on 2017/6/7.
 */
interface MusicListener{
    fun onMusicProgress(musicInfo: MusicInfo)
    fun onMusicPause(musicInfo: MusicInfo)
    fun onMusicResume(musicInfo: MusicInfo)
    fun onChange(musicInfo: MusicInfo)
}