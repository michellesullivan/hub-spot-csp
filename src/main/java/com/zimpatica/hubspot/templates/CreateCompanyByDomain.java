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
@TemplateId(name="CreateCompanyByDomain")
// Set template type to READ since this integration does not have side effects
@IntegrationTemplateType(IntegrationTemplateRequestPolicy.WRITE)
public class CreateCompanyByDomain extends SimpleIntegrationTemplate {

    public static final String BASE_URL = "https://api.hubapi.com/companies/v2/companies";
    public static final String DOMAIN = "domain";

    @Override
    protected SimpleConfiguration getConfiguration(
            SimpleConfiguration integrationConfiguration,
            SimpleConfiguration connectedSystemConfiguration,
            PropertyPath propertyPath,
            ExecutionContext executionContext) {
        return integrationConfiguration.setProperties(
                // Make sure you make constants for all keys so that you can easily
                // access the values during execution
                textProperty(DOMAIN).label("Domain")
                        .isRequired(true)
                        .isExpressionable(true)
                        .description("Company website")
                        .build()
        );
    }

    @Override
    protected IntegrationResponse execute(
            SimpleConfiguration integrationConfiguration,
            SimpleConfiguration connectedSystemConfiguration,
            ExecutionContext executionContext) {

        //get variables required for webservice call
        String baseUrl = BASE_URL;
        String apiKey =connectedSystemConfiguration.getValue(
                HubspotConnectedSystem.API_KEY);
        String url = baseUrl + "?hapikey=" + apiKey;

        String domain = integrationConfiguration.getValue(DOMAIN);

        Map<String,Object> requestDiagnostic = IntegrationExecutionUtils.getRequestDiagnostics(connectedSystemConfiguration);
        requestDiagnostic.put("Base URL", baseUrl);
        requestDiagnostic.put("Domain", domain);
        Map<String,Object> firstMap = new HashMap<>();
        firstMap.put("domain", domain);

        List<Map<String,Object>> list = new ArrayList<>();
        for(String key : firstMap.keySet()){
            Map<String,Object> propertiesMap = new HashMap<>();
            propertiesMap.put("name", key);
            propertiesMap.put("value", firstMap.get(key));
            list.add(propertiesMap);
        }
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("properties", list);
        try{
            String json = new ObjectMapper().writeValueAsString(jsonMap);
            requestDiagnostic.put("JSON Request", json);

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
