package cn.itcast.advance.c2;

import cn.itcast.protocol.Serializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author tiancn
 * @date 2023/3/18 17:14
 */
public class TestGson {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Class.class, new Serializer.ClassCodec()).create();
        System.out.println(gson.toJson(String.class));
    }
}
