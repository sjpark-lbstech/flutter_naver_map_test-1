import 'dart:async';

import 'package:flutter/material.dart';
import 'dart:async';
import 'package:flutter_naver_map_test/flutter_naver_map_test.dart';

class TypeMap extends StatefulWidget {
  @override
  _TypeMapState createState() => _TypeMapState();
}

class _TypeMapState extends State<TypeMap> {
  int _idx = 0;
  List<MapType> _types = [
    MapType.Basic,
    MapType.Navi,
    MapType.Hybrid,
    MapType.Satellite,
    MapType.Terrain,
  ];

  Completer<NaverMapController> completer = new Completer();

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('타입별 지도 확인', style: TextStyle(color: Colors.black),),
        centerTitle: true,
        backgroundColor: Colors.white,
        automaticallyImplyLeading: true,
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _tabFab,
        backgroundColor: Colors.white,
        child: Icon(Icons.cached, color: Colors.black54,),
      ),
      body: _body(),
    );
  }

  _body() {
    String mapType;
    if(_idx == 0) mapType = 'Basic';
    else if(_idx == 1) mapType = 'Navi';
    else if(_idx == 2) mapType = 'Hybrid';
    else if(_idx == 3) mapType = 'Satellite';
    else mapType = 'Terrain';

    return Stack(
      children: <Widget>[
        NaverMap(
          mapType: _types[_idx],
          markers: [
            Marker(
              markerId: "1",
              position: LatLng(37.559746, 126.964482),
              onMarkerTab: (m)=>print(m.markerId),
            ),
            Marker(
              markerId: '2',
              position: LatLng(37.559757, 126.964473),
              onMarkerTab: (m)=>print(m.markerId),
              consumeTapEvents: false,
            ),
          ],
          onMapTab: _onMapTabbed,
          onMapCreated: (cont)=> completer.complete(cont),
        ),

        Align(
          alignment: Alignment.topCenter,
          child: GestureDetector(
            onTap: (){

            },
            child: Card(
              margin: EdgeInsets.only(top: 16),
              child: Container(
                padding: EdgeInsets.symmetric(vertical: 8, horizontal: 16),
                child: Text(
                  mapType + '타입.\n레이어 알아보기',
                  style: TextStyle(color: Colors.black87),
                  textAlign: TextAlign.center,
                ),
              ),
            ),
          ),
        )
      ],
    );
  }

  void _tabFab() {
    setState(() {
      _idx++;
      if(_idx > 4) _idx = 0;
    });
  }

  void _onMapTabbed(LatLng latLng) async{
    NaverMapController controller = await completer.future;
    Map<String, int> map = await controller.getSize();
    print('width = ${map['width']} height = ${map['height']}');
  }
}
