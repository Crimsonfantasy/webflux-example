package com.xteamstudio.exam.oms;

import com.xteamstudio.exam.oms.config.NettyConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

@EnableConfigurationProperties
@ComponentScan(basePackages = {"com.xteamstudio.exam.oms"})
@EnableWebFlux
public class OrderManagementSystemApplication {

    /**
     * spring boot invoker.
     *
     * @param args no customize args
     */
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(
                             OrderManagementSystemApplication.class)
        ) {
            context.getBean(DisposableServer.class).onDispose().block();
        }
    }

    /**
     * underline sever disposable.
     *
     * @param context spring context
     * @return a DisposableServer
     */
    @Bean
    public DisposableServer disposableServer(ApplicationContext context) {
        HttpHandler handler = WebHttpHandlerBuilder.applicationContext(context)
                .build();
        NettyConfig nettyConfig = context.getBean(NettyConfig.class);
        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(handler);
        HttpServer httpServer = HttpServer.create().port(nettyConfig.getPort());
        return httpServer.handle(adapter).bindNow();
    }


}