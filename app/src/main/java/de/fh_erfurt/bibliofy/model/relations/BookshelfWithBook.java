package de.fh_erfurt.bibliofy.model.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import de.fh_erfurt.bibliofy.model.Book;
import de.fh_erfurt.bibliofy.model.Bookshelf;

/**
 * is a definition of database relationship between bookshel and book
 */

public class BookshelfWithBook {
    @Embedded
    public Bookshelf bookshelf;
    @Relation(
            parentColumn = "bookshelfId",
            entityColumn = "bookshelfId"
    )
    public List<Book> books;
}
