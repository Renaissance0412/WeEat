package com.bbyy.weeat.models.bean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;

import java.util.Objects;

/**
 * <pre>
 *     author: wy
 *     desc  : todo事项
 * </pre>
 */
@Entity(tableName = "TODO",primaryKeys = "id")
public class TodoItem {
    @NonNull
    private String id;  //如果用自增int类型id可能有问题,所以这里用时间戳的字符串当id
    private long timestamp; //这里为item对应的时间，如果没有则为当前时间戳
    private String todo;

    private String listName;

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    private int finished;

    public TodoItem(String id,String todo) {
        this(id,"default",todo);
    }

    public TodoItem(String id,String name,String todo) {
        this(id,name,0,todo);
    }

    public TodoItem(@NonNull String id, String name, long timestamp, String todo){
        this.id=id;
        this.todo = todo;
        this.listName=name;
        this.timestamp=timestamp;
        this.finished=0;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean equals(@Nullable TodoItem item) {
        return todo.equals(Objects.requireNonNull(item).getTodo())&&listName.equals(item.getListName());
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, todo);
    }
}
