package de.fh_erfurt.bibliofy.view.fragments.update;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.fh_erfurt.bibliofy.model.Author;
import de.fh_erfurt.bibliofy.model.Book;
import de.fh_erfurt.bibliofy.model.Bookshelf;
import de.fh_erfurt.bibliofy.model.Genre;
import de.fh_erfurt.bibliofy.model.relations.BookWithAuthor;
import de.fh_erfurt.bibliofy.model.relations.BookWithGenre;
import de.fh_erfurt.bibliofy.storage.author.AuthorRepository;
import de.fh_erfurt.bibliofy.storage.book.BookRepository;
import de.fh_erfurt.bibliofy.storage.bookshelf.BookshelfRepository;
import de.fh_erfurt.bibliofy.storage.genre.GenreRepository;

/**
 * update View implementation
 */

public class UpdateViewModel extends AndroidViewModel {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookshelfRepository bookshelfRepository;


    public UpdateViewModel(Application application) {
        super(application);
        this.bookRepository = BookRepository.getRepository(application);
        this.authorRepository = AuthorRepository.getRepository(application);
        this.genreRepository = GenreRepository.getRepository(application);
        this.bookshelfRepository = BookshelfRepository.getRepository(application);
    }


    public Book getOneBook(long bookId) {
        return this.bookRepository.getBook(bookId);
    }

    public LiveData<Book> getBook(long bookId) {
        return this.bookRepository.getBookByIdAsLiveData(bookId);
    }

    public LiveData<List<BookWithAuthor>> getBooksWithAuthors(long bookId) {
        return this.bookRepository.getBooksWithAuthorsLiveDataByBookId(bookId);
    }

    public LiveData<List<BookWithGenre>> getBooksWithGenres(long bookId) {
        return this.bookRepository.getBooksWithGenresLiveDataByBookId(bookId);
    }


    public void updateAuthorById(String firstName, String lastName, Author.SalutationType salutationType, long authorId) {
        this.authorRepository.updateAuthorById(firstName, lastName, salutationType, authorId);
    }

    public void updateBookById(String sTitle, String sPublisher, String sNumberOfPages, String sISBN, String sDescription, String sYearsOfPublication, boolean hadRead, boolean favourite, boolean wish, String coverUrl, long bookId) {
        this.bookRepository.updateBookById(sTitle, sPublisher, sNumberOfPages, sISBN, sDescription, sYearsOfPublication, hadRead, favourite, wish, coverUrl, bookId);
    }

    public void updateGenreById(Genre.GenreType sDescription, long sGenreId) {
        this.genreRepository.updateGenreById(sDescription, sGenreId);
    }

    public void updateBookshelf(String sBookshelfName, long sBookshelfId) {
        this.bookshelfRepository.updateBookshelf(sBookshelfName, sBookshelfId);
    }

    public Bookshelf getOneBookshelf(long sBookshelfId) {
       return this.bookshelfRepository.getBookshelfById(sBookshelfId);
    }
}