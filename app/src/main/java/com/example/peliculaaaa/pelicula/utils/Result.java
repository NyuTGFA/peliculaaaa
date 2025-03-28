package com.example.peliculaaaa.pelicula.utils;

public class Result<T> {

    private Status status;
    private T data;
    private String message;

    private Result(Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(Status.SUCCESS, data, null);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(Status.ERROR, null, message);
    }

    public static <T> Result<T> loading() {
        return new Result<>(Status.LOADING, null, null);
    }

    public Status getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public enum Status {
        SUCCESS,
        ERROR,
        LOADING
    }
}
