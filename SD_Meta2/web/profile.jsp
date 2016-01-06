<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@	taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="description" content="">
	<meta name="author" content="">

	<title>FundStarter</title>

	<!-- Bootstrap Core CSS -->
	<link rel="stylesheet" href="css/bootstrap.css" type="text/css">

	<!-- Custom Fonts -->
	<link href='http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800' rel='stylesheet' type='text/css'>
	<link href='http://fonts.googleapis.com/css?family=Merriweather:400,300,300italic,400italic,700,700italic,900,900italic' rel='stylesheet' type='text/css'>
	<link rel="stylesheet" href="font-awesome/css/font-awesome.min.css" type="text/css">

	<!-- Plugin CSS -->
	<link rel="stylesheet" href="css/animate.min.css" type="text/css">

	<!-- Custom CSS -->
	<link rel="stylesheet" href="css/creative.css" type="text/css">

	<link rel="stylesheet" href="css/Home.css" type="text/css">
</head>

<body id="page-top">
<nav id="mainNav" class="navbar navbar-default navbar-fixed-top">
	<div class="container-fluid">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<form action="profile" method="post">
				<button style="background-color: rgba(0,0,0,0)" class="btn btn-primary navbar-brand page-scroll" type="submit">FUNDSTARTER</button>
			</form>
		</div>

		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav navbar-right">
				<div class="col-sm-4 col-md-4 pull-center">
					<form class="navbar-form" role="search" style="margin: 0px">
						<div class="form-control input-group">
							<input type="text" class="form-control" placeholder="Search" style="border: 0px" name="q">
                            <span class="input-group-btn" style="padding-left: 2px">
                                <button class="btn btn-default" type="submit"><i class="fa fa-search"></i></button>
                            </span>
						</div>
					</form>
				</div>
				<li>
					<a href="#" class="" type="button" id="dropdownnotifications" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true" onclick="saw('notifications')">
						Notifications
						<span  class="caret"></span>
						<ul id="notifications" class="dropdown-menu" aria-labelledby="dropdownnotifications">
							<li><a></a></li>
						</ul>
					</a>
				</li>
				<li>
					<a href="#" class="" type="button" id="dropdownmessages" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true" onclick="saw('messages')">
						Messages
						<span  class="caret"></span>
						<ul id="messages" class="dropdown-menu" aria-labelledby="dropdownmessages">
							<li><a></a></li>
						</ul>
					</a>
				</li>
				<li>
					<a class="page-scroll" href="#Aboutus">About us</a>
				</li>
				<li>
					<a data-toggle="modal" href="#Logout" style="margin-left: 10px; margin-right: 10px; height:50px">Profile</a>
				</li>
			</ul>
		</div>
	</div>
</nav>
<section id="services">
	<div class="container">
		<c:choose>
			<c:when test="${session.tipo == 'login'}">
				<h3>Successful login!</h3>
			</c:when>
			<c:when test="${session.tipo == 'regist'}">
				<h3>Successful registration!</h3>
			</c:when>
			<c:otherwise>

			</c:otherwise>
		</c:choose>
	</div>
	<div class="container">
		<div class="row">
			<div class="col-lg-12 text-center">
				<br><br>
				<br><br>
				<h2 class="section-heading">What do you want to do?</h2>
				<hr class="primary">
				<br><br>
				<br><br>
			</div>
		</div>
	</div>
	<div class="container">
		<div class="row">
			<div class="col-lg-3 col-md-3 text-center">
				<div class="service-box">
					<form action="projects">
						<s:hidden key="type" value="myprojects"/>
						<button class="btn btn-default" type="submit"><i class="fa fa-4x fa-unlock wow bounceIn" data-wow-delay=".1s"></i></button>
					</form>
					<h3>See My Projects</h3>
					<p class="text-muted">You can see all your projects!</p>
				</div>
			</div>
			<div class="col-lg-3 col-md-3 text-center">
				<div class="service-box">
					<form action="projects">
						<s:hidden key="type" value="actualprojects"/>
						<button class="btn btn-default" type="submit"><i class="fa fa-4x fa-search wow bounceIn" data-wow-delay=".1s"></i></button>
					</form>
					<h3>Actual Projects</h3>
					<p class="text-muted">You can see all the actual projects!</p>
				</div>
			</div>
			<div class="col-lg-3 col-md-3 text-center">
				<div class="service-box">
					<form action="projects">
						<s:hidden key="type" value="oldprojects"/>
						<button class="btn btn-default" type="submit"><i class="fa fa-4x fa-paper-plane wow bounceIn" data-wow-delay=".1s"></i></button>
					</form>
					<h3>Old Projects</h3>
					<p class="text-muted">You can see all the old projects!</p>
				</div>
			</div>
			<div class="col-lg-3 col-md-6 text-center">
				<div class="service-box">
					<!--<a href="index.html">-->
					<form action="newproject">
						<button class="btn btn-default" type="submit"><i class="fa fa-4x fa-newspaper-o wow bounceIn" data-wow-delay=".2s"></i></button>
					</form>
					<h3>Start a Project</h3>
					<p class="text-muted">You can start a project </p>
				</div>
			</div>
			<br><br>
			<div class="col-lg-3 col-md-3 text-center">
				<div class="service-box">
					<!--<a href="index.html">-->
					<form action="rewards">
						<button class="btn btn-default" type="submit"><i class="fa fa-4x fa-plus wow bounceIn" data-wow-delay=".2s"></i></button>
					</form>
					<h3>See your rewards</h3>
					<p class="text-muted">You can see your rewards </p>
				</div>
			</div>
		</div>
	</div>
</section>

<section id="Aboutus">
	<div class="container">
		<div class="row">
			<div class="col-lg-8 col-lg-offset-2 text-center">
				<h2 class="section-heading">Our Team:</h2>
				<hr class="primary">
				<p>Maria Filipa Rosa, nº 2012146116</p>
				<p>Miguel Ângelo Freitas, nº 2012142625</p>
				<hr>
				<p>Sistemas Distribuídos</p>
				<p>Licenciatura em Engenharia Informática</p>
			</div>

		</div>
	</div>
</section>



<!-- jQuery -->
<script src="js/jquery.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="js/bootstrap.min.js"></script>

<!-- Plugin JavaScript -->
<script src="js/jquery.easing.min.js"></script>
<script src="js/jquery.fittext.js"></script>
<script src="js/wow.min.js"></script>

<!-- Custom Theme JavaScript -->
<script src="js/creative.js"></script>

<script src="function.js"></script>

<%--<script type="text/javascript">
	window.onload = openSocketNotification();
	window.onload = openSocketMessage();
</script>--%>

<div class="modal" id="Logout">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">User Profile:</h4>
			</div>
			<div class="modal-body">
				<form style="text-align:left" class="login-block" method="post">
					<h3>${user.getUser().getUsername()}</h3>
					<br>
					<p>${user.getUser().getMail()}</p>
					<br>
					<p id="userMoney">Available Money: ${user.getUser().getMoney()}<p/>
				</form>
				<form action="logout" method="post">
					<button type="submit" class="btn btn-primary" method="execute">Log Out</button>
				</form>
			</div>
		</div>
	</div>
</div>
</body>
</html>