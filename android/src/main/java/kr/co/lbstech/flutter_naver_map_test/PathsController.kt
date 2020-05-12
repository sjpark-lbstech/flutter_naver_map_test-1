package kr.co.lbstech.flutter_naver_map_test

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.PathOverlay

class PathsController(
        private val density: Float,
        private val naverMap: NaverMap,
        private val listener : NaverMapListeners) {

    private val pathIdToController = HashMap<String, PathController>()

    fun addPaths(polylinesToAdd: List<Any>?) {
        polylinesToAdd ?: return
        polylinesToAdd.forEach { polylineToAdd -> addPath(polylineToAdd) }
    }

    private fun addPath(path: Any?) {
        path ?: return

        val pathBuilder = PathBuilder(density = density)
        val pathId = Convert.interpretPath(path, pathBuilder)
        
        if (pathIdToController.containsKey(pathId)){
            val controller = pathIdToController[pathId]
            Convert.interpretPath(path, controller)
        }else {
            val pathOverlay = pathBuilder.build(pathId)
            pathOverlay.map = naverMap
            pathOverlay.onClickListener = listener
            val controller = PathController(pathOverlay, density)
            pathIdToController[pathId] = controller
        }
    }
    
    fun removePath(pathId : String) {
        if(!pathIdToController.containsKey(pathId)) return
        val pathOverlay = pathIdToController[pathId]?.getPathOverlay();
        pathOverlay?.map = null
        pathIdToController.remove(pathId);
    }
}