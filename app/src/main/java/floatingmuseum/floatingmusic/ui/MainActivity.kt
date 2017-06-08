package floatingmuseum.floatingmusic.ui

import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.floatingmuseum.androidtest.functions.media.ImageItem
import com.orhanobut.logger.Logger
import floatingmuseum.floatingmusic.MusicListener
import floatingmuseum.floatingmusic.PlayerManager
import floatingmuseum.floatingmusic.R
import floatingmuseum.floatingmusic.entity.MusicInfo
import org.jetbrains.anko.find

/**
 * Created by Floatingmuseum on 2017/5/31.
 */
class MainActivity : AppCompatActivity(), MusicListener {

    val musicList = ArrayList<MusicInfo>()
    val imageList = ArrayList<ImageItem>()
    lateinit var playerManager: PlayerManager
    lateinit var rvMusicList: RecyclerView
    lateinit var adapter: MusicListAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var tvPlayingTitle: TextView
    lateinit var tvPlayingArtist: TextView
    lateinit var sbMusicProgress: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playerManager = PlayerManager.getInstance()

        initView()
        initMusic()
//        scanImage()
    }

    fun initView() {
        val ivPlayingPrevious: ImageView = find(R.id.iv_playing_previous)
        val ivPlayingNext: ImageView = find(R.id.iv_playing_next)
        var ivPlayingStateControl: ImageView = find(R.id.iv_playing_state_control)
        var ivPlayingMode: ImageView = find(R.id.iv_playing_mode)

        ivPlayingStateControl.setImageResource(if (playerManager.getPlayState().equals(PlayerManager.PLAY_STATE_PLAYING)) R.drawable.music_pause else R.drawable.music_play)
        when (playerManager.getPlayMode()) {
            PlayerManager.PLAY_MODE_REPEAT_LIST -> ivPlayingMode.setImageResource(R.drawable.music_repeat_list)
            PlayerManager.PLAY_MODE_REPEAT_ONE -> ivPlayingMode.setImageResource(R.drawable.music_repeat_one)
            PlayerManager.PLAY_MODE_SHUFFLE -> ivPlayingMode.setImageResource(R.drawable.music_shuffle)
        }

        ivPlayingPrevious.setOnClickListener { playerManager.playPrevious() }
        ivPlayingNext.setOnClickListener { playerManager.playNext() }
        ivPlayingStateControl.setOnClickListener {
            controlPlayState()
        }

        sbMusicProgress = find(R.id.sb_playing_progress)
        tvPlayingTitle = find(R.id.tv_playing_title)
        tvPlayingArtist = find(R.id.tv_playing_artist)
        rvMusicList = find(R.id.rv_music_list)

        linearLayoutManager = LinearLayoutManager(this)
        adapter = MusicListAdapter(musicList)
        rvMusicList.adapter = adapter
        rvMusicList.layoutManager = linearLayoutManager
        rvMusicList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        adapter.setOnItemClickListener { adapter, view, position -> playMusic(position) }
    }

    private fun controlPlayState() {
        if (playerManager.getCurrentMusicInfo() == null) {
            return
        }
        if (playerManager.getPlayState().equals(PlayerManager.PLAY_STATE_PLAYING)) {
            playerManager.pause()
        } else {
            playerManager.resume()
        }
    }

    private fun initMusic() {
        scanMusic()
    }

    fun replay() {
        playerManager.replay()
    }

    fun playMusic(position: Int) {
        var item = musicList[position]
        tvPlayingTitle.text = "Title:" + item.title
        tvPlayingArtist.text = "Artist:" + item.artist
        playerManager.play(item.uri)
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
            val music = MusicInfo(id, title, artist, album, duration, uri, albumId, coverUri, fileName, fileSize, year)

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

    override fun onMusicProgress(musicInfo: MusicInfo) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMusicPause(musicInfo: MusicInfo) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMusicResume(musicInfo: MusicInfo) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onChange(musicInfo: MusicInfo) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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