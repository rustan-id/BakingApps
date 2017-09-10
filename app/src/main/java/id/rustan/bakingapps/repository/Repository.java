package id.rustan.bakingapps.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import id.rustan.bakingapps.BuildConfig;
import id.rustan.bakingapps.model.data.Ingredient;
import id.rustan.bakingapps.model.data.Recipe;
import id.rustan.bakingapps.model.data.Step;



public class Repository implements IRepository {

    public final boolean enableLog = BuildConfig.DEBUG;
    private final String TAG = getClass().getName();
    private final RepositoryHelper repositoryHelper;
    private final Converter converter;
    private final PublishSubject<RepositoryEvent> publishSubject;

    public Repository(RepositoryHelper repositoryHelper, Converter converter){
        this.repositoryHelper = repositoryHelper;
        this.converter = converter;
        this.publishSubject = PublishSubject.create();
    }

    @Override
    public Observable<List<Recipe>> getRecipeNames() {
        return Observable.concat(Observable.just(RepositoryEvent.SUBSCRIBE), publishSubject)
                .doOnNext(repositoryEvent -> Log.d(TAG, "event: " + repositoryEvent.toString()))
                .concatMap(repositoryEvent -> getAllRecipeNames());
    }

    @Override
    public Observable<Long> putRecipes(List<Recipe> recipeList) {
        return Observable.fromCallable(() -> {
            long result = 0L;
            SQLiteDatabase db = repositoryHelper.getWritableDatabase();
            db.delete(RepositoryContract.RecipeEntry.TABLE_NAME, null, null);
            db.delete(RepositoryContract.StepEntry.TABLE_NAME, null, null);
            db.delete(RepositoryContract.IngredientEntry.TABLE_NAME, null, null);
            try {
                db.beginTransaction();
                for (Recipe recipe : recipeList) {
                    ContentValues cv = converter.toContentValues(recipe);
                    long recipeId = db.insert(RepositoryContract.RecipeEntry.TABLE_NAME, null, cv);
                    putSteps(db, recipeId, recipe.steps());
                    putIngredients(db, recipeId, recipe.ingredients());
                }
                db.setTransactionSuccessful();
                publishSubject.onNext(RepositoryEvent.UPDATE);
            }
            catch(Exception e){
                result = -1L;
            }
            finally {
                db.endTransaction();
            }

            logTable(RepositoryContract.RecipeEntry.TABLE_NAME);
            return result;
        });
    }

    @Override
    public Observable<Recipe> getRecipe(long id) {
        return Observable.fromCallable(() -> {
            SQLiteDatabase db = repositoryHelper.getReadableDatabase();

            String selection = RepositoryContract.RecipeEntry._ID + " =? ";
            String[] selArgs = {Long.toString(id)};
            Cursor recipeCursor = db.query(RepositoryContract.RecipeEntry.TABLE_NAME, null, selection, selArgs,
                    null, null, null);

            selection = RepositoryContract.StepEntry.COL_RECIPE_ID + " =? ";
            Cursor ingredientsCursor = db.query(RepositoryContract.IngredientEntry.TABLE_NAME, null, selection, selArgs,
                    null, null, null);


            selection = RepositoryContract.StepEntry.COL_RECIPE_ID + " =? ";
            Cursor stepCursor = db.query(RepositoryContract.StepEntry.TABLE_NAME, null, selection, selArgs,
                    null, null, null);

            Recipe recipe = converter.toRecipe(recipeCursor, ingredientsCursor, stepCursor);
            recipeCursor.close();
            ingredientsCursor.close();
            stepCursor.close();
            return recipe;
        });
    }

    @Override
    public Observable<Step> getStep(long id, int num) {
        return Observable.fromCallable(() -> {
            SQLiteDatabase db = repositoryHelper.getReadableDatabase();
            String selection = RepositoryContract.StepEntry.COL_RECIPE_ID + " =? AND " +
                    RepositoryContract.StepEntry.COL_STEP_NUM + " =?";
            String[] selArgs = {Long.toString(id), Integer.toString(num)};
            Cursor cursor = db.query(RepositoryContract.StepEntry.TABLE_NAME, null, selection, selArgs,
                    null, null, null);

            cursor.moveToFirst();
            Step step = converter.toStep(cursor);
            cursor.close();
            return step;
        });
    }

    @Override
    public Observable<List<Ingredient>> getIngredients(long recipeId) {
        return Observable.fromCallable(() -> {
            SQLiteDatabase db = repositoryHelper.getReadableDatabase();
            String selection = RepositoryContract.StepEntry.COL_RECIPE_ID + " =?";
            String[] selArgs = {Long.toString(recipeId)};
            Cursor cursor = db.query(RepositoryContract.IngredientEntry.TABLE_NAME, null, selection, selArgs,
                    null, null, null);
            List<Ingredient> list = converter.toIngredients(cursor);
            cursor.close();
            return list;
        });
    }

    /** helper methods */

    private Observable<List<Recipe>> getAllRecipeNames() {
        return Observable.fromCallable(() -> {
            SQLiteDatabase db = repositoryHelper.getReadableDatabase();
            Cursor cursor = db.query(RepositoryContract.RecipeEntry.TABLE_NAME, null, null, null,
                    null, null, null);
            List<Recipe> list = converter.toRecipeNameList(cursor);
            cursor.close();
            return list;
        });
    }

    private long putSteps(SQLiteDatabase db, long recipeId, List<Step> stepList){
        for(Step step: stepList){
            ContentValues cv = converter.toContentValues(recipeId, step);
            db.insert(RepositoryContract.StepEntry.TABLE_NAME, null, cv);
        }
        logTable(RepositoryContract.StepEntry.TABLE_NAME);
        return 0L;
    }

    private long putIngredients(SQLiteDatabase db, long recipeId, List<Ingredient> ingredientList){
        for(Ingredient ingredient: ingredientList){
            ContentValues cv = converter.toContentValues(recipeId, ingredient);
            db.insert(RepositoryContract.IngredientEntry.TABLE_NAME, null, cv);
        }
        logTable(RepositoryContract.IngredientEntry.TABLE_NAME);
        return 0L;
    }

    private void logTable(String tableName) {
        if (!enableLog) return;

        SQLiteDatabase db = repositoryHelper.getReadableDatabase();
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            Log.d(TAG, "table is empty");
        }
        while (!cursor.isAfterLast()) {
            Log.d(TAG, DatabaseUtils.dumpCurrentRowToString(cursor));
            cursor.moveToNext();
        }
    }



}
