package de.fh_erfurt.bibliofy.model.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

import de.fh_erfurt.bibliofy.model.Author;
import de.fh_erfurt.bibliofy.model.Book;

/**
 * is a definition of database relationship between author and book
 */
public class AuthorWithBook {
    @Embedded
    public Author author;
    @Relation(
            parentColumn = "authorId",
            entityColumn = "bookId",
            associateBy = @Junction(BookAuthorCrossRef.class)
    )
    public List<Book> books;
}
