package de.fh_erfurt.bibliofy.view.fragments.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.fh_erfurt.bibliofy.model.relations.BookWithAuthor;
import de.fh_erfurt.bibliofy.storage.book.BookRepository;

/**
 * home View implementation
 */

public class HomeViewModel extends AndroidViewModel {

    private final BookRepository bookRepository;

    public HomeViewModel(Application application) {
        super(application);
        this.bookRepository = BookRepository.getRepository(application);
    }

    public LiveData<List<BookWithAuthor>> getBooksWithAuthors() {
        return this.bookRepository.getBooksWithAuthorsLiveData();
    }

    public LiveData<List<BookWithAuthor>> getBooksSortedByFavourite() {
        return this.bookRepository.getBooksSortedByFavourite();
    }

    public LiveData<List<BookWithAuthor>> getBooksSortedByWish() {
        return this.bookRepository.getBooksSortedByWish();
    }

    public LiveData<List<BookWithAuthor>> getBooksSortedByHadRead() {
        return this.bookRepository.getBooksSortedByHadRead();
    }
}