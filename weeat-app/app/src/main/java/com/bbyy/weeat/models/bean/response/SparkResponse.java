package com.bbyy.weeat.models.bean.response;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class SparkResponse {
    private int code;
    private String message;
    private String sid;
    private String id;
    private long created;
    public Choice[] choices;
    private Usage usage;

    public static class Choice {
        public Delta delta;
        private int index;
        public static class Delta {
            private String role;
            public String content;
        }
    }

    public static class Usage {
        private int promptTokens;
        private int completionTokens;
        private int totalTokens;
    }
}
