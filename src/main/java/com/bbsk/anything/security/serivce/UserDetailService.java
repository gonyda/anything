package com.bbsk.anything.security.serivce;

import com.bbsk.anything.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId){
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("아이디 또는 비밀번호를 다시 확인해주세요."));
    }
}
