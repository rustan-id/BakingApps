package id.rustan.bakingapps.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import id.rustan.bakingapps.model.data.Ingredient;



public interface IIngredientsView extends MvpView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showIngredients(List<Ingredient> list);

}
