package com.github.PublishInn.utils;

public class EmailBuilder {
    public static String buildRegisterEmail(String name, String link) {
        return "Witaj, " + name + ".Dziękujemy za rejestrację konta. Kliknij w poniższy link, aby potwierdzić swój " +
                "adres e-mail: \n" + link + "\nLink wygaśnie w ciągu 30 minut.";
    }

    public static String buildResetPasswordEmail(String name, String link) {
        return "Witaj, " + name + ". Kliknij w poniższy link aby zresetować swoje hasło: \n" + link;
    }
}
