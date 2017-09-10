package id.rustan.bakingapps.activity;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import id.rustan.bakingapps.R;
import id.rustan.bakingapps.model.data.Recipe;
import id.rustan.bakingapps.ui.RecipesTest;
import id.rustan.bakingapps.ui.activity.RecipeDetailsActivity;
import id.rustan.bakingapps.ui.activity.RecipeListActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static id.rustan.bakingapps.ui.UtilsTest.withRecyclerView;

/**
 * Created by gmaimunah on 9/01/17.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {
    private List<Recipe> testRecipeList;

    @Rule
    public IntentsTestRule<RecipeListActivity> recipeListRule =
            new IntentsTestRule<>(RecipeListActivity.class, true, false);

    @Before
    public void setUp(){
        testRecipeList = new RecipesTest().getRecipes();
    }

    @After
    public void tearDown(){
        recipeListRule.getActivity().finish();
    }

    @Test
    public void checkRecipeDetails(){
        recipeListRule.launchActivity(new Intent());

        for(int i = 0; i < testRecipeList.size(); i++) {
            Recipe recipe = testRecipeList.get(i);
            onView(withRecyclerView(R.id.recipe_list).atPositionOnView(i, R.id.recipe_name))
                    .check(matches(withText(recipe.name())));

            String servings = String.valueOf(recipe.servings());
            onView(withRecyclerView(R.id.recipe_list).atPositionOnView(i, R.id.servings_value))
                    .check(matches(withText(servings)));
        }
    }

    @Test
    public void checkRecipeDetailsCanShow(){
        recipeListRule.launchActivity(new Intent());

        onView(withId(R.id.recipe_list))
                .perform(actionOnItemAtPosition(0, click()));

        String packageName = recipeListRule.getActivity().getApplicationContext().getPackageName();
        intended(toPackage(packageName));
        intended(hasComponent(RecipeDetailsActivity.class.getName()));
    }
}
