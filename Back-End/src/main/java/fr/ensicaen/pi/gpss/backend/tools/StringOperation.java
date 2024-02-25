package fr.ensicaen.pi.gpss.backend.tools;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class StringOperation {
    private static final String UPPER_CASES_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS_POOL = "0123456789";

    private StringOperation() {
    }

    public static String generateRandomStringWithNumberAndUpperCase(int length, Random randomizer) {
        StringBuilder randomString = new StringBuilder();

        for (int i = 0; i < length; i++) {
            String pool = UPPER_CASES_POOL;
            if (randomizer.nextInt(101) >= 50) {
                pool = DIGITS_POOL;
            }
            int index = randomizer.nextInt(pool.length());
            randomString.append(pool.charAt(index));
        }

        return randomString.toString();
    }

    public static String generateRandomStringWithNumberAndUpperCase(int length) {
        return generateRandomStringWithNumberAndUpperCase(length, new SecureRandom());
    }

    public static String generateRandomStringWithNumberBaseOnSeed(int length, Random randomizer) {
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            randomString.append(DIGITS_POOL.charAt(randomizer.nextInt(DIGITS_POOL.length())));
        }
        return randomString.toString();
    }

    public static String generateRandomStringWithNumberBaseOnSeed(int length, String seed) {
        Random random = new SecureRandom(seed.getBytes());
        return generateRandomStringWithNumberBaseOnSeed(length, random);
    }

    public static String generateRandomStringWithNumber(int length) {
        return generateRandomStringWithNumberBaseOnSeed(length, new SecureRandom());
    }

    public static BigInteger stringToNumeric(String string) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            String toAdd = String.valueOf(ch);
            if (Character.isUpperCase(ch)) {
                toAdd = String.valueOf(Character.getNumericValue(ch));
            }
            result.append(toAdd);
        }
        return new BigInteger(result.toString());
    }
}