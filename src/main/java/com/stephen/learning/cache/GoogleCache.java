package com.stephen.learning.cache;

import com.google.common.cache.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: jack
 * @Date: 2018/8/31 10:56
 * @Description: guava中的LoadingCache使用
 */
public class GoogleCache {
    private static LoadingCache<String,String> loadingCache=CacheBuilder.newBuilder().
            //当缓存项在指定的时间段内没有更新就会被回收
            expireAfterWrite(10,TimeUnit.MINUTES).
            //当缓存项在指定的时间段内没有被读或写就会被回收
            //expireAfterAccess(10,TimeUnit.SECONDS).
            //当缓存项上一次更新操作之后的多久会被刷新
            //refreshAfterWrite(5,TimeUnit.SECONDS).
            //最多存入的数据
            maximumSize(100).
            removalListener(new RemovalListener<String, String>() {
                @Override
                public void onRemoval(RemovalNotification<String, String> removalNotification) {
                    System.out.println(removalNotification.getKey()+" has been removed.");
                }
            }).
            build(new CacheLoader<String, String>() {
                @Override
                public String load(String key) throws Exception {
                    //这里从数据库中查数据
                    return map.get(key);
                }
            });

    private static LoadingCache<String,String> getLoadingCache(){
        return loadingCache;
    }

    public static String getValue(String key){
        try{
            return getLoadingCache().get(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void removeKey(String key){
        try{
            getLoadingCache().invalidate(key);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private static Map<String,String> map=new HashMap<>();
    static {
        map.put("name","jack");
        map.put("job","programmer");
        map.put("address","湖北襄阳");
        map.put("email","xy123zk@163.com");
    }

    public static void main(String[] args) throws Exception {
        for(int i=0;i<5;i++){
            TimeUnit.SECONDS.sleep(5);
            System.out.println(GoogleCache.getValue("name"));
        }
        map.put("name","mary");
        System.out.println("更新后："+GoogleCache.getValue("name"));
        for(int i=0;i<5;i++){
            TimeUnit.SECONDS.sleep(5);
            System.out.println(GoogleCache.getValue("name"));
        }
        System.out.println(GoogleCache.loadingCache.stats().hitRate());
        GoogleCache.loadingCache.invalidate("job");
        System.out.println(GoogleCache.loadingCache.getIfPresent("job"));
        System.out.println(GoogleCache.getValue("email"));
    }
}
