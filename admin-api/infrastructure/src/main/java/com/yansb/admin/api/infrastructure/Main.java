package com.yansb.admin.api.infrastructure;

import com.yansb.admin.api.application.UseCase;

public class Main {
  public static void main(String[] args) {
    System.out.println("Hello world!");
    System.out.println(new UseCase().execute());
  }
}