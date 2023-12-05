package com.bbsk.anything.user.service;

import com.bbsk.anything.user.dto.RequestUserDto;
import com.bbsk.anything.user.entity.User;
import com.bbsk.anything.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void save(RequestUserDto user) {

        userRepository.save(new User().updateUser(user));
    }
}
