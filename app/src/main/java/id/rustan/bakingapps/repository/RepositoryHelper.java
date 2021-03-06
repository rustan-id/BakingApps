package id.rustan.bakingapps.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

import id.rustan.bakingapps.repository.RepositoryContract.IngredientEntry;
import id.rustan.bakingapps.repository.RepositoryContract.RecipeEntry;
import id.rustan.bakingapps.repository.RepositoryContract.StepEntry;
import id.rustan.bakingapps.di.ApplicationContext;


@Singleton
public class RepositoryHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "data_recipes.db";
    public static final int DATABASE_VERSION = 2;

    @Inject
    public RepositoryHelper(@ApplicationContext Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + RecipeEntry.TABLE_NAME + " (" +
                RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecipeEntry.COL_NAME + " TEXT," +
                RecipeEntry.COL_IMAGE_URL + " TEXT," +
                RecipeEntry.COL_SERVINGS + " INTEGER" +
                ");";
        db.execSQL(createTable);

        createTable = "CREATE TABLE " + IngredientEntry.TABLE_NAME + " (" +
                IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IngredientEntry.COL_RECIPE_ID + " INTEGER, " +
                IngredientEntry.COL_INGREDIENT + " TEXT, " +
                IngredientEntry.COL_MEASURE + " TEXT, " +
                IngredientEntry.COL_QUANTITY + " REAL" +
                ");";
        db.execSQL(createTable);

        createTable = "CREATE TABLE " + StepEntry.TABLE_NAME + " (" +
                StepEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StepEntry.COL_RECIPE_ID + " INTEGER, " +
                StepEntry.COL_STEP_NUM + " INTEGER, " +
                StepEntry.COL_SHORT_DESCRIPTION + " TEXT, " +
                StepEntry.COL_DESCRIPTION + " TEXT, " +
                StepEntry.COL_VIDEO_URL + " TEXT, " +
                StepEntry.COL_THUMBNAIL_URL + " TEXT" +
                ");";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String deleteTable = "DROP TABLE IF EXISTS ";
        db.execSQL(deleteTable + RecipeEntry.TABLE_NAME);
        db.execSQL(deleteTable + IngredientEntry.TABLE_NAME);
        db.execSQL(deleteTable + StepEntry.TABLE_NAME);
        onCreate(db);
    }
}
