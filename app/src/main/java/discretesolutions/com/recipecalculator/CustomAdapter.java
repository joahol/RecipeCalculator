package discretesolutions.com.recipecalculator;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private Recipe recipe=null;
    RecyclerView rView;
    private int currentlyFocused=-1;

    public interface onFieldChange{
        void onPercentageChange(int index);
    }
public void setRecipe(Recipe recipe){
        this.recipe = recipe;
}
    private onFieldChange onFieldChangeListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView etIngre;
        private final TextView etEstimation;
        private final EditText etPercent;
        private Recipe recipe;

        public ViewHolder(View view) {
            super(view);
            final int index = getAdapterPosition();
            etIngre = view.findViewById(R.id.etIngridient);
            etEstimation = view.findViewById(R.id.etCalculated);
            etPercent = view.findViewById(R.id.etPerDist);
            view.setTag(index);
        }

        public TextView getEtIngre() {
            return etIngre;
        }

        public TextView getEtEstimation() {
            return etEstimation;
        }

        public EditText getEtPercent() {
            return etPercent;
        }

    }
     public CustomAdapter(Recipe recipe, onFieldChange onfieldchangelstener){
        this.recipe = recipe;
        this.onFieldChangeListener = onfieldchangelstener;
    }
        @NonNull
        @Override
        public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.calclist, viewGroup, false);
            final ViewHolder viewHolder = new ViewHolder(view);
              //  viewHolder.getEtPercent().setOnFocusChangeListener(new View.OnFocusChangeListener() {
               // @Override
               // public void onFocusChange(View view, boolean b) {
               // }
            //});
            ((EditText)viewHolder.getEtPercent()).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    int index = viewHolder.getAdapterPosition();
                    if(charSequence.length()>0) {
                        recipe.getIngridients().get(index).setPercentage(Double.parseDouble(charSequence.toString()));
                        recipe.calculateWeights();
                        currentlyFocused = viewHolder.getAdapterPosition();

                        Log.v("CustomAdapter: Current id",String.valueOf(currentlyFocused));
                        onFieldChangeListener.onPercentageChange(index);

                        Log.v("CustomAdapter", "update datasource:" + charSequence);
                    }
                };

                @Override
                public void afterTextChanged(Editable editable) {

                }

            });

            ((EditText)viewHolder.getEtIngre()).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    int index = viewHolder.getAdapterPosition();
                    recipe.getIngridients().get(index).setIngredient(charSequence.toString());

                    Log.v("CustomAdapter","update datasource:"+charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final CustomAdapter.ViewHolder viewHolder, int i) {

            viewHolder.getEtEstimation().setText(String.valueOf(recipe.getIngridients().get(i).Estimation));
            viewHolder.getEtPercent().setText(String.valueOf(recipe.getIngridients().get(i).Percentage));
            viewHolder.getEtIngre().setText(recipe.getIngridients().get(i).Ingredient);

        }

        @Override
        public int getItemCount() {
            if(recipe.getIngridients()==null){
                return 0;
            }
            else{
                return recipe.getIngridients().size();
            }
        }
/*
@param Add recipe item to recipe
 */
        public void addItem(RecipeItem ri){
            Log.wtf("CustomAdapter", "addItem");
            recipe.getIngridients().add(ri);
            recipe.calculateWeights();
        }
    }

