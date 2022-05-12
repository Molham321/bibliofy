package de.fh_erfurt.bibliofy.model.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

import de.fh_erfurt.bibliofy.model.Book;
import de.fh_erfurt.bibliofy.model.Genre;

/**
 * is a definition of database relationship between book and genre
 */

public class BookWithGenre {
    @Embedded
    public Book book;
    @Relation(
            parentColumn = "bookId",
            entity = Genre.class,
            entityColumn = "genreId",
            associateBy = @Junction(BookGenreCrossRef.class)
    )
    public List<Genre> genres;
}
