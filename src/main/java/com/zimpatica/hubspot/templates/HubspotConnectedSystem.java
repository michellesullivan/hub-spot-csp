package com.zimpatica.hubspot.templates;

import com.appian.connectedsystems.simplified.sdk.SimpleConnectedSystemTemplate;
import com.appian.connectedsystems.simplified.sdk.configuration.SimpleConfiguration;
import com.appian.connectedsystems.templateframework.sdk.ExecutionContext;
import com.appian.connectedsystems.templateframework.sdk.TemplateId;

@TemplateId(name="HubspotConnectedSystem")
public class HubspotConnectedSystem extends SimpleConnectedSystemTemplate {

  public static final String API_KEY = "apiKey";

  @Override
  protected SimpleConfiguration getConfiguration(
      SimpleConfiguration simpleConfiguration, ExecutionContext executionContext) {

    return simpleConfiguration.setProperties(
        // Make sure you make public constants for all keys so that associated
        // integrations can easily access this field
        encryptedTextProperty(API_KEY).label("API Key").isRequired(true).isImportCustomizable(true).build()

    );
  }
}
