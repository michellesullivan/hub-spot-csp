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
@TemplateId(name="DeleteCompany")
// Set template type to READ since this integration does not have side effects
@IntegrationTemplateType(IntegrationTemplateRequestPolicy.WRITE)
public class DeleteCompany extends SimpleIntegrationTemplate {

    public static final String BASE_URL = "https://api.hubapi.com/companies/v2/companies/";
    public static final String COMPANY_ID = "companyId";
    @Override
    protected SimpleConfiguration getConfiguration(
            SimpleConfiguration integrationConfiguration,
            SimpleConfiguration connectedSystemConfiguration,
            PropertyPath propertyPath,
            ExecutionContext executionContext) {
        return integrationConfiguration.setProperties(
                textProperty(COMPANY_ID).label("Company ID")
                        .isExpressionable(true)
                        .description("Contact ID of the contact to delete")
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
        String companyId = integrationConfiguration.getValue(COMPANY_ID);
        String baseUrl = BASE_URL+companyId;

        String apiKey =connectedSystemConfiguration.getValue(
                HubspotConnectedSystem.API_KEY);

        String url = baseUrl +"?hapikey=" + apiKey;

        //add request details to request diagnostic
        Map<String,Object> requestDiagnostic = IntegrationExecutionUtils.getRequestDiagnostics(connectedSystemConfiguration);
        requestDiagnostic.put("URL",baseUrl);
        requestDiagnostic.put("Company ID", companyId);

        return CallHttpRequest.executeDeleteRequest(
                url,
                requestDiagnostic
        );
    }
}
