package com.bbyy.weeat.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bbyy.weeat.models.bean.TodoItem;
import com.bbyy.weeat.repositories.TodoRepository;

import java.util.ArrayList;
import java.util.List;

public class TodoViewModel extends AndroidViewModel {
    private final TodoRepository repository;
    private MutableLiveData<List<TodoItem>> todoList;
    private String name="default";

    public TodoViewModel(@NonNull Application application) {
        super(application);
        repository=new TodoRepository(application.getApplicationContext());
        todoList=repository.getItemsLiveByName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MutableLiveData<List<TodoItem>> getTodoList() {
        return todoList;
    }

    public void setTodoList(MutableLiveData<List<TodoItem>> todoList) {
        this.todoList = todoList;
    }

    public void addItem(TodoItem todoItem){
        //只有调用setvalue才能检测到changed?
        List<TodoItem> currentList = todoList.getValue();
        if (currentList != null) {
            currentList.add(todoItem);
            todoList.setValue(currentList);
        }else{
            currentList=new ArrayList<>();
            currentList.add(todoItem);
        }
        repository.insertItems(todoItem);
    }

    public void deleteItem(int position,TodoItem todoItem){
        List<TodoItem> currentList = todoList.getValue();
        if (currentList != null) {
            currentList.remove(position);
            todoList.setValue(currentList);
        }
        repository.deleteItems(todoItem);
    }

    public void updateItem(int position,TodoItem item){
        List<TodoItem> currentList = todoList.getValue();
        if (currentList != null) {
            currentList.set(position,item);
        }else{
            currentList=new ArrayList<>();
            currentList.add(item);
        }
        todoList.setValue(currentList);
        repository.updateItems(item);
    }

    public void updateList() {
        todoList=repository.getItemsLiveByName(name);
    }
}