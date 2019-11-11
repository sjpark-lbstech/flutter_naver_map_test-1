import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_naver_map_test/flutter_naver_map_test.dart';

void main() => runApp(MaterialApp(home: TestMain(),));

class TestMain extends StatefulWidget {
  @override
  _TestMainState createState() => _TestMainState();
}

class _TestMainState extends State<TestMain> {
  Completer<NaverMapController> _controller = Completer<NaverMapController>();
  bool _toggle = false;
  
  OverlayImage _icon;

  @override
  Widget build(BuildContext context) {
    if(_icon == null){
      ImageConfiguration config = createLocalImageConfiguration(context);
      OverlayImage.fromAssetImage(config, 'assets/icon.png')
          .then((icon){
            setState(() {
              _icon = icon;
            });
      });
    }
    
    return Scaffold(
      appBar: AppBar(
        title: Text('네이버 맵 테스트', style: TextStyle(color: Colors.black87),),
        centerTitle: true,
        backgroundColor: Colors.white,
      ),
      body: _body(),
      floatingActionButton: FloatingActionButton(
        onPressed: _clickFab,
        backgroundColor: Colors.indigo,
        child: _toggle
            ? Icon(Icons.check, color: Colors.white,)
            : Icon(Icons.cancel, color: Colors.white,),
      ),
    );
  }

  _body() {
    return Container(
      child: NaverMap(
        initialCameraPosition: CameraPosition(
          target: LatLng(37.476943, 126.963677),
        ),
        onMapCreated: _onMapCreated,
        onMapTab: _onMapTab,
        onMapLongTab: _onMapLongTab,
        onMapDoubleTab: _onMapDoubleTab,
        onMapTwoFingerTab: _onMapTwoFingerTab,
        onSymbolTab: _onSymbolTab,
        onCameraMove: _onCameraMove,
        onCameraIdle: _onCameraIdle,
        mapType: _toggle
            ? MapType.Navi
            : MapType.Basic,
        indoorEnable: false,
        activeLayers: [MapLayer.LAYER_GROUP_BUILDING],
        nightModeEnable: true,
        initLocationTrackingMode: LocationTrackingMode.Follow,
        markers: [
          Marker(
            markerId: 'test_no.1',
            position: LatLng(37.476943, 126.963677),
            onMarkerTab: _onMarkerTab,
            infoWindow: '정보창 테스트1',
            icon: _icon,
          ),
          Marker(
            markerId: 'test_no.2',
            position: LatLng(37.476943, 126.963777),
            onMarkerTab: _onMarkerTab,
            infoWindow: '정보창 테스트2',
          ),
        ],
      ),
    );
  }

  void _onMapCreated(NaverMapController controller) {
    _controller.complete(controller);
  }

  void _clickFab() async{
    NaverMapController controller = await _controller.future;

    controller.moveCamera(CameraUpdate.scrollTo(LatLng(37.565156, 126.973580)));

  }


  void _onMarkerTab(Marker marker) {
    print(marker.markerId);
  }

  void _onMapTab(LatLng latLng) {
    print('single tab : ' + latLng.toString());
  }

  void _onMapLongTab(LatLng latLng) {
    print('long Tab : ' + latLng.toString());
  }

  void _onMapDoubleTab(LatLng latLng) {
    print('double Tab : ' + latLng.toString());
  }

  void _onMapTwoFingerTab(LatLng latLng) {
    print('two finger Tab : ' + latLng.toString());
  }

  void _onSymbolTab(LatLng position, String caption) {
    print('symbol Tab - LatLng : $position, caption : $caption');
  }

  void _onCameraMove() {
    print('camera move!');
  }

  void _onCameraIdle(){
    print('camera stop!');
  }
}
