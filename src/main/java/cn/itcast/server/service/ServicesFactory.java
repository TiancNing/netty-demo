package cn.itcast.server.service;

import cn.itcast.config.Config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tiancn
 * @date 2023/3/18 15:40
 */
public class ServicesFactory {
    static Properties properties;
    static Map<Class<?>, Object> map = new ConcurrentHashMap<>();

    static {
        try(InputStream in = Config.class.getResourceAsStream("/application.properties")){
            properties = new Properties();
            properties.load(in);
            Set<String> names = properties.stringPropertyNames();
            for (String name : names) {
                if (name.endsWith("Service")){
                    Class<?> interfaceClass = Class.forName(name);//接口 class
                    Class<?> instanceClass = Class.forName(properties.getProperty(name));//示例class
                    map.put(interfaceClass,instanceClass.newInstance());//创建对象实例

                }
            }
        }catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            throw new ExceptionInInitializerError(e);
        }
    }

    public static <T> T getService(Class<T> interfaceClass){
        return (T) map.get(interfaceClass);
    }
}
