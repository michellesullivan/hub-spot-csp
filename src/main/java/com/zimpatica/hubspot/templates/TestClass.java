package com.zimpatica.hubspot.templates;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
//import org.apache.commons.logging.LogFactory;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.appian.connectedsystems.templateframework.sdk.IntegrationResponse;
import com.appian.connectedsystems.templateframework.sdk.diagnostics.IntegrationDesignerDiagnostic;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class TestClass {
    public static void main(String[] args){
        String email = "michelle@michelles.com";
        String firstname = "Michelle";
        String lastname = "Smith";
        String website = "http://hubspot.com";
        String company = "HubSpot";
        String phone = "555-122-2323";
        String address = "25 First Street";
        String city = "Cambridge";
        String state = "MA";
        String zip = "02139";
        String dealName = "Test Deal";
        List<String> companyIds = new ArrayList<>();
        List<String> contactIds = new ArrayList<>();
        companyIds.add("1424");
        contactIds.add("2");
        Map<String,Object> firstMap = new HashMap<>();
        firstMap.put("email", email);
        firstMap.put("firstname", firstname);
        firstMap.put("lastname", lastname);
        firstMap.put("website", website);
        firstMap.put("company", company);
        firstMap.put("phone", phone);
        firstMap.put("address", address);
        firstMap.put("city", city);
        firstMap.put("state", state);
        firstMap.put("zip", zip);

        List<Map<String,Object>> list = new ArrayList<>();
        for(String key : firstMap.keySet()){
            Map<String,Object> propertiesMap = new HashMap<>();
            if(firstMap.get(key)!=null){
                propertiesMap.put("property", key);
                propertiesMap.put("value", firstMap.get(key));
                list.add(propertiesMap);
            }

        }
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("properties", list);

        String url = "https://api.hubapi.com/contacts/v1/contact/vid/1/profile?hapikey=0cc3c96b-239c-4152-a8fc-28db55d2585f";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;

        try{
            String json = new ObjectMapper().writeValueAsString(jsonMap);
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-Type", "application/json");
            response = client.execute(httpPost);
            int statusline = response.getStatusLine().getStatusCode();
            System.out.println(json);
            System.out.println(statusline);
        } catch (JsonProcessingException e) {
//            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e){

        }

        catch (ClientProtocolException e) {
//            e.printStackTrace();
        }
        catch (IOException e) {
//            e.printStackTrace();
        } finally{
            HttpClientUtils.closeQuietly(response);
        }
    }
}
