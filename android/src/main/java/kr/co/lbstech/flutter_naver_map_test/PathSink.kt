package kr.co.lbstech.flutter_naver_map_test

import com.naver.maps.geometry.LatLng

interface PathSink {
    fun setCoords(coords: List<LatLng>)

    fun setGlobalZIndex(globalZIndex: Int)

    fun setHideCollidedCaptions(hide: Boolean)

    fun setHideCollidedMarkers(hide: Boolean)

    fun setHideCollidedSymbols(hide: Boolean)

    fun setVisible(visible: Boolean)

    fun setColor(color: Number)

    fun setOutlineColor(color: Number)

    fun setPassedColor(color: Number)

    fun setPassedOutlineColor(color: Number)

    fun setPatternImage(blob: ByteArray?)

    fun setPatternInterval(interval: Int)

    fun setProgress(process: Double)

    fun setWidth(width: Int)

    fun setOutlineWidth(width: Int)
}
