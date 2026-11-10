import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TrackingService } from './tracking.service';
import { RouteSummary } from './tracking.models';
import { formatDuration } from '../shared/format';

@Component({
  selector: 'app-route-list',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './route-list.component.html',
})
export class RouteListComponent {
  private readonly api = inject(TrackingService);

  readonly routes = signal<RouteSummary[]>([]);
  readonly loading = signal(true);
  readonly error = signal(false);
  readonly formatDuration = formatDuration;

  constructor() {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(false);
    this.api.routes().subscribe({
      next: (data) => {
        this.routes.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set(true);
        this.loading.set(false);
      },
    });
  }
}
