<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>

<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>

<!DOCTYPE html>
<html>
<head>
<h1> <center><I>Caption Generator Application</I> </center> </h1>
<!-- Global site tag (gtag.js) - Google Analytics -->
<script async src="https://www.googletagmanager.com/gtag/js?id=G-Z4R5MPCQV7"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());

  gtag('config', 'G-Z4R5MPCQV7');
</script>
<script type="text/javascript">
function getFileNameWithExt(event) {
    if (!event || !event.target || !event.target.files || event.target.files.length === 0) {
        return;
    }
    var name = event.target.files[0].name;
    document.getElementById("fileName").value = name;
}
</script>
<!-- Load the JS SDK asynchronously -->
<script async defer crossorigin="anonymous" src="https://connect.facebook.net/en_US/sdk.js"></script>

<meta charset="ISO-8859-1">
<title>Caption Generator</title>
</head>
<style>
    .button {
  appearance: button;
  background-color: #708090;
  border: solid transparent;
  border-radius: 16px;
  border-width: 0 0 4px;
  box-sizing: border-box;
  color: #000000;
  cursor: pointer;
  display: inline-block;
  font-family: din-round,sans-serif;
  font-size: 15px;
  font-weight: 700;
  letter-spacing: .8px;
  line-height: 20px;
  margin: 0;
  outline: none;
  overflow: visible;
  padding: 13px 16px;
  text-align: center;
  text-transform: uppercase;
  touch-action: manipulation;
  transform: translateZ(0);
  transition: filter .2s;
  user-select: none;
  -webkit-user-select: none;
  vertical-align: middle;
  white-space: nowrap;
  width: 10%;
}

.button:after {
  background-clip: padding-box;
  background-color: #708090;
  border: solid transparent;
  border-radius: 16px;
  border-width: 0 0 4px;
  bottom: -4px;
  content: "";
  left: 0;
  position: absolute;
  right: 0;
  top: 0;
  z-index: -1;
}

.button:main,
.button:focus {
  user-select: auto;
}

.button:hover:not(:disabled) {
  filter: brightness(1.1);
  -webkit-filter: brightness(1.1);
}

.button:disabled {
  cursor: auto;
}
    </style>
