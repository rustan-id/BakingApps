package id.rustan.bakingapps.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.rustan.bakingapps.R;
import id.rustan.bakingapps.model.data.Recipe;
import id.rustan.bakingapps.model.data.Step;


public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

    private static final int INGREDIENT_TYPE = 0;
    private static final int STEP_TYPE = 1;

    private List<Step> stepList;
    private IOnItemClickListener listener;

    public interface IOnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(IOnItemClickListener listener){
        this.listener = listener;
    }

    public StepAdapter(){
        this(new ArrayList<>());
    }

    public StepAdapter(List<Step> stepList){
        this.stepList = stepList;
    }

    public void setData(Recipe recipe){
        stepList = recipe.steps();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? INGREDIENT_TYPE : STEP_TYPE;
    }

    @Override
    public StepAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_detail_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepAdapter.ViewHolder holder, int position) {
        switch(getItemViewType(position)){
            case INGREDIENT_TYPE:
                holder.bindIngredient();
                return;
            case STEP_TYPE:
                holder.bindStep(stepList.get(position - 1));
                return;
            default:
        }
    }

    @Override
    public int getItemCount() {
        return stepList == null? 0:stepList.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.step_name) TextView stepName;

        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        public void bindIngredient(){
            stepName.setText(R.string.ingredients);
        }

        public void bindStep(Step step){
            stepName.setText(step.shortDescription());
        }

        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.onItemClick(getAdapterPosition());
            }
        }
    }

}
