package com.bbyy.weeat.models.bean.response;

import java.util.List;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class ImageUploadResponse {
    private List<Item> items;
    private int count;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static class Item {
        private String name;
        private double confidence;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getConfidence() {
            return confidence;
        }

        public void setConfidence(double confidence) {
            this.confidence = confidence;
        }
    }
}