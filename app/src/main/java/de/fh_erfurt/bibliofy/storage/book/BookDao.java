package de.fh_erfurt.bibliofy.storage.book;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.fh_erfurt.bibliofy.core.Helper;
import de.fh_erfurt.bibliofy.model.Book;
import de.fh_erfurt.bibliofy.model.relations.BookWithAuthor;
import de.fh_erfurt.bibliofy.model.relations.BookWithGenre;

/**
 * Data Access Object definition for Book.
 */

@Dao
public interface BookDao {

    /**
     * insert one book to database
     *
     * @param books
     * @return
     */
    @Insert
    long[] insert(Book... books);

    @Insert
    long insertBook(Book book);

    default long insertBookWithDetails(Book book) {
        // Prepare Book for saving
        if (book.getCoverUrl() == null || book.getCoverUrl().isEmpty()) {
            book.setCoverUrl(Helper.generateCoverImageUrl());
        }
        return this.insertBook(book);
    }

    /**
     * update one book from database
     *
     * @param book
     */
    @Update
    void update(Book book);

    /**
     * Delete a book from the database
     *
     * @param book
     */
    @Delete
    void deleteBook(Book book);

    /**
     * delete all books from database
     */
    @Query("DELETE FROM bookTable")
    void deleteAll();

    /**
     * get all books from database LiveData<List<BookWithAuthor>>
     * to show all books with author information in the homepage
     *
     * @return Live Data List of BookWithAuthor
     */
    @Query("SELECT * FROM bookTable")
    LiveData<List<BookWithAuthor>> getBooksWithAuthorsLiveData();

    /**
     * to show a specific book with its author information in the "Book Details Fragment"
     *
     * @param bookId
     * @return Live Data List of BookWithAuthor
     */
    @Query("SELECT * FROM bookTable WHERE bookId = :bookId")
    LiveData<List<BookWithAuthor>> getBooksWithAuthorsLiveDataByBookId(long bookId);

    /**
     * to show a specific book with its genre information in the  "Book Details Fragment"
     *
     * @param bookId
     * @return Live Data List of BookWithAuthor
     */
    @Query("SELECT * FROM bookTable WHERE bookId = :bookId")
    LiveData<List<BookWithGenre>> getBooksWithGenresLiveDataByBookId(long bookId);//


    /**
     * update all Book Values by ID in the Database
     *
     * @all
     */
    @Query("UPDATE bookTable SET title = :sTitle, publisher = :sPublisher, numberOfPages = :sNumberOfPages, isbn = :sISBN, description = :sDescription, yearsOfPublication = :sYearsOfPublication , hadRead = :hadRead, favourite = :favourite, wish = :wish,coverUrl = :coverUrl WHERE bookId = :bookId")
    void updateBookById(String sTitle, String sPublisher, String sNumberOfPages, String sISBN, String sDescription, String sYearsOfPublication, boolean hadRead, boolean favourite, boolean wish, String coverUrl, long bookId);


    /**
     * sort by favourite
     *
     * @return
     */
    @Query("SELECT * FROM bookTable WHERE favourite = 1")
    LiveData<List<BookWithAuthor>> getBooksSortedByFavourite();

    /**
     * sort by wish
     *
     * @return
     */
    @Query("SELECT * FROM bookTable WHERE wish = 1")
    LiveData<List<BookWithAuthor>> getBooksSortedByWish();

    /**
     * sort by hadRead
     *
     * @return
     */
    @Query("SELECT * FROM bookTable WHERE hadRead = 1")
    LiveData<List<BookWithAuthor>> getBooksSortedByHadRead();

    /**
     * the query returns a specific book that is to be displayed
     * the method is used to display a specific book from the database
     *
     * @param bookId
     * @return
     */
    @Query("SELECT * FROM bookTable WHERE bookId = :bookId")
    LiveData<Book> getBookById(long bookId);

    /**
     * the query returns a specific book that is to be displayed
     * the method is used to delete a specific book from the database
     *
     * @param bookId
     * @return
     */
    @Query("SELECT * FROM bookTable WHERE bookId = :bookId")
    Book getBook(long bookId);

    @Query("SELECT * FROM bookTable b JOIN bookshelfTable n on b.bookshelfId = n.bookshelfId WHERE n.bookshelfName = :sBookshelfName")
    LiveData<List<BookWithAuthor>> getAllBooksWithBookshelfName(String sBookshelfName);



}
