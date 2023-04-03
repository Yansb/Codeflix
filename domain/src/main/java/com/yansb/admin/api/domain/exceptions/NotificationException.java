package com.yansb.admin.api.domain.exceptions;

import com.yansb.admin.api.domain.validation.handler.Notification;

public class NotificationException extends DomainException{
  public NotificationException(String aMessage, Notification notification){
    super(aMessage, notification.getErrors());
  }
}
