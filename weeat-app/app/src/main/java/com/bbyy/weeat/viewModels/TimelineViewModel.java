package com.bbyy.weeat.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bbyy.weeat.models.bean.StorageItem;
import com.bbyy.weeat.models.bean.StorageItem;
import com.bbyy.weeat.repositories.StorageRepository;
import com.bbyy.weeat.repositories.TodoRepository;
import com.bbyy.weeat.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class TimelineViewModel extends AndroidViewModel {
    private final StorageRepository repository;
    private MutableLiveData<List<StorageItem>> list;
    private String day="2024-04-12";

    public TimelineViewModel(@NonNull Application application) {
        super(application);
        repository=new StorageRepository(application.getApplicationContext());
        list=repository.getItemsForDate(System.currentTimeMillis());
    }

    public MutableLiveData<List<StorageItem>> getList() {
        return list;
    }

    public void setList(MutableLiveData<List<StorageItem>> list) {
        this.list = list;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void changeList(int tabPosition){
        list=repository.getItemsForDate(DateUtils.getTabTimestamp(tabPosition));
    }

    public void addItem(StorageItem item){
        //只有调用setvalue才能检测到changed?
        List<StorageItem> currentList = list.getValue();
        if (currentList != null) {
            currentList.add(item);
            list.setValue(currentList);
        }else{
            currentList=new ArrayList<>();
            currentList.add(item);
        }
        //repository.insertItems(item);
    }

    public void deleteItem(int position,StorageItem item){
        List<StorageItem> currentList = list.getValue();
        if (currentList != null) {
            currentList.remove(position);
            list.setValue(currentList);
        }
        //repository.deleteItems(item);
    }

    public void updateItem(int position,StorageItem item){
        List<StorageItem> currentList = list.getValue();
        if (currentList != null) {
            currentList.set(position,item);
        }else{
            currentList=new ArrayList<>();
            currentList.add(item);
        }
        list.setValue(currentList);
        //repository.updateItems(item);
    }

    public void updateList() {
        //list=repository.getItemsLiveByName(day);
    }
}