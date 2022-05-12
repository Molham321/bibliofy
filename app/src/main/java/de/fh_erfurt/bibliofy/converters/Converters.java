package de.fh_erfurt.bibliofy.converters;

import androidx.room.TypeConverter;

import de.fh_erfurt.bibliofy.model.Author;
import de.fh_erfurt.bibliofy.model.Genre;

public class Converters {

    /**
     * Converter Enum salutationType
     *
     * @param salutationType
     * @return
     */
    @TypeConverter
    public static String salutationToString(Author.SalutationType salutationType) {
        return salutationType.toString();
    }

    @TypeConverter
    public static Author.SalutationType salutationTypeFromString(String salutationTypeString) {
        return Author.SalutationType.valueOf(salutationTypeString);
    }

    /**
     * Converter Enum genreType
     *
     * @param genreType
     * @return
     */
    @TypeConverter
    public static String genreToString(Genre.GenreType genreType) {
        return genreType.toString();
    }

    @TypeConverter
    public static Genre.GenreType genreTypeFromString(String genreTypeTypeString) {
        return Genre.GenreType.valueOf(genreTypeTypeString);
    }
}
