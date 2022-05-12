package de.fh_erfurt.bibliofy.model.relations;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * an external table of book and author who have n to m relationship to each other
 */

@Entity(primaryKeys = {"bookId", "authorId"}, tableName = "bookAuthorCrossRefTable")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookAuthorCrossRef {

    @ColumnInfo(name = "bookId")
    private long bookId;

    @ColumnInfo(name = "authorId")
    private long authorId;
}
