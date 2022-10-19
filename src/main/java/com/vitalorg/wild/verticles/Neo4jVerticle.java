package com.vitalorg.wild.verticles;

import io.reactivex.Completable;
import io.vertx.reactivex.core.AbstractVerticle;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

import static org.neo4j.driver.Values.parameters;

public class Neo4jVerticle extends AbstractVerticle {

    public Completable rxStart(String uri, String user, String password) {
        Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
        return ;
    }


}
