<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page import ="java.util.ArrayList"%>
<%@ page import ="java.util.List"%>
<%@ page import = "com.example.Entity.AnnotationEntity" %>

<!DOCTYPE html>
<html>
<head>
<script>
	window.fbAsyncInit = function() {
		FB.init({
			appId      : '1134177110766361',
			xfbml      : true,
			version    : 'v13.0'
		});
		FB.AppEvents.logPageView();
	};
	(function(d, s, id){
		var js, fjs = d.getElementsByTagName(s)[0];
		if (d.getElementById(id)) {return;}
		js = d.createElement(s); js.id = id;
		js.src = "https://connect.facebook.net/en_US/sdk.js";
		fjs.parentNode.insertBefore(js, fjs);
	}(document, 'script', 'facebook-jssdk'));
</script>
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
 <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
<script type="text/javascript">
	function homepage(){
		window.location.href = "/";
	}
	
      function myFunction(){
    	  console.log("inside myFunction labels.jsp");
		FB.ui({
			display: 'popup',
			method: 'share',
			href: '<%=request.getAttribute("ImageUrl")%>',
			//hashtag:'${ImageStory}',
			quote:'${ImageStory}',
		}, function(response){
			if(response && !response.error) {
				document.getElementById('alert').style.display='block';
				setInterval(homepage, 4000);
			}
		});
      }
</script>
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
	    .button {
  border: none;
  color: white;
  padding: 15px 32px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  margin: 4px 2px;
  cursor: pointer;
}

.button1 {background-color: #4CAF50;}
.btn-success {
			background-color: #0062cc;
			border-color: #0062cc;
		}
</style>
</head>
<body>




<!-- twitter script -->
<script>
$(document).ready(function() {
   //Change quote on page
   $('#quoteBtn').click(function() {
      $('h3').html('Here is a new quote.');
   });
   
   //Change twitter content to share to new quote
   $('#quoteBtn').click(function(val) {
    val.preventDefault();
    
      // Remove existing iframe
    $('#tweet iframe').remove();
    
      //For generic button
    var newTweet = $('<a></a>')
    String url=request.getAttribute("ImageUrl");
        .addClass('twitter-share-button')
        .attr('href', 'https://twitter.com/share')
        .attr('data-url',request.getAttribute("ImageUrl"))
        .attr('data-text', request.getAttribute("ImageStory"))
       // .attr('data-hashtags', request.getAttribute("ImageStory"))
         .attr('data-count', 'none');
    $('#tweet').append(newTweet);
   
      //For custom button
    var baseTweet = "https://twitter.com/intent/tweet?url=http%3A%2F%2Fexample.com&text=";
    var newText = 'Here is a new Quote';
    var encoded = encodeURI(newText);
    var customTweet = baseTweet += encoded;
    $('#tweet-custom > a').attr('href', customTweet);
    
    twttr.widgets.load();
   });
      
});
</script>
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

<table>


<div class="container">
        
         <div id="tweet">
            <a class="twitter-share-button" href="https://twitter.com/share" data-url="<%=request.getAttribute("ImageUrl")%>" data-hashtags="<%=request.getAttribute("ImageStory")%>">Tweet</a>
         </div>
    </div>
<!--Javascript from Twitter-->
<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0];if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src="//platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>
</td></tr>
</table>
<table>
<tr><td> <div id="shareBtn">
<button class="button button1" onclick="myFunction()">
Share Image on Facebook</button></div>
	 </td></tr>		
<tr><td>
</table>


</div>    

</body>
</html>
