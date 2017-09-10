package id.rustan.bakingapps.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import id.rustan.bakingapps.model.data.Recipe;



public interface IRecipeDetailsView extends MvpView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showDetails(Recipe recipe);

}
