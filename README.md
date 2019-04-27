# GoogleContacts
Code to download Google contacts to the local disk
To run the project -
Go to the Target folder of the project (MyGoogleContacts/Target)
Download the jar file with dependencies "MyContactsApp-0.0.1-SNAPSHOT-jar-with-dependencies.jar" and save it to a location in your system
Open command Prompt
Move to the location of the downloaded jar file using cd <path-to-jar-file>
Run the jar file using command- java -jar MyContactsApp-0.0.1-SNAPSHOT-jar-with-dependencies.jar
A default browser will be opened and will prompt the user to login to the google account
If the browser doesn't get opened,then copy the https link provided after running the code and paste it in a browser
After logging to google account,it asks for the permissions to read and download the google contacts
Grant permission to the application whenever prompted
After user gets authenticated,close the browser
The downloaded contacts will be present at location - <user-home-directory>/MyGoogleContacts. For example - In Windows,the location of the downloaded contacts will be C:\Users\kupuja\MyGoogleContacts. All contacts will be downloaded as .vcf files
 
