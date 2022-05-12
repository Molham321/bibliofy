package de.fh_erfurt.bibliofy.storage.bookshelf;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import de.fh_erfurt.bibliofy.model.Author;
import de.fh_erfurt.bibliofy.model.Book;
import de.fh_erfurt.bibliofy.model.Bookshelf;
import de.fh_erfurt.bibliofy.storage.BibliofyDatabase;

public class BookshelfRepository {

//    public static final String LOG_TAG = "BookshelfRepository";

    private BookshelfDao bookshelfDao;

    private static BookshelfRepository INSTANCE;

    public static BookshelfRepository getRepository(Application application)
    {
        if (INSTANCE == null)
        {
            synchronized ( BookshelfRepository.class )
            {
                if (INSTANCE == null)
                {
                    INSTANCE = new BookshelfRepository( application );
                }
            }
        }

        return INSTANCE;
    }

    private BookshelfRepository(Context context)
    {
        BibliofyDatabase db = BibliofyDatabase.getDatabase( context );
        this.bookshelfDao = db.bookshelfDao();
    }

    private Bookshelf queryBookshelf(Callable<Bookshelf> query) {
        try {
            return BibliofyDatabase.executeWithReturn(query);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return new Bookshelf();
    }

//    public List<Bookshelf> getBookshelves()
//    {
//        return this.query( () -> this.bookshelfDao.getBookshelves() );
//    }
//
//    public List<Bookshelf> getBookshelvesForBookshelfName(String search)
//    {
//        return this.query( () -> this.bookshelfDao.getBookshelvesForBookshelfName( search ) );
//    }
//
//    public List<Bookshelf> getSectionsSortedByBookshelfName()
//    {
//        return this.query(() -> this.bookshelfDao.getSectionsSortedByBookshelfName());
//    }

    private List<Bookshelf> query(Callable<List<Bookshelf>> query)
    {
        try {
            return BibliofyDatabase.executeWithReturn( query );
        }
        catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public void updateBookshelf(Bookshelf bookshelf)
    {
        BibliofyDatabase.execute( () -> bookshelfDao.update(bookshelf) );
    }

    public void updateBookshelf(String sBookshelfName,long sBookshelfId)
    {
        BibliofyDatabase.execute( () -> bookshelfDao.updateBookshelf(sBookshelfName,sBookshelfId) );
    }

    public Bookshelf getBookshelfById(long sBookshelfId)
    {
       return this.queryBookshelf( () -> bookshelfDao.getBookshelfById(sBookshelfId) );
    }

    public long insertAndWaitBookshelf(Bookshelf bookshelf) {
        try {
            return BibliofyDatabase.executeWithReturn( () -> bookshelfDao.insertBookshelfDetails(bookshelf) );
        }
        catch (ExecutionException | InterruptedException e)
        {
            e.printStackTrace();
        }

        return -1;
    }

}
