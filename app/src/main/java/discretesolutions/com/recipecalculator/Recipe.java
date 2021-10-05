package discretesolutions.com.recipecalculator;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements MeassureChanged
{
    private String recipeName="";
    private double recipeWeight=0;
    List<RecipeItem> ingridients;

public Recipe(){
    ingridients = new ArrayList<RecipeItem>();
}
public String getRecipeName(){return recipeName;}

public void setRecipeName(String RecipeName){
    recipeName = RecipeName;
}

public void setRecipeWeight(double RecipeWeight){
    recipeWeight = RecipeWeight;
}

public double getRecipeWeight(){return recipeWeight;}

    public void setIngredients(List<RecipeItem> ingredients){
    this.ingridients = ingredients;
    };

    public List<RecipeItem> getIngridients(){return this.ingridients;}

    @Override
    public void calculateWeights() {

        for(RecipeItem r : ingridients){
            Log.d("Recipe:Weight calculation",String.valueOf(recipeWeight*r.getPercentage()/100));
            r.setEstimation((recipeWeight*r.getPercentage())/100);
        }
    }
}
