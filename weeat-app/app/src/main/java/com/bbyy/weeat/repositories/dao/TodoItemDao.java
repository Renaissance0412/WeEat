package com.bbyy.weeat.repositories.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.bbyy.weeat.models.bean.TodoItem;

import java.util.List;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
@Dao
public interface TodoItemDao {
    // 插入新的代办事项
    @Insert
    void insert(TodoItem... todoItems);

    // 更新现有的代办事项
    @Update
    void update(TodoItem... todoItems);

    // 删除代办事项
    @Delete
    void delete(TodoItem... todoItems);

    // 根据清单名字获取特定清单的代办事项列表
    @Query("SELECT * FROM TODO WHERE listName=:name ORDER BY id")
    List<TodoItem> getItemsByListName(String name);

    // 根据清单名字删除特定清单的代办事项列表
    @Query("DELETE FROM TODO WHERE listName=:name")
    void deleteItemsByListName(String name);

    // 获取所有清单名字
    @Query("SELECT DISTINCT listName FROM TODO")
    List<String> getAllListName();

    //根据日期获取所有事项
    @Query("SELECT * FROM TODO WHERE DATE(timestamp) =:day")
    List<TodoItem> getItemsByDay(String day);
}