package utils;

import java.util.UUID;

public class TestDataGenerator {

    public static String generateUniqueEmail() {

        return "test_" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                + "@test.com";
    }
}