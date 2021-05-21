package com.bitcoin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.HashMap;

public class ConverterSvc {
  private static final HashMap<String, Integer> exchangeRates = new HashMap<String, Integer>();
  static {
    exchangeRates.put("USD", 100);
    exchangeRates.put("GBP", 200);
    exchangeRates.put("EUR", 300);
  }

  private final String BITCOIN_CURRENT_PRICE_URL = "https://api.coindesk.com/v1/bpi/currentprice.json";
  private final HttpGet httpget = new HttpGet(BITCOIN_CURRENT_PRICE_URL);
  private final CloseableHttpClient httpclient;
  private CloseableHttpClient httpClient;


  public ConverterSvc() {
    this.httpclient = HttpClients.createDefault();
  }
  public ConverterSvc(CloseableHttpClient httpClient) {
    this.httpclient = httpClient;
  }

  public double getExchangeRate(String currency) {
    float rate = 0;

    try {
      CloseableHttpResponse response = this.httpclient.execute(httpget);
      InputStream inputStream = response.getEntity().getContent();
      var json = new BufferedReader(new InputStreamReader(inputStream));

      @SuppressWarnings("deprecation")
      JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
      rate = jsonObject.get("bpi").getAsJsonObject().get(currency).getAsJsonObject().get("rate_float").getAsFloat();
    }
    catch (Exception ex) {
      rate = -1;
      System.out.println(ex);
    }
    return rate;
  }

  public double convertBitcoins(String currency, int coins) {
    //return getExchangeRate(currency) * coins;
    double dollars = 0;
    var exchangeRate = getExchangeRate(currency);
    dollars = exchangeRate * coins;

    return dollars;
  }
}
