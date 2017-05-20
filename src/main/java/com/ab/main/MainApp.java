/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ab.main;

import com.hazelcast.config.Config;
import com.ab.controller.MainVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

/**
 *
 * @author root
 */
public class MainApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        System.out.println("Vertx webserver started at port 8080");

        Config hazelcastConfig = new Config();

        ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);

        VertxOptions options = new VertxOptions().setClusterManager(mgr);
        int port = 8081;
        DeploymentOptions depOptions = new DeploymentOptions()
                .setConfig(new JsonObject().put("http.port", port)
                );
        Vertx.clusteredVertx(options, res -> {
            if (res.succeeded()) {
                Vertx vertx = res.result();
                vertx.deployVerticle(new MainVerticle(), depOptions);
                System.out.println("Server started at port " + port + "...");
            } else {
                // failed!
            }
        });
    }

}
