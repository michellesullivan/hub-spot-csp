package com.zimpatica.hubspot.templates;

import java.util.Map;

import com.appian.connectedsystems.simplified.sdk.SimpleIntegrationTemplate;
import com.appian.connectedsystems.simplified.sdk.configuration.SimpleConfiguration;
import com.appian.connectedsystems.templateframework.sdk.ExecutionContext;
import com.appian.connectedsystems.templateframework.sdk.IntegrationResponse;
import com.appian.connectedsystems.templateframework.sdk.TemplateId;
import com.appian.connectedsystems.templateframework.sdk.configuration.PropertyPath;
import com.appian.connectedsystems.templateframework.sdk.metadata.IntegrationTemplateRequestPolicy;
import com.appian.connectedsystems.templateframework.sdk.metadata.IntegrationTemplateType;

// Must provide an integration id. This value need only be unique for this connected system
@TemplateId(name="GetCompanies")
// Set template type to READ since this integration does not have side effects
@IntegrationTemplateType(IntegrationTemplateRequestPolicy.READ)
public class GetCompanies extends SimpleIntegrationTemplate {

    public static final String BASE_URL = "https://api.hubapi.com/companies/v2/companies/paged";
    public static final String COUNT = "count";
    public static final String OFFSET = "offset";
    @Override
    protected SimpleConfiguration getConfiguration(
            SimpleConfiguration integrationConfiguration,
            SimpleConfiguration connectedSystemConfiguration,
            PropertyPath propertyPath,
            ExecutionContext executionContext) {
        return integrationConfiguration.setProperties(
                textProperty(COUNT).label("Count")
                        .isExpressionable(true)
                        .description("Number of results to return. Default is 20, max is 100")
                        .build(),
                textProperty(OFFSET).label("Paging Offset")
                        .isExpressionable(true)
                        .description("Value returned from previous webservice call vid-offset to determine paging")
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
        String count = integrationConfiguration.getValue(COUNT);
        String offset = integrationConfiguration.getValue(OFFSET);
        String url = baseUrl + "?hapikey=" + apiKey + "&properties=name";
        if(count!=null){
            url = url + "&limit="+count;
        }
        if(offset!=null){
            url = url + "&offset="+offset;
        }


        //add request details to request diagnostic
        Map<String,Object> requestDiagnostic = IntegrationExecutionUtils.getRequestDiagnostics(connectedSystemConfiguration);
        requestDiagnostic.put("URL",baseUrl);

        return CallHttpRequest.executeGetRequest(
                url,
                requestDiagnostic
        );
    }
}
