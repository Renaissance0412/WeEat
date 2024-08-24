package com.bbyy.weeat.repositories;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.bbyy.weeat.models.bean.TodoItem;
import com.bbyy.weeat.repositories.dao.TodoItemDao;
import com.bbyy.weeat.repositories.database.TodoDatabase;

import java.util.List;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class TodoRepository {
    private final TodoItemDao todoItemDao;

    public TodoRepository(Context context) {
        TodoDatabase todoDatabase=TodoDatabase.getDataBase(context);
        todoItemDao=todoDatabase.getTodoItemDao();
    }

    //获得对应清单名的所有代办
    public MutableLiveData<List<TodoItem>> getItemsLiveByName(String name){
        return new MutableLiveData<>(todoItemDao.getItemsByListName(name));
    }

    //获取对应day的所有代办
    public MutableLiveData<List<TodoItem>> getItemsLiveByDay(String day){
        return new MutableLiveData<>(todoItemDao.getItemsByDay(day));
    }

    //获得所有清单名字
    public MutableLiveData<List<String>> getAllListNameLive(){
        return new MutableLiveData<>(todoItemDao.getAllListName());
    }

    //创建清单
    public void insertLists(String... names){
        TodoItem[] items=new TodoItem[names.length];
        for(int i=0;i< names.length;i++){
            items[i]=new TodoItem(names[i]+"0",names[i],"Press the item to edit.");
        }
        insertItems(items);
    }

    public void insertGptLists(String name,String description){
        insertItems(new TodoItem(System.currentTimeMillis()+name,name,description));
    }

    public void insertItems(TodoItem... items){
        new InsertAsyncTask(todoItemDao).execute(items);
    }

    public void deleteItems(TodoItem... items){
        new DeleteAsyncTask(todoItemDao).execute(items);
    }

    public void deleteItemsByName(String name){
        new DeleteByNameAsyncTask(todoItemDao,name).execute();
    }

    public void updateItems(TodoItem... items){
        new UpdateAsyncTask(todoItemDao).execute(items);
    }

    //在后台线程插入数据
    private static class InsertAsyncTask extends AsyncTask<TodoItem,Void,Void> {
        private final TodoItemDao dao;
        public InsertAsyncTask(TodoItemDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(TodoItem[] objects) {
            dao.insert(objects);
            return null;
        }
    }

    //在后台线程删除数据
    private static class DeleteAsyncTask extends AsyncTask<TodoItem,Void,Void>{
        private final TodoItemDao dao;
        public DeleteAsyncTask(TodoItemDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(TodoItem[] objects) {
            dao.delete(objects);
            return null;
        }
    }

    private static class DeleteByNameAsyncTask extends AsyncTask<Void,Void,Void>{
        private final TodoItemDao dao;
        private final String name;
        public DeleteByNameAsyncTask(TodoItemDao dao,String name) {
            this.dao = dao;
            this.name=name;
        }
        @Override
        protected Void doInBackground(Void[] voids) {
            dao.deleteItemsByListName(name);
            return null;
        }
    }

    ////在后台线程更新数据
    private static class UpdateAsyncTask extends AsyncTask<TodoItem,Void,Void>{
        private final TodoItemDao dao;
        public UpdateAsyncTask(TodoItemDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(TodoItem[] objects) {
            dao.update(objects);
            return null;
        }
    }
}
