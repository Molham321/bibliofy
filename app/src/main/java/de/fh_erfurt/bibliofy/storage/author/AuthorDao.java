package de.fh_erfurt.bibliofy.storage.author;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.fh_erfurt.bibliofy.model.Author;

/**
 * Data Access Object definition for Author.
 */

@Dao
public interface AuthorDao {

    /**
     * insert one Author to database
     *
     * @param authors
     * @return
     */
    @Insert
    long[] insert(Author... authors);

    @Insert
    long insertAuthor(Author author);

    default long insertAuthorDetails(Author author) {
        return this.insertAuthor(author);
    }

    /**
     * update one Author from database
     *
     * @param author
     */
    @Update
    void update(Author author);

    /**
     * update all Author Values by ID in the Database
     *
     * @all
     */
    @Query("UPDATE authorTable SET firstName = :firstName, lastName = :lastName, salutationType = :salutationType WHERE authorId = :authorId ")
    void updateAuthorById(String firstName, String lastName, Author.SalutationType salutationType, long authorId);

    /**
     * delete one Author from database
     *
     * @param authors
     */
    @Delete
    void delete(Author... authors);

    /**
     * delete all Authors from database
     */
    @Query("DELETE FROM authorTable")
    void deleteAll();

    /**
     * get all Authors from database
     *
     * @return
     */
    @Query("SELECT * from authorTable")
    List<Author> getAuthors();

    /**
     * sort Authors by title
     *
     * @return
     */
    @Query("SELECT * from authorTable ORDER BY lastname ASC")
    List<Author> getAuthorsSortedByLastname();

    /**
     * search Author by lastname
     *
     * @param search
     * @return
     */
    @Query("SELECT * FROM authorTable WHERE lastname LIKE :search")
    List<Author> getAuthorsForLastname(String search);
}
