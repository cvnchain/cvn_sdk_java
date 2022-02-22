package org.brewchain.sdk.util;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.Message;
import com.googlecode.protobuf.format.JsonFormat;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
@Slf4j
public class JsonPBUtil {
    public final static JsonFormat jsonFormat = new JsonFormat();
//	public final static JsonPBFormat jsonPBFormat = new JsonPBFormat();

    public static void json2PB(String json, Message.Builder t) throws JsonFormat.ParseException {
        jsonFormat.merge(json, ExtensionRegistry.getEmptyRegistry(), t);
    }

    public static void json2PB(byte[] jsonByte, Message.Builder t) throws JsonFormat.ParseException {
        json2PB(new String(jsonByte, Charset.defaultCharset()), t);
    }

    public static String messageToJson(Message msg){
       return new JsonFormat().printToString(msg);
    }
}
