import { Routes } from '@angular/router';
import { VehicleListComponent } from './tracking/vehicle-list.component';
import { VehicleDetailComponent } from './tracking/vehicle-detail.component';
import { RouteListComponent } from './tracking/route-list.component';
import { RouteDetailComponent } from './tracking/route-detail.component';
import { DriverListComponent } from './tracking/driver-list.component';
import { DriverDetailComponent } from './tracking/driver-detail.component';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'vehicles' },
  { path: 'vehicles', component: VehicleListComponent, title: 'Vehicles' },
  { path: 'vehicles/:id', component: VehicleDetailComponent, title: 'Vehicle' },
  { path: 'routes', component: RouteListComponent, title: 'Routes' },
  { path: 'routes/:id', component: RouteDetailComponent, title: 'Route' },
  { path: 'drivers', component: DriverListComponent, title: 'Drivers' },
  { path: 'drivers/:id', component: DriverDetailComponent, title: 'Driver' },
  { path: '**', redirectTo: 'vehicles' },
];
