package com.bbyy.weeat.utils;

import com.bbyy.weeat.models.bean.response.ImageUploadResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class ResponseFormatter {
    public static String formatResponse(ImageUploadResponse response) {
        List<ImageUploadResponse.Item> items = response.getItems();
        Map<String, Integer> nameCountMap = new HashMap<>();

        for (ImageUploadResponse.Item item : items) {
            nameCountMap.put(item.getName(), nameCountMap.getOrDefault(item.getName(), 0) + 1);
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : nameCountMap.entrySet()) {
            sb.append(entry.getValue()).append(" ").append(entry.getKey()).append(", ");
        }

        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2); // 移除最后多余的逗号和空格
        }

        return sb.toString();
    }
}
