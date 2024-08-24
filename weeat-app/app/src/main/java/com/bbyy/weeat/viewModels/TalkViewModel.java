package com.bbyy.weeat.viewModels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bbyy.weeat.models.bean.TalkItem;
import com.bbyy.weeat.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class TalkViewModel extends AndroidViewModel {
    private final MutableLiveData<List<TalkItem>> dataList;
    private final MutableLiveData<Boolean> isTypeStyle=new MutableLiveData<>(true);
    private final MutableLiveData<Boolean> isEmpty=new MutableLiveData<>(true);
    private SharedPreferences sharedPreferences;

    public long getContext_id() {
        return context_id;
    }

    private long context_id;

    public TalkViewModel(@NonNull Application application) {
        super(application);
        init(application);
        this.dataList=new MutableLiveData<>(new ArrayList<>());
    }

    private void init(Application application){
        String USER_INFO = "user_info";
        sharedPreferences = application.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        String CONTEXT_ID = "context_id";
        context_id = sharedPreferences.getLong(CONTEXT_ID, 0L);
    }

    public MutableLiveData<Boolean> getIsTypeStyle() {
        return isTypeStyle;
    }

    public void changeTypeStyle(){
        isTypeStyle.setValue(!isTypeStyle.getValue());
    }

    public void setIsTypeStyle(boolean value){this.isTypeStyle.setValue(value);}

    public MutableLiveData<Boolean> getIsEmpty() {
        return isEmpty;
    }

    public MutableLiveData<List<TalkItem>> getDataList() {
        return dataList;
    }

    public void addItem(TalkItem item){
        //只有调用setvalue才能检测到changed?
        List<TalkItem> currentList = dataList.getValue();
        if (currentList != null) {
            currentList.add(item);
            dataList.setValue(currentList);
        }else{
            currentList=new ArrayList<>();
            currentList.add(item);
            dataList.setValue(currentList);
        }
    }

    public void updateItem(int position,TalkItem item){
        List<TalkItem> currentList = dataList.getValue();
        if (currentList != null) {
            currentList.set(position,item);
        }else{
            currentList=new ArrayList<>();
            currentList.add(item);
        }
        dataList.setValue(currentList);
    }

    public void updateList(List<TalkItem> currentList) {
        dataList.setValue(currentList);
    }

    public int getDataListSize(){
        return dataList.getValue().size();
    }
}