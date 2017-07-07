<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<script type="text/javascript" src="webjars/jquery/3.2.0/jquery.min.js"></script>
	<script type="text/javascript" src="webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.9.3/js/bootstrap-select.min.js"></script>	
	<!-- Access the bootstrap Css like this,
		Spring boot will handle the resource mapping automcatically -->
	<link rel="stylesheet" type="text/css" href="webjars/bootstrap/3.3.7/css/bootstrap.min.css" />
	<!--<spring:url value="/css/main.css" var="springCss" />
	<link href="${springCss}" rel="stylesheet" />-->
	<link rel="stylesheet" type="text/css" media="screen" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.9.3/css/bootstrap-select.min.css">
	<c:url value="/css/main.css" var="jstlCss" />
	<link href="${jstlCss}" rel="stylesheet" />
	<c:url value="/css/jquery-ui.min.css" var="queryuimincss" />
	<link href="${queryuimincss}" rel="stylesheet" />
	<c:url value="/css/jquery-ui.structure.min.css" var="queryuistructurecss" />
	<link href="${queryuistructurecss}" rel="stylesheet" />
	<c:url value="/css/jquery-ui.theme.min.css" var="queryuithemecss" />
	<link href="${queryuithemecss}" rel="stylesheet" />
	<c:url value="/js/jquery-ui.min.js" var="queryuiminjs" />
	<script src="${queryuiminjs}"></script>
<div class="navbar navbar-inverse navbar-fixed-top">
  <div class="container">  
    <div class="navbar-header">                                   
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="#">Booking Calendar</a>
        </div>
        <div class="navbar-collapse collapse">  
          <ul class="nav navbar-nav">
            <li class="active"><a href="<c:url value="/"/>">Home</a></li>
            <li><a href="<c:url value="/booking" />">Booking Calendar</a></li>
            <li><a href="<c:url value="/bookingAdvanced" />">Booking Calendar Advanced</a></li>
          </ul>
        </div>		      		 
  </div>
</div>