package com.bitcoin;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.*;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ConverterSvcTest {

  private CloseableHttpClient client;

  @BeforeMethod
  public void setUp() throws IOException {
    client = Mockito.mock(CloseableHttpClient.class);

    HttpEntity entity = Mockito.mock(HttpEntity.class);
    CloseableHttpResponse response = Mockito.mock(CloseableHttpResponse.class);
    StatusLine statusLine = Mockito.mock(StatusLine.class);

    InputStream stream = new ByteArrayInputStream("{\"time\": {\"updated\": \"Oct 15, 2020 22:55:00 UTC\",\"updatedISO\": \"2020-10-15T22:55:00+00:00\",\"updateduk\": \"Oct 15, 2020 at 23:55 BST\"},\"chartName\": \"Bitcoin\",\"bpi\": {\"USD\": {\"code\": \"USD\",\"symbol\": \"&#36;\",\"rate\": \"11,486.5341\",\"description\": \"United States Dollar\",\"rate_float\": 11486.5341},\"GBP\": {\"code\": \"GBP\",\"symbol\": \"&pound;\",\"rate\": \"8,900.8693\",\"description\": \"British Pound Sterling\",\"rate_float\": 8900.8693},\"EUR\": {\"code\": \"EUR\",\"symbol\": \"&euro;\",\"rate\": \"9,809.3278\",\"description\": \"Euro\",\"rate_float\": 9809.3278}}}".getBytes());

    Mockito.when(client.execute(Mockito.any(HttpGet.class))).thenReturn(response);
    Mockito.when(entity.getContent()).thenReturn(stream);
    Mockito.when(response.getEntity()).thenReturn(entity);
    Mockito.when(response.getStatusLine()).thenReturn(statusLine);
    Mockito.when(statusLine.getStatusCode()).thenReturn(200);
  }

  @Test
  public void getExchangeRateUSD() {
    //Mockito.when(client.execute(Mockito.any(HttpGet.class))).thenReturn(response);
    //Mockito.when(entity.getContent()).thenReturn(stream);
    //Mockito.when(response.getEntity()).thenReturn(entity);
    //Mockito.when(response.getStatusLine()).thenReturn(statusLine);
    //Mockito.when(statusLine.getStatusCode()).thenReturn(200);

    ConverterSvc converterSvc = new ConverterSvc(client);
    var actual = converterSvc.getExchangeRate("USD");

    float expected = (float)11486.5341;
    Assert.assertEquals(actual, expected);
  }

  @Test
  public void getExchangeRateGBP() {
    ConverterSvc converterSvc = new ConverterSvc(client);
    var actual = converterSvc.getExchangeRate("GBP");
    float expected = (float)8900.8693;
    Assert.assertEquals(actual, expected);
  }

  @Test
  public void getExchangeRateEUR() {
    ConverterSvc converterSvc = new ConverterSvc(client);
    var actual = converterSvc.getExchangeRate("EUR");
    float expected = (float)9809.3278;
    Assert.assertEquals(actual, expected);
  }

  @Test(enabled=false)
  public void convertBitcoins() {
    ConverterSvc converterSvc = new ConverterSvc();
    var actual = converterSvc.convertBitcoins("USD", 1);
    double expected = 100;
    Assert.assertEquals(actual, expected);
  }
}
