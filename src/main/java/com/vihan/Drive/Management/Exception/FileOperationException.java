package com.vihan.Drive.Management.Exception;

public class FileOperationException extends RuntimeException {

    private final String errorCode;

    public FileOperationException(String message) {
        super(message);
        this.errorCode = "FILE_OPERATION_ERROR";
    }

    public FileOperationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public FileOperationException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "FILE_OPERATION_ERROR";
    }

    public FileOperationException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}