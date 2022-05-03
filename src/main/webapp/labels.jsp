<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page import ="java.util.ArrayList"%>
<%@ page import ="java.util.List"%>
<%@ page import = "com.example.Entity.AnnotationEntity" %>

<!DOCTYPE html>
<html>
<head>
  <script async src="https://www.googletagmanager.com/gtag/js?id=UA-196175239-1%22%3E"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());

  gtag('config', 'UA-196175239-1');
</script> 

<meta charset="ISO-8859-1">
<title>Caption Generator</title>
<style type="text/css">
	.div-1 {
	        background-color:#4682b4;
	        opacity: 0.5;
	    } 
	    .div-2 {
	        background-color:#add8e6;
	    } 
</style>
</head>
<body>
<div align="center">
<div class="div-1" align="center" >
	<h1>Caption Generator Application</h1>
</div>
<table>
	<tr>
		<td>
			<img alt="" src="<%= request.getAttribute("ImageUrl") %>" width="450" height = "350">
			
		</td>
		<td>
			<table width="500" align="left-center" bordercolor = "#000000" border = "6">
		         <tr bgcolor="#808080" align="center">
		          <th><b>Label</b></th>
		          <th><b>Percentage</b></th>
		         </tr>
		         <% ArrayList<AnnotationEntity> labelList = new ArrayList<AnnotationEntity>();
		         labelList=(ArrayList<AnnotationEntity>)request.getAttribute("labelList");
		         for(AnnotationEntity l:labelList){%>
		          <tr bgcolor="#C0C0C0" align="center">
		                <td><%=l.getLabel() %></td>
		                <td><%=l.getScore() %></td>
		                
		          </tr>
		            <%}%>
 			</table>
 			
		</td>
	</tr>
	
</table>
<table>
<tr><td> <div class="div-2">
			<h3>Caption:</h3>
			<div><%= request.getAttribute("ImageStory") %></div>
		
			</div></td></tr>
</table>

</div>    
</body>
</html>