package floatingmuseum.floatingmusic.entity

/**
 * Created by Floatingmuseum on 2017/6/7.
 */
data class MusicInfo(val id: Long, val title: String, val artist: String, val album: String, val duration: Long, val uri: String, val albumID: Long, val coverUri: String, val fileName: String, val fileSize: Long, val year: Int, var progress: Long = 0)