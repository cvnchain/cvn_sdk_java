package org.brewchain.sdk.https;

import org.brewchain.sdk.model.ChainRequest;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.net.ssl.X509TrustManager;
import java.util.concurrent.TimeUnit;
@Slf4j
public class PureOkHttpExecutor {

    private static  final OkHttpClient client;

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
//                .addInterceptor(SSLSocketClient.HuoBiInterceptor())
                .dns(SSLSocketClient.getHuoBiDns())
                .retryOnConnectionFailure(false).build();

    }

    public static void init(){
        log.info("初始化...");
        ChainRequest cr = RequestBuilder.buildGetLastedBlock();
        post(cr.getUrl(),cr.getBody());
    }

    public static String get(String url){
        return execute(url,"","Get");
    }

    public static String post(String url,String body){
        return execute(url,body,"post");
    }

    public static void get(String url,Callback callback){
        asyncExecute(url,"","Get",callback);
    }

    public static void post(String url,String body,Callback callback){
        asyncExecute(url,body,"POST",callback);
    }

    public static String execute(String url,String body,String method){
        String result = "";
        Request request;
        if("post".equalsIgnoreCase(method)){
            request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json;charset=UTF-8")
                        ,body))
                .build();
        }else{
            request = new Request.Builder().url(url).get().build();
        }

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("url:"+request.url()+";response code:"+response.code());
                throw new RuntimeException("Unexpected code " + response);
            }
            result = response.body().string();
        } catch (Exception e) {
            log.error("请求失败，url:"+request.url(),e);
            throw new RuntimeException(e);
        }
        return result;
    }

    public static void asyncExecute(String url,String body,String method,
                                    Callback callback ){
        if(url == null){throw new RuntimeException("request must not be null!");}
        if(callback == null){throw new RuntimeException("request CallBack must not be null!");}
        Request request;
        if("post".equalsIgnoreCase(method)){
            request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json;charset=UTF-8")
                        ,body))
                .build();
        }else{
            request = new Request.Builder().url(url).get().build();
        }

        client.newCall(request).enqueue(
                callback
//                new Callback() {
//            @Override public void onFailure(Call call, IOException e) {
//                log.error("请求失败，url:"+request.url(),e);
//                cr.error(cr.getBody(),e);
//            }
//
//            @Override public void onResponse(Call call, Response response) throws IOException {
//                try (ResponseBody responseBody = response.body()) {
//                    if (!response.isSuccessful()) {
//                        cr.error(responseBody.string(),new ChainException("响应错误"));
//                    }
//                    String result = responseBody.string();
//                    cr.call(result);
//                }catch (Exception e){
//                    log.error("请求失败，url:"+request.url(),e);
//                    cr.error(response.body().string(),e);
//                }
//            }
//        }
        );
    }

}
