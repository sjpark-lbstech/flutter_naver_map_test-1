package kr.co.lbstech.flutter_naver_map_test

import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.PathOverlay

class PathsController(
        private val density: Float,
        private val naverMap: NaverMap) {

    private val pathIdToController = HashMap<String, PathController>()

    fun addPaths(polylinesToAdd: List<Any>?) {
        polylinesToAdd ?: return
        polylinesToAdd.forEach { polylineToAdd -> addPath(polylineToAdd) }
    }

    private fun addPath(path: Any?) {
        path ?: return

        val pathBuilder = PathBuilder(density = density)
        val pathId = Convert.interpretPath(path, pathBuilder)
        val pathOverlay = pathBuilder.build()
        addPath(pathId = pathId, pathOverlay = pathOverlay)
    }

    private fun addPath(pathId: String, pathOverlay: PathOverlay) {
        pathOverlay.map = naverMap
        val controller = PathController(pathOverlay, density)
        pathIdToController[pathId] = controller
    }
}