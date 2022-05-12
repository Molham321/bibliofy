package de.fh_erfurt.bibliofy.storage.book;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import de.fh_erfurt.bibliofy.model.Book;
import de.fh_erfurt.bibliofy.model.relations.BookWithAuthor;
import de.fh_erfurt.bibliofy.model.relations.BookWithGenre;
import de.fh_erfurt.bibliofy.storage.BibliofyDatabase;

public class BookRepository {
    private static BookRepository INSTANCE;
    private BookDao bookDao;
    private LiveData<List<BookWithAuthor>> allBooksWithAuthors;

    private BookRepository(Context context) {
        BibliofyDatabase db = BibliofyDatabase.getDatabase(context);
        this.bookDao = db.bookDao();
    }

    public static BookRepository getRepository(Application application) {
        if (INSTANCE == null) {
            synchronized (BookRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BookRepository(application);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * insert one book from database
     *
     * @param book
     * @return
     */
    public long insertBook(Book book) {
        try {
            return BibliofyDatabase.executeWithReturn(() -> bookDao.insertBookWithDetails(book));
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Delete a book from the database
     *
     * @param book
     */
    public void deleteBook(Book book) {
        BibliofyDatabase.execute(() -> bookDao.deleteBook(book));
    }

    public void updateBook(Book book) {
        BibliofyDatabase.execute(() -> bookDao.update(book));
    }

    /**
     * update all Book Values by ID in the Database
     *
     * @param sTitle
     * @param sPublisher
     * @param sNumberOfPages
     * @param sISBN
     * @param sDescription
     * @param sYearsOfPublication
     * @param hadRead
     * @param favourite
     * @param wish
     * @param coverUrl
     * @param bookId
     */
    public void updateBookById(String sTitle, String sPublisher, String sNumberOfPages, String sISBN, String sDescription, String sYearsOfPublication, boolean hadRead, boolean favourite, boolean wish, String coverUrl, long bookId) {
        BibliofyDatabase.execute(() -> bookDao.updateBookById(sTitle, sPublisher, sNumberOfPages, sISBN, sDescription, sYearsOfPublication, hadRead, favourite, wish, coverUrl, bookId));
    }

    /**
     * the query returns a specific book that is to be displayed
     * the method is used to delete a specific book from the database
     *
     * @param bookId
     * @return
     */
    public Book getBook(long bookId) {
        return this.queryBook(() -> this.bookDao.getBook(bookId));
    }

    public LiveData<List<BookWithAuthor>> getAllBooksWithBookshelfName(String sBookshelfName) {
        return this.queryLiveDataWithAuthor(() -> this.bookDao.getAllBooksWithBookshelfName(sBookshelfName));
    }

    /**
     * get all books from database LiveData<List<BookWithAuthor>>
     * to show all books with author information in the homepage
     *
     * @return
     */
    public LiveData<List<BookWithAuthor>> getBooksWithAuthorsLiveData() {
        if (this.allBooksWithAuthors == null)
            this.allBooksWithAuthors = this.queryLiveDataWithAuthor(this.bookDao::getBooksWithAuthorsLiveData);

        return this.allBooksWithAuthors;
    }

    /**
     * to show a specific book with its author information in the "Book Details Fragment"
     *
     * @param bookId
     * @return
     */
    public LiveData<List<BookWithAuthor>> getBooksWithAuthorsLiveDataByBookId(long bookId) {
        return this.queryLiveData(() -> this.bookDao.getBooksWithAuthorsLiveDataByBookId(bookId));
    }

    /**
     * to show a specific book with its genre information in the  "Book Details Fragment"
     *
     * @param bookId
     * @return
     */
    public LiveData<List<BookWithGenre>> getBooksWithGenresLiveDataByBookId(long bookId) {
        return this.queryLiveData(() -> this.bookDao.getBooksWithGenresLiveDataByBookId(bookId));
    }

    /**
     * sort by favourite
     *
     * @return
     */
    public LiveData<List<BookWithAuthor>> getBooksSortedByFavourite() {
        return this.queryLiveDataWithAuthor(() -> this.bookDao.getBooksSortedByFavourite());
    }

    /**
     * sort by wish
     *
     * @return
     */
    public LiveData<List<BookWithAuthor>> getBooksSortedByWish() {
        return this.queryLiveDataWithAuthor(() -> this.bookDao.getBooksSortedByWish());
    }

    /**
     * sort by hadRead
     *
     * @return
     */
    public LiveData<List<BookWithAuthor>> getBooksSortedByHadRead() {
        return this.queryLiveDataWithAuthor(() -> this.bookDao.getBooksSortedByHadRead());
    }

    /**
     * the query returns a specific book that is to be displayed
     * the method is used to display a specific book from the database
     *
     * @param bookId
     * @return
     */
    public LiveData<Book> getBookByIdAsLiveData(long bookId) {
        return this.queryLiveData(() -> this.bookDao.getBookById(bookId));
    }

    //========================= query =========================//


    private Book queryBook(Callable<Book> query) {
        try {
            return BibliofyDatabase.executeWithReturn(query);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return new Book();
    }



    private <T> LiveData<T> queryLiveData(Callable<LiveData<T>> query) {
        try {
            return BibliofyDatabase.executeWithReturn(query);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return new MutableLiveData<>();
    }

    private LiveData<List<BookWithAuthor>> queryLiveDataWithAuthor(Callable<LiveData<List<BookWithAuthor>>> query) {
        try {
            return BibliofyDatabase.executeWithReturn(query);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return new MutableLiveData<>(Collections.emptyList());
    }



}
