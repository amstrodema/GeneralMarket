package comgalaxyglotech.confirmexperts.generalmarket;

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

import java.util.ArrayList;

import comgalaxyglotech.confirmexperts.generalmarket.BL.Help.HelpAdapter;

public class Help extends AppCompatActivity {
    ArrayList<HelpModel> helpModelArrayList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private HelpAdapter mAdapter;
    private RecyclerView.LayoutManager mlayoutManager;
    private EditText searchStringField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        getHelp();
        searchBarOp();
        //recycler
        mlayoutManager =new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.recyclerViewHelp);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new HelpAdapter(helpModelArrayList);
        mRecyclerView.setLayoutManager(mlayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        searchOperandi();
        mAdapter.setOnItemClickListener(new HelpAdapter.OnAllCommoditiesItemClickListener() {
            @Override
            public void onItemClick(int position) {
                  /*intent.putExtra("recipeId",RecipeDataClass.recipeData.get(position).getId());
                progressDialog.setMessage("Loading Recipe Ingredients");
                progressDialog.show();
                dataClass.getSmartData(progressDialog,context,activity,intent);*/
            }
        });
    }


    private void getHelp(){
        helpModelArrayList.clear();
        HelpModel helpModel = new HelpModel("QUICK BRIEF","General Market - G.M is an online market place for all sellers to own a store and sell to buyers.\n" +
                "\n" +
                "General Market - G.M. is a totally free app (no advert) which uses GPS to show you a list of markets, stores and farms, alongside what is being sold, commodity catalog and how faraway these places are to your immediate location.\n" +
                "\n" +
                "With General Market, you get to know cost implications of items on your purchase list, make comparison and contact nearest sellers without leaving your home. This app displays nearby markets, stores that belong to these markets, previous and next market trade days, and distance to your immediate location. \n" +
                "\n" +
                "So ease your market experience by getting first-hand knowledge of what to buy, how much it will cost, distance of where to buy and from whom to buy.\n" +
                "\n" +
                "HOW DOES IT WORK\n" +
                "General Market (G.M.) is very easy to use, no technicality is needed, no annoying advert and yet gives you access to a global market (buy/sell).\n" +
                "As a seller, you get to own a virtual store where you can showcase every item you sell by uploading your stock image, description and cost. While buyers can easily check through stocks, find your item and put a call across without stress.\n" +
                "\n" +
                "GETTING STARTED\n" +
                "Download, register an account. Create a store while at your exact store location or away. If you registered while away, remember to modify your GPS location from within your real store.\n" +
                "\n" +
                "Note: if your store is within a market, register the market first, if it doesn't exist already. From within the virtual market, create your store. This ensures that buyers can find your store in that market.\n" +
                "\n" +
                "Farmers should directly create their farm(s) from the farm portal specially dedicated to agriculture\n" +
                "\n" +
                "Everyone deserves an easy, yet quality embedded experience at trading. Help your community get better, tell someone who deserves the best about G.M.\n" +
                "\n" +
                "Best Regards\n" +
                "GLOTECH\n");
        helpModelArrayList.add(helpModel);
        helpModel = new HelpModel("What should I do If my store or stock is banned?","Contact GLOTECH immediately, our support crew will have you sorted out.");
        helpModelArrayList.add(helpModel);
        helpModel = new HelpModel("My app doesn't have same functions as others, Why?","Others a probably using an updated version. Kindly update your app.");
        helpModelArrayList.add(helpModel);
        helpModel = new HelpModel("My Store Shows Unknown Location","Enable GPS and grant gps permission. Open your virtual store, scroll down and click the button just before your reviews");
        helpModelArrayList.add(helpModel);
        helpModel = new HelpModel("I don't want to see places that are too far","Alright! Click the setting button, activate location limit and enter maximum a distance of your choice");
        helpModelArrayList.add(helpModel);
        helpModel = new HelpModel("I don't want to see places with unknown location","Click on the setting button, activate location limit and disallow places with unknown location");
        helpModelArrayList.add(helpModel);
        helpModel = new HelpModel("I have something important to discuss","Feel free to contact us");
        helpModelArrayList.add(helpModel);
        helpModel = new HelpModel("My store rating has gone really bad","Improve your customer service appeal. Improved performance will generate better ratings from both old and new customers, this will set your ratings back up.");
        helpModelArrayList.add(helpModel);
        helpModel = new HelpModel("Can I delete products I don't sell anymore?","No, simply modify them to what you now sell.");
        helpModelArrayList.add(helpModel);
        helpModel = new HelpModel("What if I find out a store is not real and might be a hoax?","Send us proof and well take adequate step");
        helpModelArrayList.add(helpModel);
        helpModel = new HelpModel("Contact Us","Send us a mail on: glotechng@gmail.com or info@glotech.com.ng or call +234-813-739-5582");
        helpModelArrayList.add(helpModel);
        helpModel = new HelpModel("DISCLAIMER","Information included in this app are solely attributed to individuals who have chosen to disseminate such. Therefore, managements of G.M. are not responsible for any such information and contents in any way.");
        helpModelArrayList.add(helpModel);

       /* helpModel = new HelpModel("","Dope");
        helpModelArrayList.add(helpModel);*/
    }
    private void searchBarOp(){
        Button SearchAccess, stopSearch;
        final RelativeLayout topPanel, topPanel2;
        topPanel = findViewById(R.id.topPanel);
        topPanel2 = findViewById(R.id.topPanel2);
        SearchAccess = findViewById(R.id.SearchAccess);
        stopSearch = findViewById(R.id.stopSearch);
        searchStringField = findViewById(R.id.searchString);
        SearchAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show search input and hide bottom panel
                topPanel2.setVisibility(View.GONE);
                topPanel.setVisibility(View.VISIBLE);
            }
        });
        stopSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide search input and show bottom panel
                searchStringField.setText("");
                topPanel.setVisibility(View.GONE);
                topPanel2.setVisibility(View.VISIBLE);
            }
        });
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
}
