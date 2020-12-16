/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules;

import com.jeesite.modules.socket.SocketServer;
import com.jeesite.modules.socket.TagReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;

/**
 * Application
 *
 * @author ThinkGem
 * @version 2018-10-13
 */

@SpringBootApplication(exclude = {GsonAutoConfiguration.class})
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        ApplicationContext run = SpringApplication.run(Application.class, args);
        SocketServer socketServer = run.getBean(SocketServer.class);
//        TagReader tagReader = run.getBean(TagReader.class);

        new Thread(socketServer).start();
//        new Thread(tagReader).start();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        this.setRegisterErrorPageFilter(false); // 错误页面有容器来处理，而不是SpringBoot
        return builder.sources(Application.class);
    }

}