package discretesolutions.com.recipecalculator;

import android.util.Log;

public class RecipeItem {
    public String Ingredient;
    public double Percentage;
    public double Estimation;
    private MeassureChanged mc;

    public RecipeItem(MeassureChanged Mc){
        Ingredient = new String();
        Percentage = 0.0;
        Estimation = 0.0;
        mc = mc;
    }
    public void setIngredient(String ingridient){Ingredient = ingridient;}
    public void setPercentage(double percentage){
        Percentage = percentage;
        if(mc!=null) {
            mc.calculateWeights();
        }else{
            Log.wtf("RecipeItem","No messurechanged interface");
        }
    }
    public void setEstimation(double estimate){Estimation = estimate;}

    public String getIngredient(){return Ingredient;}
    public double getPercentage(){return Percentage;}
    public double getEstimation(){return Estimation;}

}
