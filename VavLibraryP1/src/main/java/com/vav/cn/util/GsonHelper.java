package com.vav.cn.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by thunde91 on 8/4/16.
 */
public class GsonHelper {
    private static GsonHelper ourInstance = new GsonHelper();

    public static GsonHelper getInstance() {
        return ourInstance;
    }

    private Gson gson;

    private GsonHelper() {
        GsonBuilder builder = new GsonBuilder();
//        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
//            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//                return new Date(json.getAsJsonPrimitive().getAsLong());
//            }
//        });
        builder.setDateFormat("yyyy-mm-dd");


        gson = builder.create();
    }

    public <T> T parseJSONString(String jsonString, Class<T> json) {
        return gson.fromJson(jsonString, json);
    }

    public String parseToJSONString(Object object) {
        return gson.toJson(object);
    }

}
