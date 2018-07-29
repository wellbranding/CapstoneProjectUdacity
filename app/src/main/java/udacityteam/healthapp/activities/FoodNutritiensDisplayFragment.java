package udacityteam.healthapp.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import udacityteam.healthapp.Model.Result;
import udacityteam.healthapp.Network.PHPService;
import udacityteam.healthapp.PHP_Retrofit_API.APIService;
import udacityteam.healthapp.PHP_Retrofit_API.APIUrl;
import udacityteam.healthapp.R;
import udacityteam.healthapp.app.ApplicationController;
import udacityteam.healthapp.databases.DatabaseHelper;
import udacityteam.healthapp.models.SelectedFood;
import udacityteam.healthapp.models.SelectedFoodmodel;

/**
 * Created by vvost on 11/26/2017.
 */

public class FoodNutritiensDisplayFragment extends Fragment {
    TextView Textv;
    Button addtoSqlite;
    String id = null;
    String foodname = null;
    String UserId = null;
    String foodselection = null;
    ProgressBar progressBar;
    TextView productname, nutritionaldisplay;
    String SharedFoodListDatabase;
    private Context context;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view =  inflater.inflate(R.layout.food_fragment,
              container, false);
        setHasOptionsMenu(true);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        //   toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        toolbar.setTitle(foodname);
        progressBar = view.findViewById(R.id.progressbar);
        productname = view.findViewById(R.id.ProductName);
        nutritionaldisplay = view.findViewById(R.id.nutritionaldysplay);
        addtoSqlite = view.findViewById(R.id.button2);
        addtoSqlite.setActivated(false);
        UserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Textv = (TextView)view.findViewById(R.id.tv2);

        StringBuilder amm = new StringBuilder();
        amm.append("https://api.nal.usda.gov/ndb/V2/reports?ndbno=");
        amm.append(id);
        amm.append("&type=f&format=json&api_key=HXLecTDsMqy1Y6jNoYPw2n3DQ30FeGXxD2XBZqJh");
        GETADDITIONALFOODINFORMATION GETADDITIONALFOODINFORMATION =
                new GETADDITIONALFOODINFORMATION();
        GETADDITIONALFOODINFORMATION.execute(amm.toString());
      return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main_blank, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();



        if(b!=null)
        {
            id =(String) b.get("id");
            foodname = (String) b.get("foodname");
            foodselection = (String) b.get("foodselection");
            SharedFoodListDatabase = (String) b.get("SharedFoodListDatabase");

            Log.d("receivedFragment", String.valueOf(id));



        }



    }
    private void AddFoodtoDatabase(List<Float> nutritiens) {
        Date date = new Date();
        Date newDate = new Date(date.getTime());
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Log.d("timestamp", timestamp.toString());

        String stringdate = dt.format(newDate);
        SelectedFood thisuser = new SelectedFood(
                id,
                foodname,
                stringdate
        );
        SelectedFood alluser = new SelectedFood(
                id,
                foodname,
                UserId, timestamp, nutritiens.get(0)
                ,nutritiens.get(1),nutritiens.get(2),nutritiens.get(3)
        );
        SelectedFoodmodel alluser1 = new SelectedFoodmodel(
                id,
                foodname,
                UserId, stringdate
        );



        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);

        Call<Result> call = PHPService.Factory.create().addSelectedFood(
                id,"shshshshs",
                ((ApplicationController)context.getApplicationContext()).getId(), timestamp, nutritiens.get(0)
                ,nutritiens.get(1),nutritiens.get(2),nutritiens.get(3),
                foodselection,0
        );
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                //change Result
                Toast.makeText(requireActivity(), response.message(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                // progressDialog.dismiss();
                Toast.makeText(requireActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        alluser1.setDate(stringdate);
        alluser1.setFoodid(id);
        alluser1.setUserId(UserId);
        alluser1.setFoodName(foodname);

    }

    public class GETADDITIONALFOODINFORMATION extends AsyncTask<String, String, List<Float>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            productname.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            nutritionaldisplay.setVisibility(View.INVISIBLE);
            Textv.setVisibility(View.INVISIBLE);
        }

        @Override
        protected List<Float> doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

           // return null;
            Log.e("ayaa", "aaaa");
            try
            {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while((line=reader.readLine())!=null)
                {
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
           JSONObject parentObject = new JSONObject(finalJson);
           JSONArray parentArray = parentObject.getJSONArray("foods");
            StringBuffer finalBufferData = new StringBuffer();

            JSONObject finalobject = parentArray.getJSONObject(0);
            JSONObject aaa = finalobject.getJSONObject("food");
            JSONArray aaa1 = aaa.getJSONArray("nutrients");
            JSONObject enerhy = aaa1.getJSONObject(1);
                JSONObject protein = aaa1.getJSONObject(3);
                JSONObject fat = aaa1.getJSONObject(4);
                JSONObject carbohydrates = aaa1.getJSONObject(6);
              float enerhykcal = Float.parseFloat(String.format(Locale.getDefault(), "%.2f", Float.parseFloat(enerhy.getString("value"))));
                float proteinvalue = Float.parseFloat(String.format(Locale.getDefault(), "%.2f",Float.parseFloat(protein.getString("value"))));
                float fatvalue = Float.parseFloat(String.format(Locale.getDefault(), "%.2f",Float.parseFloat(fat.getString("value"))));
                float carbohydratesvalue = Float.parseFloat(String.format(Locale.getDefault(),"%.2f",Float.parseFloat(carbohydrates.getString("value"))));

           List<Float> modelList = new ArrayList<>();
           modelList.add(enerhykcal);
           modelList.add(proteinvalue);
           modelList.add(fatvalue);
           modelList.add(carbohydratesvalue);
                return modelList;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection!=null)
                {
                    connection.disconnect();
                }
                try {
                    if(reader!=null)
                    {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
          return null;
        }

        @Override
        protected void onPostExecute(final List<Float> nutritiens) {
            super.onPostExecute(nutritiens);

            if (nutritiens != null) {
                addtoSqlite.setActivated(true);
                addtoSqlite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AddFoodtoDatabase(nutritiens);
                    }
                });
                productname.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                nutritionaldisplay.setVisibility(View.VISIBLE);
                Textv.setVisibility(View.VISIBLE);
                Textv.setText("CALORIES PER 100G " + nutritiens.get(3) + " KCAL");
            }
            else
            {
                Textv.setText("Oups Error");
            }
        }

        }
    }



