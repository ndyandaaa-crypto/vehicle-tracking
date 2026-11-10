import { Component, computed, inject, input, signal } from '@angular/core';
import { DatePipe, DecimalPipe, PercentPipe, TitleCasePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop';
import { switchMap, tap } from 'rxjs';
import { TrackingService } from './tracking.service';
import { VehicleDetail } from './tracking.models';
import { MapMarker, RouteMapComponent } from '../shared/route-map.component';
import { poll } from '../shared/poll';
import { formatDuration, licenseInfo } from '../shared/format';

@Component({
  selector: 'app-vehicle-detail',
  standalone: true,
  imports: [RouterLink, DatePipe, DecimalPipe, PercentPipe, TitleCasePipe, RouteMapComponent],
  templateUrl: './vehicle-detail.component.html',
})
export class VehicleDetailComponent {
  private readonly api = inject(TrackingService);

  /** Bound from the :id route parameter. */
  readonly id = input.required<string>();

  readonly detail = signal<VehicleDetail | null>(null);
  readonly loading = signal(true);
  readonly error = signal<string | null>(null);

  readonly formatDuration = formatDuration;
  readonly licenseInfo = licenseInfo;

  readonly markers = computed<MapMarker[]>(() => {
    const v = this.detail()?.vehicle;
    if (!v || v.latitude === null || v.longitude === null) return [];
    return [{ id: v.id, label: v.registrationNumber, latitude: v.latitude, longitude: v.longitude, status: v.status }];
  });

  constructor() {
    toObservable(this.id)
      .pipe(
        tap(() => {
          this.loading.set(true);
          this.error.set(null);
          this.detail.set(null);
        }),
        switchMap((id) => poll(() => this.api.vehicle(Number(id)))),
        takeUntilDestroyed(),
      )
      .subscribe((result) => {
        this.loading.set(false);
        if (result.data) {
          this.detail.set(result.data);
          this.error.set(null);
        } else if (result.error?.status === 404) {
          this.error.set("We couldn't find that vehicle.");
        } else {
          this.error.set("Can't reach the tracking API. This page keeps retrying.");
        }
      });
  }
}
