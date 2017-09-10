package id.rustan.bakingapps.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import id.rustan.bakingapps.model.data.Recipe;



@StateStrategyType(OneExecutionStateStrategy.class)
public interface IRecipeView extends MvpView {

    void showRecipes(List<Recipe> list);

    void showLoading();

    void hideLoading();

    void hideSwipeRefresh();

    void showNetworkError();

}
