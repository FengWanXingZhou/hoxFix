package com.example.hotfix;

import android.util.Log;

public class Calculate {


    public void calculate(int x,int y){
        int z = x/y;
        Log.i("hot Fix","result :"+z);

    }
    /*public void calculate(int x,int y){
        if(y !=0) {
            int z = x / y;
            Log.i("hot Fix", "result :" + z);
        }else{
            Log.i("hot Fix", "parametes invalid");
        }

    }*/

}
