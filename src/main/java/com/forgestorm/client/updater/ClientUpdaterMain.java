package com.forgestorm.client.updater;

import lombok.Getter;

@Getter
public class ClientUpdaterMain {

    public static final String FILE_URL = "https://forgestorm.com/client_files/";

    private static ClientUpdaterMain instance = null;
    public static boolean ideRun = false;

    private final UserInterface userInterface = new UserInterface();
    private final FileDownloader fileDownloader = new FileDownloader();
    private final StateMachine stateMachine = new StateMachine();

    private ClientUpdaterMain() {
    }

    public void start() {
        userInterface.buildUserInterface();
        stateMachine.trackProgress();
    }

    public static ClientUpdaterMain getInstance() {
        if (instance == null) instance = new ClientUpdaterMain();
        return instance;
    }

    public static void main(String[] args) {

        for (String arg : args) {
            if (arg.equalsIgnoreCase("ideRun")) {
                ideRun = true;
                break;
            }
        }

        ClientUpdaterMain.getInstance().start();
    }
}
