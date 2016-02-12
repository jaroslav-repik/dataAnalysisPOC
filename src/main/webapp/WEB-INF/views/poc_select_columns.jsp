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
		<h2>Step 2: Select columns for processing</h2>

		<form action="${contextPath}/createReport?${_csrf.parameterName}=${_csrf.token}" method="POST" class="cleanform">
			<div class="header">
		  		<c:if test="${not empty message}">
					<div id="message" class="success">${message}</div>	  		
		  		</c:if>
			</div>

            <c:if test="${not empty tables}">
                <c:forEach items="${tables}" var="table">
                    <p>
                        <input type="checkbox" checked id="${table.name}" name="${table.name}" style="display:inline" onclick="toggleTable('${table.name}');" />&nbsp;<strong><c:out value="${table.name}" /></strong>
                        <br/>
                        <c:forEach items="${table.columns}" var="column">
                            <div style="padding-left:10px; margin:0px;">
                                <input type="checkbox" checked id="${table.name}|${column.name}" name="DATA_${table.name}|${column.name}" style="margin-bottom:3px; display:inline" />&nbsp;<c:out value="${column.name}" />
                            </div>
                        </c:forEach>
                    </p>
                </c:forEach>
            </c:if>
            <br/>
            <button type="submit">Submit</button>
		</form>
		<hr />
		<br />
		<small>Server processing time: ${timePassed} ms</small>
		<br/>
		<small>Total memory: ${totalMemory}</small>
		<br/>
        <small>Free memory: ${freeMemory}</small>
        <br/>
        <small>Used memory: ${usedMemory}</small>


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