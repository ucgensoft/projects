package com.ucgen.letserasmus.web.filter;

import javax.servlet.ServletContext;

import org.ocpsoft.rewrite.annotation.RewriteConfiguration;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.rule.Join;

@RewriteConfiguration
public class UrlRewriteConfig extends HttpConfigurationProvider {
		
	@Override
	public Configuration getConfiguration(ServletContext context) {
		return ConfigurationBuilder.begin()
		
				// A basic join
				.addRule(Join.path("/room/search/{loc}/{locationId}").to("/pages/SearchResult.html?loc={loc}&locationId={locationId}"))
				.addRule(Join.path("/room/search/{loc}/{locationId}/").to("/pages/SearchResult.html?loc={loc}&locationId={locationId}"))
				.addRule(Join.path("/room/{cityName}/{placeId}").to("/pages/PlaceDetail.html?placeId={placeId}"))
				.addRule(Join.path("/room/{cityName}/{placeId}/").to("/pages/PlaceDetail.html?placeId={placeId}"))
				.addRule(Join.path("/profile/{userId}").to("/pages/dashboard/DisplayUser.html?userId={userId}"))
				.addRule(Join.path("/profile/{userId}/").to("/pages/dashboard/DisplayUser.html?userId={userId}"))
				.addRule(Join.path("/help").to("/pages/help/help.html"))
				.addRule(Join.path("/help/").to("/pages/help/help.html"))
				.addRule(Join.path("/help/{groupKey}").to("/pages/help/help.html?groupKey={groupKey}"))
				.addRule(Join.path("/help/{groupKey}/").to("/pages/help/help.html?groupKey={groupKey}"));
		
				/*
				// Using parameters to return physical resources
				.addRule(Join.path("/{param}").to("/pages/{param}.html"))

				// Using parameterization (the value of 'p' is converted to a
				// request parameter)
				.addRule(Join.path("/project/{p}").to("/pages/project/create.xhtml"))

				// Redirect requests to the server-side resource to the correct
				// location
				.addRule(Join.path("/signup").to("/pages/signup.xhtml").withInboundCorrection());
				*/
	}

	@Override
	public int priority() {
		return 10;
	}

}
