package com.bbyy.weeat.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.bbyy.weeat.repositories.CookTimeRepository;
import com.bbyy.weeat.repositories.StorageRepository;

public class OverviewViewModel extends AndroidViewModel {
    private final StorageRepository storageRepository;
    private final CookTimeRepository cookTimeRepository;
    public OverviewViewModel(@NonNull Application application) {
        super(application);
        storageRepository=new StorageRepository(application);
        cookTimeRepository=new CookTimeRepository(application);
    }
    // TODO: Implement the ViewModel


}