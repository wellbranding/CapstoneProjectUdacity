package udacityteam.healthapp.completeRedesign.UI.MainActivity.Views;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;


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
import java.util.ArrayList;

import dagger.android.support.AndroidSupportInjection;
import udacityteam.healthapp.R;
import udacityteam.healthapp.completeRedesign.UI.MainActivity.Adapters.SearchFoodsAdapter;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.Model;


public class FoodSearchFragment extends Fragment implements SearchView.OnQueryTextListener {
    StringBuffer buffer;
    int amm = 0;
    ArrayList<String> arrayCountry;
    String SharedFoodListDatabase;
    private final ArrayList<Model> models = new ArrayList<>();
    ArrayList<Model> models1;
    RecyclerView lv;
    RelativeLayout noresultsdisplay;
    public static String foodselection = null;
    RecyclerView main;
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TERM = "term";
    private static final String DEFAULT = "default";
    private SearchManager searchManager;
    private SearchView searchView;
    TextView message;
    private MenuItem searchMenuItem;
    private SuggestAdapter suggestionsAdapter;
    private final ArrayList<String> dummyArray = new ArrayList<String>();
    FoodListRecyclerViewListener mCallback;

    public interface FoodListRecyclerViewListener {
        public void onFoodListSelected(String id, String foodName,
                                       String whichTime );
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     View view = inflater.inflate(R.layout.activity_foodsearchfragmnet,
             container, false);
  //      Toolbar toolbar = view.findViewById(R.id.toolbar);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
     //   toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
     ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        toolbar.setTitle(foodselection);

        main = view.findViewById(R.id.main);
        noresultsdisplay = view.findViewById(R.id.noresultsdisplay);
        lv = (RecyclerView) view.findViewById(R.id.listViewCountry);
        lv.setVisibility(View.INVISIBLE);
        arrayCountry = new ArrayList<>();
        models1 = new ArrayList<>();
     return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(requireActivity());
        Intent iin = requireActivity().getIntent();
        Bundle b = getArguments();
      //  Bundle b = iin.getExtras();
        if(b!=null) {
            foodselection = (String) b.get("foodselection");
            SharedFoodListDatabase = (String) b.get("SharedFoodListDatabase");
            Log.d("receivedFragmentSearch", String.valueOf(foodselection));
            Log.d("receivedFragmentSearch", String.valueOf(SharedFoodListDatabase));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);

