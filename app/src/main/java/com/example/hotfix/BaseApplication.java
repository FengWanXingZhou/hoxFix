package com.example.hotfix;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class BaseApplication extends Application {


    private List<File> dexFileList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        //getPrivateDex(new File(Environment.getExternalStorageDirectory().getPath()));
        getPrivateDex(getApplicationContext().getExternalFilesDir(null));
        mergeDex();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    private void mergeDex(){

        ClassLoader classLoader = getClassLoader();


        for(File file:dexFileList){


            String optimizeDir = file.getAbsolutePath()+File.separator+"opt_dex";
            File fopt = new File(optimizeDir);
            if(!fopt.exists()){
                fopt.mkdirs();
            }
            DexClassLoader dexClassLoader = new DexClassLoader(file.getPath()
                    ,fopt.getAbsolutePath(),null,classLoader.getParent());

            try {
                Object oldDexPathList = getPathList(classLoader);
                Object oldElements = getDexElements(oldDexPathList);

                Object newDexPathList = getPathList(dexClassLoader);
                Object newElements = getDexElements(newDexPathList);

                Object latestElements = mergeDexElement(oldElements,newElements);

                oldDexPathList = getPathList(classLoader);
                setField(oldDexPathList,oldDexPathList.getClass(),"dexElements",latestElements);


            }catch (Exception e){
                e.printStackTrace();
            }

        }


    }

    private Object mergeDexElement(Object src,Object dest){

        int srcLength = Array.getLength(src);
        int destLength = Array.getLength(dest);

        Object latestArray = Array.newInstance(src.getClass().getComponentType(),srcLength+destLength);

        for(int i = 0;i< srcLength+destLength;i++){

            if(i < destLength){
                Array.set(latestArray,i,Array.get(dest,i));
            }else{
                Array.set(latestArray,i,Array.get(src,i-destLength));
            }

        }
        return latestArray;



    }

    private void getPrivateDex(File path){

        //File sdcardPath = path;

        if(path == null){
            Log.i("HotFix","path null");
        }else{
            Log.i("HotFix","path :"+path.getPath());
        }
        if(path!=null){



                if(path.isDirectory()){

                    File[] files = path.listFiles();
                    if(files!=null&&files.length>0){

                        for(File file:files){
                            getPrivateDex(file);
                        }

                    }

                }

                if(path.getName().endsWith(".dex")){
                    dexFileList.add(path);
                }

                Log.i("HotFix","file :"+path.getName());




        }



    }

    private static Object getPathList(Object baseDexClassLoader) throws Exception {
        return getField(baseDexClassLoader,Class.forName("dalvik.system.BaseDexClassLoader"),"pathList");
    }

    private static Object getDexElements(Object obj) throws Exception {
        return getField(obj,obj.getClass(),"dexElements");
    }

    private static Object getField(Object obj,Class<?> cl,String filed) throws Exception {
        Field localField = cl.getDeclaredField(filed);
        localField.setAccessible(true);
        return localField.get(obj);
    }

    private static void setField(Object obj,Class<?> cl, String field, Object value) throws Exception {
        Field localField = cl.getDeclaredField(field);
        localField.setAccessible(true);
        localField.set(obj,value);
    }


}
