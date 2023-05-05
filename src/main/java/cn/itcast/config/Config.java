package cn.itcast.config;

import cn.itcast.protocol.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author tiancn
 * @date 2023/3/18 14:39
 */

/**
 * 通过application.properties配置文件选择序列化方式
 */
public abstract class Config {
    static Properties properties;
    static {
        try(InputStream in = Config.class.getResourceAsStream("/application.properties")){
            properties = new Properties();
            properties.load(in);
        }catch (IOException e){
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Serializer.Algorithm getSerializerAlgorithm(){
        String value = properties.getProperty("serializer.algorithm");
        if (value == null){
            return Serializer.Algorithm.Java;//默认为Java
        }else {
            return Serializer.Algorithm.valueOf(value);
        }
    }


}
