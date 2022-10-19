package com.vitalorg.wild.verticles;

import com.ple.observabilityBridge.ObservabilityContext;
import com.ple.observabilityBridge.RecordingService;
import com.ple.util.IArrayMap;
import com.ple.util.Nullable;
import com.vitalorg.wild.BusEvent;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.reactivex.core.MultiMap;
import io.vertx.reactivex.core.http.HttpServerRequest;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.Session;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import io.vertx.reactivex.ext.web.handler.SessionHandler;
import io.vertx.reactivex.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.reactivex.ext.web.sstore.LocalSessionStore;
import io.vertx.reactivex.ext.web.sstore.SessionStore;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpServerVerticle extends AbstractVerticle {
  public HttpServerVerticle(){}

  public Completable rxStart() {
    final Router router = Router.router(vertx);
    final SessionStore store = LocalSessionStore.create(vertx);
    final SessionHandler mySesh = SessionHandler.create(store);
    mySesh.setSessionTimeout(86400000); //24 hours in milliseconds.
    final SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
    final SockJSBridgeOptions options = new SockJSBridgeOptions();
    final PermittedOptions inboundPermitted = new PermittedOptions()
        .setAddress(BusEvent.CypherQuery.name());
    options.addInboundPermitted(inboundPermitted);
    router.route().handler(this::routeStartHandler);
    router.get("/status").handler(this::statusHandler);
    router.route().handler(mySesh);
    router.get("/static/*").handler(this::staticHandler);
//    router.route().handler(this::debugHandler);
    router.route().handler(BodyHandler.create());
    router.mountSubRouter("/bus", sockJSHandler.bridge(options));
    router.errorHandler(500, this::errorHandler);
    router.route().handler(this::routeEndHandler);
    final HttpServer server = vertx.createHttpServer();
    final int port = 8080;
    final Single<HttpServer> rxListen = server
        .requestHandler(router)
        .rxListen(port);
    return rxListen.ignoreElement();
  }

  private void routeStartHandler(RoutingContext context) {
    final HttpServerRequest request = context.request();
    @Nullable final String path = request.path();
    context.next();
  }

  private void routeEndHandler(RoutingContext context) {
    final HttpServerRequest request = context.request();
    final HttpServerResponse response = context.response();
    @Nullable final String path = request.path();
    if (!response.ended()) {
      response.setStatusCode(404);
      response.end();
    }
    int code = response.getStatusCode();
  }

  private void errorHandler(RoutingContext context) {
    final HttpServerRequest request = context.request();
    @Nullable final String path = request.path();
    routeEndHandler(context);
  }

  public void writeStaticHtml(HttpServerResponse response, String path) {
    path = path.substring(1);
    final InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    if (stream != null) {
      final String text = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))
          .lines()
          .collect(Collectors.joining("\n"));
      if (path.endsWith(".html")) {
        response.putHeader("Content-Type", "text/html");
      } else if (path.endsWith(".css")) {
        response.putHeader("Content-Type", "text/css");
      } else if (path.endsWith(".js")) {
        response.putHeader("Content-Type", "text/javascript");
      } else {
      }
      response.setStatusCode(200);
      response.end(text);
    } else {
      response.setStatusCode(404);
      response.end();
    }
  }

  private void statusHandler(RoutingContext context) {
    final HttpServerResponse response = context.response();
    response.putHeader("Content-Type", "text/html");
    response.setStatusCode(200);
    response.end("<html><body>All is well</body></html>");
    // we are not doing context.next() here because we don't want to create a session for every health check
    routeEndHandler(context);
  }

  private void staticHandler(RoutingContext context) {
    final HttpServerResponse response = context.response();
    final HttpServerRequest request = context.request();
    @Nullable String path = request.path();
    try {
      response.setChunked(true);
      Session session = context.session();
      String username = session.get(SessionKey.username.name());
      if (username != null && path.equals("/static/login.html") || username != null && path.equals("/static/jscrawl.html")) {
        WebUtils.redirect(response, "/static/jscrawl.html");
        return;
      }
      writeStaticHtml(response, path);
    } catch (Throwable e) {
      response.setStatusCode(502);
      response.end();
    }
    context.next();
  }

  private void busHandler(RoutingContext context) {
    try {
      final EventBus eb = vertx.eventBus();
      final HttpServerRequest request = context.request();
      final HttpServerResponse response = context.response();
      final MultiMap params = request.params();
      response.setChunked(true);
      final String absoluteURI = request.absoluteURI();
      final String busAddress = absoluteURI.replaceAll("^.*/bus/", "");
      Session session = context.session();
      JsonObject object = new JsonObject();
      for (Map.Entry<String, String> entry : params.entries()) {
        object.put(entry.getKey(), entry.getValue());
      }
      eb.rxRequest(busAddress, object.encode())
          .subscribe(e -> {
                final JsonObject json = JsonObject.mapFrom(e.body());
                final String message = json.getString("response");
                final String redirect = json.getString("redirect");
                if (redirect.equals("login.html")) {
                  // redirect to login page.
/* TODO: Figure out why this isn't working.
              String html = WebUtils.generateHtml("/home/jaredgurr/training-crawl/03_java_backend/src/main/resources/static/login.html", message);
              response.write(html);
              writeStaticHtml(response, "/static/login.html");
              R.debug("message =" + message);
              R.debug("html = " + html);
*/
                } else if (redirect.equals("jscrawl.html")) {
                  session.put(SessionKey.username.name(), object.getString("username"));
                }
                WebUtils.redirect(response, "/static/" + redirect);
              },
              err -> {
                if (err.getMessage() == null) {
                  WebUtils.redirect(response, "/static/serverError.html");
                }
              });
    } catch (Exception e) {
    } finally {
    }
    context.next();
  }
}
