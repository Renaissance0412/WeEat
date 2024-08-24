package com.bbyy.weeat.viewModels;

import android.app.Application;
import android.widget.MultiAutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bbyy.weeat.models.bean.StorageItem;
import com.bbyy.weeat.repositories.StorageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StorageViewModel extends AndroidViewModel {
    private final StorageRepository repository;
    private MutableLiveData<Integer> position=new MutableLiveData<>(0);
    private MutableLiveData<List<StorageItem>> storageList;
    private MutableLiveData<List<StorageItem>> tabList=new MutableLiveData<>(new ArrayList<>());
    private int tab=0;

    public StorageViewModel(@NonNull Application application) {
        super(application);
        repository=new StorageRepository(application);
        storageList=repository.getItems();
        while(storageList==null){}
        tabList.setValue(storageList.getValue());
    }

    public MutableLiveData<List<StorageItem>> getTabList() {
        return tabList;
    }

    public void changeTabList(int tab) {
        storageList=repository.getItems();
        this.tab=tab;
        switch (tab){
            case 0:
                tabList.setValue(storageList.getValue());
                break;
            case 1:
                tabList.setValue(getFreshList());
                break;
            case 2:
                tabList.setValue(getExpiredList());
                break;
            default:
                break;
        }
    }

    private List<StorageItem> getFreshList(){
        // 实现过滤未过期的逻辑
        return storageList.getValue().stream()
                .filter(item ->!item.isExpired())
                .collect(Collectors.toList());
    }

    private List<StorageItem> getExpiredList(){
        // 实现过滤过期的逻辑
        return storageList.getValue().stream()
                .filter(item ->item.isExpired())
                .collect(Collectors.toList());
    }

    public MutableLiveData<List<StorageItem>> getStorageList() {
        return storageList;
    }

    public MutableLiveData<Integer> getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position.setValue(position);
    }

    public void addItem(StorageItem item){
        List<StorageItem> currentList = storageList.getValue();
        if (currentList == null)
            currentList=new ArrayList<>();
        currentList.add(item);
        storageList.setValue(currentList);
        switch (tab){
            case 0:
                tabList.setValue(storageList.getValue());
                break;
            case 1:
                tabList.setValue(getFreshList());
                break;
            case 2:
                tabList.setValue(getExpiredList());
                break;
            default:
                break;
        }
        repository.insertItems(item);
    }

    public void deleteItem(int position,StorageItem item){
        List<StorageItem> currentList = tabList.getValue();
        if (currentList != null) {
            currentList.remove(position);
            tabList.setValue(currentList);
        }
        repository.deleteItems(item);
        storageList=repository.getItems();
    }

    public void updateItem(int position,StorageItem item){
        List<StorageItem> currentList = tabList.getValue();
        if (currentList != null) {
            currentList.remove(position);
            tabList.setValue(currentList);
        }
        repository.updateItems(item);
        storageList=repository.getItems();
    }
}