package de.fh_erfurt.bibliofy.model.relations;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * an external table of book and genre who have n to m relationship to each other
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(primaryKeys = {"bookId", "genreId"}, tableName = "bookGenreCrossRefTable")
public class BookGenreCrossRef {

    @ColumnInfo(name = "bookId")
    private long bookId;

    @ColumnInfo(name = "genreId")
    private long genreId;
}
