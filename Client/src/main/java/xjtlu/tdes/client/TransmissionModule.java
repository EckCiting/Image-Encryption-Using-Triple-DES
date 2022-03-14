package xjtlu.tdes.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import xjtlu.tdes.client.Utilities.WebUtility;
import xjtlu.tdes.client.Config;
public class TransmissionModule {
    public static String stringToJson(TDESImage tdesImage){
        ObjectMapper mapper = new ObjectMapper();
        String tdsImageJson = "";
        try {
            tdsImageJson = mapper.writeValueAsString(tdesImage);
            System.out.println(tdsImageJson);
        } catch (JsonProcessingException e) {
            System.out.println(e);
        }
        return tdsImageJson;
    }
    public static void addImageToDatabase(String imageName,String expDate) throws Exception {
        TDESImage tdesImage = new TDESImage(imageName,expDate);
        WebUtility.postRequest(Config.BACKEND_URL + "addimage/",stringToJson(tdesImage));
    }
    public static void getSaltFromDatabase(String imageName){
        TDESImage tdesImage = new TDESImage(imageName);
        WebUtility.postRequest(Config.BACKEND_URL + "getsalt/",stringToJson(tdesImage));
    }


    public static void main(final String[] args) throws Exception {
        //addImageToDatabase("XJTLU-logo.png","2022-03-14 17:15:00");
        getSaltFromDatabase("XJTLU-logo.png");
    }
}
