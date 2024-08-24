package com.bbyy.weeat.repositories.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.bbyy.weeat.models.bean.CookTimeItem;
import com.bbyy.weeat.models.bean.TodoItem;

import java.util.Date;
import java.util.List;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
@Dao
public interface CookTimeItemDao {
    // 插入
    @Insert
    void insert(CookTimeItem... items);

    // 更新
    @Update
    void update(CookTimeItem... items);

    // 删除
    @Delete
    void delete(CookTimeItem... items);

    // 获取startOfDay后总时长的方法
    @Query("SELECT SUM(w.time) FROM cook w WHERE w.startTime >= :startOfDay")
    long getTotalDuration(long startOfDay);

    // 获取startOfDay后tag总时长的方法
    @Query("SELECT SUM(w.time) FROM cook w WHERE w.startTime >= :startOfDay and w.tag=:tag")
    long getTotalDuration(long startOfDay,String tag);
}
