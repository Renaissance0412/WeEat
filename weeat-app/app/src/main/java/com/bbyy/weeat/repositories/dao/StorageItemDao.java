package com.bbyy.weeat.repositories.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.bbyy.weeat.models.bean.CookTimeItem;
import com.bbyy.weeat.models.bean.StorageItem;

import java.util.List;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
@Dao
public interface StorageItemDao {
    @Insert
    void insert(StorageItem... items);

    // 更新
    @Update
    void update(StorageItem... items);

    // 删除
    @Delete
    void delete(StorageItem... items);

    @Query("SELECT * FROM storage WHERE completionTime >= :startOfDay AND completionTime < :endOfDay")
    List<StorageItem> getItemsForDate(long startOfDay, long endOfDay);

    @Query("SELECT * FROM storage where completionTime=0")
    List<StorageItem> getItems();

    @Query("SELECT * FROM storage WHERE isExpired=0")
    List<StorageItem> getFreshItems();

    @Query("SELECT * FROM storage WHERE isExpired!=0")
    List<StorageItem> getExpiredItems();
}
