package com.jobtracker.dto;


public class ApiResponse {

	    private boolean status;
	    private String message;
	    private Object data;

	    public ApiResponse(boolean status, String message, Object data) {
	        this.status = status;
	        this.message = message;
	        this.data = data;
	    }

	    // Getters and setters
	    public boolean isStatus() {
	        return status;
	    }

	    public void setStatus(boolean status) {
	        this.status = status;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }

	    public Object getData() {
	        return data;
	    }

	    public void setData(Object data) {
	        this.data = data;
	    }
}
