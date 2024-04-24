package com.sieum.user.service;

import com.sieum.user.domain.SecurityUser;
import com.sieum.user.domain.User;
import com.sieum.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findBySocialId(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + " : User does not exist");
        }

        return new SecurityUser(user.get());
    }
}
