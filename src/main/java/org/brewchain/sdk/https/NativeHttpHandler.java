package org.brewchain.sdk.https;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
@Slf4j
@Deprecated
public class NativeHttpHandler {
    
    
    public static String post(String url,String body) throws IOException {
        return request(url,body,"POST");
    }
    
    public static String get(String url,String body) throws IOException {
        return request(url,body,"GET");
    }
    
    public static String  request(String url,String body,String method) throws IOException {
//        String url = "http://localhost:8080/helpPage/upload";
//        String body = "{\"name\":\"张三\",\"age\":\"18\"}";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        //设置请求头
        con.setRequestMethod(method.toUpperCase());
//        con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

//        // 发送请求
//        con.setDoOutput(true);
        if("post".equalsIgnoreCase(method)){
            con.setDoOutput(true);
            PrintWriter wr = new PrintWriter(con.getOutputStream());
            wr.write(body);
            wr.flush();
            wr.close();
        }

        int responseCode = con.getResponseCode();
        log.debug("Sending request to URL : " + url);
        log.debug("Post parameters : " + body);
        log.debug("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //打印一下结果
        log.debug(response.toString());
        return response.toString();
    }
    
    
    
}
