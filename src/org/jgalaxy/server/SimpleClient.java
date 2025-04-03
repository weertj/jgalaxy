package org.jgalaxy.server;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.http.HttpClient;

public class SimpleClient {

  static public HttpClient createClient() {
    HttpClient httpClient = HttpClient.newBuilder().authenticator(new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication("weert", "weert".toCharArray());
      }
    }).build();
    return httpClient;
  }

}
