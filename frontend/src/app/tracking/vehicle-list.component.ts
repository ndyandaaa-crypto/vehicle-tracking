import { Component, computed, inject, signal } from '@angular/core';
import { DatePipe, DecimalPipe, TitleCasePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { TrackingService } from './tracking.service';
import { Vehicle, VehicleStatus } from './tracking.models';
import { poll } from '../shared/poll';

type StatusFilter = 'ALL' | VehicleStatus;

@Component({
  selector: 'app-vehicle-list',
  standalone: true,
  imports: [RouterLink, DatePipe, DecimalPipe, TitleCasePipe],
  templateUrl: './vehicle-list.component.html',
})
export class VehicleListComponent {
  private readonly api = inject(TrackingService);

  readonly vehicles = signal<Vehicle[]>([]);
  readonly loading = signal(true);
  readonly error = signal(false);
  readonly filter = signal<StatusFilter>('ALL');

  readonly filters: { value: StatusFilter; label: string }[] = [
    { value: 'ALL', label: 'All' },
    { value: 'MOVING', label: 'Moving' },
    { value: 'IDLE', label: 'Idle' },
    { value: 'MAINTENANCE', label: 'Maintenance' },
  ];

  readonly counts = computed<Record<string, number>>(() => {
    const all = this.vehicles();
    return {
      ALL: all.length,
      MOVING: all.filter((v) => v.status === 'MOVING').length,
      IDLE: all.filter((v) => v.status === 'IDLE').length,
      MAINTENANCE: all.filter((v) => v.status === 'MAINTENANCE').length,
    };
  });

  readonly visible = computed(() => {
    const f = this.filter();
    return f === 'ALL' ? this.vehicles() : this.vehicles().filter((v) => v.status === f);
  });

  constructor() {
    poll(() => this.api.vehicles())
      .pipe(takeUntilDestroyed())
      .subscribe((result) => {
        this.loading.set(false);
        if (result.data) {
          this.vehicles.set(result.data);
          this.error.set(false);
        } else {
          this.error.set(true);
        }
      });
  }
}
