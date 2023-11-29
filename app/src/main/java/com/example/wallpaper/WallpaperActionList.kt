package com.example.wallpaper

import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WallpaperActionList {
    fun setWallpaper(context: Context, drawableResource: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            // 긴 작업 수행
            val wallpaperManager = WallpaperManager.getInstance(context)

            // 예제로 drawable 리소스를 사용한 경우
            val bitmap = BitmapFactory.decodeResource(context.resources, drawableResource)

            // 배경화면 설정
            try {
                wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // 결과를 UI에 반영해야 하는 경우
            launch(Dispatchers.Main) {
                // UI 업데이트 작업 수행
                // 예: UI에 결과를 표시하는 코드
            }
        }
    }
}