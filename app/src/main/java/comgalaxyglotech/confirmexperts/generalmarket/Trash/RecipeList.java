package comgalaxyglotech.confirmexperts.generalmarket.Trash;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.DataClass;
import comgalaxyglotech.confirmexperts.generalmarket.HomePage;
import comgalaxyglotech.confirmexperts.generalmarket.DAL.Repository.ModelClass;
import comgalaxyglotech.confirmexperts.generalmarket.R;
import comgalaxyglotech.confirmexperts.generalmarket.ClassPack.TransitionAdvertDisplay;

public class RecipeList extends AppCompatActivity {
    private FloatingActionButton newRecipe;
    private DataClass dataClass = new DataClass();
    private RecyclerView mRecyclerView;
    private ArchiveRecipeDisplayAdapter mAdapter;
    private RecyclerView.LayoutManager mlayoutManager;
    private ProgressDialog progressDialog;
    private Context context = this;
    private Activity activity = this;
    private ModelClass modelClass = new ModelClass();
    private EditText searchStringField;
    private boolean isLoggedIn = modelClass.userLoggedIn();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        //dataClass.getData();
        searchBarOp();
        basePanelListeners();
        searchStringField = findViewById(R.id.searchStringField);
        try{
            TransitionAdvertDisplay.StopThisActivity.finish();
        }
        catch (Exception ex){}
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        newRecipe = findViewById(R.id.homeBtnx);
        newRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLoggedIn){
                    progressDialog.setMessage("Loading Ingredient Items");
                    progressDialog.show();
                    dataClass.getSmarterData(progressDialog,context, new Intent(RecipeList.this, NewRecipe.class));
                }
                else{
                    Toast.makeText(RecipeList.this,"Log in first!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //recycler
        mlayoutManager =new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.recipeRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ArchiveRecipeDisplayAdapter(RecipeDataClass.recipeData);
        mRecyclerView.setLayoutManager(mlayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ArchiveRecipeDisplayAdapter.OnAllCommoditiesItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent =new Intent(RecipeList.this,Recipe.class);
                intent.putExtra("recipeId",RecipeDataClass.recipeData.get(position).getId());
                progressDialog.setMessage("Loading Recipe Contents...");
                progressDialog.show();
                dataClass.getSmarterData(progressDialog,context,intent);

            }
        });
        searchOperandi();
    }
    private void searchOperandi(){
        searchStringField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  Toast.makeText(StoreActivity.this,s,Toast.LENGTH_SHORT).show();
                mAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    private void basePanelListeners(){
        Button home, account, market, store;
        Button gps;
        home = findViewById(R.id.home_floor);
        account = findViewById(R.id.account_floor);
        market = findViewById(R.id.store_floor);
        store = findViewById(R.id.store_ll_floor);
        gps = findViewById(R.id.gps);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(RecipeList.this,TransitionAdvertDisplay.class);
                intent.putExtra("Location","Archive_Transition");
                startActivity(intent);
            }
        });
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(getApplicationContext(),NotificationView.class);
                //  startActivity(intent);
            }
        });
        market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent("comgalaxyglotech.confirmexperts.generalmarket.ClassPack.TransitionAdvertDisplay");
                intent.putExtra("Location","Market_Transition");
                startActivity(intent);
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent("comgalaxyglotech.confirmexperts.generalmarket.ClassPack.TransitionAdvertDisplay");
                intent.putExtra("Location","Store_Transition");
                startActivity(intent);
            }
        });
    }
    private void searchBarOp(){
        Button SearchAccess, stopSearch;
        final RelativeLayout topPanel, topPanel2,bottomPanel;
        bottomPanel = findViewById(R.id.bottomPanel);
        topPanel = findViewById(R.id.topPanel);
        topPanel2 = findViewById(R.id.topPanel2);
        SearchAccess = findViewById(R.id.SearchAccess);
        stopSearch = findViewById(R.id.stopSearch);
        SearchAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show search input and hide bottom panel
                topPanel2.setVisibility(View.GONE);
                topPanel.setVisibility(View.VISIBLE);
                bottomPanel.setVisibility(View.GONE);
            }
        });
        stopSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide search input and show bottom panel
                topPanel.setVisibility(View.GONE);
                topPanel2.setVisibility(View.VISIBLE);
                bottomPanel.setVisibility(View.VISIBLE);
            }
        });
    }
}
