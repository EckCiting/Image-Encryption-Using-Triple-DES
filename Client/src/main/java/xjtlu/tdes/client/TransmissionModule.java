package xjtlu.tdes.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import xjtlu.tdes.client.Utilities.WebUtility;
import java.io.IOException;

public class TransmissionModule {
    public static String stringToJson(TDESImage tdesImage){
        ObjectMapper mapper = new ObjectMapper();
        String tdsImageJson = "";
        try {
            tdsImageJson = mapper.writeValueAsString(tdesImage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return tdsImageJson;
    }
    public static TDESImage jsonToObject(String json) {
        ObjectMapper mapper = new ObjectMapper();
        TDESImage tdesImage = null;
        try {
            tdesImage = mapper.readValue(json, TDESImage.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return tdesImage;
    }
    public static void addImageToDatabase(String imageName,String expDate) {
        TDESImage tdesImage = null;
        try {
            tdesImage = new TDESImage(imageName, expDate);
            WebUtility.postRequest(Config.BACKEND_URL + "addimage/",stringToJson(tdesImage));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static String getSaltFromDatabase(String imageName){
        TDESImage tdesImage = null;
        try {
            tdesImage = new TDESImage(imageName);
            return WebUtility.postRequest(Config.BACKEND_URL + "getsalt/",stringToJson(tdesImage));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
