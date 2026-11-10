import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL } from '../api.config';
import { Driver, RouteDetail, RouteSummary, Vehicle, VehicleDetail } from './tracking.models';

@Injectable({ providedIn: 'root' })
export class TrackingService {
  private readonly http = inject(HttpClient);

  vehicles(): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(`${API_URL}/vehicles`);
  }

  vehicle(id: number): Observable<VehicleDetail> {
    return this.http.get<VehicleDetail>(`${API_URL}/vehicles/${id}`);
  }

  routes(): Observable<RouteSummary[]> {
    return this.http.get<RouteSummary[]>(`${API_URL}/routes`);
  }

  route(id: number): Observable<RouteDetail> {
    return this.http.get<RouteDetail>(`${API_URL}/routes/${id}`);
  }

  drivers(): Observable<Driver[]> {
    return this.http.get<Driver[]>(`${API_URL}/drivers`);
  }

  driver(id: number): Observable<Driver> {
    return this.http.get<Driver>(`${API_URL}/drivers/${id}`);
  }
}
