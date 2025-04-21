package com.desafio.conexa.saude.conexa_backend.utils;

import java.util.UUID;

public class UuidGeneratorUtils {
    public static UUID generate() {
        return UUID.randomUUID();
    }
    public static String generateString() {
        return UUID.randomUUID().toString();
    }
}
