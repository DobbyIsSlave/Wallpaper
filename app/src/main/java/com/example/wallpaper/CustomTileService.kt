package com.example.wallpaper

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class CustomTileService : TileService() {

    override fun onTileAdded() {
        super.onTileAdded()
        qsTile.state = Tile.STATE_INACTIVE
        qsTile.updateTile()
    }

    override fun onClick() {
        super.onClick()
        val context = applicationContext

        if (qsTile.state == Tile.STATE_ACTIVE) {
            // 특정 기능을 비활성화
            qsTile.state = Tile.STATE_INACTIVE
            WallpaperActionList().setWallpaper(context, R.drawable._1f854bd38a2b)
        } else {
            // 특정 기능을 활성화
            qsTile.state = Tile.STATE_ACTIVE
            WallpaperActionList().setWallpaper(context, R.drawable.i16001892294_2)
        }

        qsTile.updateTile()
    }
}