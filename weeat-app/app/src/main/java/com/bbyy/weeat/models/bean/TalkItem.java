package com.bbyy.weeat.models.bean;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import java.util.Objects;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class TalkItem {
    private int index;
    private String text;
    private Bitmap bitmap;
    private boolean isFromMe;
    private boolean isKeyText;
    private String mediaPath;
    public TalkItem(int index,String text, Bitmap bitmap, boolean isFromMe) {
        this.index=index;
        this.text = text;
        this.bitmap = bitmap;
        this.isFromMe = isFromMe;
        isKeyText = false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index,text,bitmap,mediaPath);
    }

    public boolean equals(@Nullable TalkItem obj) {
        if(text==null&&obj.getText()!=null||mediaPath==null&&obj.getMediaPath()!=null){
            return false;
        }
        return index==obj.getIndex()
                &&text.equals(obj.getText())
                &&mediaPath.equals(obj.getMediaPath());
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public boolean isKeyText() {
        return isKeyText;
    }

    public void setKeyText(boolean keyText) {
        isKeyText = keyText;
    }

    public boolean isFromMe() {
        return isFromMe;
    }

    public void setFromMe(boolean fromMe) {
        isFromMe = fromMe;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
