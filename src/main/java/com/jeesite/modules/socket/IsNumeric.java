package com.jeesite.modules.socket;

import java.util.List;
import java.util.regex.Pattern;

public class IsNumeric {

    /** 判断是否为整数
     * @param str 传入的字符串
     * @return 是整数且不为空返回true,否则返回false
     **/

    public boolean isInteger(String str) {
        if (str != null){
            Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
            return pattern.matcher(str).matches();
        }else {
            return false;
        }
    }

    /** 判断是否为整数
     * @param stringList 传入的字符串类型的列表
     * @return 是整数且不为空返回true,否则返回false
     **/

    public boolean isIntegerList(List<String> stringList){
        if (stringList != null){
            int count = 0;
            for (int i = 0; i < stringList.size(); i++){
                if (isInteger(stringList.get(i)) != true){
                    count++;
                }
            }
            if (count == 0){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
}
