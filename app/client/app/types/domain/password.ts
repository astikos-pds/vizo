export interface PasswordRequirements {
  minLength: {
    regex: RegExp;
    text: string;
  };
  hasNumber: {
    regex: RegExp;
    text: string;
  };
  hasLowercase: {
    regex: RegExp;
    text: string;
  };
  hasUppercase: {
    regex: RegExp;
    text: string;
  };
}
