package kr.co.lbstech.flutter_naver_map_test

import android.graphics.BitmapFactory
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay

class PathBuilder(private val density: Float) : PathSink {
    private val pathOverlay = PathOverlay()

    fun build(): PathOverlay = pathOverlay

    override fun setCoords(coords: List<LatLng>) {
        pathOverlay.coords = coords
    }

    override fun setGlobalZIndex(globalZIndex: Int) {
        pathOverlay.globalZIndex = globalZIndex
    }

    override fun setHideCollidedCaptions(hide: Boolean) {
        pathOverlay.isHideCollidedCaptions = hide
    }

    override fun setHideCollidedMarkers(hide: Boolean) {
        pathOverlay.isHideCollidedMarkers = hide
    }

    override fun setHideCollidedSymbols(hide: Boolean) {
        pathOverlay.isHideCollidedSymbols = hide
    }

    override fun setVisible(visible: Boolean) {
        pathOverlay.isVisible = visible
    }

    override fun setColor(color: Number) {
        pathOverlay.color = color.toInt()
    }

    override fun setOutlineColor(color: Number) {
        pathOverlay.outlineColor = color.toInt()
    }

    override fun setPassedColor(color: Number) {
        pathOverlay.passedColor = color.toInt()
    }

    override fun setPassedOutlineColor(color: Number) {
        pathOverlay.passedOutlineColor = color.toInt()
    }

    override fun setPatternImage(blob: ByteArray?) {
        blob?.let {
            pathOverlay.patternImage = OverlayImage.fromBitmap(BitmapFactory.decodeByteArray(blob, 0, blob.size))
        }
    }

    override fun setPatternInterval(interval: Int) {
        pathOverlay.patternInterval = interval
    }

    override fun setProgress(process: Double) {
        pathOverlay.progress = process
    }

    override fun setWidth(width: Int) {
        pathOverlay.width = (width.toFloat() * density).toInt()
    }

    override fun setOutlineWidth(width: Int) {
        pathOverlay.outlineWidth = (width.toFloat() * density).toInt()
    }
}