package br.app.vizo.core.user;

import br.app.vizo.core.shared.exception.InvalidDocumentException;

public record Document(
        String value
) {

    public Document {
        if (value == null || !isCPFValid(value)) {
            throw new InvalidDocumentException();
        }

        value = value.replaceAll("\\D", "");
    }

    private boolean isCPFValid(String cpf) {
        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11 || cpf.chars().distinct().count() == 1) {
            return false;
        }

        try {
            int dig1 = calculateDigit(cpf.substring(0, 9), 10);
            int dig2 = calculateDigit(cpf.substring(0, 9) + dig1, 11);
            return cpf.equals(cpf.substring(0, 9) + dig1 + dig2);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int calculateDigit(String str, int weightStart) {
        int sum = 0;
        for (int i = 0; i < str.length(); i++) {
            sum += Character.getNumericValue(str.charAt(i)) * (weightStart - i);
        }
        int remainder = sum % 11;
        return (remainder < 2) ? 0 : 11 - remainder;
    }

}
