package de.fh_erfurt.bibliofy.model.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

import de.fh_erfurt.bibliofy.model.Book;
import de.fh_erfurt.bibliofy.model.Genre;

/**
 * is a definition of database relationship between genre and book
 */

public class GenreWithBook {
    @Embedded
    public Genre genre;
    @Relation(
            parentColumn = "genreId",
            entityColumn = "bookId",
            associateBy = @Junction(BookAuthorCrossRef.class)
    )
    public List<Book> books;
}
