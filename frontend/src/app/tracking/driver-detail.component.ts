import { Component, inject, input, signal } from '@angular/core';
import { DatePipe, TitleCasePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { toObservable, takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { switchMap, tap } from 'rxjs';
import { TrackingService } from './tracking.service';
import { Driver } from './tracking.models';
import { initials, licenseInfo, toneClass } from '../shared/format';

@Component({
  selector: 'app-driver-detail',
  standalone: true,
  imports: [RouterLink, DatePipe, TitleCasePipe],
  templateUrl: './driver-detail.component.html',
})
export class DriverDetailComponent {
  private readonly api = inject(TrackingService);

  /** Bound from the :id route parameter. */
  readonly id = input.required<string>();

  readonly driver = signal<Driver | null>(null);
  readonly loading = signal(true);
  readonly error = signal<string | null>(null);

  readonly initials = initials;
  readonly toneClass = toneClass;
  readonly licenseInfo = licenseInfo;

  constructor() {
    toObservable(this.id)
      .pipe(
        tap(() => {
          this.loading.set(true);
          this.error.set(null);
          this.driver.set(null);
        }),
        switchMap((id) => this.api.driver(Number(id))),
        takeUntilDestroyed(),
      )
      .subscribe({
        next: (d) => {
          this.driver.set(d);
          this.loading.set(false);
        },
        error: (err: HttpErrorResponse) => {
          this.error.set(
            err.status === 404
              ? "We couldn't find that driver."
              : "Could not load this driver. Check that the API is running, then try again.",
          );
          this.loading.set(false);
        },
      });
  }
}
