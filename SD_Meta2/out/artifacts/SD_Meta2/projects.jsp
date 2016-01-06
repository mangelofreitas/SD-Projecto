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
            <c:choose>
                <c:when test="${session.user == null}">
                    <form action="principal" method="post">
                        <button style="background-color: rgba(0,0,0,0)" class="btn btn-primary navbar-brand page-scroll" type="submit">FUNDSTARTER</button>
                    </form>
                </c:when>
                <c:otherwise>
                    <form action="profile" method="post">
                        <button style="background-color: rgba(0,0,0,0)" class="btn btn-primary navbar-brand page-scroll" type="submit">FUNDSTARTER</button>
                    </form>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <c:choose>
            <c:when test="${session.user == null}">

            </c:when>
            <c:otherwise>
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
            </c:otherwise>
        </c:choose>
    </div>

</nav>

<section id="services">
    <div class="container">
        <div class="row">
            <div class="text-left">
                <c:forEach items="${session.projects}" var="project">
                <c:choose>
                    <c:when test="${session.projectstype == 'myprojects'}">
                    <form action="cancel" method="post">
                        <input id="projectID" type="number" name="ProjectID"  value="${project.getProjectID()}" hidden>
                        <input class="btn btn-primary btn-xl" value="Cancel ${project.getProjectName()}" type="submit">
                    </form>
                    </c:when>
                    <c:otherwise>

                    </c:otherwise>
                </c:choose>
                <h2 class="section-heading">Administrator: <c:out value="${project.getUser().getUsername()}"/></h2>
                <br>
                <h2 class="section-heading">Project: <c:out value="${project.getProjectName()}"/></h2>
                <br>
                <h2 class="section-heading">Decription: <c:out value="${project.getDescription()}"/></h2>
                <br>
                <h2 class="section-heading">Deadline: <c:out value="${project.getDateLimit()}"/></h2>
                <br>
                <h2 class="section-heading">Requested Value:  <c:out value="${project.getRequestedValue()}"/></h2>
                <br>
                <h2 id="currentAmount_${project.getProjectID()}" class="section-heading">Current Amount: <c:out value="${project.getCurrentAmount()}"/></h2>
                <br>

                <h2 class="section-heading">Rewards: </h2>
                    <c:forEach items="${project.getRewards()}" var="rewards">
                        <div class="thumbnail" style="padding-left: 60px">
                        <c:choose>
                            <c:when test="${session.user==null}">
                                <h3 class="section-heading">Description:  <c:out value="${rewards.getDescription()}"/></h3>
                                <br>
                                <h3 class="section-heading">Value Of Reward:  <c:out value="${rewards.getValueOfReward()}"/></h3>
                                <br>
                            </c:when>
                            <c:when test="${session.projectstype == 'myprojects'}">
                                <form action="editreward" method="post">
                                <button class="btn btn-default thumb_reward" type="submit">
                                    <s:hidden key="type" value="remove"/>
                                    <input id="id" type="number" name="Id"  value="${rewards.getRewardID()}" hidden>
                                    <input id="projectid" type="number" name="Projectid"  value="${project.getProjectID()}" hidden>
                                    <h3 class="textreward"></h3>
                                    <h3 class="section-heading">Description:  <c:out value="${rewards.getDescription()}"/></h3>
                                    <br>
                                    <h3 class="section-heading">Value Of Reward:  <c:out value="${rewards.getValueOfReward()}"/></h3>
                                </button>
                                </form>
                            </c:when>
                            <c:when test="${session.projectstype == 'actualprojects'}">

                                <button data-toggle="modal" href="#donate" id="donatebutton" class="btn btn-default" onclick="donateMoney('${rewards.getDescription()}','${rewards.getValueOfReward()}','${project.getProductTypes()}',${project.getProjectID()})">
                                    <input id="id" type="number" name="Id"  value="${rewards.getRewardID()}" hidden>
                                    <input id="projectid" type="number" name="Projectid"  value="${project.getProjectID()}" hidden>
                                    <h3 class="donatemoney"></h3>
                                    <h3 class="section-heading">Description:  <c:out value="${rewards.getDescription()}"/></h3>
                                    <br>
                                    <h3 class="section-heading">Value Of Reward:  <c:out value="${rewards.getValueOfReward()}"/></h3>
                                    <br>
                                </button>
                            </c:when>
                            <c:otherwise>
                                    <h3 class="section-heading">Description:  <c:out value="${rewards.getDescription()}"/></h3>
                                    <br>
                                    <h3 class="section-heading">Value Of Reward:  <c:out value="${rewards.getValueOfReward()}"/></h3>
                                    <br>
                            </c:otherwise>
                        </c:choose>
                        </div>
                    </c:forEach>
                    <c:choose>
                        <c:when test="${session.projectstype == 'myprojects'}">
                            <div id="rwrd_${project.getProjectID()}">
                                <input type="button" id="rwrdbutton_${project.getProjectID()}" class="btn btn-primary" value="Add Reward" onclick="add_reward('rwrd_${project.getProjectID()}','rwrdbutton_${project.getProjectID()}',${project.getProjectID()});">
                            </div>
                            <br>
                        </c:when>
                    </c:choose>
                <br>
                <h2 class="section-heading">Product Types: </h2>
                    <c:forEach items="${project.getProductTypes()}" var="productTypes">
                        <div style="padding-left: 60px">
                        <h3 class="section-heading">Type:   <c:out value="${productTypes.getType()}"/></h3>
                        <br>
                        <h3 class="section-heading">Vote:   <c:out value="${productTypes.getVote()}"/></h3>
                        <br>
                        </div>
                    </c:forEach>
                    <c:choose>
                        <c:when test="${session.projectstype == 'myprojects'}">
                            <h2 class="section-heading">Messages: </h2>
                            <c:forEach items="${project.getMessages()}" var="message">
                            <div class="form-group">
                                <h4 id="message_${message.getMessageID()}" class="form-control input-lg" type="text">${message.getUser().getUsernameID()}:${message.getMessage()}</h4>
                            </div>
                                <c:forEach items="${message.getReplies()}" var="replie">
                                    <h5 style="padding-left: 50px" class="form-control input-lg" type="text">${replie.getUser().getUsernameID()}:${replie.getMessage()}</h5>
                                </c:forEach>
                                <form action="sendreply" method="post">
                                    <input id="projectID" type="number" name="ProjectID"  value="${project.getProjectID()}" hidden>
                                    <input id="messageID" type="number" name="MessageID" value="${message.getMessageID()}" hidden>
                                    <input type="text" id="message" name="Message" class="form-control" placeholder="Send a reply" required>
                                    <input class="btn btn-primary btn-xl" value="Send" type="submit" onclick="sendMessage('reply',${message.getMessageID()})">
                                </form>
                            </c:forEach>
                        </c:when>
                        <c:when test="${session.projectstype == 'actualprojects'}">
                            <h2 class="section-heading">Messages: </h2>
                            <c:forEach items="${project.getMessages()}" var="message">
                                <div class="form-group">
                                    <h4 class="form-control input-lg" type="text">${message.getUser().getUsernameID()}: ${message.getMessage()}</h4>
                                </div>
                                <c:forEach items="${message.getReplies()}" var="replie">
                                    <h5 id="reply_${replie.getReplyID()}" style="padding-left: 50px" class="form-control input-lg" type="text">${replie.getUser().getUsernameID()}: ${replie.getMessage()}</h5>
                                </c:forEach>
                            </c:forEach>
                            <c:choose>
                                <c:when test="${session.user == null}">

                                </c:when>
                                <c:otherwise>
                                    <form action="sendmessage" method="post">
                                        <input id="projectID" type="number" name="projectID"  value="${project.getProjectID()}" hidden>
                                        <input type="text" id="message" name="Message" class="form-control" placeholder="Send a message" required>
                                        <input class="btn btn-primary btn-xl" value="Send" type="submit" onclick="sendMessage('message',${project.getProjectID()})">
                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <hr>
                            <c:choose>
                                <c:when test="${project.getSuccess() == true}">
                                    <h2 style="color: green;" class="section-heading">Success!</h2>
                                </c:when>
                                <c:otherwise>
                                    <h2 style="color: darkred" class="section-heading">No success!</h2>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                <br><br>
                <br><br>
                </c:forEach>

                <hr>
                <c:choose>
                    <c:when test="${session.user == null}">
                        <form action="principal" method="post">
                            <input class="btn btn-primary btn-xl" type="submit" value="Back">
                        </form>
                    </c:when>
                    <c:otherwise>
                        <form action="profile" method="post">
                            <input class="btn btn-primary btn-xl" type="submit" value="Back">
                        </form>
                    </c:otherwise>
                </c:choose>

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
<div class="modal" id="donate">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Project Donation:</h4>
            </div>
            <div class="modal-body">
                <br>
                <p id="descriptiondonate"></p>
                <br>
                <p id="valuedonate"><p/>
                <br>
                <div id="donateptypes">
                </div>
                <div class="modal-footer">
                    <a href="#" data-dismiss="modal" class="btn">Close</a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
