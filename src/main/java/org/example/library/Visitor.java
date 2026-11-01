package org.example.library;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Посетитель библиотеки с персональными данными и списком избранных книг.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Visitor {

    private String name;
    private String surname;
    private String phone;
    private boolean subscribed;
    private List<Book> favoriteBooks;
}
