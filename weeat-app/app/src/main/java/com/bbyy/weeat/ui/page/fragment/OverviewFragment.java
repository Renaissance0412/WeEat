package com.bbyy.weeat.ui.page.fragment;

import static com.bbyy.weeat.utils.ChartUtils.initHBarChart;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bbyy.weeat.R;
import com.bbyy.weeat.databinding.FragmentOverviewBinding;
import com.bbyy.weeat.ui.view.XYMarkerView;
import com.bbyy.weeat.utils.DecimalFormatter;
import com.bbyy.weeat.viewModels.OverviewViewModel;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

//TODO：这里加点东西
public class OverviewFragment extends Fragment {

    private OverviewViewModel viewModel;
    private FragmentOverviewBinding binding;

    public static OverviewFragment newInstance() {
        return new OverviewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_overview,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setLifecycleOwner(this);
        viewModel=new ViewModelProvider(this).get(OverviewViewModel.class);
        initHBarChart(requireContext(),binding.chart);
    }

}