package kr.co.lbstech.flutter_naver_map_test

import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.PolylineOverlay

class PolylinesController(
        private val density: Float,
        private val naverMap: NaverMap) {

    private val polylineIdToController = HashMap<String, PolylineController>()

    fun addPolylines(polylinesToAdd: List<Any>?) {
        polylinesToAdd ?: return
        polylinesToAdd.forEach { polylineToAdd -> addPolyline(polylineToAdd) }
    }

    private fun addPolyline(polyline: Any?) {
        polyline ?: return

        val polylineBuilder = PolylineBuilder(density = density)
        val polylineId = Convert.interpretPolyline(polyline, polylineBuilder)
        val polylineOverlay = polylineBuilder.build()
        addPolyline(polylineId = polylineId, polylineOverlay = polylineOverlay)
    }

    private fun addPolyline(polylineId: String, polylineOverlay: PolylineOverlay) {
        polylineOverlay.map = naverMap
        val controller = PolylineController(polylineOverlay, density)
        polylineIdToController[polylineId] = controller
    }
}