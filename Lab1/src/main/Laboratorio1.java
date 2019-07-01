// Laboratorio 1

// AUTOR: LESTER CORDERO
// CARNET: B62110

// Libreria varias y de autentificacion "OAuth2"
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
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

// Libreria de manejo de servicios de Google
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.api.services.gmail.model.ListLabelsResponse;
import com.google.api.services.gmail.model.ListMessagesResponse;

// Libreria IO de Java
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.net.URLDecoder;

// Libreria NIO de Java
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

// Libreria de excepciones
import java.security.GeneralSecurityException;

// Libreria de Utilidades
import java.text.Normalizer;
import java.util.Scanner;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Dictionary;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

public class Laboratorio1 {
	
	// Funciones por defecto de Google
    private static final String APPLICATION_NAME = "Laboratorio 1";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES =  Arrays.asList(GmailScopes.GMAIL_LABELS,GmailScopes.MAIL_GOOGLE_COM);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

	// Obtiene los credenciales
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		
		// Esta instruccion, redirigue los warning de la entrada y salida de Google (fuente: https://github.com/googleapis/google-http-java-client/issues/315)
		// Limpia muchisimo el output de google excesivo
		final java.util.logging.Logger redirectWarningLogging = java.util.logging.Logger.getLogger(FileDataStoreFactory.class.getName()); redirectWarningLogging.setLevel(java.util.logging.Level.SEVERE);
        
        InputStream in = Laboratorio1.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
	
	// Funcion local creada por mi, que carga una lista de palabras de un archivo en una lista(osea los stopwords)
	private static List<String> wordsfromFiletoWordsList(String filename) throws IOException{
		List<String> ls = new ArrayList<String>();;
		try(BufferedReader buffer = new BufferedReader(new FileReader(filename))) {
			for(String ln; (ln = buffer.readLine()) != null;) {
				ls.add(ln);
			}
		}
		return ls;
	}
	
	// Funcion local que cree, que borra tildes simples
	private static String deleteAccents(String str) {
		str = str.replaceAll("í","i").replaceAll("ó","o").replaceAll("á","a").replaceAll("é","e"); 		
		return str;
	}
	
