package com.github.PublishInn;

import com.github.PublishInn.model.entity.AppUser;
import com.github.PublishInn.model.entity.enums.AppUserRole;
import com.github.PublishInn.model.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataLoader implements ApplicationRunner {
    private final UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        AppUser admin = new AppUser("matz98", "matix3578@gmail.com",
                "$2a$10$zeO42jMBl3pI2hj8Ylp8p.Kds5UrQ80Sh30Bg0MmiCKPpoTGvlDjK", AppUserRole.ADMIN);
        admin.setEnabled(true);
        AppUser user = new AppUser("user", "matt@edu.pl",
                "$2a$10$zeO42jMBl3pI2hj8Ylp8p.Kds5UrQ80Sh30Bg0MmiCKPpoTGvlDjK", AppUserRole.USER);
        user.setEnabled(true);
        userRepository.save(admin);
        userRepository.save(user);
    }
}
