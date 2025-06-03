export function isTokenExpired(token: string | undefined): boolean {
  if (!token) return true;

  try {
    const payload = JSON.parse(atob(token.split(".")[1]));
    const exp = payload;

    if (!exp) return true;

    const now = Math.floor(Date.now() / 1000);
    return exp < now;
  } catch (e) {
    return true;
  }
}
