package discretesolutions.com.recipecalculator;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FileDialogFragment.FileDialogInterface{

    FloatingActionButton btnAdd;
    RecyclerView rv;
    StorageHandler store;
    RecyclerView.LayoutManager layoutManager;
    Recipe recipe;
    EditText etWeight;
    EditText etName;
    CustomAdapter ca;
    String fileToLoad="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        rv = (RecyclerView)findViewById(R.id.rvItems);
        etWeight = findViewById(R.id.etWeight);
        etName = findViewById(R.id.etName);
        recipe = new Recipe();
        store = new StorageHandler(this);

        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setItemAnimator(null);
        RecyclerView.ItemDecoration rid = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(rid);


        etWeight.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0){
                    recipe.setRecipeWeight(Double.parseDouble(charSequence.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("MainActivity: ","Recipe Weight Changed");
                recipe.calculateWeights();
            }
        });

         ca = new CustomAdapter(recipe, new CustomAdapter.onFieldChange() {


             @Override
             public void onPercentageChange(final int index) {
              if(!rv.isComputingLayout() && rv.getScrollState()==RecyclerView.SCROLL_STATE_IDLE){
                  ca.notifyItemChanged(index);
              }
             }

         });

        rv.setAdapter(ca);

        ca.notifyDataSetChanged();
        setSupportActionBar(toolbar);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeItem ri = new RecipeItem(recipe);
                ri.setIngredient("Ing");
                ri.setPercentage(10);

                ca.addItem(ri);
                ca.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.action_settings:{
                return true;
            }
            case R.id.save:{

                //save();
                store.SaveRecipe(recipe);
                Log.v("MainActivity:","Return from storage save recipe");
                break;
            }
            case R.id.load:{
                ArrayList<String> files = store.getFileList();
                showFileDialog(files);
                recipe = store.loadRecipe("null.txt");
                Log.v("MainActivity:","Return from storage load recipe");
                ca.setRecipe(recipe);
                ca.notifyDataSetChanged();

                break;
            }
            default: {break;}
        }

        return super.onOptionsItemSelected(item);
    }

    public void save(){
        int some=-1;
        Intent inten = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        inten.addCategory(Intent.CATEGORY_OPENABLE);
        inten.setType("*/*");
        startActivityForResult(inten, some);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode,resultCode,intent);
        Uri uri  = intent.getData();
        Log.v("ActResult",uri.toString());
    }
    public void showFileDialog(ArrayList<String> files){
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("filenames", files);
        FileDialogFragment fDialog = new FileDialogFragment();
        fDialog.setArguments(bundle);
        fDialog.show(getSupportFragmentManager(),"File");
    }


    /*

    @Param Short version of filename, without extension
     */
    @Override
    public void onSelectedFileEvent(String file) {
    fileToLoad = file;
    Log.v("onSelected file:",file);
    }

    /*

    User aborted without selecting a file
     */
    @Override
    public void onCanceldEvent() {

    }
}
