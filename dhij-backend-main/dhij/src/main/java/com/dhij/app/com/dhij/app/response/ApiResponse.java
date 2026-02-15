package com.dhij.app.com.dhij.app.response;

import java.time.LocalDateTime;

public class ApiResponse<T> {
	 
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private T data;
 
    // Default constructor
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }
 
    // Success response constructor
    public ApiResponse(int status, String message, T data) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.data = data;
    }
 
    // Error response constructor
    public ApiResponse(int status, String message) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
    }
 
    // Getters and Setters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
 
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
 
    public int getStatus() {
        return status;
    }
 
    public void setStatus(int status) {
        this.status = status;
    }
 
    public String getMessage() {
        return message;
    }
 
    public void setMessage(String message) {
        this.message = message;
    }
 
    public T getData() {
        return data;
    }
 
    public void setData(T data) {
        this.data = data;
    }
}