package de.fh_erfurt.bibliofy.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * is a database table where relevant information for authors can be stored.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(tableName = "authorTable")
public class Author {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "authorId")
    private long authorId;
    @ColumnInfo(name = "firstName")
    private String firstName;
    @ColumnInfo(name = "lastName")
    private String lastName;
    @ColumnInfo(name = "salutationType")
    private SalutationType salutationType;

    /**
     * assigns images to the exercises
     */
    public Author(@NonNull String firstName, @NonNull String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public enum SalutationType {
        Herr,
        Frau,
        Divers
    }
}
