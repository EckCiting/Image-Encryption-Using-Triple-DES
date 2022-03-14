package xjtlu.tdes.client.Utilities;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.util.Timeout;

public class WebUtility {

    public static String postRequest(String url, String jsonBody) {
        String returnValue = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(2))
                .setConnectionRequestTimeout(Timeout.ofSeconds(2))
                .setResponseTimeout(Timeout.ofSeconds(2))
                .build();
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("Content-Type", "application/json");


        httpPost.setEntity(new StringEntity(jsonBody));
        try (CloseableHttpResponse res = httpClient.execute(httpPost)) {
            if (res.getCode() == HttpStatus.SC_OK) {
                HttpEntity entity = res.getEntity();
                returnValue =  EntityUtils.toString(entity);
            } else {
                // NOTE: Temporary output the Fail Response, special processing is necessary in the future
                returnValue =  "Failed: " + res.getCode();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpPost.reset();
        }
        return returnValue;
    }
}
