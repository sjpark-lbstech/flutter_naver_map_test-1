import 'package:flutter/material.dart';
import 'dart:async';
import 'package:flutter/services.dart';
import 'package:flutter_naver_map_test/flutter_naver_map_test.dart';
import 'package:flutter_naver_map_test_example/circle_overlay_test.dart';

import './typeMap.dart';
import 'capture.dart';

void main() => runApp(MaterialApp(home: TestMain(),));

class TestMain extends StatefulWidget {
  @override
  _TestMainState createState() => _TestMainState();
}

class _TestMainState extends State<TestMain> {
  Completer<NaverMapController> _controller = Completer<NaverMapController>();
  bool _toggle = false;

  OverlayImage _icon;
  List<String> menus = [
    '타입별 지도 확인',
    '지도 캡처 테스트',
    '원형 오버레이',
  ];

  @override
  Widget build(BuildContext context) {
    if (_icon == null) {
      ImageConfiguration config = createLocalImageConfiguration(context);
      OverlayImage.fromAssetImage(config, 'assets/icon.png').then((icon) {
        setState(() {
          _icon = icon;
        });
      });
    }

    return Scaffold(
      appBar: AppBar(
        title: Text(
          '네이버 맵 테스트',
          style: TextStyle(color: Colors.black87),
        ),
        centerTitle: true,
        backgroundColor: Colors.white,
      ),
      body: _body(),
      floatingActionButton: FloatingActionButton(
//        onPressed: _clickFab,
        backgroundColor: Colors.indigo,
        child: _toggle
            ? Icon(
                Icons.check,
                color: Colors.white,
              )
            : Icon(
                Icons.cancel,
                color: Colors.white,
              ),
      ),
    );
  }

  _body() {
    return Container(
      color: Colors.white,
      padding: EdgeInsets.all(16),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: menus.map((menu){
          return GestureDetector(
            onTap: ()=>_onMenuTab(menu),
            child: Container(
              color: Theme.of(context).primaryColor,
              padding: EdgeInsets.all(16),
              margin: EdgeInsets.symmetric(vertical: 8),
              child: Center(
                child: Text(
                  menu,
                  style: TextStyle(
                    color: Colors.white,
                  ),
                ),
              ),
            ),
          );
        }).toList(),
      ),
    );
  }

  _onMenuTab(String menu) {
    switch(menu){
      case '타입별 지도 확인':
        Navigator.of(context)
            .push(MaterialPageRoute(builder: (context)=>TypeMap()));
        break;
      case '지도 캡처 테스트' :
        Navigator.of(context)
            .push(MaterialPageRoute(builder: (context)=>CaptureTest()));
        break;
      case '원형 오버레이' :
        Navigator.of(context).push(MaterialPageRoute(builder: (_) => CircleOverlayTest()));
        break;
    }
  }

}
