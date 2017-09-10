package id.rustan.bakingapps.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.rustan.bakingapps.BakingApps;
import id.rustan.bakingapps.R;
import id.rustan.bakingapps.model.ActivityUtility;
import id.rustan.bakingapps.presentation.presenter.IngredientAndStepsPresenter;
import id.rustan.bakingapps.presentation.view.IIngredientAndStepsView;
import id.rustan.bakingapps.ui.CustomMvpActivity;
import id.rustan.bakingapps.ui.fragment.IngredientsFragment;
import id.rustan.bakingapps.ui.fragment.StepFragment;

public class IngredientAndStepsActivity extends CustomMvpActivity implements IIngredientAndStepsView {

    @BindView(R.id.previous_button) Button previousButton;
    @BindView(R.id.next_button) Button nextButton;

    @InjectPresenter
    IngredientAndStepsPresenter presenter;

    private static final String INGREDIENTS_TAG = "ingredients_tag";
    private static final String STEP_TAG = "step_tag";

    @ProvidePresenter
    IngredientAndStepsPresenter providePresenter(){
        return new IngredientAndStepsPresenter(((BakingApps) getApplication()).getAppComponent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_and_step);
        ActivityUtility.setDisplayHomeAsUpEnabled(this);
        ButterKnife.bind(this);
        previousButton.setOnClickListener(v -> presenter.toPreviousPart());
        nextButton.setOnClickListener(v -> presenter.toNextPart());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showData(long recipeId, int position) {
        if (position == 0) {
            replaceFragment(R.id.fragment_container, () -> IngredientsFragment.newInstance(recipeId),
                    false, INGREDIENTS_TAG);
        }
        else {
            replaceFragment(R.id.fragment_container, () -> StepFragment.newInstance(recipeId, position - 1),
                    false, STEP_TAG + String.valueOf(position - 1));
        }
    }

    @Override
    public void enableNavigation(boolean enablePrevBut, boolean enableNextBut) {
        previousButton.setEnabled(enablePrevBut);
        nextButton.setEnabled(enableNextBut);
    }

}
