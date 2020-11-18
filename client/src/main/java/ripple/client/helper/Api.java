package ripple.client.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import ripple.client.Endpoint;
import ripple.client.entity.Item;

import java.util.HashMap;
import java.util.Map;

public final class Api {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static Item get(String address, int port, String key) {
        try {
            Map<String, String> headers = new HashMap<>(1);
            headers.put("x-ripple-key", key);
            String url = "http://" + address + ":" + port + Endpoint.GET;
            String returnValue = Http.get(url, headers);
            return MAPPER.readValue(returnValue, Item.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static boolean put(String address, int port, String key, String value) {
        try {
            Map<String, String> headers = new HashMap<>(2);
            headers.put("x-ripple-key", key);
            headers.put("x-ripple-value", value);
            String url = "http://" + address + ":" + port + Endpoint.PUT;
            String returnValue = Http.post(url, headers);
            return MAPPER.readValue(returnValue, Boolean.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public static boolean subscribe(String serverAddress, int serverPort
            , String callbackAddress, int callbackPort, String key) {
        try {
            Map<String, String> headers = new HashMap<>(3);
            headers.put("x-ripple-callback-address", callbackAddress);
            headers.put("x-ripple-callback-port", String.valueOf(callbackPort));
            headers.put("x-ripple-key", key);
            String url = "http://" + serverAddress + ":" + serverPort + Endpoint.SUBSCRIBE;
            String returnValue = Http.post(url, headers);
            return MAPPER.readValue(returnValue, Boolean.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public static boolean unsubscribe(String serverAddress, int serverPort
            , String callbackAddress, int callbackPort, String key) {
        try {
            Map<String, String> headers = new HashMap<>(3);
            headers.put("x-ripple-callback-address", callbackAddress);
            headers.put("x-ripple-callback-port", String.valueOf(callbackPort));
            headers.put("x-ripple-key", key);
            String url = "http://" + serverAddress + ":" + serverPort + Endpoint.UNSUBSCRIBE;
            String returnValue = Http.post(url, headers);
            return MAPPER.readValue(returnValue, Boolean.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
