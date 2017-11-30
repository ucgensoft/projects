package com.ucgen.letserasmus.web.filter;

import javax.servlet.ServletContext;

import org.ocpsoft.rewrite.annotation.RewriteConfiguration;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.Path;
import org.ocpsoft.rewrite.servlet.config.Redirect;
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
				.addRule(Join.path("/universities/{uniName}").to("/pages/universities/{uniName}.html"))
				.addRule(Join.path("/universities/{uniName}/").to("/pages/universities/{uniName}.html"))
				.addRule(Join.path("/universities").to("/pages/Universities.html"))
				.addRule(Join.path("/universities/").to("/pages/Universities.html"))
				.addRule(Join.path("/community").to("/pages/CommunityTopicList.html"))
				.addRule(Join.path("/community/{cityName}").to("/pages/CommunityTopicList.html?cityName={cityName}"));
				//.addRule().when(Path.matches("*/places/*")).perform(Redirect.permanent("www.letserasmus.com"))
				//.addRule().when(Path.matches("*/members/*")).perform(Redirect.permanent("www.letserasmus.com"));
	}

	@Override
	public int priority() {
		return 10;
	}

}
