package discretesolutions.com.recipecalculator;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
FloatingActionButton btnAdd;
RecyclerView rv;
StorageHandler store;
    RecyclerView.LayoutManager layoutManager;
    Recipe recipe;
   // ArrayList<RecipeItem> items;
    EditText etWeight;
    EditText etName;

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
        //items = new ArrayList<RecipeItem>();
        etWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0){
                recipe.setRecipeWeight(Double.parseDouble(charSequence.toString()));
                recipe.calculateWeights();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });

        final CustomAdapter ca = new CustomAdapter(recipe);

        rv.setAdapter(ca);
        ca.notifyDataSetChanged();
        setSupportActionBar(toolbar);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeItem ri = new RecipeItem(recipe);
                ri.setIngredient("Ing");
                ri.setPercentage(10.5);

                ca.addItem(ri);
                ca.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_settings:{
                return true;
            }
            case R.id.save:{
                store.SaveRecipe(recipe);
                break;
            }
            case R.id.load:{
                break;
            }
            default: {break;}
        }

        return super.onOptionsItemSelected(item);
    }


}