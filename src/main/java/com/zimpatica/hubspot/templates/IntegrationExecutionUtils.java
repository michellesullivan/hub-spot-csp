package com.zimpatica.hubspot.templates;

import com.appian.connectedsystems.simplified.sdk.configuration.SimpleConfiguration;
import com.appian.connectedsystems.templateframework.sdk.IntegrationError;
import com.appian.connectedsystems.templateframework.sdk.IntegrationResponse;
import com.appian.connectedsystems.templateframework.sdk.diagnostics.IntegrationDesignerDiagnostic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class IntegrationExecutionUtils {
    private IntegrationExecutionUtils() {
    }

    /**
     * Creates common fields of diagnostics objects for both Send File and Create Folder templates.
     * Diagnostic is information that will be displayed in the Request and Response tabs. You can
     * include information that is helpful for the developer to debug, such as HTTP parameters.
     */
    public static Map<String,Object> getRequestDiagnostics(SimpleConfiguration connectedSystemConfiguration) {
        Map<String,Object> requestDiagnostic = new HashMap<>();
        //Request Diagnostic values will be shown on the Request tab on Appian Integration Designer Interface,
        //which will be visible to designers. Only add to diagnostics values that you wish the designer to see.
        requestDiagnostic.put("api key", "***********");
        //For sensitive values, mask it so that it won't be visible to designers
        return requestDiagnostic;
    }



    public static IntegrationResponse handleClientProtocolException(
            ClientProtocolException e,
            Map<String,Object> requestDiagnostics,
            long start) {

        final long end = System.currentTimeMillis();
        final long elapsed =  end - start;

        final IntegrationDesignerDiagnostic diagnostic = IntegrationDesignerDiagnostic.builder()
                .addExecutionTimeDiagnostic(elapsed)
                .addRequestDiagnostic(requestDiagnostics)
                .build();

        IntegrationError error = IntegrationError.builder()
                .title("Client Protocol Exception")
                .message(e.toString())
                .build();

        return IntegrationResponse.forError(error).withDiagnostic(diagnostic).build();
    }

    public static IntegrationResponse handleIOException(
            IOException e,
            Map<String,Object> requestDiagnostics,
            long start) {

        final long end = System.currentTimeMillis();
        final long elapsed =  end - start;

        final IntegrationDesignerDiagnostic diagnostic = IntegrationDesignerDiagnostic.builder()
                .addExecutionTimeDiagnostic(elapsed)
                .addRequestDiagnostic(requestDiagnostics)
                .build();

        IntegrationError error = IntegrationError.builder()
                .title("IO Exception")
                .message(e.toString())
                .build();

        return IntegrationResponse.forError(error).withDiagnostic(diagnostic).build();
    }

    public static IntegrationResponse handleIllegalArgumentException(
            IllegalArgumentException e,
            Map<String,Object> requestDiagnostics,
            long start) {

        final long end = System.currentTimeMillis();
        final long elapsed =  end - start;

        final IntegrationDesignerDiagnostic diagnostic = IntegrationDesignerDiagnostic.builder()
                .addExecutionTimeDiagnostic(elapsed)
                .addRequestDiagnostic(requestDiagnostics)
                .build();

        IntegrationError error = IntegrationError.builder()
                .title("Unsupported Encoding Exception")
                .message(e.toString())
                .build();

        return IntegrationResponse.forError(error).withDiagnostic(diagnostic).build();
    }
    public static IntegrationResponse handleUnsupportedEncodingException(
            UnsupportedEncodingException e,
            Map<String,Object> requestDiagnostics,
            long start) {

        final long end = System.currentTimeMillis();
        final long elapsed =  end - start;

        final IntegrationDesignerDiagnostic diagnostic = IntegrationDesignerDiagnostic.builder()
                .addExecutionTimeDiagnostic(elapsed)
                .addRequestDiagnostic(requestDiagnostics)
                .build();

        IntegrationError error = IntegrationError.builder()
                .title("Unsupported Encoding Exception")
                .message(e.toString())
                .build();

        return IntegrationResponse.forError(error).withDiagnostic(diagnostic).build();
    }

    public static IntegrationResponse handleJsonProcessingException(
            JsonProcessingException e,
            Map<String,Object> requestDiagnostics,
            long start) {

        final long end = System.currentTimeMillis();
        final long elapsed =  end - start;

        final IntegrationDesignerDiagnostic diagnostic = IntegrationDesignerDiagnostic.builder()
                .addExecutionTimeDiagnostic(elapsed)
                .addRequestDiagnostic(requestDiagnostics)
                .build();

        IntegrationError error = IntegrationError.builder()
                .title("Unsupported Encoding Exception")
                .message(e.toString())
                .build();

        return IntegrationResponse.forError(error).withDiagnostic(diagnostic).build();
    }

    public static IntegrationResponse handleBadInput(
            String errorMessage,

            Map<String,Object> requestDiagnostics,
            Map<String,Object> responseDiagnostic,
            long start) {

        final long end = System.currentTimeMillis();
        final long elapsed =  end - start;

        final IntegrationDesignerDiagnostic diagnostic = IntegrationDesignerDiagnostic.builder()
                .addExecutionTimeDiagnostic(elapsed)
                .addRequestDiagnostic(requestDiagnostics)
                .addResponseDiagnostic(responseDiagnostic)
                .build();

        IntegrationError error = IntegrationError.builder()
                .title("Invalid Inputs")
                .message(errorMessage)
                .build();

        return IntegrationResponse.forError(error).withDiagnostic(diagnostic).build();
    }
//    public static IntegrationResponse handleNoContentResponse(
//            Map<String,Object> requestDiagnostic,
//            long start
//    ){
//        final long end = System.currentTimeMillis();
//        final long elapsed =  end - start;
//        String quote = "\"";
//        String responseBody = "{"+quote+"success"+quote+":"+quote+true+quote+"}";
//        Map<String,Object> responseMap = IntegrationExecutionUtils.getResponseMap(responseBody);
//        final IntegrationDesignerDiagnostic diagnostic = IntegrationDesignerDiagnostic.builder()
//                .addExecutionTimeDiagnostic(elapsed)
//                .addRequestDiagnostic(requestDiagnostic)
//                .build();
//
//        return IntegrationResponse
//                .forSuccess(responseMap)
//                .withDiagnostic(diagnostic)
//                .build();
//    }

    public static  Map<String, Object> getResponseDiagnostic(String jsonString) {
        Map<String, Object> diagnostic = new HashMap<>();
        diagnostic.put("Raw Response", jsonString);
        return diagnostic;
    }
    public static Map<String,Object> getResponseMap(String jsonResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> responseMap = objectMapper.readValue(jsonResponse,
                new TypeReference<HashMap<String,Object>>() {
                });
        return responseMap;
    }
}
