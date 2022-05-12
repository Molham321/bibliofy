package de.fh_erfurt.bibliofy.model.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

import de.fh_erfurt.bibliofy.model.Author;
import de.fh_erfurt.bibliofy.model.Book;

/**
 * is a definition of database relationship between book and author
 */

public class BookWithAuthor {
    @Embedded
    public Book book;
    @Relation(
            parentColumn = "bookId",
            entity = Author.class,
            entityColumn = "authorId",
            associateBy = @Junction(BookAuthorCrossRef.class)
    )
    public List<Author> authors;
}
