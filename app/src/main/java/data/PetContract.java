package data;

import android.provider.BaseColumns;

public final class PetContract {

    private PetContract() {

    }

    public static abstract class PetEntry implements BaseColumns {

        public static final String TABLE_NAME = "pet";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PET_NAME = "name";
        public static final String COLUMN_PET_BREED = "breed";
        public static final String COLUMN_PET_GENDER = "gender";
        public static final String COLUMN_PET_WEIGHT = "WEIGHT";

        /*
        Defining constant for gender
        0 for unknown, 1 for male, 2 for female
         */
        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;

       public static final String SQL_CREATE_PETS_TABLE = " CREATE TABLE "
                + PetContract.PetEntry.TABLE_NAME
                + "(" + PetContract.PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PetContract.PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL,"
                + PetContract.PetEntry.COLUMN_PET_BREED + " TEXT,"
                + PetContract.PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL,"
                + PetContract.PetEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0);";
    }
}
