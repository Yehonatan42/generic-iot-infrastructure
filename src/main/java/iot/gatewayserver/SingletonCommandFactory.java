package iot.gatewayserver;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SingletonCommandFactory<K, T, R> {
    private final Map<K, Function<T, R>> map = Collections.synchronizedMap(new HashMap<>());
    private static SingletonCommandFactory instance = null;
    private static final Object lock = new Object();

    public void add(K key, Function<T, R> recipe){
        map.put(key, recipe);
    }
    
    public R execute(K key, T data){
        return map.get(key).apply(data);                                                                                                         
    }
    
    public static <K, T, R> SingletonCommandFactory<K, T, R> getInstance(){
        if (instance == null){
            synchronized (lock){
                if (instance == null){
                    instance = new SingletonCommandFactory<>();
                }
            }
        }
        return instance;
    }
}
