package ru.otus.repository;

import java.util.Optional;
import ru.otus.model.User;

public interface UserDao {

    Optional<User> findById(long id);

    Optional<User> findRandomUser();

    Optional<User> findByLogin(String login);
}
