package de.fh_erfurt.bibliofy.storage.genre;

import android.app.Application;
import android.content.Context;

import java.util.concurrent.ExecutionException;

import de.fh_erfurt.bibliofy.model.Genre;
import de.fh_erfurt.bibliofy.storage.BibliofyDatabase;

public class GenreRepository {

    private static GenreRepository INSTANCE;
    private final GenreDao genreDao;

    private GenreRepository(Context context) {
        BibliofyDatabase db = BibliofyDatabase.getDatabase(context);
        this.genreDao = db.genreDao();
    }

    public static GenreRepository getRepository(Application application) {
        if (INSTANCE == null) {
            synchronized (GenreRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GenreRepository(application);
                }
            }
        }

        return INSTANCE;
    }

    /**
     * update GenreType by ID in the Database
     *
     * @param sDescription
     * @param sGenreId
     */
    public void updateGenreById(Genre.GenreType sDescription, long sGenreId) {
        BibliofyDatabase.execute(() -> genreDao.updateGenreById(sDescription, sGenreId));
    }

    /**
     * add one Genre to database
     *
     * @param genre
     * @return
     */
    public long insertAndWaitGenre(Genre genre) {
        try {
            return BibliofyDatabase.executeWithReturn(() -> genreDao.insertGenreDetails(genre));
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return -1;
    }

}
