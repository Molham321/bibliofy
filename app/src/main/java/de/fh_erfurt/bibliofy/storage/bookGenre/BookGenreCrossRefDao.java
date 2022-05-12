package de.fh_erfurt.bibliofy.storage.bookGenre;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import de.fh_erfurt.bibliofy.model.relations.BookGenreCrossRef;

@Dao

public interface BookGenreCrossRefDao {

    /**
     * insert one Enter to database
     *
     * @param bookGenreCrossRefs
     */
    @Insert
    void insert(BookGenreCrossRef... bookGenreCrossRefs);

    @Insert
    long insertBookGenreCrossRef(BookGenreCrossRef bookGenreCrossRef);

    default long insertBookAuthorCrossRefDetails(BookGenreCrossRef bookGenreCrossRef) {
        return this.insertBookGenreCrossRef(bookGenreCrossRef);
    }

    /**
     * update one Enter from database
     *
     * @param bookGenreCrossRef
     */
    @Update
    void update(BookGenreCrossRef bookGenreCrossRef);

    /**
     * Delete all Enters from Database
     */
    @Query("DELETE FROM bookGenreCrossRefTable")
    void deleteAll();
}
