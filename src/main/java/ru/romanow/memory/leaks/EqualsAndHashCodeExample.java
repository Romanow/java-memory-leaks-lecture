/*
 * Copyright (c) Romanov Alexey, 2024
 */

package ru.romanow.memory.leaks;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class EqualsAndHashCodeExample {
    private static final Logger logger = LoggerFactory.getLogger("EqualsAndHashCodeExample");

    public static void main(String[] args) {
        final var map = new HashMap<KeyHolder, Integer>();
        for (int i = 0; i < 100; i++) {
            map.put(new KeyHolder("key"), i);
        }

        logger.info("Map size: {}", map.size());
        map.remove(new KeyHolder("key"));
        logger.info("Map size after remove: {}", map.size());
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
