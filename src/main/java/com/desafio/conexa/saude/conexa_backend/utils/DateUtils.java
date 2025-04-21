package com.desafio.conexa.saude.conexa_backend.utils;

import java.time.LocalDateTime;

public class DateUtils {
    public static LocalDateTime threeMonthsLater(){
        LocalDateTime now = LocalDateTime.now();
        return now.plusMonths(3);
    }
}
