import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;


public class ConfigurationParser {
    public String emailToString(Message email){
        byte[] emailBytes;
        String body;
        emailBytes = Base64.decodeBase64(email.getPayload().getBody().getData());
        body = new String(emailBytes);
        return body;
    }

    public Email stringToEmail(){
        Email email=new Email();
        return email;
    }
}
