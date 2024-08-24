package com.bbyy.weeat.repositories;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.bbyy.weeat.models.bean.CookTimeItem;
import com.bbyy.weeat.repositories.dao.CookTimeItemDao;
import com.bbyy.weeat.repositories.database.TodoDatabase;
import com.bbyy.weeat.utils.DateUtils;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class CookTimeRepository {
    private final CookTimeItemDao dao;
    public CookTimeRepository(Context context){
        TodoDatabase todoDatabase=TodoDatabase.getDataBase(context);
        this.dao=todoDatabase.getCookTimeItemDao();
    }

    public MutableLiveData<Long> getTotalDurationForToday(){
        return new MutableLiveData<>(dao.getTotalDuration(DateUtils.getToday()));
    }

    public MutableLiveData<Long> getTotalDurationForToday(String tag){
        return new MutableLiveData<>(dao.getTotalDuration(DateUtils.getToday(),tag));
    }

    public MutableLiveData<Long> getTotalDurationForThisWeek(){
        return new MutableLiveData<>(dao.getTotalDuration(DateUtils.getFirstDayOfThisWeek()));
    }

    public MutableLiveData<Long> getTotalDurationForThisWeek(String tag){
        return new MutableLiveData<>(dao.getTotalDuration(DateUtils.getFirstDayOfThisWeek(),tag));
    }

    public MutableLiveData<Long> getTotalDurationForThisMonth(){
        return new MutableLiveData<>(dao.getTotalDuration(DateUtils.getFirstDayOfThisMonth()));
    }

    public MutableLiveData<Long> getTotalDurationForThisMonth(String tag){
        return new MutableLiveData<>(dao.getTotalDuration(DateUtils.getFirstDayOfThisMonth(),tag));
    }

    public MutableLiveData<Long> getTotalDurationForThisYear(){
        return new MutableLiveData<>(dao.getTotalDuration(DateUtils.getFirstDayOfThisYear()));
    }

    public MutableLiveData<Long> getTotalDurationForThisYear(String tag){
        return new MutableLiveData<>(dao.getTotalDuration(DateUtils.getFirstDayOfThisYear(),tag));
    }

    public void insertItems(CookTimeItem... items){
        new InsertAsyncTask(dao).execute(items);
    }

    //在后台线程插入数据
    private static class InsertAsyncTask extends AsyncTask<CookTimeItem,Void,Void> {
        private final CookTimeItemDao dao;
        public InsertAsyncTask(CookTimeItemDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(CookTimeItem[] objects) {
            dao.insert(objects);
            return null;
        }
    }
}
