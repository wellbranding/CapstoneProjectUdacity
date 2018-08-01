package udacityteam.healthapp.completeRedesign.UI.AddedFoods.Views;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

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

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import udacityteam.healthapp.R;
import udacityteam.healthapp.activities.MainActivity;
import udacityteam.healthapp.completeRedesign.FoodListComplete;
import udacityteam.healthapp.completeRedesign.UI.MainActivity.ViewModels.MainActivityViewModelGood;

/**
 * Created by vvost on 11/26/2017.
 */

public class FoodNutritiensDisplayFragment extends Fragment {
    TextView  mCalories, mProtein, mFats, mCarbos;
    Button addtoSqlite;
    String id = null;
    String foodname = null;
    String foodselection = null;
    ProgressBar progressBar;
    TextView productname;
    String SharedFoodListDatabase;
    private Context context;

    private MainActivityViewModelGood viewModel;

    @Inject
    ViewModelProvider.Factory ViewModelFactory;

    private boolean isHasAdded = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view =  inflater.inflate(R.layout.food_fragment,
              container, false);
        setHasOptionsMenu(true);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        //   toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(foodname);
        setHasOptionsMenu(true);
        progressBar = view.findViewById(R.id.progressbar);
        productname = view.findViewById(R.id.ProductName);
        mCalories = view.findViewById(R.id.calories_display);
        mProtein = view.findViewById(R.id.protein_display);
        mCarbos= view.findViewById(R.id.carbos_display);
        mFats = view.findViewById(R.id.fats_display);

        addtoSqlite = view.findViewById(R.id.button2);
        addtoSqlite.setActivated(false);
        productname.setText(foodname);
        String amm = "https://api.nal.usda.gov/ndb/V2/reports?ndbno=" +
                id +
                "&type=f&format=json&api_key=HXLecTDsMqy1Y6jNoYPw2n3DQ30FeGXxD2XBZqJh";
        GETADDITIONALFOODINFORMATION GETADDITIONALFOODINFORMATION =
                new GETADDITIONALFOODINFORMATION();
        GETADDITIONALFOODINFORMATION.execute(amm);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main_blank, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }
    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity(), ViewModelFactory).
                get(MainActivityViewModelGood.class);

        Bundle b = getArguments();

        viewModel.getResultMediatorLiveData().observe(this, resultApiResponse ->
        {
            if(resultApiResponse!=null)
            if (resultApiResponse.isSuccessful()
                    && isHasAdded) {

                Intent intent = new Intent(requireActivity(), FoodListComplete.class);
                Date date = new Date();
                Date newDate = new Date(date.getTime());
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                Log.d("timestamp", timestamp.toString());
                String stringdate = dt.format(newDate);
                intent.putExtra("foodselection", foodselection);
                intent.putExtra("SharedFoodListDatabase", SharedFoodListDatabase);
                intent.putExtra("requestdate", stringdate);
                startActivity(intent);
                Log.d("importnant", resultApiResponse.body.getMessage());
                Toast.makeText(requireActivity(), resultApiResponse.body.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        if(b!=null)
        {
            id =(String) b.get("id");
            foodname = (String) b.get("foodname");
            foodselection = (String) b.get("whichtime");
            SharedFoodListDatabase = (String) b.get("SharedFoodListDatabase");

        }



    }

    public class GETADDITIONALFOODINFORMATION extends AsyncTask<String, String, List<Float>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            productname.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
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
                      //  AddFoodtoDatabase(nutritiens);
                        viewModel.IfHasPosted();
                        viewModel.AddFoodtoDatabase(foodselection, id,
                                foodname, nutritiens);
                        isHasAdded = true;
                    }
                });
                mCalories.setText("CALORIES: " + nutritiens.get(0));
                mProtein.setText("PROTEIN: " + nutritiens.get(1));
                mFats.setText("FATS: " + nutritiens.get(2));
                mCarbos.setText("CARBOHYDRATES: " + nutritiens.get(3));
                productname.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
            else
            {
            }
        }

        }
    }



