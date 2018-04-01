package com.ef.enumerator;

public enum Duration {
	HOURLY("hourly"),
    DAILY("daily");
 
    private String description;
 
    Duration(String description) {
        this.description = description;
    }
 
    public String getDescription() {
        return description;
    }
}
