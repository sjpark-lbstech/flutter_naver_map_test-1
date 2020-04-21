part of flutter_naver_map;

/// [NaverMap]의 [PolylineOverlay]에 대한 유일 식별자
///
/// 전역적으로 유일할 필요는 없으며 목록상에서 유일하면 된다.
@immutable
class PolylineOverlayId {
  PolylineOverlayId(this.value) : assert(value != null);

  final String value;

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other.runtimeType != runtimeType) return false;
    final PolylineOverlayId typedOther = other;
    return value == typedOther.value;
  }

  @override
  int get hashCode => value.hashCode;

  @override
  String toString() {
    return 'PolylineOverlayId{value: $value}';
  }
}

/// 지도에 선을 나타내는 오버레이.
@immutable
class PolylineOverlay {
  /// 기본 전역 Z 인덱스.
  static const defaultGlobalZIndex = -200000;

  /// [PolylineOverlay]의 유일 식별자.
  ///
  /// null일 수 없습니다.
  final PolylineOverlayId polylineOverlayId;

  /// 좌표열을 지정합니다.
  ///
  /// null이거나 [List.length]가 2 미만일 수 없습니다.
  final List<LatLng> coords;

  /// 끝 지점의 모양을 지정합니다.
  ///
  /// 기본값은 [LineCap.butt]입니다.
  final LineCap capType;

  /// 색상을 지정합니다.
  ///
  /// 기본값은 [Colors.black]입니다.
  final Color color;

  /// 전역 Z 인덱스를 지정합니다.
  ///
  /// 여러 오버레이가 화면에서 겹쳐지면 전역 Z 인덱스가 큰 오버레이가 작은 오버레이를 덮습니다. 또한 값이 0 이상이면 오버레이가 심벌 위에, 0 미만이면 심벌 아래에 그려집니다.
  final int globalZIndex;

  /// 연결점의 모양을 지정합니다.
  ///
  /// 기본값은 [LineJoin.miter]입니다.
  final LineJoin joinType;

  /// 지도상에 표시할지를 지정합니다.
  ///
  /// 기본값은 true입니다.
  final bool visible;

  /// 점선 패턴을 지정합니다.
  ///
  /// 패턴은 픽셀 단위의 배열로 표현되며, 각각 2n번째 요소는 실선의 길이, 2n + 1번째 요소는 공백의 길이를 의미합니다.
  /// 빈 배열일 경우 실선이 됩니다.
  /// 기본값은 빈 배열입니다.
  final List<int> pattern;

  /// 테두리의 두께를 반환합니다. 픽셀 단위.
  ///
  /// 0일 경우 테두리가 그려지지 않습니다.
  /// 기본값은 5입니다.
  final int width;

  Map<String, dynamic> get json => {
    'polylineOverlayId': polylineOverlayId.value,
    'coords': coords.map<List<double>>((coord) => coord.json).toList(),
    'capType': capType.index,
    'color': color.value,
    'globalZIndex': globalZIndex,
    'joinType': joinType.index,
    'visible': visible,
    'pattern': pattern,
    'width': width,
  };

  /// 기본 생성자.
  ///
  /// [polylineOverlayId]는 null일 수 없습니다.
  /// [coords]는 null이거나 [List.length]가 2 미만일 수 없습니다.
  const PolylineOverlay(
      this.polylineOverlayId,
      this.coords, {
        this.capType = LineCap.butt,
        this.color = Colors.black,
        this.globalZIndex = defaultGlobalZIndex,
        this.joinType = LineJoin.miter,
        this.visible = true,
        this.pattern = const [],
        this.width = 5,
      })  : assert(polylineOverlayId != null),
        assert(coords != null),
        assert(coords.length > 1);

  /// 주어진 파라미터를 덮어씌운 새로운 [PolylineOverlay] 객체를 생성합니다.
  PolylineOverlay copyWith({
    List<LatLng> coordsParam,
    LineCap capTypeParam,
    Color colorParam,
    int globalZIndexParam,
    LineJoin joinTypeParam,
    bool visibleParam,
    List<int> patternParam,
    int widthParam,
  }) =>
      PolylineOverlay(
        polylineOverlayId,
        coordsParam ?? coords,
        capType: capTypeParam ?? capType,
        color: colorParam ?? color,
        globalZIndex: globalZIndexParam ?? globalZIndex,
        joinType: joinTypeParam ?? joinType,
        visible: visibleParam ?? visible,
        pattern: patternParam ?? pattern,
        width: widthParam ?? width,
      );

  /// 같은 값을 지닌 새로운 [PolylineOverlay] 객체를 생성합니다.
  PolylineOverlay clone() => copyWith(
    coordsParam: List<LatLng>.from(coords),
    patternParam: List<int>.from(pattern),
  );

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other.runtimeType != runtimeType) return false;
    final PolylineOverlay typedOther = other;
    return polylineOverlayId == typedOther.polylineOverlayId &&
        listEquals(coords, typedOther.coords) &&
        capType == typedOther.capType &&
        color == typedOther.color &&
        globalZIndex == typedOther.globalZIndex &&
        joinType == typedOther.joinType &&
        visible == typedOther.visible &&
        listEquals(pattern, typedOther.pattern) &&
        width == typedOther.width;
  }

  @override
  int get hashCode => polylineOverlayId.hashCode;
}

Map<PolylineOverlayId, PolylineOverlay> _keyByPolylineOverlayId(
    Iterable<PolylineOverlay> polylineOverlays) {
  if (polylineOverlays == null) return {};

  return Map<PolylineOverlayId, PolylineOverlay>.fromEntries(
      polylineOverlays.map((PolylineOverlay polylineOverlay) =>
          MapEntry<PolylineOverlayId, PolylineOverlay>(
              polylineOverlay.polylineOverlayId, polylineOverlay.clone())));
}

List<Map<String, dynamic>> _serializePolylineOverlaySet(
    Set<PolylineOverlay> polylineOverlays) {
  if (polylineOverlays == null) return null;

  return polylineOverlays
      .map<Map<String, dynamic>>((PolylineOverlay p) => p.json)
      .toList();
}

/// [PolylineOverlay]의 끝 지점의 모양.
enum LineCap {
  /// 평면. 끝 지점이 좌표에 딱 맞게 잘립니다.
  butt,

  /// 원형. 끝 지점에 지름이 두께만 한 원이 그려집니다.
  round,

  /// 사각형. 끝 지점에 두께의 반만큼의 직사각형이 추가됩니다.
  square,
}

/// [PolylineOverlay]의 연결점의 모양.
enum LineJoin {
  /// 사면. 연결점에서 뾰족하게 튀어나온 부분이 잘려나갑니다.
  bevel,

  /// 미터. 연결점이 뾰족하게 그려집니다.
  miter,

  /// 원형. 연결점이 둥글게 그려집니다.
  round
}
