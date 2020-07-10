import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_naver_map_test/flutter_naver_map_test.dart';

class CircleOverlayTest extends StatefulWidget {
  @override
  _CircleOverlayTestState createState() => _CircleOverlayTestState();
}

class _CircleOverlayTestState extends State<CircleOverlayTest> {
  Completer<NaverMapController> _controller = Completer();
  List<CircleOverlay> _circles = [];

  int index = 0;

  int prevIndex;

  double sliderValue = 20;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: _body(),
    );
  }

  _body() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: <Widget>[

        Expanded(
          child: NaverMap(
            onMapCreated: _onMapCreated,
            initLocationTrackingMode: LocationTrackingMode.Face,
            onMapTab: _onMapTap,
            onSymbolTab: _onSymbolTap,
            circles: _circles,
          ),
        ),

        Container(
          padding: EdgeInsets.all(16),
          child: Slider(
            value: sliderValue,
            onChanged: _onSliderChange,
            label: '${sliderValue}m',
            min: 10,
            max: 1000,
          ),
        ),

      ],
    );
  }

  void _onMapCreated(NaverMapController controller) {
    if (_controller.isCompleted) _controller = Completer();
    _controller.complete(controller);
  }

  void _onMapTap(LatLng latLng) {
    print('지도 클릭');

    setState(() {
      _circles.add(CircleOverlay(
        overlayId: '$index',
        center: latLng,
        onTap: _onCircleTap,
        radius: 200,
        color: Colors.black26,
      ));
      index ++;
    });
  }


  void _onCircleTap(String overlayId) {
    int index = int.parse(overlayId);

    print('flutter 원형 오버레이 : $index');
    setState(() {
      if (prevIndex != null) _circles[prevIndex].color = Colors.black26;

      _circles[index].color = Colors.red.withOpacity(0.25);
      sliderValue = _circles[index].radius;

      prevIndex = index;
    });
  }

  void _onSliderChange(double value) {
    setState(() {
      sliderValue = value;
      _circles[prevIndex].radius = value;
    });
  }

  void _onSymbolTap(LatLng position, String caption) {
    print('심볼텝');
  }
}
