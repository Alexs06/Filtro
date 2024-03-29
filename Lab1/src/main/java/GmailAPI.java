import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GmailAPI {

    public static Gmail service;
    private static final String APPLICATION_NAME = "Gmail API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Arrays.asList(GmailScopes.MAIL_GOOGLE_COM);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */

    public void exc() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        // Print the labels in the user's account.
        String user = "me";
        ListLabelsResponse listResponse = service.users().labels().list(user).execute();
        List<Label> labels = listResponse.getLabels();
        if (labels.isEmpty()) {
            System.out.println("No labels found.");
        } else {
            System.out.println("Labels:");
            for (Label label : labels) {
                System.out.printf("- %s\n", label.getName());
            }
        }
        List<String> labelIds = Arrays.asList("SPAM");
        ListMessagesResponse messagesResponse = service.users().messages().list(user).setLabelIds(labelIds).execute();

        // Crea una lista para almacenar los mensajes obtenidos de las respuestas del servidor
        List<Message> messages = new ArrayList<Message>();
        while (messagesResponse.getMessages() != null) {
            messages.addAll(messagesResponse.getMessages());
            if (messagesResponse.getNextPageToken() != null) {
                String pageToken = messagesResponse.getNextPageToken();
                messagesResponse = service.users().messages().list(user).setLabelIds(labelIds).setPageToken(pageToken).execute();
            } else {
                break;
            }
        }
        // Imprime los mensajes
        System.out.println("\nEmails: \n");
        int msgCounter = 1;
        for (Message msgIterator : messages) {

            // Itere solamente por n-correos
            if (msgCounter <= 10) {

                System.out.println("Email details #" + (msgCounter) + ": \n");
                // Obtenga los datos puros de dicho correo, es decir "raw"
                Message msgCurrent = service.users().messages().get(user, msgIterator.getId()).setFormat("full").execute();

                // Imprimi los headers del correo.
                for (MessagePartHeader header : msgCurrent.getPayload().getHeaders()) {
                    if (header.getName().contains("Date") || header.getName().contains("From") || header.getName().contains("To") || header.getName().contains("Subject")) {

                        // Construya el string del header recuperado
                        String headerCurrent = (header.getName() + ": " + header.getValue());

                        // Para efectos de impresion, recortelo un poco si el header es absurdamente largo
                        headerCurrent = headerCurrent.substring(0, Math.min(headerCurrent.length(), 80));
                        System.out.println(" - " + headerCurrent);
                    }
                }
                // Obtenga la lista de partes del mensaje
                List<MessagePart> msgParts = msgCurrent.getPayload().getParts();

                // Y de dicha lista, solamente extraiga el cuerpo
                String body = new String(Base64.decodeBase64(msgParts.get(0).getBody().getData()));

                // Para efectos de claridad, arregle el correo con tecnicas de Reflex
                // Este proceso elimina saltos de paginas, saltos excesivos, simbolos innecesario, chars no-imprimibles
                body = body.replaceAll("[\r\n]{1,}", "\n");
                body = body.replaceAll("[\t]{1,}", "");
                body = body.replaceAll("&quot;", "'");
                body = body.replaceAll("&amp;", "");

                // Imprima el correo
                System.out.println("\nEmail body #" + (msgCounter) + ": \n");
                System.out.println(body);
            }
        }
    }

    public static Credential getCredentials (final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = Authenticator.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
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
}
