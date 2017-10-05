/*
 * Fix sidebar at some point and remove
 * fixed position at content bottom
 */
$(window).scroll(
		function() {
			var fixSidebar = $('.with-photos').innerHeight();
			var contentHeight = $('.details-section').innerHeight();
			var sidebarHeight = $('.book-it__container').height();
			var summaryHeight = $('.room-section').height();
			var headerHeight = $('.nav-top').height();
			var trigger = $(window).scrollTop() - fixSidebar;
			var sidebarBottomPos = contentHeight - sidebarHeight
					+ summaryHeight - headerHeight;
			var $win = $(window);
			var winWidth = $win.width();

			if (winWidth < 1128) {
				return;
			} else {
				if ($(window).scrollTop() >= fixSidebar) {
					$('.book-it__container').addClass('fixed');
					$('.book-it__container').css('margin-top', '57px')
				} else {
					$('.book-it__container').removeClass('fixed');
					$('.book-it__container').css('margin-top', '0px')
				}

				if (trigger >= sidebarBottomPos) {
					$('.book-it__container').addClass('bottom');
					$('.book-it__container').css('padding-top',
							sidebarBottomPos)
				} else {
					$('.book-it__container').removeClass('bottom');
					$('.book-it__container').css('padding-top', 0)
				}
			}

		});