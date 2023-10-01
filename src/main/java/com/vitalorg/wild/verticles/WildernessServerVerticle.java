package com.vitalorg.wild.verticles;

//import com.ple.observabilityBridge.SystemOutLogHandler;
//import com.ple.observabilityBridge.RecordingService;
import io.vertx.core.VertxOptions;
import io.vertx.rxjava3.core.Vertx;

public class WildernessServerVerticle {

  public static void main(final String[] args) {
    final WildernessServerVerticle  wildernessServer = new WildernessServerVerticle();
  }

  private final Vertx vertx;
  private final boolean debug = true;

  WildernessServerVerticle() {
    final VertxOptions options = new VertxOptions();
    if(debug) {
      options.setBlockedThreadCheckInterval(Long.MAX_VALUE >> 2);
    }


    vertx = Vertx.vertx(options);
    vertx.deployVerticle(new HttpServerVerticle());
    vertx.deployVerticle(new Neo4jVerticle());
//		vertx.deployVerticle(new UserVerticle(recordingService.clone()));
//		vertx.deployVerticle(new CouchbaseVerticle(recordingService.clone()));
//		vertx.deployVerticle(new MysqlVerticle(recordingService.clone()));
//		vertx.deployVerticle(new GameListVerticle(recordingService.clone()));

  }

}
