package floatingmuseum.floatingmusic.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.floatingmuseum.androidtest.functions.media.ImageItem
import com.orhanobut.logger.Logger
import floatingmuseum.floatingmusic.MusicItem
import floatingmuseum.floatingmusic.R
import org.jetbrains.anko.find

/**
 * Created by Floatingmuseum on 2017/5/31.
 */
class MainActivity : AppCompatActivity() {

    val musicList = ArrayList<MusicItem>()
    val imageList = ArrayList<ImageItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initPlayer()

        scanMusic()
//        scanImage()
    }

    lateinit var rvMusicList: RecyclerView
    lateinit var adapter: MusicListAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var player: MediaPlayer
    lateinit var tvPlayingTitle: TextView

    fun initView() {
        val tvPlayingPlay: TextView = find(R.id.tv_playing_play)
        val tvPlayingPause: TextView = find(R.id.tv_playing_pause)
        val tvPlayingStop: TextView = find(R.id.tv_playing_stop)
        tvPlayingTitle = find(R.id.tv_playing_title)

        tvPlayingPlay.setOnClickListener { replay() }
        tvPlayingPause.setOnClickListener { pauseMusic() }
        tvPlayingStop.setOnClickListener { stopMusic() }

        rvMusicList = find(R.id.rv_music_list)
        linearLayoutManager = LinearLayoutManager(this)
        adapter = MusicListAdapter(musicList)
        rvMusicList.adapter = adapter
        rvMusicList.layoutManager = linearLayoutManager
        rvMusicList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        adapter.setOnItemClickListener { adapter, view, position -> playMusic(position) }
    }

    fun initPlayer() {
        player = MediaPlayer()
    }

    fun replay() {
        if (player.isPlaying) {
            player.seekTo(0)
        }
    }

    fun playMusic(position: Int) {
        player.reset()
        var item = musicList[position]
        player.setDataSource(item.uri)
        tvPlayingTitle.text = "Title:" + item.title
        player.prepare()
        player.start()
    }

    fun pauseMusic() {
        if (player.isPlaying) {
            player.pause()
        }else{
            player.start()
        }
    }

    fun stopMusic() {
        if (player.isPlaying) {
            player.stop()
            tvPlayingTitle.text = "Title:"
        }
    }

    /**
     * 扫描歌曲
     */
    fun scanMusic() {
        musicList.clear()
        val path = Environment.getExternalStorageDirectory().absolutePath + "/netease/cloudmusic/Music"
        Logger.d("Music信息...path:" + path)
        val cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER)
//        if (cursor == null) {
//            return
//        }
        while (cursor.moveToNext()) {
            // 是否为音乐
            val isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC))
            if (isMusic == 0) {
                continue
            }
            // 音乐uri
            val uri = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
            if (!uri.contains(path)) {
                continue
            }
            // ID
            val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns._ID))
            // 标题
            val title = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE)))
            // 艺术家
            val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST))
            // 专辑
            val album = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM)))
            // 持续时间
            val duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION))
            // 专辑封面id，根据该id可以获得专辑图片uri
            val albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID))
            val coverUri = getCoverUri(albumId)
            // 音乐文件名
            val fileName = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DISPLAY_NAME)))
            // 音乐文件大小
            val fileSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.SIZE))
            // 发行时间
            val year = cursor.getInt((cursor.getColumnIndex(MediaStore.Audio.AudioColumns.YEAR)))
            val music = MusicItem(id, title, artist, album, duration, uri, albumId, coverUri, fileName, fileSize, year)

            musicList.add(music)
        }
        cursor.close()
        for (item in musicList) {
            Logger.d("Music信息:" + item.toString())
        }
        adapter.notifyDataSetChanged()
    }

    /**
     * 查询专辑封面图片uri
     */
    fun getCoverUri(albumId: Long): String {
//        var uri = null
//        val cursor = contentResolver.query(
//                Uri.parse("content://media/external/audio/albums/" + albumId),  arrayOf("album_art") , null, null, null)
//        if (cursor != null) {
//            cursor.moveToNext()
//            uri = cursor.getString(0) as Nothing?
//            cursor.close()
//        }
////        CoverLoader.getInstance().loadThumbnail(uri)
//        return uri!!
        return ""
    }

    private fun scanImage() {
        imageList.clear()
        val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Images.Media.DEFAULT_SORT_ORDER)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID))
//            val count = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns._COUNT))
            val title = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.TITLE))
            val size = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.SIZE))
            val width = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH))
            val height = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.HEIGHT))
            val mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.MIME_TYPE))
            val displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME))
            val dateModified = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_MODIFIED))
            val dateAdded = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_ADDED))
            val dateTaken = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN))
            val data = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA))
            val picasaID = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.PICASA_ID))
            val orientation = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION))
            val miniThumbMagic = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.MINI_THUMB_MAGIC))
            val longitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LONGITUDE))
            val latitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LATITUDE))
            val isPrivate = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.IS_PRIVATE))
            val description = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DESCRIPTION))
            val bucketID = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_ID))
            val bucketDisplayName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME))
            val image = ImageItem(id, 0, title, size, width, height, mimeType, displayName, dateModified, dateAdded, dateTaken, data, picasaID, orientation, miniThumbMagic, longitude, latitude, isPrivate, description, bucketID, bucketDisplayName)
            imageList.add(image)
        }
        cursor.close()
        for (item in imageList) {
            Logger.d("Image信息:" + item.toString())
        }
    }
}