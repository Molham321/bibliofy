package de.fh_erfurt.bibliofy.view.fragments.add;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import de.fh_erfurt.bibliofy.model.Author;
import de.fh_erfurt.bibliofy.model.Book;
import de.fh_erfurt.bibliofy.model.Bookshelf;
import de.fh_erfurt.bibliofy.model.Genre;
import de.fh_erfurt.bibliofy.model.relations.BookAuthorCrossRef;
import de.fh_erfurt.bibliofy.model.relations.BookGenreCrossRef;
import de.fh_erfurt.bibliofy.storage.author.AuthorRepository;
import de.fh_erfurt.bibliofy.storage.book.BookRepository;
import de.fh_erfurt.bibliofy.storage.bookAuthor.BookAuthorCrossRefRepository;
import de.fh_erfurt.bibliofy.storage.bookGenre.BookGenreCrossRefRepository;
import de.fh_erfurt.bibliofy.storage.bookshelf.BookshelfRepository;
import de.fh_erfurt.bibliofy.storage.genre.GenreRepository;


/**
 * add View implementation
 */

public class AddViewModel extends AndroidViewModel {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookshelfRepository bookshelfRepository;
    private final BookAuthorCrossRefRepository bookAuthorCrossRefRepository;
    private final BookGenreCrossRefRepository bookGenreCrossRefRepository;

    public AddViewModel(Application application) {
        super(application);
        this.bookRepository = BookRepository.getRepository(application);
        this.authorRepository = AuthorRepository.getRepository(application);
        this.genreRepository = GenreRepository.getRepository(application);
        this.bookshelfRepository = BookshelfRepository.getRepository(application);
        this.bookAuthorCrossRefRepository = BookAuthorCrossRefRepository.getRepository(application);
        this.bookGenreCrossRefRepository = BookGenreCrossRefRepository.getRepository(application);
    }

    public long saveAuthor(Author author) {
        return this.authorRepository.insertAndWaitAuthor(author);
    }

    public long saveBook(Book book) {
        return this.bookRepository.insertBook(book);
    }

    public long saveGenre(Genre genre) {
        return this.genreRepository.insertAndWaitGenre(genre);
    }

    public long saveBookshelf(Bookshelf bookshelf) {
        return this.bookshelfRepository.insertAndWaitBookshelf(bookshelf);
    }

    public long saveBookAuthorCrossRef(BookAuthorCrossRef bookAuthorCrossRef) {
        return this.bookAuthorCrossRefRepository.insertAndWaitBookAuthorCrossRef(bookAuthorCrossRef);
    }

    public long saveBookGenreCrossRef(BookGenreCrossRef bookGenreCrossRef) {
        return this.bookGenreCrossRefRepository.insertAndWaitBookGenreCrossRef(bookGenreCrossRef);
    }

}