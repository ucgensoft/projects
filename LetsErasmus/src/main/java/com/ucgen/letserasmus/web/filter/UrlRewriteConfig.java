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
				.addRule(Join.path("/profile/{userId}").to("/pages/dashboard/DisplayUser.html?userId={userId}"))
				.addRule(Join.path("/profile/{userId}/").to("/pages/dashboard/DisplayUser.html?userId={userId}"))
				.addRule(Join.path("/room/search/{loc}/{locationId}").to("/pages/SearchResult.html?loc={loc}&locationId={locationId}"))
				.addRule(Join.path("/room/search/{loc}/{locationId}/").to("/pages/SearchResult.html?loc={loc}&locationId={locationId}"))
				.addRule(Join.path("/room/{placeId}").to("/pages/PlaceDetail.html?placeId={placeId}"))
				.addRule(Join.path("/room/{placeId}/").to("/pages/PlaceDetail.html?placeId={placeId}"))
				.addRule(Join.path("/help").to("/pages/help/help.html"))
				.addRule(Join.path("/help/").to("/pages/help/help.html"))
				.addRule(Join.path("/help/{groupKey}").to("/pages/help/help.html?groupKey={groupKey}"))
				.addRule(Join.path("/help/{groupKey}/").to("/pages/help/help.html?groupKey={groupKey}"))
				.addRule(Join.path("/about-us").to("/pages/AboutUs.html"))
				.addRule(Join.path("/about-us/").to("/pages/AboutUs.html"))
				.addRule(Join.path("/policies").to("/pages/Policies.html"))
				.addRule(Join.path("/policies/").to("/pages/Policies.html"))
				.addRule(Join.path("/contact-us").to("/pages/ContactUs.html"))
				.addRule(Join.path("/contact-us/").to("/pages/ContactUs.html"))
				.addRule(Join.path("/become-a-host").to("/pages/Place.html"))
				.addRule(Join.path("/become-a-host/").to("/pages/Place.html"))
				.addRule(Join.path("/dashboard/edit-profile").to("/pages/dashboard/EditUser.html"))
				.addRule(Join.path("/dashboard/edit-profile/").to("/pages/dashboard/EditUser.html"))
				.addRule(Join.path("/dashboard/trust-and-verification").to("/pages/dashboard/TrustAndVerification.html"))
				.addRule(Join.path("/dashboard/trust-and-verification/").to("/pages/dashboard/TrustAndVerification.html"))
				.addRule(Join.path("/dashboard/payout-methods").to("/pages/dashboard/PayoutMethods.html"))
				.addRule(Join.path("/dashboard/payout-methods/").to("/pages/dashboard/PayoutMethods.html"))				
				.addRule(Join.path("/dashboard/messages").to("/pages/dashboard/MessageList.html"))
				.addRule(Join.path("/dashboard/messages/").to("/pages/dashboard/MessageList.html"))
				.addRule(Join.path("/dashboard/conversation").to("/pages/dashboard/Conversation.html"))
				.addRule(Join.path("/dashboard/conversation/").to("/pages/dashboard/Conversation.html"))
				.addRule(Join.path("/dashboard/your-listings").to("/pages/dashboard/Listings.html"))
				.addRule(Join.path("/dashboard/your-listings/").to("/pages/dashboard/Listings.html"))
				.addRule(Join.path("/dashboard/reservation-list").to("/pages/dashboard/ReservationList.html"))
				.addRule(Join.path("/dashboard/reservation-list/").to("/pages/dashboard/ReservationList.html"))
				.addRule(Join.path("/dashboard/trip-list").to("/pages/dashboard/TripList.html"))
				.addRule(Join.path("/dashboard/trip-list/").to("/pages/dashboard/TripList.html"))
				.addRule(Join.path("/dashboard/wish-list").to("/pages/dashboard/WishList.html"))
				.addRule(Join.path("/dashboard/wish-list/").to("/pages/dashboard/WishList.html"))
				.addRule(Join.path("/universities/bilkent-university").to("/pages/universities/bilkent-university.html"))
				.addRule(Join.path("/universities/bilkent-university/").to("/pages/universities/bilkent-university.html"))
				.addRule(Join.path("/universities/bogazici-university").to("/pages/universities/bogazici-university.html"))
				.addRule(Join.path("/universities/bogazici-university/").to("/pages/universities/bogazici-university.html"))
				.addRule(Join.path("/universities/hacettepe-university").to("/pages/universities/hacettepe-university.html"))
				.addRule(Join.path("/universities/hacettepe-university/").to("/pages/universities/hacettepe-university.html"))
				.addRule(Join.path("/universities/imperial-college-london").to("/pages/universities/imperial-college-london.html"))
				.addRule(Join.path("/universities/imperial-college-london/").to("/pages/universities/imperial-college-london.html"))
				.addRule(Join.path("/universities/istanbul-technical-university").to("/pages/universities/istanbul-technical-university.html"))
				.addRule(Join.path("/universities/istanbul-technical-university/").to("/pages/universities/istanbul-technical-university.html"))	
				.addRule(Join.path("/universities/istanbul-university").to("/pages/universities/istanbul-university.html"))
				.addRule(Join.path("/universities/istanbul-university/").to("/pages/universities/istanbul-university.html"))
				.addRule(Join.path("/universities/koc-university").to("/pages/universities/koc-university.html"))
				.addRule(Join.path("/universities/koc-university/").to("/pages/universities/koc-university.html"))
				.addRule(Join.path("/universities/middle-east-technical-university").to("/pages/universities/middle-east-technical-university.html"))
				.addRule(Join.path("/universities/middle-east-technical-university/").to("/pages/universities/middle-east-technical-university.html"))
				.addRule(Join.path("/universities/sabanci-university").to("/pages/universities/sabanci-university.html"))
				.addRule(Join.path("/universities/sabanci-university/").to("/pages/universities/sabanci-university.html"))
				.addRule(Join.path("/universities/ucl-university-college-london").to("/pages/universities/ucl-university-college-london.html"))
				.addRule(Join.path("/universities/ucl-university-college-london/").to("/pages/universities/ucl-university-college-london.html"))
				.addRule(Join.path("/universities/universidad-autonoma-de-madrid").to("/pages/universities/universidad-autonoma-de-madrid.html"))
				.addRule(Join.path("/universities/universidad-autonoma-de-madrid/").to("/pages/universities/universidad-autonoma-de-madrid.html"))
				.addRule(Join.path("/universities/universitat-autonoma-de-barcelona").to("/pages/universities/universitat-autonoma-de-barcelona.html"))
				.addRule(Join.path("/universities/universitat-autonoma-de-barcelona/").to("/pages/universities/universitat-autonoma-de-barcelona.html"))
				.addRule(Join.path("/universities/universitat-de-barcelona").to("/pages/universities/universitat-de-barcelona.html"))
				.addRule(Join.path("/universities/universitat-de-barcelona/").to("/pages/universities/universitat-de-barcelona.html"))
				.addRule(Join.path("/universities/university-cambridge").to("/pages/universities/university-cambridge.html"))
				.addRule(Join.path("/universities/university-cambridge/").to("/pages/universities/university-cambridge.html"))
				.addRule(Join.path("/universities/university-complutense-madrid").to("/pages/universities/university-complutense-madrid.html"))
				.addRule(Join.path("/universities/university-complutense-madrid/").to("/pages/universities/university-complutense-madrid.html"))
				.addRule(Join.path("/universities/university-oxford").to("/pages/universities/university-oxford.html"))
				.addRule(Join.path("/universities/university-oxford/").to("/pages/universities/university-oxford.html"));
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
