package de.fh_erfurt.bibliofy.storage.bookAuthor;

import android.app.Application;
import android.content.Context;

import java.util.concurrent.ExecutionException;

import de.fh_erfurt.bibliofy.model.relations.BookAuthorCrossRef;
import de.fh_erfurt.bibliofy.storage.BibliofyDatabase;

public class BookAuthorCrossRefRepository {

    private static BookAuthorCrossRefRepository INSTANCE;
    private BookAuthorCrossRefDao bookAuthorCrossRefDao;

    private BookAuthorCrossRefRepository(Context context) {
        BibliofyDatabase db = BibliofyDatabase.getDatabase(context);
        this.bookAuthorCrossRefDao = db.bookAuthorCrossRefDao();
    }

    public static BookAuthorCrossRefRepository getRepository(Application application) {
        if (INSTANCE == null) {
            synchronized (BookAuthorCrossRefRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BookAuthorCrossRefRepository(application);
                }
            }
        }

        return INSTANCE;
    }

    /**
     * insert one Enter to database
     *
     * @param bookAuthorCrossRef
     * @return
     */
    public long insertAndWaitBookAuthorCrossRef(BookAuthorCrossRef bookAuthorCrossRef) {
        try {
            return BibliofyDatabase.executeWithReturn(() -> bookAuthorCrossRefDao.insertBookAuthorCrossRefDetails(bookAuthorCrossRef));
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return -1;
    }

}
