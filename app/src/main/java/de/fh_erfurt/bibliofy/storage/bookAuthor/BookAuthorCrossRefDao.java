package de.fh_erfurt.bibliofy.storage.bookAuthor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import de.fh_erfurt.bibliofy.model.relations.BookAuthorCrossRef;

@Dao
public interface BookAuthorCrossRefDao {

    /**
     * insert one Enter to database
     *
     * @param bookAuthorCrossRefs
     */
    @Insert
    void insert(BookAuthorCrossRef... bookAuthorCrossRefs);

    @Insert
    long insertBookAuthorCrossRef(BookAuthorCrossRef bookAuthorCrossRef);

    default long insertBookAuthorCrossRefDetails(BookAuthorCrossRef bookAuthorCrossRef) {
        return this.insertBookAuthorCrossRef(bookAuthorCrossRef);
    }

    /**
     * update one book from database
     *
     * @param bookAuthorCrossRef
     */
    @Update
    void update(BookAuthorCrossRef bookAuthorCrossRef);

    /**
     * delete all Entries from database
     */
    @Query("DELETE FROM bookAuthorCrossRefTable")
    void deleteAll();

}
