export function formatTime(remainingTimeInSeconds: number) {
  const minutes = Math.floor(remainingTimeInSeconds / 60);
  const seconds = remainingTimeInSeconds % 60;
  return `${minutes.toFixed(0).padStart(2, "0")}:${seconds
    .toFixed(0)
    .padStart(2, "0")}`;
}
