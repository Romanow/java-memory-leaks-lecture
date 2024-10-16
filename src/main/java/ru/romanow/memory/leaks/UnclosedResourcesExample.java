/*
 * Copyright (c) Romanov Alexey, 2024
 */
package ru.romanow.memory.leaks;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Scanner;

public class UnclosedResourcesExample {
    public static final String POEM = "Rudyard Kipling. If.txt";

    public static void main(String[] args)
            throws IOException {
        var input = "";
        var scanner = new Scanner(System.in);
        while (!input.equals("exit")) {
            final var classLoader = UnclosedResourcesExample.class.getClassLoader();

            var totalBytesRead = 0;
            for (int i = 0; i < 50_000; i++) {
                final var inputStream = new BufferedInputStream(classLoader.getResourceAsStream("data/" + POEM));
                totalBytesRead += inputStream.readAllBytes().length;

            }
            System.out.printf("Total bytes read: %d\n", totalBytesRead);

            System.out.print("Continue? (type 'exit' for exit): ");
            input = scanner.nextLine();
        }
    }
}
