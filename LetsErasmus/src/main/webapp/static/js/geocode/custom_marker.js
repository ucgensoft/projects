function CustomMarker(latlng, map, args) {
	this.latlng = latlng;
	this.args = args;
	this.setMap(map);
	this.animationInterval = null;
}

CustomMarker.prototype = new google.maps.OverlayView();

CustomMarker.prototype.draw = function() {

	var self = this;

	var div = this.div;

	if (!div) {

		div = document.getElementById(self.args.marker_element_id);

		var hiddenMarker = new google.maps.Marker({
			icon : ' ',
			map : self.map,
			position : self.latlng
		});
		self.marker = hiddenMarker;
		google.maps.event.addDomListener(div, "click", function(event) {
			var divInfo = document.getElementById('articleProduct_' + 1);
			//var divWindow = document.getElementById('divPlaceInfoWindow');
			self.args.infoWindow.setContent(divInfo.outerHTML);
			self.args.infoWindow.open(self.map, hiddenMarker);
		});

		var panes = this.getPanes();
		panes.overlayImage.appendChild(div);
		this.div = div;
	}

	var point = this.getProjection().fromLatLngToDivPixel(this.latlng);

	if (point) {
		div.style.left = (point.x - 21) + 'px';
		div.style.top = (point.y - 26) + 'px';
	}
};

CustomMarker.prototype.remove = function() {
	if (this.div) {
		this.div.parentNode.removeChild(this.div);
		this.div = null;
	}
};

CustomMarker.prototype.getPosition = function() {
	return this.latlng;
};

CustomMarker.prototype.bounce = function(flag) {
	var self = this;
	if (flag) {
		if (this.animationInterval == null) {
			$(self.div).effect( "bounce", { mode: "effect", times: 3, distance: 20 }, "slow" );
			this.animationInterval = setInterval(function(){ 
				$(self.div).effect( "bounce", { mode: "effect", times: 3, distance: 20 }, "slow" );
			}, 500);
		}
	} else {
		if (this.animationInterval != null) {
			clearInterval(this.animationInterval);
			this.animationInterval = null;
		}
	}	
};