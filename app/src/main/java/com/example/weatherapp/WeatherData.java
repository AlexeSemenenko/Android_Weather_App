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

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public String getPressure() {
        return pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getImg() {
        return img;
    }

    public String getDescription() {
        return description;
    }

    public String getWind() {
        return wind;
    }

    public String getClouds() {
        return clouds;
    }

    public String getDate() {
        return date;
    }

    public static WeatherData getAllData(JSONObject jsonObject) {
        try {
            WeatherData weatherData = new WeatherData();

            weatherData.city = getCityFromJson(jsonObject);
            weatherData.country = getCountyFromJson(jsonObject);
            weatherData.temperature = getTemperatureFromJson(jsonObject);
            weatherData.feelsLike = getFeelsLikeFromJson(jsonObject);
            weatherData.pressure = getPressureFromJson(jsonObject);
            weatherData.humidity = getHumidityFromJson(jsonObject);
            weatherData.img = getIconFromJson(jsonObject);
            weatherData.description = getDescriptionFromJson(jsonObject);
            weatherData.wind = getWindFromJson(jsonObject);
            weatherData.clouds = getCloudsFromJson(jsonObject);
            weatherData.date = getDateFromJson(jsonObject);

            return weatherData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getCityFromJson(JSONObject jsonObject)  throws JSONException {
        return jsonObject.getString("name");
    }

    private static String getCountyFromJson(JSONObject jsonObject)  throws JSONException {
        return jsonObject.getJSONObject("sys").getString("country");
    }

    private static double getTemperatureFromJson(JSONObject jsonObject)  throws JSONException {
        return Double.parseDouble(jsonObject.getJSONObject("main").getString("temp")) - 273.15;
    }

    private static double getFeelsLikeFromJson(JSONObject jsonObject)  throws JSONException {
        return Double.parseDouble(jsonObject.getJSONObject("main").getString("feels_like")) - 273.15;
    }

    private static String getPressureFromJson(JSONObject jsonObject)  throws JSONException {
        return jsonObject.getJSONObject("main").getString("pressure");
    }

    private static String getHumidityFromJson(JSONObject jsonObject)  throws JSONException {
        return jsonObject.getJSONObject("main").getString("humidity");
    }

    private static String getIconFromJson(JSONObject jsonObject)  throws JSONException {
        return "http://openweathermap.org/img/wn/" + jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon") + "@2x.png";
    }

    private static String getDescriptionFromJson(JSONObject jsonObject)  throws JSONException {
        return jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
    }

    private static String getWindFromJson(JSONObject jsonObject)  throws JSONException {
        return jsonObject.getJSONObject("wind").getString("speed");
    }

    private static String getCloudsFromJson(JSONObject jsonObject)  throws JSONException {
        return jsonObject.getJSONObject("clouds").getString("all");
    }

    private static String getDateFromJson(JSONObject jsonObject) throws JSONException {
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
