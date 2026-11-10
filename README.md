# Vehicle tracking

A tracking dashboard: see the vehicles being tracked and where they are, their routes, and driver details.

| Folder | What it is | Runs on |
|---|---|---|
| `backend/` | Spring Boot 3 REST API, Java 17, in-memory H2, OpenAPI (Swagger UI) | http://localhost:8080 |
| `frontend/` | Angular 18 app that calls the API | http://localhost:4200 |

Vehicle movement is **simulated**: every few seconds each moving vehicle advances along its route, standing in for real GPS devices. Restarting the API resets all data.

## 1. Start the API

```bash
cd backend
mvn spring-boot:run
```

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- H2 console: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:trackingdb`, user `sa`, empty password)

Run the API tests with `mvn test`.

## 2. Start the Angular app

```bash
cd frontend
npm install
npm start
```

Then open http://localhost:4200.

## Endpoints

| Method | Path | Purpose |
|---|---|---|
| GET | `/api/vehicles` | List tracked vehicles with latest position. Optional `?status=MOVING`, `IDLE` or `MAINTENANCE` |
| GET | `/api/vehicles/{id}` | One vehicle with its driver and route (stops included) |
| GET | `/api/routes` | List routes |
| GET | `/api/routes/{id}` | One route with its stops and the vehicles on it |
| GET | `/api/drivers` | List drivers |
| GET | `/api/drivers/{id}` | One driver with licence details and assigned vehicle |

Unknown ids return 404 with a JSON problem body.

## Settings

| Setting | Where | Default |
|---|---|---|
| Port | `PORT` environment variable | 8080 |
| Allowed browser origins (CORS) | `CORS_ALLOWED_ORIGINS` environment variable (comma-separated) | http://localhost:4200 |
| Turn the movement simulation off | `tracking.simulation.enabled=false` in `application.properties` | on |
| API address used by the UI | `frontend/src/app/api.config.ts` | http://localhost:8080/api |

## Deploying

`backend/Dockerfile` builds and runs the API in a container, so it works on hosts such as Render (choose the Docker runtime and set the root directory to `backend`). For the frontend, use `npm install && npm run build` and publish `dist/vehicle-tracking-ui/browser`. Then point `api.config.ts` at the deployed API and set `CORS_ALLOWED_ORIGINS` to the frontend's address.