	// Inicio del programa
    public static void main(String... args) throws IOException, GeneralSecurityException {
		
        // Crea un cliente nuevo autorizado
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT)).setApplicationName(APPLICATION_NAME).build();

        // Lista de labels disponibles y accesibles para la cuenta y este programa
        String user = "me";
        ListLabelsResponse listResponse = service.users().labels().list(user).execute();
        List<Label> labels = listResponse.getLabels();
        if (labels.isEmpty()) {
            System.out.println("No Labels.");
        } else {
			int counter = 5;
            System.out.println("\nLabels: \n");
            for (Label label : labels) {
                System.out.printf("%s | ", label.getName());
				counter--;
				if(counter == 0){
					counter = 5;
					System.out.println();
				}
            }
			System.out.println();
        }

        // Setea los labels a SPAM, para que solo extraiga el correo SPAM
        List<String> labelIds = Arrays.asList("SPAM");

        // Ejecuta una respuesta del servidor solicitando mensajes con el Label SPAM
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
		
		// Si no existen datos, nisiquiera pregunte si desea cargarlos
		boolean skipAsk = false; // No debe preguntar si desea cargar datos, SI NO HAY datos
		String fileSaveLocation = "wordStoredInformation.data";
		File saveData = new File(fileSaveLocation);
		if(!saveData.exists()){
			System.out.println("No email saved data in this account.");
			skipAsk = true;
		}
				
		// Controla la entrada
		Scanner sc = new Scanner(System.in);
		
		// Bandera que controla si vamos a cargar los datos, o bien generar datos desde los correos nuevos
		char value = 'N';
		
		if (!skipAsk){
			System.out.println("REQUIRES USER INPUT] Do you wanna load saved Email Data (Words, Frecuency, Probability)? (Y/N)");
			value = sc.next().charAt(0);
		}
		
		if (value == 'Y'){
			
			// Cargue el .txt con informacion e imprimalo en pantalla
			// Para este caso, usaremos New IO (de Java 7)
			// Indique que vamos a imprimir las probas y las palabras
			System.out.println("\nEmail Word Analysis:\n ");
			Object[] tabulator = new String[3];
			tabulator[0] = "WORD";
			tabulator[1] = "FRECUENCY";
			tabulator[2] = "PROBABILITY";
			System.out.format("   %-15s %15s %16s \n\n", tabulator);
			
			System.out.println(Files.readAllBytes(Paths.get(fileSaveLocation)));
			
			
			// Detenga la ejecucion, no es necesario seguir ejecutando el resto, ya el usuario tiene los datos
			return;
		}
		
		
		// Mapa de palabras con sus frecuencias y otro con sus probabilidades
		Map<String, Integer> wordsWithFrecuency = new LinkedHashMap<String, Integer>();
		Map<String, Float> wordsWithProbability = new LinkedHashMap<String, Float>();
		
		// Cargue los stopwords del idioma ingles
		List<String> stopwords = wordsfromFiletoWordsList("src/main/resources/stopwords.txt");
		System.out.println("\nstopwords.txt has been loaded.\n");
		
		
		// Preguntarle al usuario, cuantos correos desea utilizar
        System.out.println("[REQUIRES USER INPUT] How much mails do you want to load from the SPAM inbox?");
		int msgLimit = sc.nextInt();
		sc.close();
		
		// Imprime los mensajes
		System.out.println("\nEmails: \n");
		int msgCounter = 1;
		for(Message msgIterator : messages){
			
			// Itere solamente por n-correos
			if(msgCounter<=msgLimit){
				
				System.out.println("Email details #" + (msgCounter) + ": \n");
				// Obtenga los datos puros de dicho correo, es decir "raw"
				Message msgCurrent = service.users().messages().get(user,msgIterator.getId()).setFormat("full").execute();
				
				// Imprimi los headers del correo.
				for (MessagePartHeader header : msgCurrent.getPayload().getHeaders()) {
				  if (header.getName().contains("Date") || header.getName().contains("From") || header.getName().contains("To") || header.getName().contains("Subject")){
					  
					  // Construya el string del header recuperado
					  String headerCurrent = (header.getName() + ": " + header.getValue());
					  
					  // Para efectos de impresion, recortelo un poco si el header es absurdamente largo
					  headerCurrent = headerCurrent.substring(0,Math.min(headerCurrent.length(),80));
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
				body = body.replaceAll("&quot;","'"); 
				body = body.replaceAll("&amp;",""); 
				body = deleteAccents(body);
				
				// Imprima el correo
				System.out.println("\nEmail body #" + (msgCounter) + ": \n");
				System.out.println(body);
				
				// Cree la lista de palabras y recortele los espacios en blanco que no sirven para nada
				String[] wordsInThisBody = body.trim().split("[\\s]+");
								
				// Itere todas las palabras y calcule sus frecuencias
				for (String w : wordsInThisBody) {
					w = w.toLowerCase();
					w = w.replaceAll("[^a-zA-Z0-9]+","");
					// Si la palabra no es monosilabo ni stopwork, agreguela a la lista de palabras
					if (w.length()>2 && w.length()<12 && !stopwords.contains(w)){
						
						// Obtenga la frecuencia actual de la palabra
						Integer frecuency = wordsWithFrecuency.get(w);
						
						if(frecuency == null){
							//Si es nulo, es que es la primera ocurrencia de la palabra
							wordsWithFrecuency.put(w, new Integer(1));
						}else{
							// Sino, sumele 1 a su frecuencia
							wordsWithFrecuency.put(w, new Integer(wordsWithFrecuency.get(w).intValue()+1));
						}
					}
				}
				
				// Itere por los demas correos
				msgCounter++;
			}
			
			
		}
		
		// Indique que vamos a imprimir las probas y las palabras
		System.out.println("\nEmail Word Analysis:\n ");
		Object[] outputFancyWords = new String[3];
		outputFancyWords[0] = "WORD";
		outputFancyWords[1] = "FRECUENCY";
		outputFancyWords[2] = "PROBABILITY";
		System.out.format("   %-15s %15s %16s \n\n", outputFancyWords);
		
		boolean error = false;
		
		// Borre lo almacenado, el usuario decidio no utilizarlo
		saveData.delete();
		
		// Ahora que ya tiene todas las frecuencias de todas la palabras, calcule su probabilidad
		for (Map.Entry<String, Integer> iterator : wordsWithFrecuency.entrySet()) {
			
			// Calcule las probabilidades con la cardinalidad de palabras
			Float a = Float.valueOf(iterator.getValue());
			Float b = Float.valueOf(wordsWithFrecuency.size());
			Float result = a.floatValue()/b.floatValue();
			
			// Redondeelo solo a 3 decimales, no queremos tanta precision realmente
			String currentProbability = String.format("%.3f", result);
			
			// Finalmente, imprima las palabras con sus probabilidades y frecuencias
			// Usaremos System.out.format, para que quede bien fancy!!!
			outputFancyWords[0] = iterator.getKey();
			outputFancyWords[1] = String.valueOf(iterator.getValue().intValue());
			outputFancyWords[2] = currentProbability;
			
			// Imprima en pantalla
			System.out.format("   %-15s %15s %15s%%\n", outputFancyWords);
			
			// Formato de salia en 
			String outputToFile = outputFancyWords[0] + " " + outputFancyWords[1] + " " + outputFancyWords[2] + "\n";
			
			// Escriba en el archivo de salida
			try{
				FileWriter fileWriter = new FileWriter(saveData, true);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				bufferedWriter.write(outputToFile);
				bufferedWriter.close();
			} catch(IOException e) {
				error = true;
			}
			
		}
		
		if(!error){
			System.out.println("\nData saved on " + fileSaveLocation);
		}else{
			System.out.println("\nError while saving words data.");
		}
		
		


    }
}