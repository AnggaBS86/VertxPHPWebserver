package com.ab.controller;

import static com.ab.config.Setting.PHP_DOC_ROOT;
import static com.ab.config.Setting.PORT;
import com.ab.engine.parser.CLIParser;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import java.io.File;

public class MainVerticle extends AbstractVerticle {

    // Create a router object.
    Router router = Router.router(vertx);

    @Override
    public void start(Future<Void> future) {

        router.route("/").handler(routingContext -> {
            new Controller().handlerHome(routingContext);
        });

        router.route("/test").handler(routingContext -> {
            new Controller().handlerTest(routingContext);
        });

        router.route("/php/:file").handler(r -> {

            HttpServerResponse response = r.response();
            String file = r.request().getParam("file");
            File f1 = new File(PHP_DOC_ROOT + file);
            if (f1.exists()) {
                response.putHeader("content-type", "text/html; charset=utf-8")
                        .end(CLIParser.getResultExec("php -q /var/www/html/" + file));
            } else {
                response.putHeader("content-type", "text/html; charset=utf-8")
                        .end("Not found !");
            }
        });

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(PORT, result -> {
                    if (result.succeeded()) {
                        future.complete();
                    } else {
                        future.fail(result.cause());
                    }
                });
    }

}
