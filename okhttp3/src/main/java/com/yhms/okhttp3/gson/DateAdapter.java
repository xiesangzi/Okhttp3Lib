package com.yhms.okhttp3.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class DateAdapter implements JsonSerializer<Date>, JsonDeserializer {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return new JsonPrimitive(sdf.format(src));
    }

    @Override
    public Date deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
        return convertToDate(arg0.getAsJsonPrimitive().getAsString());
    }

    public static Date convertToDate(String dateString) {
        try {
            if (isDate(dateString)) {
                SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
                return formatter.parse(dateString);
            } else if (isTimeLegal(dateString)) {
                SimpleDateFormat formatter = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
                return formatter.parse(dateString);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final Pattern PA = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s((([0-1][0-9])|(2?[0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");

    /**
     * 功能描述： yyyy-MM-dd HH:mm:ss
     *
     * @author 邪桑子
     * @date 2019/7/24 16:23
     * @param dateString
     * @return boolean
     */
    public static boolean isTimeLegal(String dateString) {
        return PA.matcher(dateString).matches();
    }

    public static final Pattern PB = Pattern.compile("((((0?[0-9])|([1][0-9])|([2][0-4]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");

    /**
     * 功能描述：HH:mm:ss
     *
     * @author 邪桑子
     * @date 2019/7/24 16:23
     * @param dateString
     * @return boolean
     */
    public static boolean isTime(String dateString) {
        return PB.matcher(dateString).matches();
    }

    public static final Pattern PC = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))?$");

    /**
     * 功能描述：yyyy-MM-dd
     *
     * @author 邪桑子
     * @date 2019/7/24 16:25
     * @param dateString
     * @return boolean
     */
    public static boolean isDate(String dateString) {
        return PC.matcher(dateString).matches();
    }

}