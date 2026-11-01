package org.example.library;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Книга из списка избранного посетителя библиотеки.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    private String name;
    private String author;
    private int publishingYear;
    private String isbn;
    private String publisher;
}
