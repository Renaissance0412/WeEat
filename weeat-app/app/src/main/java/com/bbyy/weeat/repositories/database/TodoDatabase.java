package com.bbyy.weeat.repositories.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.bbyy.weeat.models.bean.CookTimeItem;
import com.bbyy.weeat.models.bean.StorageItem;
import com.bbyy.weeat.models.bean.TodoItem;
import com.bbyy.weeat.repositories.dao.CookTimeItemDao;
import com.bbyy.weeat.repositories.dao.StorageItemDao;
import com.bbyy.weeat.repositories.dao.TodoItemDao;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
@Database(entities = {TodoItem.class, CookTimeItem.class, StorageItem.class},version =5,exportSchema = false)
public abstract class TodoDatabase extends RoomDatabase {
    private static TodoDatabase INSTANCE;
    public abstract TodoItemDao getTodoItemDao();
    public abstract CookTimeItemDao getCookTimeItemDao();
    public abstract StorageItemDao getStorageItemDao();
    public static synchronized TodoDatabase getDataBase(Context context){
        if(INSTANCE==null){
            INSTANCE= Room.databaseBuilder(context.getApplicationContext(),TodoDatabase.class,"todo_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
}
