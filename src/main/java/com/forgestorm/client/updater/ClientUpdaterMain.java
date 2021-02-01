package com.forgestorm.client.updater;

import lombok.Getter;

@Getter
public class ClientUpdaterMain {

    private static ClientUpdaterMain instance = null;
    public static boolean ideRun = false;

    private UserInterface userInterface;
    private FileDownloader network;
    private StateMachine stateMachine;

    private ClientUpdaterMain() {
    }

    public void start() {
        userInterface = new UserInterface();
        network = new FileDownloader();
        stateMachine = new StateMachine();
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
