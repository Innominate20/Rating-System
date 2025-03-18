package com.ratingsystem.RatingSystem.util;

import jakarta.servlet.http.HttpServletRequest;

public class CookieUtility {

    public static String getIdFromCookie(HttpServletRequest request, String uuidName){
        if (request.getCookies() != null){
            for (var cookie: request.getCookies()){
                if (cookie.getName().equals(uuidName)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
