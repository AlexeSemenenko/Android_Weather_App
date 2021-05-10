package com.example.weatherapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class WeatherData {

    private String city;
    private String country;
    private double temperature;
    private double feelsLike;
    private String pressure;
    private String humidity;
    private String img;
    private String description;
    private String wind;
    private String clouds;
    private String date;

    public static WeatherData getAllData(JSONObject jsonObject) {
        try {
            WeatherData weatherData = new WeatherData();

            weatherData.city = getCity(jsonObject);
            weatherData.country = getCounty(jsonObject);
            weatherData.temperature = getTemperature(jsonObject);
            weatherData.feelsLike = getFeelsLike(jsonObject);
            weatherData.pressure = getPressure(jsonObject);
            weatherData.humidity = getHumidity(jsonObject);
            weatherData.img = getIcon(jsonObject);
            weatherData.description = getDescription(jsonObject);
            weatherData.wind = getWind(jsonObject);
            weatherData.clouds = getClouds(jsonObject);
            weatherData.date = getDate(jsonObject);

            return weatherData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getCity(JSONObject jsonObject)  throws JSONException {
        return jsonObject.getString("name");
    }

    private static String getCounty(JSONObject jsonObject)  throws JSONException {
        return jsonObject.getJSONObject("sys").getString("country");
    }

    private static double getTemperature(JSONObject jsonObject)  throws JSONException {
        return Double.parseDouble(jsonObject.getJSONObject("main").getString("temp")) - 273.15;
    }

    private static double getFeelsLike(JSONObject jsonObject)  throws JSONException {
        return Double.parseDouble(jsonObject.getJSONObject("main").getString("feels_like")) - 273.15;
    }

    private static String getPressure(JSONObject jsonObject)  throws JSONException {
        return jsonObject.getJSONObject("main").getString("pressure");
    }

    private static String getHumidity(JSONObject jsonObject)  throws JSONException {
        return jsonObject.getJSONObject("main").getString("humidity");
    }

    private static String getIcon(JSONObject jsonObject)  throws JSONException {
        return "http://openweathermap.org/img/wn/" + jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon") + "@2x.png";
    }

    private static String getDescription(JSONObject jsonObject)  throws JSONException {
        return jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
    }

    private static String getWind(JSONObject jsonObject)  throws JSONException {
        return jsonObject.getJSONObject("wind").getString("speed");
    }

    private static String getClouds(JSONObject jsonObject)  throws JSONException {
        return jsonObject.getJSONObject("clouds").getString("all");
    }

    private static String getDate(JSONObject jsonObject) throws JSONException {
        String timezone_find = jsonObject.getString("timezone");

        Calendar calendar = Calendar.getInstance();
        Date date = new Date(calendar.getTimeInMillis());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy \nHH:mm");
        TimeZone my_time = TimeZone.getTimeZone("UTC");

        my_time.setRawOffset(Integer.parseInt(timezone_find) * 1000);

        simpleDateFormat.setTimeZone(my_time);
        String formattedDate = simpleDateFormat.format(date);

        return formattedDate;
    }
}
