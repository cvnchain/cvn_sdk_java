package org.brewchain.sdk.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexUtil {
    
    public static String getValue(Pattern p,String content){

        Matcher m=p.matcher(content);
        if(!m.find()) 
            return null;
        return m.group(0);
        
    }
    
    public static List<String> getMatchList(Pattern p,String content){
        List<String> list = new ArrayList<>();
        Matcher m=p.matcher(content);
        while (m.find()){
            list.add(m.group());
        }
        return list;
    }


    public static void main(String[] args) {
        Pattern p =  Pattern.compile("\"subItems\": \\[\\{[\\S\\s]*?\\}\\]");
        
        String s = "{\"retCode\": 1,\"items\": [{\"key\": \"appeals\",\"subItems\": [{\"key\": \"item\",\"subItems\":" +
                " [{\"key\": \"status\",\"value\": \"1\"},{\"key\": \"content\",\"value\": " +
                "\"080112160a06616161616161120ce794b3e8af89e794b3e8af89\"},{\"key\": \"payId\",\"value\": " +
                "\"8de9c9a051cfed17b6a3c90aa8bca1d044f41571887a213095bf7a59fb534594\"},{\"key\": \"blockNumber\"," +
                "\"value\": \"72920\"},{\"key\": \"timestamp\",\"value\": \"1568615546035\"}]},{\"key\": \"item\"," +
                "\"subItems\": [{\"key\": \"status\",\"value\": \"1\"},{\"key\": \"content\",\"value\": " +
                "\"080112160a06616161616161120ce794b3e8bea9e794b3e8bea9\"},{\"key\": \"payId\",\"value\": " +
                "\"8de9c9a051cfed17b6a3c90aa8bca1d044f41571887a213095bf7a59fb534594\"},{\"key\": \"blockNumber\"," +
                "\"value\": \"72940\"},{\"key\": \"timestamp\",\"value\": \"1568615566292\"}]}]}]}";
        List<String> str = getMatchList(p,s);
        System.out.println(str);
        System.out.println(str.size());
        
    }
    
}
