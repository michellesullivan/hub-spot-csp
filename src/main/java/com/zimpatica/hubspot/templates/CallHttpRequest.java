package com.zimpatica.hubspot.templates;

import com.appian.connectedsystems.templateframework.sdk.IntegrationResponse;
import com.appian.connectedsystems.templateframework.sdk.diagnostics.IntegrationDesignerDiagnostic;
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

public class CallHttpRequest {
    public static IntegrationResponse executeGetRequest(
            String url,
            Map<String,Object> requestDiagnostic
    ){

        Map<String,Object> responseMap;
        Map<String,Object> responseDiagnostic;
        CloseableHttpClient client = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);
        final long start = System.currentTimeMillis();
        CloseableHttpResponse response = null;

        try{
            response = client.execute(httpGet);
            String responseBody = EntityUtils.toString(response.getEntity());
            responseMap = IntegrationExecutionUtils.getResponseMap(responseBody);
            responseDiagnostic = IntegrationExecutionUtils.getResponseDiagnostic(responseBody);


            if(responseMap.containsKey("error")){
                String errorMessage = responseMap.get("error").toString();
                return IntegrationExecutionUtils.handleBadInput(
                        errorMessage,
                        requestDiagnostic,
                        responseDiagnostic,
                        start
                );
            }
            client.close();


        } catch(ClientProtocolException e){
            return IntegrationExecutionUtils.handleClientProtocolException(
                    e,
                    requestDiagnostic,
                    start
            );

        } catch(IOException e){
            return IntegrationExecutionUtils.handleIOException(
                    e,
                    requestDiagnostic,
                    start
            );

        }finally{
            HttpClientUtils.closeQuietly(response);
        }
        final long end = System.currentTimeMillis();
        final long executionTime = end - start;
        final IntegrationDesignerDiagnostic diagnostic = IntegrationDesignerDiagnostic.builder()
                .addExecutionTimeDiagnostic(executionTime)
                .addRequestDiagnostic(requestDiagnostic)
                .addResponseDiagnostic(responseDiagnostic)
                .build();

