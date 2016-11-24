<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Lets Erasmus</title>
<meta property="description"
	content="The international student housing platform for exchange and internship students ">

<meta http-equiv="X-UA-Compatible" content="IE=edge">

<meta property="fb:app_id" content="410300212483493">
<meta property="og:site_name" content="LetsErasmus">
<meta property="og:type" content="letserasmus:listing">
<meta property="og:url" content="https://letserasmus.com/">
<meta property="og:title" content="Lets Erasmus">
<meta property="og:description"
	content="The international student housing platform for exchange and internship students ">

<link rel="icon" href="/LetsErasmus/static/site_icon_32.jpg">

<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

<link rel="stylesheet" type="text/css" href="/LetsErasmus/static/css/main/main.css">
	
<script src="/LetsErasmus/static/js/Common.js"></script>
<script src="/LetsErasmus/static/js/App.js"></script>
<script src="/LetsErasmus/static/js/Services/CommentService.js"></script>
<!--<script src="/LetsErasmus/static/js/ga.js"></script> -->
<script src="/LetsErasmus/static/js/controller/MainController.js"></script>
	
<body ng-app="myApp" id="index"
	style="background-image: url(/LetsErasmus/static/images/home.jpg); background-position: center center; background-repeat: no-repeat; background-attachment: fixed; background-size: cover; padding-top: 0px; padding-bottom: 0px;""
	ng-controller="MainCtrl as ctrl">	
	<div id="lightningjs-wootric" style="display: none;">
		<div>
			<iframe frameborder="0" id="lightningjs-frame-wootric"
				src=""></iframe>
		</div>
	</div>

	<header id="top" class="nav-top nav-top__home">
		<a href="https://letserasmus.com/" class="nav-top--logo"
			data-event="Clicked Logo in header" data-type="icon"> <svg
				class="svg-logo">
	      			
		    </svg>
		</a>
		<nav id="n-l-nav" class="nav-top--right-wrapper">
			<nav class="nav-top--right" data-reactid=".0">
				<div class="nav-top--right" data-reactid=".0.0">
					<a id="a-register"
						title="Register to book or list a room or pick a tenant" href=""
						class="nav-top--right--link hidden-sm-down" data-reactid=".0.0.0">
						<span data-reactid=".0.0.0.0">Sign up</span>
					</a><a id="a-login"
						title="Login to see new messages and potential tenants" href=""
						class="nav-top--right--link hidden-sm-down m-r-1"
						data-reactid=".0.0.1"><span data-reactid=".0.0.1.0">
							Log in </span></a>
				</div>
				<div class="nav-top--right" data-reactid=".0.1">
					<a class="nav-top--list-room-btn"
						href="https://letserasmus.com/list-your-place" role="button"
						id="btnSubmitRoom" data-reactid=".0.1.0"><span
						class="hidden-sm-down" data-reactid=".0.1.0.0">List your
							place for free</span><span class="hidden-md-up uppercase"
						data-reactid=".0.1.0.1">Rent out</span></a>
				</div>
				<select class="nav-top--language" data-reactid=".0.2"><option
						selected="" value="en" disabled="" data-reactid=".0.2.$en">English</option>
					<option value="fr" data-reactid=".0.2.$fr">Français</option>
					<option value="de" data-reactid=".0.2.$de">Deutsch</option>
					<option value="es" data-reactid=".0.2.$es">Español</option>
					<option value="it" data-reactid=".0.2.$it">Italiano</option>
					<option value="nl" data-reactid=".0.2.$nl">Nederlands</option>
					<option value="pt" data-reactid=".0.2.$pt">Português</option>
				</select>
			</nav>
		</nav>
	</header>
	<div id="MobileNav">
		<div class="nav-mobile" data-reactid=".2">
			<button class="nav-mobile__toggle" data-reactid=".2.0">
				<i class="fa fa-bars fa-1x" data-reactid=".2.0.0"></i>
			</button>
			<div class="nav-mobile__overflow" data-reactid=".2.1"></div>
			<div class="nav-mobile-wrapper" data-reactid=".2.2">
				<div class="nav-mobile-row" data-reactid=".2.2.0">
					<div class="nav-mobile-login-buttons" data-reactid=".2.2.0.0">
						<div class="row" data-reactid=".2.2.0.0.0">
							<div class="col-xs-12 p-r-0_5" data-reactid=".2.2.0.0.0.0">
								<a
									href="https://letserasmus.com/register?return_url=https%3A%2F%2Fletserasmus.com%2F"
									class="btn btn-default btn-wrap display-block btn-lg uppercase"
									title="Sign up" data-reactid=".2.2.0.0.0.0.0">Sign up</a>
							</div>
							<div class="col-xs-12 p-l-0_5" data-reactid=".2.2.0.0.0.1">
								<a
									href="https://letserasmus.com/login?return_url=https%3A%2F%2Fletserasmus.com%2F"
									class="btn btn-grey-outline btn-block btn-lg uppercase"
									title="Log in" data-reactid=".2.2.0.0.0.1.0">Log in</a>
							</div>
						</div>
					</div>
				</div>
				<div class="nav-mobile-row" data-reactid=".2.2.1">
					<div class="m-y-1" data-reactid=".2.2.1.1">
						<select class="form-dark c-select" data-reactid=".2.2.1.1.0"><option
								selected="" value="en" disabled="" data-reactid=".2.2.1.1.0.$en">English</option>
							<option value="fr" data-reactid=".2.2.1.1.0.$fr">Français</option>
							<option value="de" data-reactid=".2.2.1.1.0.$de">Deutsch</option>
							<option value="es" data-reactid=".2.2.1.1.0.$es">Español</option>
							<option value="it" data-reactid=".2.2.1.1.0.$it">Italiano</option>
							<option value="nl" data-reactid=".2.2.1.1.0.$nl">Nederlands</option>
							<option value="pt" data-reactid=".2.2.1.1.0.$pt">Português</option>
						</select>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="home-first-screen" style="height: 762.45px;">

		<div class="flex-grow-1 flex-vertical-middle">
			<div style="margin: auto; background-color: rgba(0, 0, 0, 0.5); border-radius: 10px;">
				<h1 class="landing-h1">Where will you stay next semester ?</h1>
				<h3 class="landing-h2">Rent your new home from locals online</h3>
				<div>
					<form action="https://letserasmus.com/search" method="post"
						class="staticForm home--search">
						<input type="hidden" name="csrf_token"
							value="Q78stubbZPfb/t5v4mVk0ThClVTVEYqYmUHTi7ERvy3goqfBwKS5/E7R0p18m//483P05Onfr7utiOlESGYYdg==">
						<div class="formErrors" style="display: none;"></div>
						<input autofocus="autofocus" id="txtCity"
							class="geo-autocomplete-city home--search--input form-control"
							type="text" name="city" placeholder="Where will you go?"
							data-event="Selected search field" data-type="field"
							autocomplete="off" />
						<div id="HomeSearchDates" style="margin-left: 4px">
							<div class="home-search--dates" data-reactid=".5">
								<input type="text" class="form-control" placeholder="Start date"
									value="" readonly="" data-reactid=".5.0" />
								<input type="text"
									class="form-control" placeholder="End date" value=""
									readonly="" data-reactid=".5.1" style="margin-left: 4px;" />
								<noscript data-reactid=".5.2"></noscript>
								<input type="hidden" name="startDate" value=""
									data-reactid=".5.3" />
								<input type="hidden" name="endDate"
									value="" data-reactid=".5.4" />
							</div>
						</div>
						<input class="btn-brand home--search--btn" id="btnSearch"
							type="submit" value="search &amp; book"
							data-event="Clicked Search button" data-type="button">
					</form>
				</div>
			</div>
		</div>
		<div class="landing-numbers"
			style="background-color: rgba(0, 0, 0, 0.5); border-radius: 10px;">
			<div>
				<div class="landing-numbers-h1">100591</div>
				<div class="landing-numbers-h2">Places to rent</div>
			</div>
			<div>
				<div class="landing-numbers-h1">53</div>
				<div class="landing-numbers-h2">countries</div>
			</div>
			<div>
				<div class="landing-numbers-h1">482</div>
				<div class="landing-numbers-h2">cities</div>
			</div>
		</div>
	</div>
	
</body>
</html>