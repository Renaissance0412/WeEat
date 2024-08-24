package com.bbyy.weeat.models.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.bbyy.weeat.utils.DateUtils;

import java.util.Objects;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
@Entity(tableName = "STORAGE")
public class StorageItem implements Parcelable {
    public boolean equals(@Nullable StorageItem item) {
        return id==item.getId()
                &&expirationTime==item.getExpirationTime()
                &&foodName.equals(item.getFoodName())
                &&imagePath.equals(item.getImagePath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,expirationTime,foodName,imagePath);
    }

    public static final Creator<StorageItem> CREATOR = new Creator<StorageItem>() {
        @Override
        public StorageItem createFromParcel(Parcel in) {
            return new StorageItem(in);
        }

        @Override
        public StorageItem[] newArray(int size) {
            return new StorageItem[size];
        }
    };
    @PrimaryKey(autoGenerate = true)
    private int id;
    private long expirationTime;
    private long completionTime;
    private String foodName;
    private String description;
    private String imagePath;
    private int quantity;
    private boolean isExpired;

    public StorageItem(int id,long expirationTime, long completionTime, String foodName, String description, String imagePath, int quantity, boolean isExpired) {
        this.id=id;
        this.expirationTime = expirationTime;
        this.completionTime = completionTime;
        this.foodName = foodName;
        this.description = description;
        this.imagePath = imagePath;
        this.quantity = quantity;
        this.isExpired = isExpired;
    }

    @Ignore
    public StorageItem(long expirationTime, String foodName, String description, String imagePath, int quantity) {
        this.expirationTime = expirationTime;
        this.foodName = foodName;
        this.description = description;
        this.imagePath = imagePath;
        this.quantity = quantity;
        if(getRemainDays()<0)
            this.isExpired=true;
    }

    protected StorageItem(Parcel in) {
        id=in.readInt();
        expirationTime = in.readLong();
        completionTime = in.readLong();
        foodName = in.readString();
        description = in.readString();
        imagePath = in.readString();
        quantity = in.readInt();
        isExpired = in.readByte()!= 0;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public long getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(long completionTime) {
        this.completionTime = completionTime;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeLong(expirationTime);
        dest.writeLong(completionTime);
        dest.writeString(foodName);
        dest.writeString(description);
        dest.writeString(imagePath);
        dest.writeInt(quantity);
        dest.writeByte((byte) (isExpired? 1 : 0));
    }

    public int getRemainDays(){
        return DateUtils.getDurationDays(expirationTime);
    }
}
