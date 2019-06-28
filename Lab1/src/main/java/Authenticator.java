import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;

public class Authenticator {

    public void logIn () {

    }
    public void logOut () throws IOException{
        GmailAPI gmailapi = new GmailAPI();
        String path = System.getProperty("C:\\Users\\alex0\\Desktop\\Lab1\\tokens\\StoredCredentials.file");
        File file = new File(path);
        gmailapi.service.users().stop("me").execute();
        FileInputStream inputFile = new FileInputStream(file);
        inputFile.close();
        file.delete();
    }
}
