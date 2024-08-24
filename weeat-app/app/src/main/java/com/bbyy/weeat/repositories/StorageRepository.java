package com.bbyy.weeat.repositories;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.bbyy.weeat.models.bean.StorageItem;
import com.bbyy.weeat.models.bean.TodoItem;
import com.bbyy.weeat.repositories.dao.StorageItemDao;
import com.bbyy.weeat.repositories.dao.TodoItemDao;
import com.bbyy.weeat.repositories.database.TodoDatabase;
import com.bbyy.weeat.utils.DateUtils;

import java.util.List;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class StorageRepository {
    private final StorageItemDao dao;

    public StorageRepository(Context context) {
        TodoDatabase database=TodoDatabase.getDataBase(context);
        dao=database.getStorageItemDao();
    }

    //获取这个时间戳对应的一天中所有已完成的item
    public MutableLiveData<List<StorageItem>> getItemsForDate(long time){
        return new MutableLiveData<>(dao.getItemsForDate(DateUtils.getStartOfDay(time),DateUtils.getEndOfDay(time)));
    }

    public void updateAllFreshItems(){
        new UpdateAllAsyncTask(dao).execute();
    }

    //获取所有item
    public MutableLiveData<List<StorageItem>> getItems(){
        return new MutableLiveData<>(dao.getItems());
    }

    public MutableLiveData<List<StorageItem>> getExpiredItems(){
        return new MutableLiveData<>(dao.getExpiredItems());
    }

    //插入item
    public void insertItems(StorageItem... items){
        new InsertAsyncTask(dao).execute(items);
    }

    public void deleteItems(StorageItem... items){
        new DeleteAsyncTask(dao).execute(items);
    }

    public void updateItems(StorageItem... items){
        new UpdateAsyncTask(dao).execute(items);
    }

    //在后台线程插入数据
    private static class InsertAsyncTask extends AsyncTask<StorageItem,Void,Void> {
        private final StorageItemDao dao;
        public InsertAsyncTask(StorageItemDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(StorageItem... items) {
            dao.insert(items);
            return null;
        }
    }

    //在后台线程更新
    private static class UpdateAsyncTask extends AsyncTask<StorageItem,Void,Void> {
        private final StorageItemDao dao;
        public UpdateAsyncTask(StorageItemDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(StorageItem... items) {
            dao.update(items);
            return null;
        }
    }

    //在后台线程更新
    private static class UpdateAllAsyncTask extends AsyncTask<StorageItem,Void,Void> {
        private final StorageItemDao dao;
        public UpdateAllAsyncTask(StorageItemDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(StorageItem... items) {
            List<StorageItem> list=dao.getFreshItems();
            for(StorageItem item:list){
                if(item.getRemainDays()<0){
                    item.setExpired(true);
                    dao.update(item);
                }
            }
            return null;
        }
    }

    //在后台线程删除数据
    private static class DeleteAsyncTask extends AsyncTask<StorageItem,Void,Void> {
        private final StorageItemDao dao;
        public DeleteAsyncTask(StorageItemDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(StorageItem... items) {
            dao.delete(items);
            return null;
        }
    }
}
