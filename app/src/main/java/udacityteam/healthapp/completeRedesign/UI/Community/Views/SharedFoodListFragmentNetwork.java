package udacityteam.healthapp.completeRedesign.UI.Community.Views;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import udacityteam.healthapp.R;
import udacityteam.healthapp.completeRedesign.Repository.Status;
import udacityteam.healthapp.completeRedesign.UI.Community.Adapters.SharedFoodListsAdapterNew;
import udacityteam.healthapp.completeRedesign.UI.Community.ViewModels.SharedFoodListsViewModelNew;
import udacityteam.healthapp.databinding.CommunityListFragmentBinding;

public class SharedFoodListFragmentNetwork extends Fragment {

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
            side = bundle.getString(getString(R.string.which_time_key), null);
            SharedFoodListDatabase = bundle.getString("SharedFoodListDatabase");
        }
        viewModel.setWhichTime(SharedFoodListDatabase);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        communityListFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.community_list_fragment, container, false);
        InitializeRecyclerView();
        observeResult();
        filterData = communityListFragmentBinding.getRoot().findViewById(R.id.filterdata);
        progressBar = communityListFragmentBinding.getRoot().findViewById(R.id.progressbar);

        filterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FilterFragment dialog = new FilterFragment();
                dialog.show(ft, getString(R.string.filter_dialog_tag));
            }
        });
        return communityListFragmentBinding.getRoot();
    }


    private void observeResult() {

        viewModel.getSharedFoodLists().observe(this, repositories ->
        {
            if (repositories != null) {
                if (repositories.status == Status.SUCCESS) {
                    progressBar.setVisibility(View.INVISIBLE);
                    customAdapterFoodListPrievew.setSelectedFoods(repositories.data);
                    customAdapterFoodListPrievew.notifyDataSetChanged();
                } else if (repositories.status == Status.ERROR) {
                    progressBar.setVisibility(View.INVISIBLE);
                } else if (repositories.status == Status.LOADING) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
        viewModel.getFilteredData().observe(this, filtered ->
        {
            customAdapterFoodListPrievew.setSelectedFoods(filtered.data);
            customAdapterFoodListPrievew.notifyDataSetChanged();
        });

    }


    private void InitializeRecyclerView() {
        customAdapterFoodListPrievew = new
                SharedFoodListsAdapterNew(side);
        communityListFragmentBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        communityListFragmentBinding.recyclerView.setHasFixedSize(true);
        communityListFragmentBinding.recyclerView.setAdapter(customAdapterFoodListPrievew);
    }
}
