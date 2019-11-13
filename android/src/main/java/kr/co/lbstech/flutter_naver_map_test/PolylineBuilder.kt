package kr.co.lbstech.flutter_naver_map_test

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.PolylineOverlay
import kotlin.math.roundToInt

class PolylineBuilder(private val density: Float) : PolylineSink {
    private val polylineOverlay = PolylineOverlay()

    fun build(): PolylineOverlay = polylineOverlay

    override fun setColor(color: Number) {
        polylineOverlay.color = color.toInt()
    }

    override fun setLineCap(capType: PolylineOverlay.LineCap) {
        polylineOverlay.capType = capType
    }

    override fun setJointType(joinType: PolylineOverlay.LineJoin) {
        polylineOverlay.joinType = joinType
    }

    override fun setPattern(pattern: List<Int>) {
        polylineOverlay.setPattern(*pattern.toIntArray())
    }

    override fun setCoords(coords: List<LatLng>) {
        polylineOverlay.coords = coords
    }

    override fun setVisible(visible: Boolean) {
        polylineOverlay.isVisible = visible
    }

    override fun setWidth(width: Int) {
        polylineOverlay.width = (width.toFloat() * density).roundToInt()
    }

    override fun setGlobalZIndex(globalZIndex: Int) {
        polylineOverlay.globalZIndex = globalZIndex
    }
}