<!-- Facebook Pixel Code -->
<script>
!function(f,b,e,v,n,t,s)
{if(f.fbq)return;n=f.fbq=function(){n.callMethod?
n.callMethod.apply(n,arguments):n.queue.push(arguments)};
if(!f._fbq)f._fbq=n;n.push=n;n.loaded=!0;n.version='2.0';
n.queue=[];t=b.createElement(e);t.async=!0;
t.src=v;s=b.getElementsByTagName(e)[0];
s.parentNode.insertBefore(t,s)}(window, document,'script',
'https://connect.facebook.net/en_US/fbevents.js%27);
fbq('init', '1134177110766361');
fbq('track', 'PageView');
</script>
<noscript><img height="1" width="1" style="display:none"
src="https://www.facebook.com/tr?id=1134177110766361&ev=PageView&noscript=1"
/></noscript>
<!-- End Facebook Pixel Code -->
<body bgcolor="#bc8f8f">
<script>

  function statusChangeCallback(response) {  // Called with the results from FB.getLoginStatus().
    console.log('statusChangeCallback');
    console.log(response);                   // The current login status of the person.
    if (response.status === 'connected') {   // Logged into your webpage and Facebook.
      //testAPI();
      pic();
    } else {                                 // Not logged into your webpage or we are unable to tell.
      document.getElementById('status').innerHTML = 'Please log ' +
        'into facebook for images.';
    }
  }
  
  function test(){
	  FB.login(function()
	{   FB.api('/me/feed', 'post', {message: 'Hello, world!'});  }, {scope: 'publish_actions'});
	  document.getElementById('status').innerHTML = 'The publish_actions permission was deprecated and removed'
	  + ' 3 years ago now. <br>Im not going to spend the time to look for an alternative to a 3 years outdated assignment that should be up to date.<br>'
	  + 'see <a href="https://developers.facebook.com/blog/post/2018/04/24/new-facebook-platform-product-changes-policy-updates/">this link</a>'
	  + ' under Facebook Login section for details.';
  }


  function checkLoginState() {               // Called when a person is finished with the Login Button.
    FB.getLoginStatus(function(response) {   // See the onlogin handler
      statusChangeCallback(response);
    });
  }


  window.fbAsyncInit = function() {
    FB.init({
      appId      : '1134177110766361',
      cookie     : true,                     // Enable cookies to allow the server to access the session.
      xfbml      : true,                     // Parse social plugins on this webpage.
      version    : 'v13.0'           // Use this Graph API version for this call.
    });


    FB.getLoginStatus(function(response) {   // Called after the JS SDK has been initialized.
      statusChangeCallback(response);        // Returns the login status.
    });
    
  };
 
  function testAPI() {                      // Testing Graph API after login.  See statusChangeCallback() for when this call is made.
    console.log('Welcome!  Fetching your information.... ');
    FB.api('/me', {fields: 'name,email,last_name,first_name,birthday,gender'}, function(response) {
      console.log('Successful login for: ' + response.name);
      document.getElementById('status').innerHTML =
        'Thanks for logging in, ' + response.name + '!<br>'
        + 'Step 1: '+ '<br>'
        + 'Email: ' + response.email + '<br>'
        + 'First: ' + response.first_name + '<br>'
        + 'Last: ' + response.last_name + '<br><br>'
        + 'Step 4: ' + '<br>'
        + 'Birthday: ' + response.birthday + '<br>'
        + 'Gender: ' + response.gender;
    });
  }
  
  function pic() {           
	    FB.api('/me/photos/uploaded', {fields: 'id,name,images'}, function(response) {
	    	var idstring = JSON.stringify(response.data);
	    	var imgHtml = '';
	    	var url = '';
	    	for(var i = 0; i < response.data.length; i++) {
	    		var imageId = response.data[i]['id'];
	    		var imgages=response.data[i]['images'];
	    		var imageurl= imgages[i]['source'];
	    		var imgHtml= imgHtml + ' <div><img style="border: solid" "align:left" src=\"' + imageurl +'\" width=\"550px\"  height=\"250px\" onclick=imgOnclick("' + imageurl + '","' + imageId + '") ><div align="center-left"> <a class=\"info\" onclick=imgOnclick("' + imageurl + '","' + imageId + '")><I>Click here to Generate Caption.</I></a></div></div>';
	    		document.getElementById('status').innerHTML =imgHtml;
	  	    	  
	    		
	    	};
	      
	    });
	  }
function imgOnclick(src,id) {
	var hiddenInput = '<input type="hidden" name="hiddenField" value="' + src +'"/>';
    var hiddenInput2 = '<input type="hidden" name="Fb_image_id" value="' + id +'"/>';
    document.getElementById('imageform').innerHTML += hiddenInput;
    document.getElementById('imageform').innerHTML += hiddenInput2;
    document.getElementById('imageform').submit();
	  
}
  
  </script>
  <div align="center">
  <!-- <div style="border:thin; ; border-color: #ADD8E6; border-style: groove; background-color:#F0F8FF; opacity:inherit; width: 25%; height: 40px;" > -->
        <form action="<%= blobstoreService.createUploadUrl("/upload") %>" method="post" enctype="multipart/form-data">
         <!--	<div class="container"> -->
      <div class="button-wrap">
            <input type="file" name="myFile"  onChange='getFileNameWithExt(event)'   required="">
            <input type="hidden" id="fileName" name="fileName" />
           <!--   <input type="submit" value="Submit"> -->
             <input type="submit" class="button" value="Submit">
        </form>
      <!--  </div> -->
        <!-- <div style="border:thin; border-style: groove; border-color: #ADD8E6; background-color: #F0F8FF; width: 25%; height: 40px;"> -->
        <div>
        	<fb:login-button scope="public_profile,email" onlogin="checkLoginState();">
			</fb:login-button>
        </div>
        <div id="status" align="left">
		</div>
		<!-- </div> -->
        <form action="<%= blobstoreService.createUploadUrl("/CloudVision") %>" id="imageform" method="post" enctype="multipart/form-data">
        	<div>
        		
        	</div>
        </form>
   </div>
</body>
</html>