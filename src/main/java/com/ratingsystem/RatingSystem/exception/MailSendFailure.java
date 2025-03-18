package com.ratingsystem.RatingSystem.exception;

public class MailSendFailure extends RuntimeException{
    public MailSendFailure(String message){
        super(message);
    }
}
