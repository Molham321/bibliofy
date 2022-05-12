package de.fh_erfurt.bibliofy.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * is a database table where relevant information for bookshelf can be stored.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(tableName = "bookshelfTable")
public class Bookshelf {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bookshelfId")
    private long bookshelfId;

    @ColumnInfo(name = "bookshelfName")
    private String bookshelfName;
}
