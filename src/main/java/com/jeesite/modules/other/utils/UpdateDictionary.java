package com.jeesite.modules.socket;

public class UpdateDictionary {

    //tree_sort自增
    public static String treeSortRaise(String treeSort){
        return String.valueOf(Integer.parseInt(treeSort) + 30);
    }

    //tee_sorts自增  0000000120, 十位数字
    public static String treeSortsRaise(String treeSorts){
        String number = treeSorts.substring(0,10);
        String nextTreeSorts = String.valueOf(Integer.parseInt(number) + 30);
        while (nextTreeSorts.length() < 10){
            nextTreeSorts = "0" + nextTreeSorts;
        }
        return nextTreeSorts + ",";
    }

    //生成code 1339833136263890004
    public static String createCode(String code){
        String header = code.substring(0,15);
        String nextCode = code.substring(15,19);
        nextCode = String.valueOf(Integer.parseInt(nextCode) + 1);
        while (nextCode.length() < 4){
            nextCode = "0" + nextCode;
        }
        return header + nextCode;
    }
}
