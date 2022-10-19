package com.ead.authUser.configs.security;

import com.ead.authUser.models.UserModel;
import com.ead.authUser.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = userRepository.findByUsername( username )
                .orElseThrow(
                        ()-> new UsernameNotFoundException("User not found with username: " + username)
                );

        return UserDetailImpl.build( userModel );
    }
}
