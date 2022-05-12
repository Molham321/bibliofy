package de.fh_erfurt.bibliofy.view.fragments.library;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.fh_erfurt.bibliofy.model.Book;
import de.fh_erfurt.bibliofy.model.relations.BookWithAuthor;
import de.fh_erfurt.bibliofy.storage.book.BookRepository;
import de.fh_erfurt.bibliofy.storage.bookshelf.BookshelfRepository;

/**
 * library view implementation
 */

public class LibraryViewModel extends AndroidViewModel {

    private final BookshelfRepository bookshelfRepository;
    private final BookRepository bookRepository;


    public LibraryViewModel(Application application)
    {
        super(application);
        this.bookshelfRepository = BookshelfRepository.getRepository(application);
        this.bookRepository = BookRepository.getRepository(application);
    }


    public LiveData<List<BookWithAuthor>> getAllBooksWithBookshelfName(String sBookshelfName)
    {
        return this.bookRepository.getAllBooksWithBookshelfName(sBookshelfName);
    }

}