      //  (((AppCompatActivity)requireActivity()).getSupportActionBar()).setDisplayShowHomeEnabled(true);
    //    Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        searchMenuItem = menu.findItem(R.id.menuSearch);
        searchManager = (SearchManager) requireActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);

    }

    @Override
    public boolean onQueryTextChange(final String newText) {

        StringBuilder amm = new StringBuilder();
        //  amm.append("https://api.nal.usda.gov/ndb/V2/reports?ndbno=");
        amm.append("https://api.nal.usda.gov/ndb/search/?format=json&q=");
        amm.append(newText);
        amm.append("&sort=r&max=10&offset=10&ds=Standard+Reference&api_key=HXLecTDsMqy1Y6jNoYPw2n3DQ30FeGXxD2XBZqJh");
        //   amm.append("&type=f&format=json&api_key=HXLecTDsMqy1Y6jNoYPw2n3DQ30FeGXxD2XBZqJh");
        new JSONTask().execute(amm.toString());


        return true;
    }
    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);

        try {
            mCallback = (FoodListRecyclerViewListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


    @Override
    public boolean onQueryTextSubmit(final String query) {
        StringBuilder amm = new StringBuilder();
        //  amm.append("https://api.nal.usda.gov/ndb/V2/reports?ndbno=");

        amm.append("https://api.nal.usda.gov/ndb/search/?format=json&q=");
        amm.append(query);
        amm.append("&sort=r&max=50&offset=0&ds=Standard+Reference&api_key=HXLecTDsMqy1Y6jNoYPw2n3DQ30FeGXxD2XBZqJh");
        //   amm.append("&type=f&format=json&api_key=HXLecTDsMqy1Y6jNoYPw2n3DQ30FeGXxD2XBZqJh");
        main.setVisibility(View.VISIBLE);
        lv.setVisibility(View.INVISIBLE);
        searchView.setQuery("", false);
        models1.clear();
        new FoodSearchFragment.JSONTask1().execute(amm.toString(), query);
        // TODO Auto-generated method stub
        return false;
    }

    private class SuggestAdapter extends CursorAdapter implements View.OnClickListener {

        private final ArrayList<Model> mObjects;
        private final LayoutInflater mInflater;
        private TextView tvSearchTerm;

        public SuggestAdapter(final Context ctx, final Cursor cursor, final ArrayList<Model> mObjects) {
            super(ctx, cursor, 0);

            this.mObjects = mObjects;
            this.mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View newView(final Context ctx, final Cursor cursor, final ViewGroup parent) {
            final View view = mInflater.inflate(R.layout.text_row_item1, parent, false);


            tvSearchTerm = (TextView) view.findViewById(R.id.textView);

            return view;
        }

        @Override
        public void bindView(final View view, final Context ctx, final Cursor cursor) {

            tvSearchTerm = (TextView) view.findViewById(R.id.textView);

            final int position = cursor.getPosition();

            if (cursorInBounds(position)) {

                final String term = mObjects.get(position).getName();
                tvSearchTerm.setText(term);

                view.setTag(position);
                view.setOnClickListener(this);

            } else {
                // Something went wrong
            }
        }

        private boolean cursorInBounds(final int position) {
            return position < mObjects.size();
        }

        @Override
        public void onClick(final View view) {

            final int position = (Integer) view.getTag();

            if (cursorInBounds(position)) {


//                Intent intent = new Intent(requireActivity(), FoodNutritiensDisplay.class);
//                StringBuilder amm = new StringBuilder();
//                amm.append("https://api.nal.usda.gov/ndb/V2/reports?ndbno=");
//                amm.append(models.get(position).getId());
//                amm.append("&type=f&format=json&api_key=HXLecTDsMqy1Y6jNoYPw2n3DQ30FeGXxD2XBZqJh");
                //new GETADDITIONALFOODINFORMATION().execute(amm.toString());
                
                mCallback.onFoodListSelected(models.get(position).getId(),
                        models.get(position).getName(), foodselection);
//                intent.putExtra("id", models.get(position).getId());
//                intent.putExtra("foodname", models.get(position).getName());
//                intent.putExtra("foodselection", foodselection);
//
//                startActivity(intent);
                Log.d("ama", "Element " + position + " clicked.");

                final String selected = mObjects.get(position).getId();

                Toast.makeText(requireActivity(), selected, Toast.LENGTH_SHORT).show();

                // Do something

            } else {
                // Something went wrong
            }
        }
    }

    private MatrixCursor getCursor(final ArrayList<String> suggestions) {

        final String[] columns = new String[] { COLUMN_ID, COLUMN_TERM };
        final Object[] object = new Object[] { 0, DEFAULT };

        final MatrixCursor matrixCursor = new MatrixCursor(columns);

        for (int i = 0; i < suggestions.size(); i++) {

            object[0] = i;
            object[1] = suggestions.get(i);

            matrixCursor.addRow(object);
        }

        return matrixCursor;
    }


    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            // return null;
            Log.e("ayaa", "aaaa");
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONObject parentArray = parentObject.getJSONObject("list");
                JSONArray array = parentArray.getJSONArray("item");
                JSONObject finalobjectt = array.getJSONObject(0);
                String name = finalobjectt.getString("name");
                models.clear();
                dummyArray.clear();

                for (int i = 0; i < array.length(); i++) {

                    JSONObject finalobject = array.getJSONObject(i);
                    Model model = new Model(finalobject.getString("name"), finalobject.getString("offset"),
                            finalobject.getString("ndbno"));
                    models.add(model);
                    dummyArray.add(model.getName());
                    Log.e("aaa", String.valueOf(models.size()));
                }


                return finalobjectt.getString("ndbno");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                try {
                    Log.d("ahhaa", String.valueOf(models.size()));
                    final MatrixCursor matrixCursor = getCursor(dummyArray);
                    suggestionsAdapter = new SuggestAdapter(requireActivity(), matrixCursor, models);
                    searchView.setSuggestionsAdapter(suggestionsAdapter);
                    suggestionsAdapter.notifyDataSetChanged();
                    // aki.setText(String.valueOf(models.size()));
                } catch (Exception e) {
                    // aki.setText(s);
                }


            }


        }
    }
    public class JSONTask1 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            // return null;
            Log.e("ayaa", "aaaa");
            try {
                URL url = new URL(strings[0]);
                String query = strings[1];
                Log.d("taga", query);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONObject parentArray = parentObject.getJSONObject("list");
                JSONArray array = parentArray.getJSONArray("item");
                JSONObject finalobjectt = array.getJSONObject(0);
                String name = finalobjectt.getString("name");
                models1.clear();
                for (int i = 0; i < array.length(); i++) {

                    JSONObject finalobject = array.getJSONObject(i);
                    Model model = new Model(finalobject.getString("name"), finalobject.getString("offset"),
                            finalobject.getString("ndbno"));
                    models1.add(model);
                    Log.e("aaa", String.valueOf(models1.size()));
                }
                return finalobjectt.getString("ndbno");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                try {
                    LinearLayoutManager liner = new LinearLayoutManager(requireActivity());
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(main.getContext(),
                            liner.getOrientation());
                    if (models1.size() != 0) {
                        main.setLayoutManager(liner);
                        SearchFoodsAdapter adapter = new SearchFoodsAdapter(models1, foodselection, (id, name, whichTime) -> mCallback.onFoodListSelected(id,
                                name, whichTime));

                        main.setAdapter(adapter);
                        main.setVisibility(View.VISIBLE);
                        noresultsdisplay.setVisibility(View.GONE);
                    }


                }
                catch (Exception e) {
                    // aki.setText(s);
                }
            }
            else
            {
                noresultsdisplay.setVisibility(View.VISIBLE);
            }

        }
    }



    }


