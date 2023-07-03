package com.example.demo;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import io.netty.util.AsciiString;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Random;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.server.HttpServer;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
public class Server {

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }



    public Server() throws CertificateException, IOException {
        h2c();
//        h2();
    }

    int index=0;
Random random=new Random();
    HttpHandler httpHandler = RouterFunctions.toHttpHandler(
            route(GET("/"), request -> ServerResponse.ok()
                    .header("my-header","my-value"+random.nextInt())
                    .bodyValue("你好"))
    );
    HttpHandler imgHttpHandler = RouterFunctions.toHttpHandler(

            route(GET("/image/**"), request -> {
                Path imagePath = Paths.get("/Users/ywz/http2/image2.jpg");
                byte[] imageBytes = new byte[0];
                try {
                    imageBytes = Files.readAllBytes(imagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return ServerResponse.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .bodyValue(imageBytes);
            })

    );
    private void h2c() {


        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);
        HttpServer httpServer = HttpServer.create().port(90)
                .protocol(
                        HttpProtocol.H2C,
                        HttpProtocol.HTTP11
                ).handle(adapter);
        httpServer.bindNow();
    }

    private void h2() throws  CertificateException {
        SelfSignedCertificate cert = new SelfSignedCertificate();

        HttpServer server = HttpServer.create()
                .secure(spec -> spec.sslContext(SslContextBuilder.forServer(cert.certificate(), cert.privateKey())))
                .port(8443)
                .protocol(
                        HttpProtocol.H2,
                        HttpProtocol.HTTP11
                )
                .handle( new ReactorHttpHandlerAdapter(imgHttpHandler))
                ;
        server.bindNow();
    }
}
