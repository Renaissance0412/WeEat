package com.bbyy.weeat.utils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
/**
 * <pre>
 *     author: wy
 *     desc  : 图片工具类
 * </pre>
 */
public final class ImageUtils {

    /**
     * 返回bitmap对象
     *
     * @param filePath 图片文件路径
     * @return bitmap
     */
    public static Bitmap getBitmap(final String filePath) {
        if (isSpace(filePath)) {
            return null;
        }
        return BitmapFactory.decodeFile(filePath);
    }

    private static boolean isSpace(final String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
