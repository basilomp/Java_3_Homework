package clientserver;

import clientserver.commands.*;

import java.io.Serializable;
import java.util.List;

public class Command implements Serializable {

    private CommandType type;
    private Object data;

    public CommandType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public static Command authCommand(String login, String password) {
        Command command = new Command();
        command.type = CommandType.AUTH;
        command.data = new AuthCommandData(login, password);
        return command;
    }

    public static Command authOkCommand(String username) {
        Command command = new Command();
        command.type = CommandType.AUTH_OK;
        command.data = new AuthOkCommandData(username);
        return command;
    }

    public static Command errorCommand(String errorMessage) {
        Command command = new Command();
        command.type = CommandType.ERROR;
        command.data = new ErrorCommandData(errorMessage);
        return command;
    }

    public static Command messageInfoCommand(String message) {
        Command command = new Command();
        command.type = CommandType.INFO_MESSAGE;
        command.data = new MessageInfoCommandData(message);
        return command;
    }

    public static Command publicMessage(String username, String message) {
        Command command = new Command();
        command.type = CommandType.PUBLIC_MESSAGE;
        command.data = new PublicMessageCommandData(username, message);
        return command;
    }

    public static Command clientMessageCommand(String sender, String message) {
        Command command = new Command();
        command.type = CommandType.CLIENT_MESSAGE;
        command.data = new ClientMessageCommandData(sender, message);
        return command;
    }

    public static Command privateMessageCommand(String sender, String receiver, String message) {
        Command command = new Command();
        command.type = CommandType.PRIVATE_MESSAGE;
        command.data = new PrivateMessageCommandData(sender, receiver, message);
        return command;
    }

    public static Command endCommand() {
        Command command = new Command();
        command.type = CommandType.END;
        return command;
    }

    public static Command updateUsersListCommand(List<String> users) {
        Command command = new Command();
        command.type = CommandType.UPDATE_USERS;
        command.data = new UpdateUsersListCommandData(users);
        return command;
    }




//
//    public static Command messageToMyself(String sender, String receiver, String message) {
//        Command command = new Command();
//        command.type = CommandType.MESSAGE_TO_MYSELF;
//        command.data = new MessageToMyself(sender, receiver, message);
//        return command;
//
//}
}