        return IntegrationResponse
                .forSuccess(responseMap)
                .withDiagnostic(diagnostic)
                .build();
    }
    public static IntegrationResponse executePutRequest(
            String url,
            Map<String,Object> requestDiagnostic
    ){

        Map<String,Object> responseMap;
        Map<String,Object> responseDiagnostic;
        CloseableHttpClient client = HttpClients.createDefault();

        HttpPut httpPut = new HttpPut(url);
        httpPut.setHeader("Content-Type", "application/json");
        final long start = System.currentTimeMillis();
        CloseableHttpResponse response = null;
        try{
            response = client.execute(httpPut);
            String responseBody = EntityUtils.toString(response.getEntity());
            responseMap = IntegrationExecutionUtils.getResponseMap(responseBody);
            responseDiagnostic = IntegrationExecutionUtils.getResponseDiagnostic(responseBody);


            if(responseMap.containsKey("error")){
                String errorMessage = responseMap.get("error").toString();
                return IntegrationExecutionUtils.handleBadInput(
                        errorMessage,
                        requestDiagnostic,
                        responseDiagnostic,
                        start
                );
            }
            client.close();


        } catch(ClientProtocolException e){
            return IntegrationExecutionUtils.handleClientProtocolException(
                    e,
                    requestDiagnostic,
                    start
            );

        } catch(IOException e){
            return IntegrationExecutionUtils.handleIOException(
                    e,
                    requestDiagnostic,
                    start
            );

        }finally{
            HttpClientUtils.closeQuietly(response);
        }
        final long end = System.currentTimeMillis();
        final long executionTime = end - start;
        final IntegrationDesignerDiagnostic diagnostic = IntegrationDesignerDiagnostic.builder()
                .addExecutionTimeDiagnostic(executionTime)
                .addRequestDiagnostic(requestDiagnostic)
                .addResponseDiagnostic(responseDiagnostic)
                .build();

        return IntegrationResponse
                .forSuccess(responseMap)
                .withDiagnostic(diagnostic)
                .build();
    }
    public static IntegrationResponse executePostRequest(
            String url,
            Map<String,Object> requestDiagnostic,
            String json
    ){
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        final long start = System.currentTimeMillis();

        //intialize responseMap for response and responseDiagnostic
        Map<String,Object> responseMap;
        Map<String,Object> responseDiagnostic;
        CloseableHttpResponse response = null;

        //make webservice request
        try {
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-Type", "application/json");
            try{
                //get response
                response = client.execute(httpPost);
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody;
                if(statusCode==201){
                    String quote = "\"";
                    responseBody = "{"+quote+"success"+quote+":"+quote+true+quote+"}";
                }
                else{
                    responseBody = EntityUtils.toString(response.getEntity());
                }
                responseMap = IntegrationExecutionUtils.getResponseMap(responseBody);
                responseDiagnostic = IntegrationExecutionUtils.getResponseDiagnostic(responseBody);

                //return error response if contains error but status 200
                if(responseMap.containsKey("error")){
                    String errorMessage = responseMap.get("error").toString();
                    String errorDetails;
//                    if(responseMap.containsKey("errors"))
                    return IntegrationExecutionUtils.handleBadInput(
                            errorMessage,
                            requestDiagnostic,
                            responseDiagnostic,
                            start
                    );
                }
                client.close();
                //handle client protocol exception
            } catch(ClientProtocolException e){
                return IntegrationExecutionUtils.handleClientProtocolException(
                        e,
                        requestDiagnostic,
                        start
                );
                //handle io exception
            } catch(IOException e){
                return IntegrationExecutionUtils.handleIOException(
                        e,
                        requestDiagnostic,
                        start
                );
            }finally{
                HttpClientUtils.closeQuietly(response);
            }
        }
        //handle unsupported encoding exception
        catch(UnsupportedEncodingException e){
            return IntegrationExecutionUtils.handleUnsupportedEncodingException(
                    e,
                    requestDiagnostic,
                    start
            );

        }
        final long end = System.currentTimeMillis();
        final long executionTime = end - start;

        final IntegrationDesignerDiagnostic diagnostic = IntegrationDesignerDiagnostic.builder()
                .addExecutionTimeDiagnostic(executionTime)
                .addRequestDiagnostic(requestDiagnostic)
                .addResponseDiagnostic(responseDiagnostic)
                .build();

        return IntegrationResponse
                .forSuccess(responseMap)
                .withDiagnostic(diagnostic)
                .build();
    }
    public static IntegrationResponse executeDeleteRequest(
            String url,
            Map<String,Object> requestDiagnostic
    ){

        Map<String,Object> responseMap;
        Map<String,Object> responseDiagnostic;
        CloseableHttpClient client = HttpClients.createDefault();

        HttpDelete httpDelete = new HttpDelete(url);
        final long start = System.currentTimeMillis();
        CloseableHttpResponse response = null;

        try{
            response = client.execute(httpDelete);
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody;
            if(statusCode==201){
                String quote = "\"";
                responseBody = "{"+quote+"success"+quote+":"+quote+true+quote+"}";
            }
            else{
                responseBody = EntityUtils.toString(response.getEntity());
            }
            responseMap = IntegrationExecutionUtils.getResponseMap(responseBody);
            responseDiagnostic = IntegrationExecutionUtils.getResponseDiagnostic(responseBody);


            if(responseMap.containsKey("error")){
                String errorMessage = responseMap.get("error").toString();
                return IntegrationExecutionUtils.handleBadInput(
                        errorMessage,
                        requestDiagnostic,
                        responseDiagnostic,
                        start
                );
            }
            client.close();


        } catch(ClientProtocolException e){
            return IntegrationExecutionUtils.handleClientProtocolException(
                    e,
                    requestDiagnostic,
                    start
            );

        } catch(IOException e){
            return IntegrationExecutionUtils.handleIOException(
                    e,
                    requestDiagnostic,
                    start
            );

        }finally{
            HttpClientUtils.closeQuietly(response);
        }
        final long end = System.currentTimeMillis();
        final long executionTime = end - start;
        final IntegrationDesignerDiagnostic diagnostic = IntegrationDesignerDiagnostic.builder()
                .addExecutionTimeDiagnostic(executionTime)
                .addRequestDiagnostic(requestDiagnostic)
                .addResponseDiagnostic(responseDiagnostic)
                .build();

        return IntegrationResponse
                .forSuccess(responseMap)
                .withDiagnostic(diagnostic)
                .build();
    }
//    public static IntegrationResponse executePostRequest(
//            String url,
//            Map<String,Object> requestDiagnostic
//    ){
//        CloseableHttpClient client = HttpClients.createDefault();
//        HttpPost httpPost = new HttpPost(url);
//        final long start = System.currentTimeMillis();
//        Map<String,Object> responseMap;
//        Map<String,Object> responseDiagnostic;
//
//
//        httpPost.setHeader("Content-Type", "application/json");
//
//        try{
//            CloseableHttpResponse response = client.execute(httpPost);
//            String responseBody = EntityUtils.toString(response.getEntity());
//            responseMap = IntegrationExecutionUtils.getResponseMap(responseBody);
//            responseDiagnostic = IntegrationExecutionUtils.getResponseDiagnostic(responseBody);
//
//
//            if(responseMap.containsKey("error")){
//                String errorMessage = responseMap.get("error").toString();
//                return IntegrationExecutionUtils.handleBadInput(
//                        errorMessage,
//                        requestDiagnostic,
//                        responseDiagnostic,
//                        start
//                );
//            }
//            client.close();
//
//
//        } catch(ClientProtocolException e){
//            return IntegrationExecutionUtils.handleClientProtocolException(
//                    e,
//                    requestDiagnostic,
//                    start
//            );
//
//        } catch(IOException e){
//            return IntegrationExecutionUtils.handleIOException(
//                    e,
//                    requestDiagnostic,
//                    start
//            );
//
//        }
//        final long end = System.currentTimeMillis();
//        final long executionTime = end - start;
//        final IntegrationDesignerDiagnostic diagnostic = IntegrationDesignerDiagnostic.builder()
//                .addExecutionTimeDiagnostic(executionTime)
//                .addRequestDiagnostic(requestDiagnostic)
//                .addResponseDiagnostic(responseDiagnostic)
//                .build();
//
//        return IntegrationResponse
//                .forSuccess(responseMap)
//                .withDiagnostic(diagnostic)
//                .build();
//    }
}
