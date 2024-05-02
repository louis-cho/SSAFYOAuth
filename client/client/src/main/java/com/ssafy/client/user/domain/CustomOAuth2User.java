package com.ssafy.client.user.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomOAuth2User implements OAuth2User {
    private final UserDTO userDto;

    public CustomOAuth2User(UserDTO userDto) {

        this.userDto = userDto;
    }

    @Override
    public Map<String, Object> getAttributes() {
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("name", userDto.getName());
        map.put("userName", userDto.getUsername());
        map.put("role", userDto.getRole());
        return map;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return userDto.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        return userDto.getName();
    }

    public String getUsername() {

        return userDto.getUsername();
    }
}
