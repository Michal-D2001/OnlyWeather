package com.onlyweather.OnlyWeather.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherInfoDto {
    private String description;
    private String icon;
}