package id.rustan.bakingapps.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.rustan.bakingapps.R;
import id.rustan.bakingapps.helper.GlideApp;
import id.rustan.bakingapps.model.data.Recipe;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private Context context;
    private View emptyView;
    private List<Recipe> items;
    private IOnItemClickListener itemClickListener;

    public interface IOnItemClickListener{
        void onItemClick(int position);
    }

    public void setItemClickListener(IOnItemClickListener listener){
        itemClickListener = listener;
    }

    public RecipeAdapter(Context context, View emptyView){
        this(context, new ArrayList<>(), emptyView);
    }

    public RecipeAdapter(Context context, List<Recipe> items, View emptyView){
        this.context = context;
        this.items = items;
        this.emptyView = emptyView;
        showEmptyView(items);
    }

    public void setItems(List<Recipe> items){
        this.items = items;
        showEmptyView(items);
        notifyDataSetChanged();
    }

    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items == null? 0:items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.recipe_image) ImageView recipeImageView;
        @BindView(R.id.recipe_name) TextView recipeNameView;
        @BindView(R.id.servings_value) TextView servingsView;

        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        public void bind(Recipe recipe){
            GlideApp.with(context)
                    .load(recipe.imageUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(recipeImageView);

            recipeNameView.setText(recipe.name());

            int servings = recipe.servings();
            String servingsStr = servings <= 0? "-":String.valueOf(servings);
            servingsView.setText(servingsStr);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null){
                itemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    /** util methods */

    private void showEmptyView(List<Recipe> list){
        if (emptyView != null) {
            int visibility = (list == null || list.isEmpty()) ? View.VISIBLE : View.INVISIBLE;
            this.emptyView.setVisibility(visibility);
        }
    }

}
