package by.home;

import by.home.factory.service.AccountServiceSingleton;

public class Main {

    public static void main(String[] args) {
        new StartMenu(AccountServiceSingleton.getInstance()).start(args);
    }
}
