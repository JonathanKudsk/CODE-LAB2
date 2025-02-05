package org.example;
public class PostMessage {
    protected String promtInput;
    protected String message;
    public PostMessage(String promtInput, String message) {
        this.promtInput = promtInput;
        this.message = message;
    }

    public String getPromtInput() {
        return promtInput;
    }


    public String getMessage() {
        return message;
    }


    public String toHtmlString() {
        return "<div id=" + promtInput + ">" +
                message + "</div>";
    }
}