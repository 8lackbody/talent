package com.jeesite.modules.common.config;

public class UrlConfig {

    /**
     * 一共有三个 请求地址
     * 第一个请求：开始标签和结束标签然后返回需要查找的数量 这个改动需要在APP里修改，APP请求到服务器，然后调用他的接口
     * 第二个请求：开始标签和结束标签和已查找标签 返回异常标签 这个改动同上
     * 第三个请求：给一个标签号，查找标签名字和标签的异常状态 这个改动在服务端改动，需要优化，看是否需要修改接口
     */
    public static final String URL_ADDRESS = "http://127.0.0.1:8580/selectFilePerson/selectFilePersonListAction!";

    public static final String URL_NAME = URL_ADDRESS + "req_file_name.action";

    public static final String URL_NUMBER = URL_ADDRESS + "req_file_number.action";

    public static final String URL_DETAIL = URL_ADDRESS + "req_file_detail.action";

}
