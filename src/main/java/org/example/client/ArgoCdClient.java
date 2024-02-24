package org.example.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.example.client.request.ArgoCdAppRequest;
import org.example.config.ArgoCdConfig;
import org.example.error.exception.ArgoCdException;
import org.springframework.stereotype.Component;

@Component
public class ArgoCdClient {

  private final OkHttpClient client;
  private final ArgoCdConfig argoCdConfig;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public ArgoCdClient(ArgoCdConfig argoCdConfig)
      throws NoSuchAlgorithmException, KeyManagementException {
    this.argoCdConfig = argoCdConfig;

    final TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {
          @Override
          public void checkClientTrusted(X509Certificate[] chain, String authType)
              throws CertificateException {
            // trust all client certs
          }

          @Override
          public void checkServerTrusted(X509Certificate[] chain, String authType)
              throws CertificateException {
            // trust all server certs
          }

          @Override
          public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
          }
        }
    };
    final SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
    sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
    final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
    final HostnameVerifier hostnameVerifier = (hostname, session) -> this.argoCdConfig.getOrigin()
        .contains(hostname);

    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    // trust all certs
    builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
    builder.hostnameVerifier(hostnameVerifier);
    builder.connectionSpecs(List.of(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT));
    // log all requests
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    builder.addInterceptor(logging);
    this.client = builder.build();
  }

  public String createApplication(ArgoCdAppRequest app) throws IOException {
    byte[] body = objectMapper.writeValueAsBytes(app);
    RequestBody requestBody = RequestBody.create(body);
    Request request = new Request.Builder()
        .url(argoCdConfig.getOrigin() + "/api/v1/applications?upsert=true&validate=true")
        .post(requestBody)
        .addHeader("Authorization", "Bearer " + argoCdConfig.getToken())
        .addHeader("Content-Type", "application/json")
        .build();
    try (Response response = client.newCall(request).execute()) {
      String responseBody = response.body().string();
      if (!response.isSuccessful()) {
        throw new ArgoCdException("Failed to create application: " + responseBody);
      }
      return responseBody;
    }
  }
}
