package bapspatil.wallcards.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by bapspatil
 */

public class FavsContract {

    public static final String AUTHORITY = "bapspatil.wallcards";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVS = "favorites";

    public static final class FavsEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVS).build();

        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_COLOR = "color";
        public static final String COLUMN_LIKES = "likes";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_USER_PIC = "user_pic";
        public static final String COLUMN_USER_NAME = "user_name";

    }
}
