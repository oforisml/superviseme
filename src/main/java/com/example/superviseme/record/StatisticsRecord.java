package com.example.superviseme.record;

public record StatisticsRecord (
    int monthYear,
    int monthNumber,
    String monthName,
    int count
){
}
