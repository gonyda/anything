package com.bbsk.anything.user.entity;

import com.bbsk.anything.news.entity.NewsKeyword;
import com.bbsk.anything.security.serivce.Sha512CustomPasswordEncoder;
import com.bbsk.anything.user.dto.RequestUserDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@ToString
@Getter
@DynamicInsert
public class User implements UserDetails {

    @Id
    private String userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keywordId")
    private NewsKeyword newsKeyword;

    @Column(nullable = false)
    private String userPw;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime registDate;

    public User updateUser(RequestUserDto user) {
        this.userId = user.getUserId();
        this.userPw = new Sha512CustomPasswordEncoder().encode(user.getUserPw());
        return this;
    }

    public User updateKeyword(NewsKeyword newsKeyword) {
        this.newsKeyword = newsKeyword;
        return this;
    }

    /*
    * security
    * */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.userPw;
    }

    @Override
    public String getUsername() {
        return this.userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
