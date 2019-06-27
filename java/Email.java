public class Email {

    private String id;
    private String body;
    private String subject;

    public Email() {
        id = "";
        body = "";
        subject = "";

    }
    public Email(String id, String subject, String body){
        this.id = id;
        this.body = body;
        this.subject = subject;
    }

    public void setId(String id) {

        this.id = id;
    }

    public void setBody(String body) {

        this.body = body;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public String getSubject() {
        return subject;
    }
}
