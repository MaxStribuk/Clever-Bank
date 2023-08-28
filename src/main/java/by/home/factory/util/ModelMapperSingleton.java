package by.home.factory.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ModelMapperSingleton {

    private static volatile ModelMapper instance;

    public static ModelMapper getInstance() {
        if (instance == null) {
            synchronized (ModelMapperSingleton.class) {
                if (instance == null) {
                    instance = new ModelMapper();
                }
            }
        }
        return instance;
    }
}
