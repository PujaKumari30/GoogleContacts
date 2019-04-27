# GoogleContacts
## Code to download Google contacts to the local disk.
### To run the project -

* Download the "MyContactsApp-0.0.1-SNAPSHOT-jar-with-dependencies.jar" jar file from "https://github.com/PujaKumari30/GoogleContacts/releases/download/v1/MyContactsApp-0.0.1-SNAPSHOT-jar-with-dependencies.jar" or from the Target folder of this project and save it to your system.
* Open Command Prompt or Terminal.
* Traverse to the location of the downloaded jar file using cd <path-to-jar-file>
* Run the jar file using command- java -jar MyContactsApp-0.0.1-SNAPSHOT-jar-with-dependencies.jar
* A browser instance will open and will prompt the user to login to the google account.
* If the browser doesn't open, then copy the https link provided after running the jar file and open it in a browser.
* After logging to google account,it asks for the permissions to read and download the google contacts.
* Grant permission to the application when prompted.
* After user gets authenticated,you may close the browser.
* The contatcs will be downloaded at the following location - "user-home-directory/MyGoogleContacts".
  For example - In Windows,the location of the downloaded contacts will be C:\Users\kupuja\MyGoogleContacts. All contacts will be     downloaded as .vcf files
* The test case document can be accessed through this link - https://github.com/PujaKumari30/GoogleContacts/releases/download/v1/MyGoogleContacts_TestCases.xlsx
