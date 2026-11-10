# Vehicle Tracking – Angular UI

Angular 18 (standalone components, signals) client for the Spring Boot API in `../backend`.

## Pages

- **Vehicles** – live table of tracked vehicles (status filter, refreshes every 5 seconds) and a detail page with position, driver, route and a route diagram.
- **Routes** – list of routes and a detail page with stops, a diagram and the vehicles driving it.
- **Drivers** – list of drivers and a detail page with contact details, licence status and assigned vehicle.

## Run

Start the backend first (see the top-level README), then:

```bash
npm install
npm start
```

Open http://localhost:4200.

## Build

```bash
npm run build
```

Output goes to `dist/vehicle-tracking-ui/browser`.

## API address

The backend address is set in `src/app/api.config.ts` (`API_URL`). Change it when the API runs somewhere other than `http://localhost:8080`, and add the UI's address to the backend's `CORS_ALLOWED_ORIGINS`.
