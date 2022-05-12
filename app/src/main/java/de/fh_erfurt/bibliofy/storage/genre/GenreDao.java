package de.fh_erfurt.bibliofy.storage.genre;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import de.fh_erfurt.bibliofy.model.Genre;

// Data Access Object definition for Contacts.
@Dao
public interface GenreDao {

    /**
     * add one Genre to database
     *
     * @param genres
     * @return
     */
    @Insert
    long[] insert(Genre... genres);

    @Insert
    long insertGenre(Genre genre);

    default long insertGenreDetails(Genre genre) {
        return this.insertGenre(genre);
    }

    /**
     * update one book from database
     *
     * @param genre
     */
    @Update
    void update(Genre genre);

    /**
     * delete all Genre from database
     */
    @Query("DELETE FROM genreTable")
    void deleteAll();

    /**
     * update GenreType by ID in the Database
     *
     * @param sDescription
     */
    @Query("Update genreTable SET description = :sDescription WHERE genreId = :sGenreId")
    void updateGenreById(Genre.GenreType sDescription, long sGenreId);

}
