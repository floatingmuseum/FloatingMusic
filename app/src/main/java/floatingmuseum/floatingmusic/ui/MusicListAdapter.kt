package floatingmuseum.floatingmusic.ui

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import floatingmuseum.floatingmusic.MusicItem
import floatingmuseum.floatingmusic.R

/**
 * Created by Floatingmuseum on 2017/6/2.
 */
class MusicListAdapter(data: List<MusicItem>) : BaseQuickAdapter<MusicItem, BaseViewHolder>(R.layout.item_music, data) {

    override fun convert(helper: BaseViewHolder, item: MusicItem) {
        helper.setText(R.id.tv_title, "Title:" + item.title)
                .setText(R.id.tv_album_title, "Album:" + item.album)
                .setText(R.id.tv_artist, "Artist:" + item.artist)
                .setText(R.id.tv_duration, "Duration:" + item.duration)
    }
}