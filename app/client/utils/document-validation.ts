export function isDocumentValid(document: string): boolean {
  const cpf = document.replace(/\D/g, "");

  if (cpf.length !== 11) {
    return false;
  }

  if (/^(\d)\1{10}$/.test(cpf)) {
    return false;
  }

  let sum = 0;
  for (let i = 0; i < 9; i++) {
    sum += parseInt(cpf.charAt(i)) * (10 - i);
  }

  let remainder = (sum * 10) % 11;
  const digit1 = remainder === 10 || remainder === 11 ? 0 : remainder;

  if (digit1 !== parseInt(cpf.charAt(9))) {
    return false;
  }

  sum = 0;
  for (let i = 0; i < 10; i++) {
    sum += parseInt(cpf.charAt(i)) * (11 - i);
  }

  remainder = (sum * 10) % 11;
  const digit2 = remainder === 10 || remainder === 11 ? 0 : remainder;

  return digit2 === parseInt(cpf.charAt(10));
}
