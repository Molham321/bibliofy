package de.fh_erfurt.bibliofy.view.fragments.bookdetails;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.fh_erfurt.bibliofy.model.Book;
import de.fh_erfurt.bibliofy.model.relations.BookWithAuthor;
import de.fh_erfurt.bibliofy.model.relations.BookWithGenre;
import de.fh_erfurt.bibliofy.storage.book.BookRepository;

/**
 * book detail page view implementation
 */

public class BookDetailViewModel extends AndroidViewModel {

    private final BookRepository bookRepository;

    public BookDetailViewModel(Application application) {
        super(application);
        this.bookRepository = BookRepository.getRepository(application);
    }

    public LiveData<Book> getBook(long bookId) {
        return this.bookRepository.getBookByIdAsLiveData(bookId);
    }

    public Book getOneBook(long bookId) {
        return this.bookRepository.getBook(bookId);
    }

    public LiveData<List<BookWithAuthor>> getBooksWithAuthors(long bookId) {
        return this.bookRepository.getBooksWithAuthorsLiveDataByBookId(bookId);
    }

    public LiveData<List<BookWithGenre>> getBooksWithGenres(long bookId) {
        return this.bookRepository.getBooksWithGenresLiveDataByBookId(bookId);
    }

    public void deleteBook(Book book) {
        this.bookRepository.deleteBook(book);
    }

    public void updateBook(Book book) {
        this.bookRepository.updateBook(book);
    }

}