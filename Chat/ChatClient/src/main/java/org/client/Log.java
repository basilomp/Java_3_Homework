package org.client;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Log {
    private static PrintWriter pw;


    public static void start(String nickname) {
        try {
            pw = new PrintWriter(new FileOutputStream(generateLogFileName(nickname), true), true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void stop() {
        if (pw != null)
            pw.close();
    }

    //Создаем имя логфайла для клиента
    private static String generateLogFileName(String nickname) {
        return "logFiles/log_" + nickname + ".txt";
    }

    //Пишем в логфайл
    public static void writeToFile(String message) {
        pw.println(message);
    }


    //Метод для отображения последних 100 сообщений
    public static String displayLog (String nickname) {
        File f = new File(generateLogFileName(nickname));
        StringBuilder stringBuilder = new StringBuilder();
        if (f.exists()) {
            try {
                int x = 0;
                List<String> log100 = Files.readAllLines(Paths.get(generateLogFileName(nickname)));
                int size = log100.size();
                if (size > 100) {
                    x = size - 100;
                }
                for (int i = x; i < size; i++) {
                    stringBuilder.append(log100.get(i)).append(System.lineSeparator());

                }
                return stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            return "";
        } return stringBuilder.toString();

    }
}
