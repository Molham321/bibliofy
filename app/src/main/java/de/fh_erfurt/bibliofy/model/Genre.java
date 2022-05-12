package de.fh_erfurt.bibliofy.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * is a database table where relevant information for genre can be stored.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(tableName = "genreTable")
public class Genre {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "genreId")
    private long genreId;

    @ColumnInfo(name = "description")
    private GenreType description;

    /**
     * enum type to define specific types of book Genre
     */
    public enum GenreType {
        Drama,
        Fable,
        Fantasy,
        Fiction,
        Horror,
        Humor
    }
}
