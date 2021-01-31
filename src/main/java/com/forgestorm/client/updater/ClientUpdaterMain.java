package com.forgestorm.client.updater;

import lombok.Getter;

@Getter
public class ClientUpdaterMain {

    private static ClientUpdaterMain instance = null;

    private UserInterface userInterface;
    private Network network;
    private ProgressTracker progressTracker;

    private ClientUpdaterMain() {
    }

    public void start() {
        userInterface = new UserInterface();
        network = new Network();
        progressTracker = new ProgressTracker();
    }

    public static ClientUpdaterMain getInstance() {
        if (instance == null) instance = new ClientUpdaterMain();
        return instance;
    }

    public static void main(String[] args) {
        ClientUpdaterMain.getInstance().start();
    }

}
