package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.library.Book;
import org.example.library.SmsMessage;
import org.example.library.Visitor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    /**
     * Читает JSON-файл из classpath и десериализует его в список посетителей.
     */
    private static List<Visitor> loadVisitors() {
        Gson gson = new GsonBuilder().create();
        try (InputStream is = Main.class.getClassLoader()
                .getResourceAsStream("books.json")) {
            if (is == null) {
                throw new RuntimeException("Файл books.json не найден в resources");
            }
            String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Visitor[] array = gson.fromJson(json, Visitor[].class);
            return List.of(array);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения books.json", e);
        }
    }

    // ======================== Задание 1 ========================

    /**
     * Выводит список всех посетителей и их количество.
     */
    private static void task1(List<Visitor> visitors) {
        System.out.println("=== Задание 1. Посетители библиотеки ===");
        long count = visitors.stream().count();
        System.out.println("Количество посетителей: " + count);
        visitors.stream().forEach(visitor ->
                System.out.println("  " + visitor.getSurname() + " " + visitor.getName()));
        System.out.println();
    }

    // ======================== Задание 2 ========================

    /**
     * Выводит список всех книг без повторений и их количество.
     */
    private static void task2(List<Visitor> visitors) {
        System.out.println("=== Задание 2. Книги без повторений ===");
        List<String> uniqueBooks = visitors.stream()
                .flatMap(v -> v.getFavoriteBooks().stream())
                .map(Book::getName)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        System.out.println("Количество уникальных книг: " + uniqueBooks.size());
        uniqueBooks.forEach(book -> System.out.println("  - " + book));
        System.out.println();
    }

    // ======================== Задание 3 ========================

    /**
     * Выводит все книги, отсортированные по году издания.
     */
    private static void task3(List<Visitor> visitors) {
        System.out.println("=== Задание 3. Сортировка книг по году издания ===");
        List<Book> sortedBooks = visitors.stream()
                .flatMap(v -> v.getFavoriteBooks().stream())
                .sorted(Comparator.comparing(Book::getPublishingYear))
                .collect(Collectors.toList());
        sortedBooks.forEach(book ->
                System.out.println("  " + book.getPublishingYear() + " — " + book.getName() + " (" + book.getAuthor() + ")"));
        System.out.println();
    }

    // ======================== Задание 4 ========================

    /**
     * Проверяет, есть ли у кого-то в списке избранных книг произведение автора "Jane Austen".
     */
    private static void task4(List<Visitor> visitors) {
        System.out.println("=== Задание 4. Поиск произведений Jane Austen ===");
        boolean found = visitors.stream()
                .flatMap(v -> v.getFavoriteBooks().stream())
                .anyMatch(b -> "Jane Austen".equals(b.getAuthor()));
        System.out.println("Произведения Jane Austen найдены: " + found);
        if (found) {
            visitors.stream()
                    .flatMap(v -> v.getFavoriteBooks().stream())
                    .filter(b -> "Jane Austen".equals(b.getAuthor()))
                    .map(Book::getName)
                    .distinct()
                    .forEach(title -> System.out.println("  — " + title));
        }
        System.out.println();
    }

    // ======================== Задание 5 ========================

    /**
     * Выводит максимальное число книг, добавленных одним посетителем в избранное.
     */
    private static void task5(List<Visitor> visitors) {
        System.out.println("=== Задание 5. Максимальное число избранных книг ===");
        int maxBooks = visitors.stream()
                .mapToInt(v -> v.getFavoriteBooks().size())
                .max()
                .orElse(0);
        System.out.println("Максимальное число книг у одного посетителя: " + maxBooks);
        System.out.println();
    }

    // ======================== Задание 6 ========================

    /**
     * Формирует и выводит SMS-сообщения для подписчиков, сгруппированные по категориям:
     *   - больше среднего → "you are a bookworm"
     *   - меньше среднего → "read more"
     *   - равно среднему → "fine"
     */
    private static void task6(List<Visitor> visitors) {
        System.out.println("=== Задание 6. SMS-сообщения подписчикам ===");

        double average = visitors.stream()
                .mapToInt(v -> v.getFavoriteBooks().size())
                .average()
                .orElse(0.0);

        System.out.println("Среднее число избранных книг: " + average);

        // Группировка подписчиков по категориям через groupingBy
        Map<String, List<Visitor>> grouped = visitors.stream()
                .filter(Visitor::isSubscribed)
                .collect(Collectors.groupingBy(visitor -> {
                    int bookCount = visitor.getFavoriteBooks().size();
                    if (bookCount > average) {
                        return "bookworm";
                    } else if (bookCount < average) {
                        return "read_more";
                    } else {
                        return "fine";
                    }
                }));

        List<SmsMessage> smsMessages = new ArrayList<>();

        grouped.getOrDefault("bookworm", List.of()).forEach(visitor ->
                smsMessages.add(new SmsMessage(visitor.getPhone(), "you are a bookworm")));
        grouped.getOrDefault("read_more", List.of()).forEach(visitor ->
                smsMessages.add(new SmsMessage(visitor.getPhone(), "read more")));
        grouped.getOrDefault("fine", List.of()).forEach(visitor ->
                smsMessages.add(new SmsMessage(visitor.getPhone(), "fine")));

        System.out.println("Сгенерировано SMS-сообщений: " + smsMessages.size());
        smsMessages.forEach(msg ->
                System.out.println("  " + msg.getPhoneNumber() + " — " + msg.getMessage()));
        System.out.println();
    }

    // ======================== Точка входа ========================

    public static void main(String[] args) {
        List<Visitor> visitors = loadVisitors();
        task1(visitors);
        task2(visitors);
        task3(visitors);
        task4(visitors);
        task5(visitors);
        task6(visitors);
        System.out.println("Все задания выполнены.");
    }
}
