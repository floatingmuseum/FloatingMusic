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
import butterknife.bindView
import com.floatingmuseum.androidtest.functions.media.ImageItem
import com.orhanobut.logger.Logger
import floatingmuseum.floatingmusic.MusicListener
import floatingmuseum.floatingmusic.PlayerManager
import floatingmuseum.floatingmusic.R
import floatingmuseum.floatingmusic.entity.MusicInfo
import floatingmuseum.floatingmusic.utils.formatMilliseconds

/**
 * Created by Floatingmuseum on 2017/5/31.
 */
class MainActivity : AppCompatActivity(), MusicListener {


    val musicList = ArrayList<MusicInfo>()
    val imageList = ArrayList<ImageItem>()
    val playerManager = PlayerManager.getInstance()
    val rvMusicList: RecyclerView by bindView(R.id.rv_music_list)
    val ivPlayingStateControl: ImageView by bindView(R.id.iv_playing_state_control)
    val tvPlayingTitle: TextView by bindView(R.id.tv_playing_title)
    val tvPlayingArtist: TextView by bindView(R.id.tv_playing_artist)
    val tvPlayingDuration: TextView by bindView(R.id.tv_playing_duration)
    val tvPlayDuration: TextView by bindView(R.id.tv_play_duration)
    val sbMusicProgress: SeekBar by bindView(R.id.sb_playing_progress)
    val ivPlayingPrevious: ImageView by bindView(R.id.iv_playing_previous)
    val ivPlayingNext: ImageView by bindView(R.id.iv_playing_next)
    val ivPlayingMode: ImageView by bindView(R.id.iv_playing_mode)
    lateinit var adapter: MusicListAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initMusic()
    }

    fun initView() {
        ivPlayingStateControl.setImageResource(if (playerManager.getPlayState() == PlayerManager.PLAY_STATE_PLAYING) R.drawable.music_pause else R.drawable.music_play)
        initPlayMode(playerManager.getPlayMode())
        val musicInfo = playerManager.getMusicInfo()
        musicInfo?.let { initMusicInfo(musicInfo) }
        ivPlayingPrevious.setOnClickListener { playerManager.playPrevious() }
        ivPlayingNext.setOnClickListener { playerManager.playNext() }
        ivPlayingStateControl.setOnClickListener {
            controlPlayState()
        }

        ivPlayingMode.setOnClickListener { playerManager.changePlayMode() }

        sbMusicProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Logger.d("SeekBar使用...onProgressChanged:...progress:$progress...fromUser:$fromUser")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                Logger.d("SeekBar使用...onStartTrackingTouch")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Logger.d("SeekBar使用...onStopTrackingTouch")
            }
        })

        linearLayoutManager = LinearLayoutManager(this)
        adapter = MusicListAdapter(musicList)
        rvMusicList.adapter = adapter
        rvMusicList.layoutManager = linearLayoutManager
        rvMusicList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        adapter.setOnItemClickListener { _, _, position -> playMusic(position) }
    }

    private fun initMusicInfo(musicInfo: MusicInfo) {
        tvPlayingTitle.text = musicInfo.title
        tvPlayingArtist.text = musicInfo.artist
        tvPlayDuration.text = formatMilliseconds(musicInfo.duration)
    }

    fun initPlayMode(mode: Int) {
        when (mode) {
            PlayerManager.PLAY_MODE_REPEAT_LIST -> ivPlayingMode.setImageResource(R.drawable.music_repeat_list)
            PlayerManager.PLAY_MODE_REPEAT_ONE -> ivPlayingMode.setImageResource(R.drawable.music_repeat_one)
            PlayerManager.PLAY_MODE_PLAY_LIST -> ivPlayingMode.setImageResource(R.drawable.music_play_list)
            PlayerManager.PLAY_MODE_SHUFFLE -> ivPlayingMode.setImageResource(R.drawable.music_shuffle)
        }
    }

    private fun controlPlayState() {
        playerManager.getMusicInfo().let {
            if (playerManager.getPlayState() == PlayerManager.PLAY_STATE_PLAYING) {
                playerManager.pause()
            } else {
                playerManager.resume()
            }
        }
    }

    fun initMusic() {
        playerManager.setMusicListener(this)
        scanMusic()
//        scanImage()
    }

    fun replay() {
        playerManager.replay()
    }

    fun playMusic(position: Int) {
        var item = musicList[position]
        tvPlayDuration.text = formatMilliseconds(item.duration)
        tvPlayingTitle.text = "Title:" + item.title
        tvPlayingArtist.text = "Artist:" + item.artist
        playerManager.play(item)
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

        playerManager.refreshMusicList(musicList)
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

    override fun onMusicPrepared(musicInfo: MusicInfo) {
        ivPlayingStateControl.setImageResource(R.drawable.music_pause)
    }

    override fun onMusicProgress(musicInfo: MusicInfo) {
        val progress = (musicInfo.progress.toFloat() / musicInfo.duration.toFloat() * 100).toInt()
        tvPlayingDuration.text = formatMilliseconds(musicInfo.progress)
        sbMusicProgress.progress = progress
    }

    override fun onMusicPause(musicInfo: MusicInfo) {
        ivPlayingStateControl.setImageResource(R.drawable.music_play)
    }

    override fun onMusicResume(musicInfo: MusicInfo) {
        ivPlayingStateControl.setImageResource(R.drawable.music_pause)
    }

    override fun onMusicChanged(musicInfo: MusicInfo) {
        sbMusicProgress.progress = 0
        tvPlayingTitle.text = "Title:" + musicInfo.title
        tvPlayingArtist.text = "Artist:" + musicInfo.artist
        tvPlayingDuration.text = "00:00"
        tvPlayDuration.text = formatMilliseconds(musicInfo.duration)
    }

    override fun onPlayModeChanged(mode: Int) {
        initPlayMode(mode)
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