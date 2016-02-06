<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:if test="${!ajaxRequest}">
<html>
<head>
	<title>fileupload | mvc-showcase</title>
	<link href="<c:url value="/resources/form.css" />" rel="stylesheet"  type="text/css" />		
	<script type="text/javascript" src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/jqueryform/2.8/jquery.form.js" />"></script>	
</head>
<body>
</c:if>
	<div id="selectColumnsDiv">
		<h1>Data Analysis POC</h1>
		<h2>Step 3: Data Report</h2>

		<form action="${contextPath}/export?${_csrf.parameterName}=${_csrf.token}" method="POST" class="cleanform">
			<div class="header">
		  		<c:if test="${not empty message}">
					<div id="message" class="success">${message}</div>	  		
		  		</c:if>
			</div>

            <table border="1">
                <tr><th>Table</th><th>Column</th><th>Records Total</th><th>Records Unique</th></tr>
                <c:if test="${not empty tables}">
                    <c:forEach items="${tables}" var="table">
                        <tr><td>${table.name}</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
                        <c:forEach items="${table.columns}" var="column">
                            <tr><td>&nbsp;</td><td>${column.name}</td><td>${column.recordsTotal}</td><td>${column.recordsUnique}</td></tr>
                        </c:forEach>
                    </c:forEach>
                </c:if>
            </table>
            <br />
            <button type="submit">Export to DB</button>
		</form>

	</div>


<c:if test="${!ajaxRequest}">
</body>
</html>
</c:if>