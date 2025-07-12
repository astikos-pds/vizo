package br.app.vizo.util;

import java.util.Random;

public class CodeGenerator {

    public static String generate() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        int LENGTH = 6;
        for (int i = 0; i < LENGTH; i++) {
            sb.append(random.nextInt(0, 10));
        }

        return sb.toString();
    }
}
