package com.zimpatica.hubspot.templates;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.appian.connectedsystems.simplified.sdk.SimpleIntegrationTemplate;
import com.appian.connectedsystems.simplified.sdk.configuration.SimpleConfiguration;
import com.appian.connectedsystems.templateframework.sdk.ExecutionContext;
import com.appian.connectedsystems.templateframework.sdk.IntegrationResponse;
import com.appian.connectedsystems.templateframework.sdk.TemplateId;
import com.appian.connectedsystems.templateframework.sdk.configuration.PropertyPath;
import com.appian.connectedsystems.templateframework.sdk.metadata.IntegrationTemplateRequestPolicy;
import com.appian.connectedsystems.templateframework.sdk.metadata.IntegrationTemplateType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//import com.
// Must provide an integration id. This value need only be unique for this connected system
@TemplateId(name="UpdateContact")
// Set template type to READ since this integration does not have side effects
@IntegrationTemplateType(IntegrationTemplateRequestPolicy.WRITE)
public class UpdateContact extends SimpleIntegrationTemplate {

    public static final String BASE_URL = "https://api.hubapi.com/contacts/v1/contact/vid/";
    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String WEBSITE = "website";
    public static final String COMPANY = "company";
    public static final String PHONE = "phone";
    public static final String ADDRESS = "address";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String ZIP = "zip";
    public static final String CONTACT_ID = "contactId";

    @Override
    protected SimpleConfiguration getConfiguration(
            SimpleConfiguration integrationConfiguration,
            SimpleConfiguration connectedSystemConfiguration,
            PropertyPath propertyPath,
            ExecutionContext executionContext) {
        return integrationConfiguration.setProperties(
                // Make sure you make constants for all keys so that you can easily
                // access the values during execution
                textProperty(CONTACT_ID).label("Contact ID")
                        .isExpressionable(true)
                        .isRequired(true)
                        .description("ID of the contact to update")
                        .build(),
                textProperty(EMAIL).label("Email")
                        .isExpressionable(true)
                        .description("Email for the new contact")
                        .build(),
                textProperty(FIRST_NAME).label("First Name")
                        .isExpressionable(true)
                        .description("First name for the new contact")
                        .build(),
                textProperty(LAST_NAME).label("Last Name")
                        .isExpressionable(true)
                        .description("Last name for the new contact")
                        .build(),
                textProperty(WEBSITE).label("Website")
                        .isExpressionable(true)
                        .description("Website for the new contact")
                        .build(),
                textProperty(COMPANY).label("Company")
                        .isExpressionable(true)
                        .description("Company for the new contact")
                        .build(),
                textProperty(PHONE).label("Phone")
                        .isExpressionable(true)
                        .description("Phone number for the new contact")
                        .build(),
                textProperty(ADDRESS).label("Address")
                        .isExpressionable(true)
                        .description("Address for the new contact")
                        .build(),
                textProperty(CITY).label("City")
                        .isExpressionable(true)
                        .description("City")
                        .build(),
                textProperty(STATE).label("State")
                        .isExpressionable(true)
                        .description("State")
                        .build(),
                textProperty(ZIP).label("Zip")
                        .isExpressionable(true)
                        .description("Zip")
                        .build()

        );
    }

    @Override
    protected IntegrationResponse execute(
            SimpleConfiguration integrationConfiguration,
            SimpleConfiguration connectedSystemConfiguration,
            ExecutionContext executionContext) {

        //get variables required for webservice call
        String contactId = integrationConfiguration.getValue(CONTACT_ID);
        String baseUrl = BASE_URL + contactId + "/profile";
        String apiKey =connectedSystemConfiguration.getValue(
                HubspotConnectedSystem.API_KEY);
        String url = baseUrl + "?hapikey=" + apiKey;

        String email = integrationConfiguration.getValue(EMAIL);
        String firstname = integrationConfiguration.getValue(FIRST_NAME);
        String lastname = integrationConfiguration.getValue(LAST_NAME);
        String website = integrationConfiguration.getValue(WEBSITE);
        String company = integrationConfiguration.getValue(COMPANY);
        String phone = integrationConfiguration.getValue(PHONE);
        String address = integrationConfiguration.getValue(ADDRESS);
        String city = integrationConfiguration.getValue(CITY);
        String state = integrationConfiguration.getValue(STATE);
        String zip = integrationConfiguration.getValue(ZIP);

        Map<String,Object> requestDiagnostic = IntegrationExecutionUtils.getRequestDiagnostics(connectedSystemConfiguration);
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
        try{
            String json = new ObjectMapper().writeValueAsString(jsonMap);
            requestDiagnostic.put("JSON Request", json);
            requestDiagnostic.put("Base URL", baseUrl);
            return CallHttpRequest.executePostRequest(
                    url,
                    requestDiagnostic,
                    json
            );
        } catch (JsonProcessingException e) {
            final long start = System.currentTimeMillis();
            return IntegrationExecutionUtils.handleJsonProcessingException(
                    e,
                    requestDiagnostic,
                    start
            );
        }

    }
}
