part of flutter_naver_map;

/// [PolylineOverlay] update events to be applied to the [GoogleMap].
///
/// Used in [GoogleMapController] when the map is updated.
class _PolylineOverlayUpdates {
  /// Computes [_PolylineOverlayUpdates] given previous and current [PolylineOverlay]s.
  _PolylineOverlayUpdates.from(
      Set<PolylineOverlay> previous, Set<PolylineOverlay> current) {
    if (previous == null) {
      previous = Set<PolylineOverlay>.identity();
    }

    if (current == null) {
      current = Set<PolylineOverlay>.identity();
    }

    final Map<PolylineOverlayId, PolylineOverlay> previousPolylineOverlays =
        _keyByPolylineOverlayId(previous);
    final Map<PolylineOverlayId, PolylineOverlay> currentPolylineOverlays =
        _keyByPolylineOverlayId(current);

    final Set<PolylineOverlayId> prevPolylineOverlayIds =
        previousPolylineOverlays.keys.toSet();
    final Set<PolylineOverlayId> currentPolylineOverlayIds =
        currentPolylineOverlays.keys.toSet();

    PolylineOverlay idToCurrentPolylineOverlay(PolylineOverlayId id) {
      return currentPolylineOverlays[id];
    }

    final Set<PolylineOverlayId> _polylineOverlayIdsToRemove =
        prevPolylineOverlayIds.difference(currentPolylineOverlayIds);

    final Set<PolylineOverlay> _polylineOverlaysToAdd =
        currentPolylineOverlayIds
            .difference(prevPolylineOverlayIds)
            .map(idToCurrentPolylineOverlay)
            .toSet();

    /// Returns `true` if [current] is not equals to previous one with the
    /// same id.
    bool hasChanged(PolylineOverlay current) {
      final PolylineOverlay previous =
          previousPolylineOverlays[current.polylineOverlayId];
      return current != previous;
    }

    final Set<PolylineOverlay> _polylineOverlaysToChange =
        currentPolylineOverlayIds
            .intersection(prevPolylineOverlayIds)
            .map(idToCurrentPolylineOverlay)
            .where(hasChanged)
            .toSet();

    polylineOverlaysToAdd = _polylineOverlaysToAdd;
    polylineOverlayIdsToRemove = _polylineOverlayIdsToRemove;
    polylineOverlaysToChange = _polylineOverlaysToChange;
  }

  Set<PolylineOverlay> polylineOverlaysToAdd;
  Set<PolylineOverlayId> polylineOverlayIdsToRemove;
  Set<PolylineOverlay> polylineOverlaysToChange;

  Map<String, dynamic> _toMap() {
    final Map<String, dynamic> updateMap = <String, dynamic>{};

    void addIfNonNull(String fieldName, dynamic value) {
      if (value != null) {
        updateMap[fieldName] = value;
      }
    }

    addIfNonNull('polylineOverlaysToAdd',
        _serializePolylineOverlaySet(polylineOverlaysToAdd));
    addIfNonNull('polylineOverlaysToChange',
        _serializePolylineOverlaySet(polylineOverlaysToChange));
    addIfNonNull(
        'polylineOverlayIdsToRemove',
        polylineOverlayIdsToRemove
            .map<dynamic>((PolylineOverlayId m) => m.value)
            .toList());

    return updateMap;
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other.runtimeType != runtimeType) return false;
    final _PolylineOverlayUpdates typedOther = other;
    return setEquals(polylineOverlaysToAdd, typedOther.polylineOverlaysToAdd) &&
        setEquals(polylineOverlayIdsToRemove,
            typedOther.polylineOverlayIdsToRemove) &&
        setEquals(
            polylineOverlaysToChange, typedOther.polylineOverlaysToChange);
  }

  @override
  int get hashCode => hashValues(polylineOverlaysToAdd,
      polylineOverlayIdsToRemove, polylineOverlaysToChange);

  @override
  String toString() {
    return '_PolylineUpdates{polylineOverlaysToAdd: $polylineOverlaysToAdd, '
        'polylineIdsToRemove: $polylineOverlayIdsToRemove, '
        'polylineOverlaysToChange: $polylineOverlaysToChange}';
  }
}
