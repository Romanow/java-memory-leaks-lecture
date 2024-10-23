/*
 * Copyright (c) Romanov Alexey, 2024
 */
package ru.romanow.memory.leaks;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Executors;

import static ru.romanow.memory.leaks.utils.ExampleSupport.logMemory;

public class CustomClassLoaderExample {

    public static void main(String[] args) {
        logMemory();

        final var scanner = new Scanner(System.in);
        var url = CustomClassLoaderExample.class.getProtectionDomain().getCodeSource().getLocation();
        final var classLoader = new URLClassLoader(new URL[]{url}, null);
        final var executionService = Executors.newFixedThreadPool(10);
        while (true) {
            for (int i = 0; i < 10; i++) {
                executionService.execute(() -> loadClass(classLoader));
            }
            logMemory();

            System.out.println("Continue? (Ctrl+C for exit)");
            scanner.nextLine();
        }
    }

    private static void loadClass(ClassLoader classLoader) {
        try {
            final var cls = classLoader.loadClass("ru.romanow.memory.leaks.CustomClassLoaderExample$TestClass");
            var instance = cls.getDeclaredConstructor().newInstance();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static class TestClass {
        public final byte[] buffer = new byte[1024 * 1024];

        public TestClass() {
            new Random().nextBytes(this.buffer);
        }
    }
}
