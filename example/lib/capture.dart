import 'dart:async';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_naver_map_test/flutter_naver_map_test.dart';

class CaptureTest extends StatefulWidget {
  @override
  _CaptureTestState createState() => _CaptureTestState();
}

class _CaptureTestState extends State<CaptureTest> {
  Completer<NaverMapController> _controller = Completer();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: _appBar(),
      floatingActionButton: _fab(),
      body: _body(),
    );
  }

  _appBar() {
    return AppBar(
      centerTitle: true,
      title: Text(
        '지도 캡처',
      ),
    );
  }

  _fab() {
    return FloatingActionButton(
      onPressed: _onPressCapture,
      child: Icon(
        Icons.camera_enhance,
      ),
    );
  }

  _body() {
    return NaverMap(
      onMapCreated: _onMapCreated,
      initLocationTrackingMode: LocationTrackingMode.Face,
    );
  }


  void _onPressCapture() async{
    final controller = await _controller.future;
    final path = await controller.takeSnapshot();
    if (path != null) {
      showDialog(
        context: context,
        builder: (context){
          return AlertDialog(
            content: Image.file(
              File(path),
            ),
            contentPadding: EdgeInsets.all(0),
          );
        }
      );
    }else {
      print('캡처 실패');
    }
  }


  void _onMapCreated(NaverMapController controller) {
    if (_controller.isCompleted){
      _controller = Completer();
    }
    _controller.complete(controller);
  }
}
