<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
		 pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
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
			<a class="navbar-brand page-scroll" href="#page-top">FUNDSTARTER</a>
		</div>

		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav navbar-right">

				<li>
					<a class="page-scroll" href="#Aboutus">About us</a>
				</li>
				<li>
					<a data-toggle="modal" href="#Signup" style="margin-left: 10px; margin-right: 10px; height:50px">Sign in</a>
				</li>
				<li>
					<a data-toggle="modal" href="#Login" style="margin-left: 10px; margin-right: 10px; height:50px">Log in</a>
				</li>
			</ul>
		</div>
	</div>
</nav>

<header>
	<div class="header-content">
		<div class="header-content-inner">
			<h1>If you have an idea, start a project in Fundstarter!</h1>
			<hr>
			<form action="projects">
				<s:hidden key="type" value="actualprojects"/>
				<br><br>
				<button type="submit" class="btn btn-primary btn-xl">See all the new projects</button>
			</form>
			<form action="projects">
				<s:hidden key="type" value="oldprojects"/>
				<br><br>
				<button type="submit" class="btn btn-primary btn-xl">See all the old projects</button>
			</form>
		</div>
	</div>
</header>

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

<!-- /.navbar-collapse -->


<div class="modal" id="Signup">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">Sign In</h4>
			</div>
			<div class="modal-body">
				</a>
				<br>
				<form action="regist" style="text-align:center" class="login-block" method="POST">
					<input id="username" type="text" class="form-control" placeholder="Username" name="Username" required/><br>
					<input id="mail" type="text" class="form-control" placeholder="Email" name="Mail" required/><br>
					<input id="password" type="password" class="form-control" placeholder="Password" name="Password" required/><br>
					<input type="submit" class="btn btn-primary" method="execute" value="Sign in">
				</form>
				<div class="modal-footer">
					<a href="#" data-dismiss="modal" class="btn">Close</a>
				</div>
			</div>

		</div>
	</div>
</div>

<div class="modal" id="Login">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">Log in</h4>
			</div>
			<div class="modal-body">
				</a>
				<form action="login" method="post">
					<input id="loginType" type="text" value="tumblr" name="LoginType" hidden=""/>
					<button type="submit" method="execute" class="btn btn-block btn-primary btn-tumblr">
						<i class="fa fa-tumblr"></i> Log in with Tumblr
					</button>
				</form>

				<br>
				<form action="login" style="text-align:center" class="login-block" method="post">
					<input id="mail" type="text" class="form-control" placeholder="Email" name="Mail" required/><br>
					<input id="password" type="password" class="form-control" placeholder="Password" name="Password" required/><br>
					<input type="submit" class="btn btn-primary" method="execute" value="Log in">
				</form>
				<div class="modal-footer">
					<a href="#" data-dismiss="modal" class="btn">Close</a>
				</div>
			</div>

		</div>
	</div>
</div>

</body>
</html>