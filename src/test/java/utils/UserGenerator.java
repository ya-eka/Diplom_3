package utils;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;

public class UserGenerator {

    private static final Faker FAKER = new Faker();

    private static final String DEFAULT_PASSWORD = "Password123";

    @Step("Генерация валидного пользователя с рандомным именем и email")
    public static User getValidUser() {
        String name = FAKER.name().firstName() + "_" + System.currentTimeMillis();
        String email = FAKER.internet().emailAddress();
        return new User(name, email, DEFAULT_PASSWORD);
    }

 /*   @Step("Генерация пользователя с некорректным паролем: {password}")
    public static User getUserWithInvalidPassword(String password) {
        String name = FAKER.name().firstName() + "_" + System.currentTimeMillis();
        String email = FAKER.internet().emailAddress();
        return new User(name, email, password);
    }*/
}
