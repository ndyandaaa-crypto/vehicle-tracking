import { Component, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { TrackingService } from './tracking.service';
import { Driver } from './tracking.models';
import { initials, licenseInfo, toneClass } from '../shared/format';

@Component({
  selector: 'app-driver-list',
  standalone: true,
  imports: [RouterLink, DatePipe],
  templateUrl: './driver-list.component.html',
})
export class DriverListComponent {
  private readonly api = inject(TrackingService);

  readonly drivers = signal<Driver[]>([]);
  readonly loading = signal(true);
  readonly error = signal(false);

  readonly initials = initials;
  readonly toneClass = toneClass;
  readonly licenseInfo = licenseInfo;

  constructor() {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(false);
    this.api.drivers().subscribe({
      next: (data) => {
        this.drivers.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set(true);
        this.loading.set(false);
      },
    });
  }
}
