package de.fh_erfurt.bibliofy.storage.author;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import de.fh_erfurt.bibliofy.model.Author;
import de.fh_erfurt.bibliofy.storage.BibliofyDatabase;

public class AuthorRepository {

    private static AuthorRepository INSTANCE;
    private AuthorDao authorDao;

    private AuthorRepository(Context context) {
        BibliofyDatabase db = BibliofyDatabase.getDatabase(context);
        this.authorDao = db.authorDao();
    }

    public static AuthorRepository getRepository(Application application) {
        if (INSTANCE == null) {
            synchronized (AuthorRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AuthorRepository(application);
                }
            }
        }

        return INSTANCE;
    }

    /**
     * get all Authors from database
     *
     * @return
     */
    public List<Author> getAuthors() {
        return this.query(() -> this.authorDao.getAuthors());
    }

    /**
     * update all Author Values by ID in the Database
     *
     * @param firstName
     * @param lastName
     * @param salutationType
     * @param authorId
     */
    public void updateAuthorById(String firstName, String lastName, Author.SalutationType salutationType, long authorId) {
        BibliofyDatabase.execute(() -> authorDao.updateAuthorById(firstName, lastName, salutationType, authorId));
    }

    /**
     * insert one Author to database
     *
     * @param author
     * @return
     */
    public long insertAndWaitAuthor(Author author) {
        try {
            return BibliofyDatabase.executeWithReturn(() -> authorDao.insertAuthorDetails(author));
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return -1;
    }

    //========================= query =========================//


    private List<Author> query(Callable<List<Author>> query) {
        try {
            return BibliofyDatabase.executeWithReturn(query);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

}
