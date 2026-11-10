import { Component, computed, input } from '@angular/core';
import { VehicleStatus, Waypoint } from '../tracking/tracking.models';

export interface MapMarker {
  id: number;
  label: string;
  latitude: number;
  longitude: number;
  status: VehicleStatus;
}

interface Point {
  x: number;
  y: number;
}

type Projector = (lat: number, lon: number) => Point;

/**
 * Draws a route and vehicle positions as a simple SVG diagram. It needs no map service or
 * API key: coordinates are scaled to fit, so it shows shape and relative position only.
 */
@Component({
  selector: 'app-route-map',
  standalone: true,
  templateUrl: './route-map.component.html',
})
export class RouteMapComponent {
  readonly waypoints = input.required<Waypoint[]>();
  readonly markers = input<MapMarker[]>([]);

  readonly width = 640;
  readonly height = 380;
  private readonly padX = 76;
  private readonly padY = 56;

  private readonly project = computed<Projector>(() => {
    const points = [...this.waypoints(), ...this.markers()];
    if (points.length === 0) {
      return () => ({ x: 0, y: 0 });
    }
    const lats = points.map((p) => p.latitude);
    const lons = points.map((p) => p.longitude);
    const minLat = Math.min(...lats);
    const maxLat = Math.max(...lats);
    const minLon = Math.min(...lons);
    const maxLon = Math.max(...lons);

    // Longitude degrees get narrower away from the equator, so shrink them to keep the shape true.
    const lonFactor = Math.cos((((minLat + maxLat) / 2) * Math.PI) / 180);
    const spanX = Math.max((maxLon - minLon) * lonFactor, 1e-6);
    const spanY = Math.max(maxLat - minLat, 1e-6);
    const scale = Math.min((this.width - 2 * this.padX) / spanX, (this.height - 2 * this.padY) / spanY);
    const offsetX = (this.width - spanX * scale) / 2;
    const offsetY = (this.height - spanY * scale) / 2;

    return (lat, lon) => ({
      x: offsetX + (lon - minLon) * lonFactor * scale,
      y: offsetY + (maxLat - lat) * scale,
    });
  });

  readonly stops = computed(() =>
    this.waypoints().map((w) => ({ ...this.project()(w.latitude, w.longitude), name: w.name })),
  );

  readonly line = computed(() =>
    this.stops()
      .map((s) => `${s.x.toFixed(1)},${s.y.toFixed(1)}`)
      .join(' '),
  );

  readonly pins = computed(() =>
    this.markers().map((m) => ({
      ...this.project()(m.latitude, m.longitude),
      id: m.id,
      label: m.label,
      status: m.status,
    })),
  );

  readonly description = computed(() => {
    const names = this.waypoints().map((w) => w.name).join(', then ');
    return `Route diagram: ${names}. ${this.markers().length} vehicle position(s) shown.`;
  });
}
