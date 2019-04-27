/*Program to download all your google contacts to the local disk*/
package com.google.contacts;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.PeopleServiceScopes;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.Person;
import com.google.api.services.people.v1.model.PhoneNumber;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

public class MyGoogleContacts {
	private static final String APPLICATION_NAME = "Google Contact APP";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";

	/**
	 * Global instance of the scopes required by this application.
	 * If modifying these scopes, delete your previously saved tokens/ folder.
	 */
	private static final List<String> SCOPES = Arrays.asList(PeopleServiceScopes.CONTACTS_READONLY);
	private static final String CREDENTIALS_FILE_PATH = "/client_secret.json";

	/**
	 * Creates an authorized Credential object.
	 * @param HTTP_TRANSPORT The network HTTP Transport.
	 * @return An authorized Credential object.
	 * @throws IOException If the credentials.json file cannot be found.
	 */
	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		// Load client secrets.
		InputStream in = MyGoogleContacts.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
		if (in == null) {
			throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
		}
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
				.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
				.setAccessType("offline")
				.build();
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
		return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}

	public static void main(String... args) throws IOException, GeneralSecurityException {
		// Build a new authorized API client service.
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		PeopleService service = new PeopleService.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
				.setApplicationName(APPLICATION_NAME)
				.build();

		// Request connections.
		ListConnectionsResponse response = service.people().connections()
				.list("people/me")
				.setPageSize(2000)
				.setPersonFields("names,emailAddresses,phoneNumbers")
				.execute();

		// Save display name and contacts of connections if available in vcf format.
		List<Person> connections = response.getConnections();
		if (connections != null && connections.size() > 0) {
			for (Person person : connections) {
				List<Name> names = person.getNames();
				List<PhoneNumber> contacts = person.getPhoneNumbers();
				if (names != null && names.size() > 0 && contacts !=null && contacts.size() > 0) {
					downloadAsVCF(person);
				} 
			}
		} 
	}

	// Write each contact details in a vcf file in <user-home>/MyGoogleContacts location
	private static void downloadAsVCF(Person person) {
		try {
			String name = "dummy";	
			String phnNumber = "";
			if(null != person.getNames()) {
				name = person.getNames().get(0).getDisplayName().toString();
			}

			if(null != person.getPhoneNumbers()) {
				for(int i = 0; i < person.getPhoneNumbers().size(); i++) {
					phnNumber = phnNumber + "TEL;TYPE=work,voice;VALUE=uri:tel:" + person.getPhoneNumbers().get(i).getCanonicalForm() + "\n";
				}
			}
			String content = "BEGIN:VCARD\n" + 
					"VERSION:4.0\n" +
					"FN:" + name + "\n"+
					phnNumber + 
					"END:VCARD";
			List<String> lines = Arrays.asList(content.toString());
			String filePath = System.getProperty("user.home") + System.getProperty("file.separator") + "MyGoogleContacts";
			File dir = new File(filePath);
			if(!dir.exists()) {
				Files.createDirectory(dir.toPath());
			}
			Path file = Paths.get(filePath + System.getProperty("file.separator") + name + ".vcf");
			Files.write(file, lines, Charset.forName("UTF-8"));
		}catch(Exception e) {
			e.printStackTrace();
		}    	
	}
}