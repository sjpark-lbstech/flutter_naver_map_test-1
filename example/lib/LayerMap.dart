import 'package:flutter/material.dart';
import 'package:flutter_naver_map_test/flutter_naver_map_test.dart';

class LayerMap extends StatefulWidget {
  final mapType;

  const LayerMap({Key key, @required this.mapType}) : super(key: key);

  @override
  _LayerMapState createState() => _LayerMapState();
}

class _LayerMapState extends State<LayerMap> {
  List<MapLayer> _layers = [
    MapLayer.LAYER_GROUP_BUILDING,
    MapLayer.LAYER_GROUP_TRAFFIC,
    MapLayer.LAYER_GROUP_TRANSIT,
    MapLayer.LAYER_GROUP_BICYCLE,
    MapLayer.LAYER_GROUP_MOUNTAIN,
    MapLayer.LAYER_GROUP_CADASTRAL,
  ];

  List<MapLayer> _selectedLayer = [MapLayer.LAYER_GROUP_BUILDING,];

  List<bool> _valuse = [true, false, false, false, false, false];

  @override
  void initState() {
    super.initState();

  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('타입별 레이어 확인', style: TextStyle(color: Colors.black),),
        centerTitle: true,
        backgroundColor: Colors.white,
        automaticallyImplyLeading: true,
      ),
      body: _body(),
    );
  }

  _body() {
    return Stack(
      children: <Widget>[
        NaverMap(
          mapType: widget.mapType,
          activeLayers: _selectedLayer,
        ),

        Align(
          alignment: Alignment.topLeft,
          child: Container(
            padding: EdgeInsets.all(16),
            child: Column(
              children: <Widget>[

              ],
            ),
          ),
        )
      ],
    );
  }
}
