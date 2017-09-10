package id.rustan.bakingapps.activity;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import id.rustan.bakingapps.R;
import id.rustan.bakingapps.model.Command;
import id.rustan.bakingapps.model.KeyArguments;
import id.rustan.bakingapps.model.Router;
import id.rustan.bakingapps.model.data.Ingredient;
import id.rustan.bakingapps.model.data.Recipe;
import id.rustan.bakingapps.presentation.presenter.IngredientAndStepsPresenter;
import id.rustan.bakingapps.repository.IRepository;
import id.rustan.bakingapps.ui.MockApplication;
import id.rustan.bakingapps.ui.RecipesTest;
import id.rustan.bakingapps.ui.activity.IngredientAndStepsActivity;
import io.reactivex.schedulers.Schedulers;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static id.rustan.bakingapps.ui.UtilsTest.withRecyclerView;

/**
 * Created by gmaimunah on 9/01/17.
 */

public class IngredientsActivityTest {
    private List<Recipe> testRecipeList;
    private int recipePos;

    @Rule
    public ActivityTestRule<IngredientAndStepsActivity> activityRule =
            new ActivityTestRule<>(IngredientAndStepsActivity.class, true, false);

    @Before
    public void setUp(){
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        MockApplication mockApplication =
                (MockApplication)instrumentation.getTargetContext().getApplicationContext();

        recipePos = 0;
        testRecipeList = new RecipesTest().getRecipes();

        final IRepository repo = mockApplication.getAppComponent().Repo();
        final Router router = mockApplication.getAppComponent().Router();

        repo.putRecipes(testRecipeList)
                .concatMap(aLong -> repo.getRecipeNames())
                .observeOn(Schedulers.trampoline())
                .subscribeOn(Schedulers.trampoline())
                .subscribe(recipes -> {
                    Bundle args = new Bundle();
                    args.putLong(KeyArguments.ID, recipes.get(recipePos).id());
                    args.putInt(KeyArguments.STEP, 0);
                    router.putCommand(Command.SHOW_INGREDIENTS,
                            IngredientAndStepsPresenter.class.getName(), args);
                });
    }

    @After
    public void tearDown(){
        activityRule.getActivity().finish();
    }

    @Test
    public void checkIngredients(){
        activityRule.launchActivity(new Intent());
        List<Ingredient> ingredientList = testRecipeList.get(recipePos).ingredients();

        for(int i = 0; i < ingredientList.size(); i++) {
            Ingredient ingredient = ingredientList.get(i);
            onView(withRecyclerView(R.id.ingredient_list).atPositionOnView(i, R.id.ingredient))
                    .check(matches(withText(ingredient.ingredient())));

            String quantity = String.valueOf(ingredient.quantity());
            onView(withRecyclerView(R.id.ingredient_list).atPositionOnView(i, R.id.quantity))
                    .check(matches(withText(quantity)));

            String measure = String.valueOf(ingredient.measure());
            onView(withRecyclerView(R.id.ingredient_list).atPositionOnView(i, R.id.measure))
                    .check(matches(withText(measure)));
        }
    }
}
