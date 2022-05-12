package de.fh_erfurt.bibliofy.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * is a database table where relevant information for books can be stored.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(tableName = "bookTable", indices = {@Index(value = {"isbn"}, unique = true)})
public class Book {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bookId")
    private long bookId;

    @ColumnInfo(name = "isbn")
    private String isbn;

    @ColumnInfo(name = "bookshelfId")
    private long bookshelfId;

    @ColumnInfo(name = "numberOfPages")
    private String numberOfPages;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "yearsOfPublication")
    private String yearsOfPublication;

    @ColumnInfo(name = "publisher")
    private String publisher;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "coverUrl")
    private String coverUrl;

    @ColumnInfo(name = "favourite", defaultValue = "0")
    private boolean favourite;

    @ColumnInfo(name = "wish", defaultValue = "0")
    private boolean wish;

    @ColumnInfo(name = "hadRead", defaultValue = "0")
    private boolean hadRead;

    /**
     * todo Mohammed update and add
     *
     * @param isbn
     * @param title
     * @param publisher
     * @param numberOfPages
     * @param description
     */
    public Book(@NonNull String isbn, @NonNull String title, @NonNull String publisher, @NonNull String numberOfPages, @NonNull String description) {

        this.isbn = isbn;
        this.title = title;
        this.publisher = publisher;
        this.numberOfPages = numberOfPages;
        this.description = description;
    }
}
