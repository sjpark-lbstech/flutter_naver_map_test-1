package kr.co.lbstech.flutter_naver_map_test

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.PolylineOverlay

interface PolylineSink {
    fun setColor(color: Number)

    fun setLineCap(capType: PolylineOverlay.LineCap)

    fun setJointType(joinType: PolylineOverlay.LineJoin)

    fun setPattern(pattern: List<Int>)

    fun setCoords(coords: List<LatLng>)

    fun setVisible(visible: Boolean)

    fun setWidth(width: Int)

    fun setGlobalZIndex(globalZIndex: Int)
}