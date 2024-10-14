/*
 * Copyright (c) Romanov Alexey, 2024
 */

package ru.romanow.memory.leaks;

import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Scanner;

public class EqualsAndHashCodeExample {
    private static final Logger logger = LoggerFactory.getLogger("EqualsAndHashCodeExample");

    public static void main(String[] args) {
        var input = "";
        var scanner = new Scanner(System.in);
        final var map = new HashMap<KeyHolder, Integer>();
        while (!input.equals("exit")) {
            for (int i = 0; i < 100_000; i++) {
                map.put(new KeyHolder("key"), i);
            }

            logger.info("Map size: {}", map.size());
            map.remove(new KeyHolder("key"));
            logger.info("Map size after remove: {}", map.size());

            System.out.println("Continue? (type 'exit' for exit)");
            input = scanner.nextLine();
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    private static class KeyHolder {
        private final String key;

        public KeyHolder(@NotNull String key) {
            this.key = key;
        }

        @NotNull
        public String key() {
            return key;
        }
    }

//    private record KeyHolder(@NotNull String key) {
//    }
}
