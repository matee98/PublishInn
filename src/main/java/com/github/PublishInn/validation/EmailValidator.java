package com.github.PublishInn.validation;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class EmailValidator  implements Predicate<String> {

    @Override
    public boolean test(String s) {
        //todo: regex to validate
        return true;
    }
}
