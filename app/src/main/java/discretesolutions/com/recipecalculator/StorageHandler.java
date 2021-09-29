package discretesolutions.com.recipecalculator;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class StorageHandler {
    Context handle;
    public StorageHandler(Context handle){
        this.handle = handle;
        Log.v("StorageHandler","Constructor");
    }

private static final int CREATE_FILE =1;

    private  void createFile(Uri fileuri){
    Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
    intent.addCategory(Intent.CATEGORY_OPENABLE);
    intent.setType("application/json");
    intent.setType("text/plain");
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, "Recipe.json");
        if(handle instanceof Activity) {
            ((Activity)handle).startActivityForResult(intent, CREATE_FILE);
            Log.v("createFile: ","passed the test");
        }
    }

public boolean fileExists(URI uri){
    boolean exists = false;
    return exists;
}

public void SaveRecipe(Recipe tRecipe){
        try {
            List<RecipeItem> ingredients = tRecipe.getIngridients();
            JSONArray jsonArr = createJSONObject(ingredients);
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject jo = (JSONObject) jsonArr.get(i);
                Log.e("StorageHandler",jo.toString());
            }

        }catch (JSONException je){}

}




 private JSONArray createJSONObject(List<RecipeItem> recipe){
    JSONArray jsonarr = new JSONArray();
       try {
           for (RecipeItem ri : recipe) {
               JSONObject jsonStore = new JSONObject();

               jsonStore.put("ingridient", ri.getIngredient());
               jsonStore.put("percentage", ri.getPercentage());
               jsonStore.put("estimate", ri.getEstimation());
               System.out.println(jsonStore.toString());
           }
       }catch (JSONException jse){
           jse.printStackTrace();
           return null;
       }
       return jsonarr;
}
}
