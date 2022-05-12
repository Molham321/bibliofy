package de.fh_erfurt.bibliofy.storage.bookshelf;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.fh_erfurt.bibliofy.model.Book;
import de.fh_erfurt.bibliofy.model.Genre;
import de.fh_erfurt.bibliofy.model.Bookshelf;

// Data Access Object definition for Contacts.
@Dao
public interface BookshelfDao {
//    @Insert
//    void insert(Bookshelf... bookshelves);
//
    @Insert
    long insertBookshelf (Bookshelf bookshelf);
    default long insertBookshelfDetails(Bookshelf bookshelf)
    {
        return this.insertBookshelf(bookshelf);
    }

    /**
     * update one book from database
     * @param bookshelf
     */
    @Update
    void update(Bookshelf bookshelf);
//
//    @Update
//    void update(Bookshelf... bookshelves);
//
//    @Delete
//    void delete(Bookshelf... bookshelves);
//
//    @Query("DELETE FROM bookshelfTable")
//    void deleteAll();
//
//    @Query("SELECT count(*) FROM bookshelfTable")
//    int count();

    @Query("SELECT * FROM bookshelfTable")
    List<Bookshelf> getBookshelves();

    @Query("SELECT * FROM bookshelfTable ORDER BY bookshelfName ASC")
    List<Bookshelf> getSectionsSortedByBookshelfName();

    @Query("SELECT * FROM bookshelfTable WHERE bookshelfName LIKE :search")
    List<Bookshelf> getBookshelvesForBookshelfName(String search);


    /**
     * update BookshelfName by ID in the Database
     * @param sBookshelfName
     */
    @Query("Update bookshelfTable SET bookshelfName = :sBookshelfName WHERE bookshelfId = :sBookshelfId")
    void updateBookshelf(String sBookshelfName,long sBookshelfId);

    @Query("SELECT * FROM bookshelfTable WHERE bookshelfId = :sBookshelfId")
    Bookshelf getBookshelfById(long sBookshelfId);

}
