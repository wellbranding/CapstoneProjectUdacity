package udacityteam.healthapp.completeRedesign.UI.Community.Views;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import javax.inject.Inject;

import io.apptik.widget.MultiSlider;
import udacityteam.healthapp.R;
import udacityteam.healthapp.completeRedesign.UI.Community.ViewModels.SharedFoodListsViewModelNew;

public class FilterFragment extends DialogFragment {

    TextView carbohydratesend, proteinbegin, proteinend, caloriesbegin, caloriesend,
            fatsbegin, fatsend;
    MultiSlider carbohydrates, protein, calories, fats;
    String SharedFoodListDatabase;
    Button confirm;

    SharedFoodListsViewModelNew viewModel;

    @Inject
    ViewModelProvider.Factory ViewModelFactory;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_filter, container, false);
        carbohydratesend = view.findViewById(R.id.carboend);
        proteinbegin = view.findViewById(R.id.proteinbegin);
        proteinend = view.findViewById(R.id.proteinend);
        caloriesbegin = view.findViewById(R.id.caloriesbegin);
        caloriesend = view.findViewById(R.id.caloriesnend);
        fatsbegin = view.findViewById(R.id.fatbegin);
        fatsend = view.findViewById(R.id.fatnend);
        confirm = view.findViewById(R.id.confirm);


        Intent iin = getActivity().getIntent();

        Bundle b = iin.getExtras();

        SharedFoodListDatabase = (String) b.get("SharedFoodListDatabase");


        carbohydrates = view.findViewById(R.id.carbohydrates);
        protein = view.findViewById(R.id.carbos);
        calories = view.findViewById(R.id.calories);
        fats = view.findViewById(R.id.fat);
        carbohydrates.setMax(10000);
        protein.setMax(10000);
        fats.setMax(10000);
        calories.setMax(10000);
        calories.jumpDrawablesToCurrentState();
        carbohydrates.getThumb(0).setValue(0);
        carbohydrates.getThumb(1).setValue(10000);
        protein.getThumb(0).setValue(0);
        protein.getThumb(1).setValue(10000);
        fats.getThumb(0).setValue(0);
        fats.getThumb(1).setValue(10000);
        calories.getThumb(0).setValue(0);
        calories.getThumb(1).setValue(10000);
        carbohydratesend.setText(R.string.any_filter_text);
        proteinend.setText(R.string.any_filter_text);
        fatsend.setText(R.string.any_filter_text);
        caloriesend.setText(R.string.any_filter_text);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetFilteredSharedDiets();
                dismiss();
            }
        });

        carbohydrates.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider,
                                       MultiSlider.Thumb thumb,
                                       int thumbIndex,
                                       int value) {
                if (carbohydrates.getThumb(0).getValue() == 0) {
                    carbohydratesend.setText(String.format("Up to %s", String.valueOf(carbohydrates.getThumb(1).getValue())));
                }
                if (carbohydrates.getThumb(1).getValue() == 10000) {
                    carbohydratesend.setText(String.format("From  %s", String.valueOf(carbohydrates.getThumb(0).getValue())));
                }
                if (carbohydrates.getThumb(0).getValue() != 0 && carbohydrates.getThumb(1).getValue() != 10000)
                    carbohydratesend.setText(String.format("%s - %s", String.valueOf(carbohydrates.getThumb(0).getValue()), String.valueOf(carbohydrates.getThumb(1).getValue())));
                if (carbohydrates.getThumb(0).getValue() == 0 &&
                        carbohydrates.getThumb(1).getValue() == 10000)
                    carbohydratesend.setText(R.string.fiilter_any);

            }
        });
        protein.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider,
                                       MultiSlider.Thumb thumb,
                                       int thumbIndex,
                                       int value) {
                if (protein.getThumb(0).getValue() == 0) {
                    proteinend.setText(String.format("Up to %s", String.valueOf(protein.getThumb(1).getValue())));
                }
                if (protein.getThumb(1).getValue() == 10000) {
                    proteinend.setText(String.format("From  %s", String.valueOf(protein.getThumb(0).getValue())));
                }
                if (protein.getThumb(0).getValue() != 0 && protein.getThumb(1).getValue() != 10000)
                    proteinend.setText(String.format("%s - %s", String.valueOf(protein.getThumb(0).getValue()),
                            String.valueOf(protein.getThumb(1).getValue())));
                if (protein.getThumb(0).getValue() == 0 &&
                       protein.getThumb(1).getValue() == 10000)
                    proteinend.setText(R.string.fiilter_any);
            }
        });
        fats.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider,
                                       MultiSlider.Thumb thumb,
                                       int thumbIndex,
                                       int value) {
                if (fats.getThumb(0).getValue() == 0) {
                   fatsend.setText(String.format("Up to %s", String.valueOf(fats.getThumb(1).getValue())));
                }
                if (fats.getThumb(1).getValue() == 10000) {
                    fatsend.setText(String.format("From  %s",
                            String.valueOf(fats.getThumb(0).getValue())));
                }
                if (fats.getThumb(0).getValue() != 0 && fats.getThumb(1).getValue() != 10000)
                   fatsend.setText(String.format("%s - %s", String.valueOf(fats.getThumb(0).
                                   getValue()),
                            String.valueOf(fats.getThumb(1).getValue())));
                if (fats.getThumb(0).getValue() == 0 &&
                        fats.getThumb(1).getValue() == 10000)
                    fatsend.setText(R.string.fiilter_any);
            }
        });
        calories.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider,
                                       MultiSlider.Thumb thumb,
                                       int thumbIndex,
                                       int value) {
                if (calories.getThumb(0).getValue() == 0) {
                  caloriesend.setText(String.format("Up to %s", String.valueOf(
                          calories.getThumb(1).getValue())));
                }
                if (calories.getThumb(1).getValue() == 10000) {
                   caloriesend.setText(String.format("From  %s",
                            String.valueOf(fats.getThumb(0).getValue())));
                }
                if (calories.getThumb(0).getValue() != 0 && calories.getThumb(1).getValue() != 10000)
                    caloriesend.setText(String.format("%s - %s", String.valueOf(calories.getThumb(0).
                                    getValue()),
                            String.valueOf(calories.getThumb(1).getValue())));
                if (calories.getThumb(0).getValue() == 0 &&
                        calories.getThumb(1).getValue() == 10000)
                   caloriesend.setText(R.string.fiilter_any);
            }
        });
        return view;
    }

    private void GetFilteredSharedDiets()
    {
        viewModel.GetFilteredSharedDiets(protein, calories, carbohydrates, fats, SharedFoodListDatabase);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity(), ViewModelFactory).
                get(SharedFoodListsViewModelNew.class);
    }
}




