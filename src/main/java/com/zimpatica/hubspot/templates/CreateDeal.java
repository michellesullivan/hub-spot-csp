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
import com.appian.connectedsystems.templateframework.sdk.configuration.SystemType;

//import com.
// Must provide an integration id. This value need only be unique for this connected system
@TemplateId(name="CreateDeal")
// Set template type to READ since this integration does not have side effects
@IntegrationTemplateType(IntegrationTemplateRequestPolicy.WRITE)
public class CreateDeal extends SimpleIntegrationTemplate {

    public static final String BASE_URL = "https://api.hubapi.com/deals/v1/deal";
    public static final String COMPANY_IDS = "companyIds";
    public static final String CONTACT_IDS = "contactIds";
    public static final String DEAL_NAME = "dealName";

    @Override
    protected SimpleConfiguration getConfiguration(
            SimpleConfiguration integrationConfiguration,
            SimpleConfiguration connectedSystemConfiguration,
            PropertyPath propertyPath,
            ExecutionContext executionContext) {
        return integrationConfiguration.setProperties(
                // Make sure you make constants for all keys so that you can easily
                // access the values during execution
                listTypeProperty(COMPANY_IDS)
                        .itemType(SystemType.STRING)
                        .isExpressionable(true)
                        .label("Company IDs")
                        .description("Company IDs associated with the deal")
                        .build(),
                listTypeProperty(CONTACT_IDS)
                        .isExpressionable(true)
                        .itemType(SystemType.STRING)
                        .label("Contact IDs").key(CONTACT_IDS)
                        .description("Contact IDs associated with the deal")
                        .build(),
                textProperty(DEAL_NAME).label("Deal Name")
                        .isExpressionable(true)
                        .description("Name of the deal to create")
                        .isRequired(true)
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

        List<String> companyIds = new ArrayList<>();
        List<String> contactIds = new ArrayList<>();

        List<String> companyIdsAppian = integrationConfiguration.getValue(COMPANY_IDS);
        List<String> contactIdsAppian = integrationConfiguration.getValue(CONTACT_IDS);
        String dealName = integrationConfiguration.getValue(DEAL_NAME);

        companyIds.add("3864887359");
        contactIds.add("1");

        Map<String,Object> requestDiagnostic = IntegrationExecutionUtils.getRequestDiagnostics(connectedSystemConfiguration);
        Map<String,Object> firstMap = new HashMap<>();
        Map<String,Object> associationsMap = new HashMap<>();

        associationsMap.put("associatedCompanyIds", companyIds);
        associationsMap.put("associatedVids", contactIds);
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("associations", associationsMap);

        firstMap.put("dealname", dealName);

        List<Map<String,Object>> list = new ArrayList<>();
        for(String key : firstMap.keySet()){
            Map<String,Object> propertiesMap = new HashMap<>();
            if(firstMap.get(key)!=null){
                propertiesMap.put("name", key);
                propertiesMap.put("value", firstMap.get(key));
                list.add(propertiesMap);
            }

        }
        jsonMap.put("properties", list);
        requestDiagnostic.put("companyIds", companyIds);
        requestDiagnostic.put("contactIds", contactIds);
        requestDiagnostic.put("appianCompanyIds", companyIdsAppian);
        requestDiagnostic.put("appianContactIdsssss", contactIdsAppian);
        try{
            String json = new ObjectMapper().writeValueAsString(jsonMap);
            requestDiagnostic.put("jsonRequest", json);
            requestDiagnostic.put("baseUrl", baseUrl);
            requestDiagnostic.put("companyIds", companyIds);
            requestDiagnostic.put("contactIds", contactIds);
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
