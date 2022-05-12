package de.fh_erfurt.bibliofy.storage.bookGenre;

import android.app.Application;
import android.content.Context;

import java.util.concurrent.ExecutionException;

import de.fh_erfurt.bibliofy.model.relations.BookGenreCrossRef;
import de.fh_erfurt.bibliofy.storage.BibliofyDatabase;

public class BookGenreCrossRefRepository {

    private static BookGenreCrossRefRepository INSTANCE;
    private final BookGenreCrossRefDao bookGenreCrossRefDao;

    private BookGenreCrossRefRepository(Context context) {
        BibliofyDatabase db = BibliofyDatabase.getDatabase(context);
        this.bookGenreCrossRefDao = db.bookGenreCrossRefDao();
    }

    public static BookGenreCrossRefRepository getRepository(Application application) {
        if (INSTANCE == null) {
            synchronized (BookGenreCrossRefRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BookGenreCrossRefRepository(application);
                }
            }
        }

        return INSTANCE;
    }

    /**
     * insert one Enter to database
     *
     * @param bookGenreCrossRef
     * @return
     */
    public long insertAndWaitBookGenreCrossRef(BookGenreCrossRef bookGenreCrossRef) {
        try {
            return BibliofyDatabase.executeWithReturn(() -> bookGenreCrossRefDao.insertBookAuthorCrossRefDetails(bookGenreCrossRef));
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return -1;
    }


}
