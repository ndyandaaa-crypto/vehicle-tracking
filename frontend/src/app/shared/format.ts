/** "5 h 30 min" for 330, "45 min" for 45 */
export function formatDuration(minutes: number): string {
  const h = Math.floor(minutes / 60);
  const m = minutes % 60;
  if (h === 0) return `${m} min`;
  return m === 0 ? `${h} h` : `${h} h ${m} min`;
}

export interface LicenseInfo {
  state: 'expired' | 'soon' | 'valid';
  label: string;
}

/** Licences expiring within 90 days are flagged. */
export function licenseInfo(expiry: string): LicenseInfo {
  const days = Math.floor((new Date(`${expiry}T00:00:00`).getTime() - Date.now()) / 86_400_000);
  if (days < 0) return { state: 'expired', label: 'Expired' };
  if (days <= 90) return { state: 'soon', label: 'Expires soon' };
  return { state: 'valid', label: 'Valid' };
}

export function initials(firstName: string, lastName: string): string {
  return `${firstName.charAt(0)}${lastName.charAt(0)}`.toUpperCase();
}

/** Gives each person a stable tile colour (tone-0 to tone-5, see styles.css). */
export function toneClass(id: number): string {
  return `tone-${id % 6}`;
}
