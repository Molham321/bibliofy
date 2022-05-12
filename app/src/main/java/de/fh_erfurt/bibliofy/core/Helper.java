package de.fh_erfurt.bibliofy.core;

import java.util.Random;
import java.util.UUID;

public class Helper {
    /**
     * Helper method to generate random profile images for our books
     *
     * @return the image url
     */
    public static String generateCoverImageUrl() {

        final int random = new Random().nextInt(10);

        if (random % 2 == 0) {
            return String.format("https://cdn.picpng.com/book/book-view-30965.png", UUID.randomUUID());
        } else {
            return String.format("https://purepng.com/public/uploads/large/purepng.com-booksbookillustratedwrittenprintedliteratureclipart-1421526451843dou0k.png", UUID.randomUUID());
        }

    }
}

