export type Color = "error"
| "primary"
| "secondary"
| "success"
| "info"
| "warning"
| "neutral"
| undefined;

export interface Badge {
  color: Color;
  text: string;
}
