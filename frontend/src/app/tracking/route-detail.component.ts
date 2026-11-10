import { Component, computed, inject, input, signal } from '@angular/core';
import { DecimalPipe, TitleCasePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop';
import { switchMap, tap } from 'rxjs';
import { TrackingService } from './tracking.service';
import { RouteDetail } from './tracking.models';
import { MapMarker, RouteMapComponent } from '../shared/route-map.component';
import { poll } from '../shared/poll';
import { formatDuration } from '../shared/format';

@Component({
  selector: 'app-route-detail',
  standalone: true,
  imports: [RouterLink, DecimalPipe, TitleCasePipe, RouteMapComponent],
  templateUrl: './route-detail.component.html',
})
export class RouteDetailComponent {
  private readonly api = inject(TrackingService);

  /** Bound from the :id route parameter. */
  readonly id = input.required<string>();

  readonly route = signal<RouteDetail | null>(null);
  readonly loading = signal(true);
  readonly error = signal<string | null>(null);
  readonly formatDuration = formatDuration;

  readonly markers = computed<MapMarker[]>(() =>
    (this.route()?.vehicles ?? [])
      .filter((v) => v.latitude !== null && v.longitude !== null)
      .map((v) => ({
        id: v.id,
        label: v.registrationNumber,
        latitude: v.latitude as number,
        longitude: v.longitude as number,
        status: v.status,
      })),
  );

  constructor() {
    toObservable(this.id)
      .pipe(
        tap(() => {
          this.loading.set(true);
          this.error.set(null);
          this.route.set(null);
        }),
        switchMap((id) => poll(() => this.api.route(Number(id)))),
        takeUntilDestroyed(),
      )
      .subscribe((result) => {
        this.loading.set(false);
        if (result.data) {
          this.route.set(result.data);
          this.error.set(null);
        } else if (result.error?.status === 404) {
          this.error.set("We couldn't find that route.");
        } else {
          this.error.set("Can't reach the tracking API. This page keeps retrying.");
        }
      });
  }
}
