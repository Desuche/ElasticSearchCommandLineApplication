package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;


import java.util.Scanner;

public class Main {

    private static ElasticsearchService elasticsearchService;

    private static void add(String[] input) {
        if (input.length < 2) {
            System.out.println("ERROR: Add request rejected. Check parameters");
            return;
        }

        switch (input[1]) {
            case "person":
                try {
                    Person person = new Person(input[2], input[3], Integer.parseInt(input[4]));
                    elasticsearchService.addPerson(person);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("ERROR: Add request rejected. Check parameters");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;

            default:
                System.out.printf("ERROR: adding %s objects not supported", input[1]);
                System.out.println();
        }
    }
    private static void search(String[] input){
        if (input.length < 2) {
            System.out.println("ERROR: Add request rejected. Check parameters");
            return;
        }

        switch (input[1]) {
            case "person":
                try {
                    elasticsearchService.searchPersonLastName(input[2]);
                    elasticsearchService.searchPersonFirstName(input[2]);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("ERROR: Add request rejected. Check parameters");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;

            default:
                System.out.printf("ERROR: %s objects not supported yet", input[1]);
                System.out.println();
        }
    }


    public static void main(String[] args) throws IOException {
        String propertiesFilePath = "config.properties"; // Add config.properties file to project root;

        try (InputStream input = Files.newInputStream(Paths.get(propertiesFilePath))) {
            Properties properties = new Properties();
            properties.load(input);

            properties.forEach((key, value) -> System.setProperty((String) key, (String) value));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        Main.elasticsearchService = new ElasticsearchService();


        System.out.println("Enter your commands: ");
        while (true) {
            String input = scanner.nextLine();
            String[] inputArray = input.split(" ");
            switch (inputArray[0]) {
                case "add":
                    add(inputArray);
                    break;
                case "search":
                    search(inputArray);
                    break;
                case "exit":
                    System.exit(0);
                    break;
                default:
                    System.out.printf("%s command is not recognised", inputArray[0]);
                    System.out.println();
            }
        }
    }
}