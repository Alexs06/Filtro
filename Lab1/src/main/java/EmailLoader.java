import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;

import java.io.IOException;



import java.util.ArrayList;
import java.util.List;

public class EmailLoader {


    public ArrayList<Email> getNewMail(Gmail service, String label,long totalEmail) throws IOException {

        Gmail.Users.Messages.List request = service.users().messages().list("me");
        request.setQ("is:" + label).setMaxResults(totalEmail);
        ListMessagesResponse listMessagesResponse = request.execute();

        //Get new mail from a message list
        List<Message> emailMessagesResponse = listMessagesResponse.getMessages();
        ArrayList<Email> emailList = new ArrayList<>();
        if (emailMessagesResponse == null) {
            return emailList;
        } else {
            Email email=new Email();
            for (Message message : emailMessagesResponse) {
                email = email.toEmail(message);
                emailList.add(email);
            }
            return emailList;
        }
    }
}

