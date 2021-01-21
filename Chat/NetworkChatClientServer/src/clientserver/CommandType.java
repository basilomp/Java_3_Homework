package clientserver;

public enum CommandType {
    AUTH,
    AUTH_OK,
    ERROR,
    PRIVATE_MESSAGE,
    PUBLIC_MESSAGE,

    INFO_MESSAGE,
    CLIENT_MESSAGE,

    END,
    UPDATE_USERS,
    MESSAGE_TO_MYSELF,
}
