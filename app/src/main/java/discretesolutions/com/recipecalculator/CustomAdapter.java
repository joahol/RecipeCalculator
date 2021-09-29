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

import java.io.ObjectInputStream;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private Recipe recipe=null;
    //private ArrayList<RecipeItem> data;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView etIngre;
        private final TextView etEstimation;
        private final EditText etPercent;
        private Recipe recipe;

        public ViewHolder(View view) {
            super(view);
            final int index = super.getAdapterPosition();
            etIngre = view.findViewById(R.id.etIngridient);
            etEstimation = view.findViewById(R.id.etCalculated);
            etPercent = view.findViewById(R.id.etPerDist);
            etPercent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            etIngre.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

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
  //  public CustomAdapter(Recipe recipe){
    //    this.recipe = recipe;
      //  data = (ArrayList<RecipeItem>) recipe.getIngridients();
    //}

    //public CustomAdapter(ArrayList<RecipeItem> dataset)
     public CustomAdapter(Recipe recipe){
        this.recipe = recipe;

    }
        @NonNull
        @Override
        public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.calclist, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final CustomAdapter.ViewHolder viewHolder, int i) {

            viewHolder.getEtEstimation().setText(String.valueOf(recipe.getIngridients().get(i).Estimation));
            viewHolder.getEtPercent().setText(String.valueOf(recipe.getIngridients().get(i).Percentage));
            viewHolder.getEtIngre().setText(recipe.getIngridients().get(i).Ingredient);
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

                        //notifyItemChanged(index);
                        Log.v("CustomAdapter", "update datasource:" + charSequence);

                    }
                    }

                @Override
                public void afterTextChanged(Editable editable) {
              try {
                  notifyDataSetChanged();
              }catch(Exception e){}
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

        public void addItem(RecipeItem ri){
            Log.wtf("CustomAdapter", "addItem");
            recipe.getIngridients().add(ri);
            recipe.calculateWeights();
        }
    }

