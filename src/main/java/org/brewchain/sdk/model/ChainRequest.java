package org.brewchain.sdk.model;
 
public class ChainRequest {
    
    private String domain;
    
    private String url;
    
    private String body;

    public ChainRequest(String url, String body){
        this.url = url;
        this.body = body;
    }
    public String getUrl(){
        return url;
    }
    public void setUrl(String _url) { this.url = _url;}
    public String getBody(){
        return body;
    }
    
}
