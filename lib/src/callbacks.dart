part of flutter_naver_map;


typedef void MapCreateCallback(NaverMapController controller);

typedef void CameraPositionCallback(CameraPosition position);

typedef void OnMarkerTab(Marker marker);

typedef void OnMapTab(LatLng latLng);

typedef void OnMapLongTab(LatLng latLng);

typedef void OnMapDoubleTab(LatLng latLng);

typedef void OnMapTwoFingerTab(LatLng latLng);

typedef void OnSymbolTab(LatLng position, String caption);