package com.yansb.admin.api.application;

public abstract class UseCase<IN, OUT> {
    public abstract OUT execute(IN anInput);
}