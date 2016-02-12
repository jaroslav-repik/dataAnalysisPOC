<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:if test="${!ajaxRequest}">
<html>
<head>
	<title>Data Analysis POC</title>
	<link href="<c:url value="/resources/form.css" />" rel="stylesheet"  type="text/css" />		
	<script type="text/javascript" src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/jqueryform/2.8/jquery.form.js" />"></script>	
</head>
<body>
</c:if>
	<div id="selectColumnsDiv">
		<h1>Data Analysis POC</h1>
		<h2>Step 4: Database Export Result</h2>

		<form action="${contextPath}/export?${_csrf.parameterName}=${_csrf.token}" method="POST" class="cleanform">
			<div class="header">
		  		<c:if test="${not empty message}">
					<div id="message" class="success">${message}</div>	  		
		  		</c:if>
			</div>

            <c:forEach items="${dbMessages}" var="dbMessage">
            	<p>${dbMessage}</p>
            </c:forEach>
		</form>

	</div>
	<hr />
	<br />
	<small>Server processing time: ${timePassed} ms</small>
	<br/>
	<small>Total memory: ${totalMemory}</small>
	<br/>
	<small>Free memory: ${freeMemory}</small>
	<br/>
	<small>Used memory: ${usedMemory}</small>


<c:if test="${!ajaxRequest}">
</body>
</html>
</c:if>