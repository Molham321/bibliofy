package de.fh_erfurt.bibliofy.storage;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.github.javafaker.Faker;

import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.fh_erfurt.bibliofy.converters.Converters;
import de.fh_erfurt.bibliofy.core.Helper;
import de.fh_erfurt.bibliofy.model.Author;
import de.fh_erfurt.bibliofy.model.Book;
import de.fh_erfurt.bibliofy.model.Bookshelf;
import de.fh_erfurt.bibliofy.model.Genre;
import de.fh_erfurt.bibliofy.model.relations.BookAuthorCrossRef;
import de.fh_erfurt.bibliofy.model.relations.BookGenreCrossRef;
import de.fh_erfurt.bibliofy.storage.author.AuthorDao;
import de.fh_erfurt.bibliofy.storage.book.BookDao;
import de.fh_erfurt.bibliofy.storage.bookAuthor.BookAuthorCrossRefDao;
import de.fh_erfurt.bibliofy.storage.bookGenre.BookGenreCrossRefDao;
import de.fh_erfurt.bibliofy.storage.bookshelf.BookshelfDao;
import de.fh_erfurt.bibliofy.storage.genre.GenreDao;

/**
 * Our Database Management class
 * room database for data storage and retrieval.
 */
@Database(entities = {Author.class, Book.class, Genre.class, Bookshelf.class, BookAuthorCrossRef.class, BookGenreCrossRef.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class BibliofyDatabase extends RoomDatabase {

    private static final String LOG_TAG_DB = "BibliofyDB";
    /**
     * Executor service to perform database operations asynchronous and independent from UI thread
     */
    private static final int NUMBER_OF_THREADS = 4;
    private static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    /**
     * Singleton Instance
     */
    private static volatile BibliofyDatabase INSTANCE;
    private static final RoomDatabase.Callback createCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {   //onOpen
            super.onCreate(db);

            Log.i(LOG_TAG_DB, "onCreate() called");

            execute(() -> {
                BookDao bookDao = INSTANCE.bookDao();
                bookDao.deleteAll();

                AuthorDao authorDao = INSTANCE.authorDao();
                authorDao.deleteAll();

                GenreDao genreDao = INSTANCE.genreDao();
                genreDao.deleteAll();

                BookAuthorCrossRefDao bookAuthorCrossRefDao = INSTANCE.bookAuthorCrossRefDao();
                bookAuthorCrossRefDao.deleteAll();

                BookGenreCrossRefDao bookGenreCrossRefDao = INSTANCE.bookGenreCrossRefDao();
                bookGenreCrossRefDao.deleteAll();

                Faker faker = Faker.instance();
                for (int i = 0; i < 10; i++) {
                    Book book = new Book();
                    book.setTitle(faker.book().title());
                    book.setCoverUrl(Helper.generateCoverImageUrl());
                    book.setWish(true);

                    long bookID;
                    bookID = bookDao.insert(book)[0];

                    Author author = new Author();
                    author.setSalutationType(Author.SalutationType.Herr);
                    author.setFirstName(faker.name().firstName());
                    author.setLastName(faker.name().lastName());
                    //author.setBirthday(new Date());

                    long authorID;
                    authorID = authorDao.insert(author)[0];

                    Log.i(LOG_TAG_DB, author.getAuthorId() + "");

                    BookAuthorCrossRef bookAuthorCrossRef = new BookAuthorCrossRef();

                    bookAuthorCrossRef.setBookId(bookID);
                    bookAuthorCrossRef.setAuthorId(authorID);

                    bookAuthorCrossRefDao.insert(bookAuthorCrossRef);

                    Genre genre = new Genre();
                    genre.setDescription(Genre.GenreType.Fable);

                    long genreID;
                    genreID = genreDao.insert(genre)[0];

                    BookGenreCrossRef bookGenreCrossRef = new BookGenreCrossRef();

                    bookGenreCrossRef.setBookId(bookID);
                    bookGenreCrossRef.setGenreId(genreID);

                    bookGenreCrossRefDao.insert(bookGenreCrossRef);


                }

                Log.i(LOG_TAG_DB, "Inserted 10 books to DB");

            });
        }
    };

    /**
     * Helper methods to ease external usage of ExecutorService
     * e.g. perform async database operations
     *
     * @param task
     * @param <T>
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static <T> T executeWithReturn(Callable<T> task)
            throws ExecutionException, InterruptedException {
        return databaseWriteExecutor.invokeAny(Collections.singletonList(task));
    }

    public static void execute(Runnable runnable) {
        databaseWriteExecutor.execute(runnable);
    }

    /**
     * Singleton 'getInstance' method to create database instance thereby opening and, if not
     * already done, init the database. Note the 'createCallback'.
     *
     * @param context
     * @return
     */
    public static BibliofyDatabase getDatabase(final Context context) {
        Log.i(LOG_TAG_DB, "getDatabase() called");
        if (INSTANCE == null) {
            synchronized (BibliofyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BibliofyDatabase.class, "bibliofy_db")
                            .addCallback(createCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Book DAO reference, will be filled by Android
     *
     * @return
     */
    public abstract BookDao bookDao();

    public abstract AuthorDao authorDao();

    public abstract GenreDao genreDao();

    public abstract BookshelfDao bookshelfDao();

    public abstract BookAuthorCrossRefDao bookAuthorCrossRefDao();

    public abstract BookGenreCrossRefDao bookGenreCrossRefDao();
}

