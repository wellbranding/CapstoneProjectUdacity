package udacityteam.healthapp.completeRedesign.UI.Community.Views;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import udacityteam.healthapp.R;
import udacityteam.healthapp.completeRedesign.UI.Community.Adapters.SharedFoodListsAdapterNew;
import udacityteam.healthapp.completeRedesign.Repository.Status;
import udacityteam.healthapp.completeRedesign.UI.Community.ViewModels.SharedFoodListsViewModelNew;
import udacityteam.healthapp.databinding.CommunityListFragmentBinding;

/**
 * Created by vvost on 11/16/2017.
 */

public class SharedFoodListFragmentNetwork extends Fragment{
    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;
   private RecyclerView.LayoutManager layoutManager;


    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected SharedFoodListFragmentNetwork.LayoutManagerType mCurrentLayoutManagerType;

    protected RadioButton mLinearLayoutRadioButton;
    protected RadioButton mGridLayoutRadioButton;

    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    TextView listodydis;
    protected String[] mDataset;
    public static String value = "Breakfast";
    ProgressBar progressBar;

    Button filterData;
    String side, SharedFoodListDatabase;
    private CommunityListFragmentBinding communityListFragmentBinding;
    private SharedFoodListsViewModelNew viewModel;
    SharedFoodListsAdapterNew customAdapterFoodListPrievew;

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


    }
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        communityListFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.community_list_fragment, container, false);
        InitializeRecyclerView();
        observeResult();
        filterData =  communityListFragmentBinding.getRoot().findViewById(R.id.filterdata);
        progressBar = communityListFragmentBinding.getRoot().findViewById(R.id.progressbar);

        filterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager ft = getActivity().getSupportFragmentManager();
                FilterFragment dialog = new FilterFragment();
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
            progressBar.setVisibility(View.INVISIBLE);
            customAdapterFoodListPrievew.setSelectedFoods(repositories.data);
            customAdapterFoodListPrievew.notifyDataSetChanged();
        }
        else if(repositories.status==Status.ERROR)
        {
            progressBar.setVisibility(View.INVISIBLE);
        }
        else if(repositories.status == Status.LOADING)
        {
            progressBar.setVisibility(View.VISIBLE);
        }
    });
    viewModel.getFilteredData().observe(this, filtered->
    {
        customAdapterFoodListPrievew.setSelectedFoods(filtered.data);
        customAdapterFoodListPrievew.notifyDataSetChanged();
    });

}


    private void InitializeRecyclerView()
    {
       customAdapterFoodListPrievew = new
                SharedFoodListsAdapterNew(side);
        communityListFragmentBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        communityListFragmentBinding.recyclerView.setHasFixedSize(true);
        communityListFragmentBinding.recyclerView.setAdapter(customAdapterFoodListPrievew);
    }


}
