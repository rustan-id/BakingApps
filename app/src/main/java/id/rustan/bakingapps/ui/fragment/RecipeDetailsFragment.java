package id.rustan.bakingapps.ui.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.rustan.bakingapps.BakingApps;
import id.rustan.bakingapps.R;
import id.rustan.bakingapps.di.AppComponent;
import id.rustan.bakingapps.model.data.Recipe;
import id.rustan.bakingapps.presentation.presenter.RecipeDetailsPresenter;
import id.rustan.bakingapps.presentation.view.IRecipeDetailsView;
import id.rustan.bakingapps.adapters.StepAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailsFragment extends MvpAppCompatFragment implements IRecipeDetailsView {

    @InjectPresenter
    RecipeDetailsPresenter presenter;

    @BindView(R.id.step_list)
    RecyclerView stepRecyclerView;

    private StepAdapter stepAdapter;
    private Parcelable listState;
    private static final String LIST_STATE = "list_state";
    private static final String TAG = RecipeDetailsFragment.class.getName();

    @ProvidePresenter
    RecipeDetailsPresenter providePresenter() {
        AppComponent component = ((BakingApps) getActivity().getApplication()).getAppComponent();
        return new RecipeDetailsPresenter(component);
    }

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            listState = savedInstanceState.getParcelable(LIST_STATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, view);

        stepAdapter = new StepAdapter();
        stepAdapter.setOnItemClickListener(position -> presenter.onItemClick(position));
        stepRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        stepRecyclerView.setAdapter(stepAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LIST_STATE,
                stepRecyclerView.getLayoutManager().onSaveInstanceState());

    }

    /**
     * view interface
     */

    @Override
    public void showDetails(Recipe recipe) {
        getActivity().setTitle(recipe.name());
        stepAdapter.setData(recipe);
        if (listState != null){
            stepRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }
}
