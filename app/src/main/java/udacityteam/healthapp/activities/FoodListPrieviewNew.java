package udacityteam.healthapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import udacityteam.healthapp.R;
import udacityteam.healthapp.adapters.CustomAdapter;
import udacityteam.healthapp.adapters.CustomAdapterFoodListPrievew;
import udacityteam.healthapp.models.SelectedFood;
import udacityteam.healthapp.models.SharedFoodProducts;

public class FoodListPrieviewNew extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;
    DatabaseReference usersdatabase;
    String foodselection, key;

    String catergoryId = "ff";
    FirebaseRecyclerAdapter<SelectedFood, FoodViewHolder> adapter;
    String stringdate;
    Button share;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        mAuth = FirebaseAuth.getInstance();


        Intent iin = getIntent();

        Bundle b = iin.getExtras();
        foodselection = (String) b.get("foodselection");
        key = (String) b.get("key");
            Date date = new Date();
            Date newDate = new Date(date.getTime());
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
            stringdate = dt.format(newDate);



        database = FirebaseDatabase.getInstance();
        getSupportActionBar().setTitle(foodselection);
        foodList = database.getReference("MainFeed").child(foodselection).child("SharedDiets").child(key).child("SelectedFoods");
        Log.d("kej", key);
        foodList.orderByChild("date").equalTo(stringdate);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        loadListFood(catergoryId);


    }

    private void loadListFood(String catergoryId) {
        foodList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Getting current user Id
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


                // Filter User
                List<String> userList = new ArrayList<>();
//                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                            Log.d("shhss", "ahahha");
//                         //   listodydis.setText();
//                            if (!dataSnapshot1.getValue(SelectedFood.class).getUserId().equals(uid)) {
//                                userList.add(dataSnapshot1.getValue(SelectedFood.class));
//                            }
//                        }
                ArrayList<SelectedFood> selectedFoods = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    SelectedFood foundFood = dataSnapshot1.getValue(SelectedFood.class);
                     selectedFoods.add(foundFood);

                }

                CustomAdapterFoodListPrievew mAdapter = new CustomAdapterFoodListPrievew(selectedFoods);
                recyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
                //listodydis.setText("errrooas");
            }
        });


        sharefoodlist();
    }

    private void sharefoodlist() //only if today
    {
        share = findViewById(R.id.share);
        share.setText("Cant share");
//
    }
}
