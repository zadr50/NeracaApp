package com.talagasoft.util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dell on 09/15/2016.
 */
public class MyLib {
    public static String[] ArrayListHashMapToStringArray(ArrayList<HashMap<String, String>> arrList, String field) {
        String arrRet[]=new String[arrList.size()];
        for(int i=0;i<arrList.size();i++){
            HashMap<String,String> map=new HashMap<>();
            map = arrList.get(i);
            arrRet[i]=map.get(field).toString();
        }
        return arrRet;
    }
}
