package com.bbyy.weeat.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bbyy.weeat.models.bean.response.Recipe;
import com.bbyy.weeat.repositories.TodoRepository;

import java.util.ArrayList;
import java.util.List;

public class TodoListViewModel extends AndroidViewModel {
    private MutableLiveData<List<String>> nameList;
    private final TodoRepository repository;

    public TodoListViewModel(@NonNull Application application) {
        super(application);
        repository=new TodoRepository(application.getApplicationContext());
        nameList=repository.getAllListNameLive();
    }

    public MutableLiveData<List<String>> getNameList() {
        return nameList;
    }

    public void setNameList(MutableLiveData<List<String>> nameList) {
        this.nameList = nameList;
    }

    public void addList(String listName){
        //只有调用setvalue才能检测到changed?
        List<String> currentList = nameList.getValue();
        if (currentList != null) {
            currentList.add(listName);
            nameList.setValue(currentList);
        }else{
            currentList=new ArrayList<>();
            currentList.add(listName);
        }
        repository.insertLists(listName);
    }

    public void addGptList(List<Recipe> list){
        List<String> currentList = nameList.getValue();
        if(currentList==null){
            currentList=new ArrayList<>();
        }
        for(Recipe recipe:list){
            currentList.add(recipe.getName());
            repository.insertLists(recipe.getName());
            repository.insertGptLists(recipe.getName(),recipe.getDesc());
        }
        nameList.postValue(currentList);
    }

    public void deleteList(int position,String listName){
        List<String> currentList = nameList.getValue();
        if (currentList != null) {
            currentList.remove(position);
            nameList.setValue(currentList);
        }
        repository.deleteItemsByName(listName);
    }
}