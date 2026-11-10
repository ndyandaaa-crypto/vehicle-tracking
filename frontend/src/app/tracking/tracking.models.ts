export type VehicleStatus = 'MOVING' | 'IDLE' | 'MAINTENANCE';
export type VehicleType = 'TRUCK' | 'VAN' | 'CAR' | 'BUS';

export interface DriverRef {
  id: number;
  fullName: string;
  phone: string;
}

export interface RouteRef {
  id: number;
  name: string;
  origin: string;
  destination: string;
}

export interface Vehicle {
  id: number;
  registrationNumber: string;
  make: string;
  model: string;
  manufactureYear: number;
  type: VehicleType;
  status: VehicleStatus;
  latitude: number | null;
  longitude: number | null;
  speedKph: number | null;
  /** ISO timestamp of the latest position report */
  lastUpdated: string | null;
  /** 0 to 1 */
  routeProgress: number;
  driver: DriverRef | null;
  route: RouteRef | null;
}

export interface VehicleSummary {
  id: number;
  registrationNumber: string;
  make: string;
  model: string;
  type: VehicleType;
  status: VehicleStatus;
}

export interface Waypoint {
  name: string;
  latitude: number;
  longitude: number;
}

export interface RouteSummary {
  id: number;
  name: string;
  origin: string;
  destination: string;
  distanceKm: number;
  estimatedMinutes: number;
  vehicleCount: number;
}

export interface RouteDetail {
  id: number;
  name: string;
  origin: string;
  destination: string;
  distanceKm: number;
  estimatedMinutes: number;
  waypoints: Waypoint[];
  vehicles: Vehicle[];
}

export interface Driver {
  id: number;
  firstName: string;
  lastName: string;
  fullName: string;
  phone: string;
  email: string;
  licenseNumber: string;
  /** yyyy-MM-dd */
  licenseExpiry: string;
  vehicle: VehicleSummary | null;
}

export interface VehicleDetail {
  vehicle: Vehicle;
  driver: Driver | null;
  route: RouteDetail | null;
}
