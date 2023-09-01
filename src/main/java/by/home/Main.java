package by.home;

import by.home.factory.service.AccountServiceSingleton;

/**
 * класс для запуска консольной версии приложения
 * <p>
 * не забудьте аргументы коммандной строки
 */
public class Main {

    public static void main(String[] args) {
        new StartMenu(AccountServiceSingleton.getInstance()).start(args);
    }
}
