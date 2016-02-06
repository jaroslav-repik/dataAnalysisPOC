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
		<h2>Step 2: Select columns for processing</h2>

		<c:url var="actionUrl" value="selectColumns?${_csrf.parameterName}=${_csrf.token}"/>
		<form id="fileuploadForm" action="${contextPath}/selectColumns?${_csrf.parameterName}=${_csrf.token}" method="POST" enctype="multipart/form-data" class="cleanform">
			<div class="header">
		  		<c:if test="${not empty message}">
					<div id="message" class="success">${message}</div>	  		
		  		</c:if>
			</div>

            <c:if test="${not empty tables}">
                <c:forEach items="${tables}" var="table">
                    <p>
                        <input type="checkbox" id="${table.name}" name="${table.name}" style="display:inline" onclick="toggleTable('${table.name}');" />&nbsp;<strong><c:out value="${table.name}" /></strong>
                        <br/>
                        <c:forEach items="${table.columns}" var="column">
                            &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" id="${table.name}|${column.name}" name="${table.name}|${column.name}" style="display:inline" />&nbsp;<c:out value="${column.name}" />
                            <br/>
                        </c:forEach>
                    </p>
                </c:forEach>
            </c:if>
            <br/>
            <button type="submit">Submit</button>
		</form>

	</div>
	<script>
	    function toggleTable(tableName) {
	        if (jQuery("#"+tableName).is(":checked")) {
                jQuery('input[type=checkbox]').each(function () {
                    var id = this.id;
                    if (id.startsWith(tableName+"|")) {
                        jQuery(this).prop('checked', true);
                    }
                });
	        } else {
	            jQuery('input[type=checkbox]').each(function () {
                    var id = this.id;
                    if (id.startsWith(tableName+"|")) {
                        jQuery(this).prop('checked', false);
                    }
                });
	        }
	    }
	</script>

<c:if test="${!ajaxRequest}">
</body>
</html>
</c:if>