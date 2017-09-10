package id.rustan.bakingapps.activity;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import id.rustan.bakingapps.R;
import id.rustan.bakingapps.model.Command;
import id.rustan.bakingapps.model.KeyArguments;
import id.rustan.bakingapps.model.Router;
import id.rustan.bakingapps.model.data.Recipe;
import id.rustan.bakingapps.model.data.Step;
import id.rustan.bakingapps.presentation.presenter.RecipeDetailsPresenter;
import id.rustan.bakingapps.repository.IRepository;
import id.rustan.bakingapps.ui.MockApplication;
import id.rustan.bakingapps.ui.RecipesTest;
import id.rustan.bakingapps.ui.activity.RecipeDetailsActivity;
import io.reactivex.schedulers.Schedulers;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static id.rustan.bakingapps.ui.UtilsTest.withRecyclerView;

/**
 * Created by gmaimunah on 9/01/17.
 */

public class RecipeDetailsActivityTest {
    private List<Step> testSteps;

    @Rule
    public IntentsTestRule<RecipeDetailsActivity> recipeDetailsRule =
            new IntentsTestRule<>(RecipeDetailsActivity.class, true, false);

    @Before
    public void setup(){
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        MockApplication mockApplication =
                (MockApplication)instrumentation.getTargetContext().getApplicationContext();

        int recipePos = 0;
        List<Recipe> testRecipeList = new RecipesTest().getRecipes();
        testSteps = testRecipeList.get(recipePos).steps();

        IRepository iRepository = mockApplication.getAppComponent().Repo();
        Router router = mockApplication.getAppComponent().Router();

        iRepository.putRecipes(testRecipeList)
                .concatMap(aLong -> iRepository.getRecipeNames())
                .observeOn(Schedulers.trampoline())
                .subscribeOn(Schedulers.trampoline())
                .subscribe(recipes -> {
                    Bundle args = new Bundle();
                    args.putLong(KeyArguments.ID, recipes.get(recipePos).id());
                    router.putCommand(Command.SHOW_DETAILS,
                            RecipeDetailsPresenter.class.getName(), args);
                });
    }

    @After
    public void tearDown(){
        recipeDetailsRule.getActivity().finish();
    }

    @Test
    public void checkRecipeDetails(){
        recipeDetailsRule.launchActivity(new Intent());

        onView(withRecyclerView(R.id.step_list).atPositionOnView(0, R.id.step_name))
                .check(matches(withText("Ingredients")));

        for(int i = 0; i < testSteps.size(); i++) {
            Step step = testSteps.get(i);
            onView(withRecyclerView(R.id.step_list).atPositionOnView(i + 1, R.id.step_name))
                    .check(matches(withText(step.shortDescription())));
        }
    }

}
