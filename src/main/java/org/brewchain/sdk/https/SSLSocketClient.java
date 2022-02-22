package org.brewchain.sdk.https;

import okhttp3.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class SSLSocketClient {

    public static SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, getTrustManager(), new SecureRandom());
            
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static TrustManager[] getTrustManager() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };
        return trustAllCerts;
    }

    public static HostnameVerifier getHostnameVerifier() {
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };
        return hostnameVerifier;
    }
    
    public static Interceptor HuoBiInterceptor(){
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl url = original.url();
                if("otc-api.huobi.co".equals(url.host())){
                    url = original.url().newBuilder().host("104.19.213.37").build();
                }
                Request completeRequest = original.newBuilder()
                        .url(url)
                        .build();
                Request.Builder requestBuilder = completeRequest.newBuilder();
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
    }
    
    public static Dns getHuoBiDns(){
        
        return new Dns(){
            private final Dns SYSTEM = Dns.SYSTEM;

            @Override
            public List<InetAddress> lookup(String hostname) throws UnknownHostException {
                if("otc-api.huobi.co".equals(hostname)){
                    InetAddress ip = InetAddress.getByAddress(new byte[]{104,19,(byte)213,37});
                    List<InetAddress> list = new ArrayList<>();
                    list.add(ip);
                    return list;
                }
                return SYSTEM.lookup(hostname);
            }
        };
    }
}