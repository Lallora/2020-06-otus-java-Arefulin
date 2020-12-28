package hw17.server.message;

import com.google.gson.Gson;

public class ResponseMessage {
    private final ResponseType response;

    public ResponseMessage(ResponseType response) {
        this.response = response;
    }

    public String toJson(){
        final Gson gson = new Gson();
        return gson.toJson(this);
    }
}



