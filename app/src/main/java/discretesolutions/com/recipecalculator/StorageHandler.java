package discretesolutions.com.recipecalculator;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.drm.DrmStore;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;

import android.util.JsonToken;
import android.util.JsonWriter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class StorageHandler {
    Context handle;
    Activity activity;
    //public StorageHandler(Context handle){
      public StorageHandler(Activity active){
        this.activity = active;
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

private void checkPermissions(){
    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
    Log.v("StorageHandler: ","Permission write external granted");
    }
    else{
        Log.v("StorageHandler: ","Permission write external denied");
        String permissions[] = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissions(activity,permissions,1);
    }
    }


public void SaveRecipe(Recipe tRecipe){
        try {
            //AppCompatActivity. requestPermission(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE)
            String state = Environment.getExternalStorageState();
            if(state.equals(Environment.MEDIA_MOUNTED)){
                File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"Recipes");
               //if directory do not exist create it
                if(!dir.exists()) {
                    dir.mkdirs();
                }
                //prepare data
                List<RecipeItem> ingredients = tRecipe.getIngridients();
                JSONArray jsonArr = createJSONObject(ingredients);
                String saveString ="";
                JSONObject jso = new JSONObject();
                jso.put("name",tRecipe.getRecipeName());
                jso.put("weight",tRecipe.getRecipeWeight());
                jso.put("ing",jsonArr);
                saveString = jso.toString();
                Log.v("JSON STRING:",saveString);
                File sFile = new File(dir,tRecipe.getRecipeName()+".json");
                checkPermissions();
                saveFile(sFile,saveString);
                //Save data file to directory
            }


        }catch (JSONException je){

            Log.v("StorageHandler:",je.toString());
        }

}

private boolean saveFile(File file, String data) {
 boolean test = true;
 try {
     FileOutputStream fs = new FileOutputStream(file);
     fs.write(data.getBytes());
     fs.close();
     test = false;
 }catch(Exception e){
     Log.v("Error: ","SaveFile: "+e.toString());
 }
return test;
    }

 private JSONArray createJSONObject(List<RecipeItem> recipe){

    JSONArray jsonarr = new JSONArray();
       try {
           for (RecipeItem ri : recipe) {
               JSONObject jsonStore = new JSONObject();

               jsonStore.put("ingredient", ri.getIngredient());
               jsonStore.put("percentage", ri.getPercentage());
               jsonStore.put("estimate", ri.getEstimation());
               jsonarr.put(jsonStore);

           }
       }catch (JSONException jse){
           jse.printStackTrace();
           return null;
       }
       return jsonarr;
}

/*
@return Returns a list of json filenames stored in DIRECTORY_DOCUMENTS/Recipes
 */
public ArrayList<String> getFileList(){
        ArrayList<String> fileList = new ArrayList<String>();
    File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"Recipes");
    FilenameFilter fnf = new FilenameFilter() {
        @Override
        public boolean accept(File file, String filename) {
            Log.v("FileList:",filename);
            int index = filename.indexOf(".");
            String extension = filename.substring(index,filename.length());
                if (extension.toLowerCase().equals(".json")) {
                    return true;
                }

        return false;}
    };
    File[] files = dir.listFiles(fnf);
        for(File f : files){
            Log.v("FileName:",f.getName());
            fileList.add(f.getName());
        }
        return fileList;
}

public void openDir(Uri uri){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI,uri);
        startActivityForResult((Activity)handle,intent, 2,null);
}

    public Recipe loadRecipe(String fileName)  {

        final Recipe r = new Recipe();
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"Recipes");
        final String fname = dir.getAbsolutePath()+"/"+fileName;
        //final File file = new File(fname);

        Log.v("LoadFile:",fname);
        try {
            JsonReader jsonReader = new JsonReader(new FileReader(fname));
            jsonReader.beginObject();
            if (jsonReader.hasNext()){


                String name = jsonReader.nextName();
                String current = jsonReader.nextString();

                String ingre = jsonReader.nextName();

                Log.v(",,,",ingre);

                double  we = jsonReader.nextDouble();
                String pen = jsonReader.nextName();
                Log.v("Read: ",name+ " "+current+" "+ingre+" "+pen+" "+we);
                r.setRecipeName(current);
                r.setRecipeWeight(we);
                jsonReader.beginArray();
                jsonReader.beginObject();
                while(jsonReader.hasNext()){

                    RecipeItem ri = new RecipeItem();
                    if(jsonReader.peek()==JsonToken.BEGIN_OBJECT){
                        jsonReader.beginObject();
                    }
                    String sa = jsonReader.nextName();
                    String pe = jsonReader.nextString();
                    ri.setIngredient(pe);
                    String t = jsonReader.nextName();

                    double perc = jsonReader.nextDouble();

                    ri.setPercentage(perc);
                    String n = jsonReader.nextName();

                    double estim = jsonReader.nextDouble();
                    ri.setEstimation(estim);
                    r.addRecipeItem(ri);
                    Log.v("RI",ri.getIngredient()+" "+String.valueOf(ri.getPercentage())+" "+String.valueOf(ri.getEstimation()));
                    jsonReader.endObject();

                    //{"name":"test2","weight":1550,"ing":[{"ingredient":"Ing","percentage":11210,"estimate":173755},{"ingredient":"Ing","percentage":10,"estimate":155}]}
//{"name":"tewt3","weight":55551,"ing":[{"ingredient":"King","percentage":10,"estimate":5555.1},{"ingredient":"Ing234","percentage":10,"estimate":5555.1},{"ingredient":"trq","percentage":50,"estimate":27775.5}]}
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return r;
    }
}
