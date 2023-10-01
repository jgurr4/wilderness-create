package com.vitalorg.wild.verticles;

import io.reactivex.rxjava3.core.Completable;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.Vertx;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

import static org.neo4j.driver.Values.parameters;

public class Neo4jVerticle extends AbstractVerticle {

    private Vertx vertx = io.vertx.rxjava3.core.Vertx.vertx();

    public Completable rxStart(String uri, String user, String password) {
        Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
	return null;
    }

}

