<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<%@ include file = "header.jsp" %>
<c:url value="/css/calendarbooking.css" var="calbookingcss" />
<link href="${calbookingcss}" rel="stylesheet" />
<c:url value="/js/calendarbooking.js" var="calbookingjs" />
<script src="${calbookingjs}"></script>
</head>
<body>
<div class="contentArea">
<div class="sectionBorder">
<div class="col-lg-12">
<form:form class="form-inline" id="calendarBookingForm" method="post" action="${pageContext.request.contextPath}/bookingResults">
<div class="col-lg-3"><label>Date <input class="form-control" type="text" id="datepicker" name="datepicker"></label></div>
<div class="col-lg-3"><label>Interval <input class="form-control" type="text" id="interval" name="interval"></label></div>
<div class="col-lg-3"><input class="btn btn-primary" type="submit" id="submit" value="Search"></input></div>
</form:form>
</div>
</div>
<div class="sectionBorder">
<div class="col-lg-12">
<div class="scrollableTable" id="calendarTable">
<table class="table table-inverse">
<c:forEach items="${bookingMap}" var="map">
<tr>
<td>${map.key}</td>
<c:forEach items="${map.value}" var="list">
<td>${list}</td>
</c:forEach>
</tr>
</c:forEach>
</table>
</div>
</div>
</div>
</div>
<%@ include file = "footer.jsp" %>
</body>   
</html>
   