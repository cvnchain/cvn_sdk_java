package org.brewchain.sdk.https;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.brewchain.sdk.model.ChainRequest;

import javax.net.ssl.X509TrustManager;
import java.util.concurrent.TimeUnit;
@Slf4j
public class OKHttpExecutor {

    private static OkHttpClient client;

    static {
        Dispatcher dispatcher = new Dispatcher();
//        dispatcher.setMaxRequestsPerHost(1);
//        dispatcher.setMaxRequests(1);

        client = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS)
                .dispatcher(dispatcher)
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(),(X509TrustManager)SSLSocketClient.getTrustManager()[0])
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .retryOnConnectionFailure(false).build();

    }
    public static void setTimeOut(long timeOut, TimeUnit timeUnit){
        client = new OkHttpClient().newBuilder()
                .connectTimeout(timeOut, timeUnit)
                .readTimeout(timeOut,timeUnit)
                .writeTimeout(timeOut,timeUnit)
                .dispatcher(client.dispatcher())
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(),(X509TrustManager)SSLSocketClient.getTrustManager()[0])
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .retryOnConnectionFailure(false).build();
    }

    public static void init(){
        log.info("初始化...");
        execute(RequestBuilder.buildGetLastedBlock());
    }

    public static String execute(ChainRequest cr){
        return execute(cr,cr.getUrl());
    }

    public static String execute(ChainRequest cr, String url){
        if(cr == null){
            log.error("ChainRequest must not be null!");
            return null;
        }
        String result = "";
        Request request = new Request.Builder()
            .url(url)
            .post(RequestBody.create(MediaType.parse("application/json;charset=UTF-8")
                ,cr.getBody()))
            .build();

        log.debug("\r\nrequest url -----"+
            cr.getUrl());
        log.debug("\r\nrequest body -----"+
            cr.getBody());

        try (Response response = client.newCall(request).execute()) {
            log.debug("请求返回，URL："+request.url()+",\tbody:"+cr.getBody());
            if (!response.isSuccessful()) {
                log.error("url:"+request.url()+";response code:"+response.code());
                return null;
            }
            result = response.body().string();
            log.debug("result---"+result);
        } catch (Exception e) {
            DomainPool.remove("");//todo
            log.error("请求失败，url:"+request.url(),e);
            return null;
        }
        return result;
    }

}
