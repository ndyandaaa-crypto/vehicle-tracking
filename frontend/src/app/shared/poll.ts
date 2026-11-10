import { HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, map, of, switchMap, takeWhile, timer } from 'rxjs';

export interface PollResult<T> {
  data: T | null;
  error: HttpErrorResponse | null;
}

/**
 * Calls the API now and then every few seconds. A failed call doesn't stop the polling
 * (the next tick tries again), except a 404, which ends it because the item doesn't exist.
 */
export function poll<T>(request: () => Observable<T>, intervalMs = 5000): Observable<PollResult<T>> {
  return timer(0, intervalMs).pipe(
    switchMap(() =>
      request().pipe(
        map((data): PollResult<T> => ({ data, error: null })),
        catchError((error: HttpErrorResponse) => of<PollResult<T>>({ data: null, error })),
      ),
    ),
    takeWhile((result) => result.error?.status !== 404, true),
  );
}
