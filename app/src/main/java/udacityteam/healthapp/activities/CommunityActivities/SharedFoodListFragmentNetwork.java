package udacityteam.healthapp.activities.CommunityActivities;

import android.app.FragmentManager;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import udacityteam.healthapp.R;
import udacityteam.healthapp.adapters.FoodViewHolder;
import udacityteam.healthapp.adapters.SharedFoodListsAdapter;
import udacityteam.healthapp.adapters.SharedFoodListsAdapterNew;
import udacityteam.healthapp.completeRedesign.Repository.Status;
import udacityteam.healthapp.completeRedesign.SharedFoodListsViewModelNew;
import udacityteam.healthapp.databinding.CommunityListFragmentBinding;
import udacityteam.healthapp.models.SelectedFood;

/**
 * Created by vvost on 11/16/2017.
 */

public class SharedFoodListFragmentNetwork extends Fragment{
    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;
   private RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<SelectedFood, FoodViewHolder> adapter;
    FirebaseFirestore storage;


    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected SharedFoodListFragmentNetwork.LayoutManagerType mCurrentLayoutManagerType;

    protected RadioButton mLinearLayoutRadioButton;
    protected RadioButton mGridLayoutRadioButton;

    protected RecyclerView mRecyclerView;
    protected SharedFoodListsAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    TextView listodydis;
    protected String[] mDataset;
    public static String value = "Breakfast";
    ProgressBar progressBar;

    private FirebaseAuth mAuth;
    DatabaseReference foodList;
    FirebaseDatabase database;
    Button filterData, test;
    String side, SharedFoodListDatabase;
    private CommunityListFragmentBinding communityListFragmentBinding;
    private SharedFoodListsViewModelNew viewModel;

    @Inject
    ViewModelProvider.Factory ViewModelFactory;

    public static SharedFoodListFragmentNetwork newInstance(Bundle values) {
        SharedFoodListFragmentNetwork storiesFragment = new SharedFoodListFragmentNetwork();
        storiesFragment.setArguments(values);
        return storiesFragment;
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
                get(SharedFoodListsViewModelNew.class);
        Bundle bundle = getArguments();
        if (bundle != null) {
            side = bundle.getString("foodselection", null);
            SharedFoodListDatabase =  bundle.getString("SharedFoodListDatabase");
        }
        viewModel.setWhichTime(SharedFoodListDatabase);

        mAuth = FirebaseAuth.getInstance();

    }
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        communityListFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.community_list_fragment, container, false);
        InitializeRecyclerView();
        observeResult();
        filterData =  communityListFragmentBinding.getRoot().findViewById(R.id.filterdata);

        filterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager ft = getActivity().getFragmentManager();
                FilterActivity dialog = new FilterActivity();
                dialog.show(ft, "MyCustomDialog");
            }
        });
        return communityListFragmentBinding.getRoot();
    }


    public void LoadFoodListMutable()
    {
        communityListFragmentBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        communityListFragmentBinding.recyclerView.setHasFixedSize(true);
    }
private void observeResult()
{

    viewModel.getRecipes().observe(this,repositories->
    {
        if(repositories.status== Status.SUCCESS) {
            SharedFoodListsAdapterNew customAdapterFoodListPrievew = new
                    SharedFoodListsAdapterNew(side);
            customAdapterFoodListPrievew.setSelectedFoods(repositories.data);
            customAdapterFoodListPrievew.notifyDataSetChanged();
            communityListFragmentBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            communityListFragmentBinding.recyclerView.setHasFixedSize(true);
            communityListFragmentBinding.recyclerView.setAdapter(customAdapterFoodListPrievew);
        }
    });

}


    private void InitializeRecyclerView()
    {
        SharedFoodListsAdapterNew customAdapterFoodListPrievew= new
                SharedFoodListsAdapterNew(side);
        viewModel.mutableLiveData.observe(getActivity(), (selectedfoods)->{
            customAdapterFoodListPrievew.setSelectedFoods(selectedfoods);
            customAdapterFoodListPrievew.notifyDataSetChanged();

        } );
        mLayoutManager = new LinearLayoutManager(getActivity());
        communityListFragmentBinding.recyclerView.setLayoutManager(mLayoutManager);
        communityListFragmentBinding.recyclerView.setAdapter(customAdapterFoodListPrievew);

    }


}
