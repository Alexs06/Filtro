import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
public class Email {
    private String from;
    private String to;
    private String subject;
    private String snippet;
    private String body;
    private ConfigurationParser parser;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    //extracts the atributes from a message to an Email
    private void splitEmail(Message id){
        String bod = parser.emailToString(id);
        Document doc = Jsoup.parse(bod);
        setBody(doc.body().text());
        setSnippet(id.getSnippet());


    }
    //Return an email
    public Email toEmail(Message id){
        Email email = new Email();
        email.splitEmail(id);
        return email;
    }
};


