package floatingmuseum.floatingmusic.ui

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import floatingmuseum.floatingmusic.R
import floatingmuseum.floatingmusic.entity.MusicInfo

/**
 * Created by Floatingmuseum on 2017/6/2.
 */
class MusicListAdapter(data: List<MusicInfo>) : BaseQuickAdapter<MusicInfo, BaseViewHolder>(R.layout.item_music, data) {

    override fun convert(helper: BaseViewHolder, info: MusicInfo) {
        helper.setText(R.id.tv_title, "Title:" + info.title)
                .setText(R.id.tv_album_title, "Album:" + info.album)
                .setText(R.id.tv_artist, "Artist:" + info.artist)
                .setText(R.id.tv_duration, "Duration:" + info.duration)
    }
}