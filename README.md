# NewsManagerProject

Create a newspaper client application with some functionalities like:
	
		Read articles
		
		Publish articles

		Manage articles
		
# Data to manage

  #Article:
  
	- Attributes -> * Title(Requiered) 
					* Subtitle 
					* Category(Requiered) 
					* Abstract(Requiered) 
					* Body 
					* Image 
					
	-   Images 	 -> * image_data , * image_media_type  |  * thumbnail_data , * thumbnail_media_type
	
# Services Call

✔ When is achieve 

✖ When is not finished yet

Requirements		
		
	• Request to REST services uses  token authentication and a API key ✖
	• The token for a user is returned by login service ✖
	• Only logged calls allow modifying articles and their images, 
	  only users in same group can edit their images ✖
	• Unlogged GET calls retrieve all data available while 
	  logged GET calls retrieve articles belonging to the group
	  of the user logged (for editing purposes). ✖
	• All REST services return a json response, and a json library should be used. ✖
	  Example classes use library com.googlecode.json-simple.
	  (https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple)
	  You’ll need to include in the build.gradle module the following dependency  
	  		"implementation('com.googlecode.json-simple:json-simple:1.1.1'){ 
				exclude group: 'org.hamcrest', module: 'hamcrest-core' 
			 }" 
 	• Since REST services uses a self-signed certificate, 
	  additional material also includes required code to make 
	  connections from android application (TrustModifier class). ✖

	
