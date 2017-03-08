var EnmDashboardTab = {
	profile : 1,
	message : 2,
	listing : 3,
	trip : 4,
	wishList : 5
};

for (var i = 1; i < 8; i++) {
	if($('#dashboardTab' + i).length > 0) {
		if (i == activeDashboardTab) {
			$('#dashboardTab' + i).attr('aria-selected', 'true');
		} else {
			$('#dashboardTab' + i).attr('aria-selected', 'false');
		}
	}
	
	if($('#dashboardLeftMenu' + i).length > 0) {
		if (i == activeLeftMenuLink) {
			$('#dashboardLeftMenu' + i).attr('aria-selected', 'true');
		} else {
			$('#dashboardLeftMenu' + i).attr('aria-selected', 'false');
		}
	}
}

