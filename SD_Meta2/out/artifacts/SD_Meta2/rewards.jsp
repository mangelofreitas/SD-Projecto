<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
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

    <link rel="stylesheet" href="css/Project.css" type="text/css">

    <link rel="stylesheet" href="css/bootstrap_overwrite.css" type="text/css">


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
        <div class="row">
            <div class="text-left">
                <c:forEach items="${session.rewards}" var="reward">
                    <h2 class="section-heading">Description: <c:out value="${reward.getDescription()}"/></h2>
                    <h2 class="section-heading">Value of Reward: <c:out value="${reward.getValueOfReward()}"/></h2>
                </c:forEach>

                <hr>
                <form action="profile" method="post">
                    <input class="btn btn-primary btn-xl" type="submit" value="Back">
                </form>
            </div>
        </div>
    </div>

    <%--<div class="container">
        <div class="row">
            <div class="col-lg-offset-3 col-lg-6 col-md-offset-5 col-md-2 text-center">
                <br><br>
                <br><br>
                <a href="#" class="btn btn-primary btn-xl">Cancel Project</a>
                <a href="#" class="btn btn-primary btn-xl">Add Reward</a>
                <a href="#" class="btn btn-primary btn-xl">Remove Reward</a>
            </div>
        </div>
    </div>--%>
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
                <br>
                <p>${user.getUser().getUsername()}</p>
                <br>
                <p>${user.getUser().getMail()}</p>
                <br>
                <p id="userMoney">Available Money: ${user.getUser().getMoney()}<p/>
                <div class="modal-footer">
                    <form action="logout" method="post">
                        <button type="submit" class="btn btn-primary" method="execute">Log Out</button>
                    </form>
                    <a href="#" data-dismiss="modal" class="btn">Close</a>
                </div>
            </div>

        </div>
    </div>
</div>
</body>
</html>
