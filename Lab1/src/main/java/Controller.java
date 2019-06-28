import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.gmail.Gmail;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class Controller {

    public static void main(String args[]) throws IOException, GeneralSecurityException {
        GmailAPI gmailapi = new GmailAPI();
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JacksonFactory.getDefaultInstance(), GmailAPI.getCredentials(HTTP_TRANSPORT)).setApplicationName("Gmail API Java Controller").build();
        gmailapi.getCredentials(HTTP_TRANSPORT);
        gmailapi.exc();
    }
}
