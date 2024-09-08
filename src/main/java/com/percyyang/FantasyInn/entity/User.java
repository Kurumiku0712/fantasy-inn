package com.percyyang.FantasyInn.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Document(collection = "users")
public class User implements UserDetails {

    @Id
    private String id;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    private String password;

    private String role;

    @DBRef
    private List<Booking> bookings = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));  // 将用户的角色封装为 SimpleGrantedAuthority 对象，并返回为列表
    }

    @Override
    public String getPassword() {
        return password;  // 没有额外处理可以不写, 因为lombok自动生成了, 这里为了保持接口实现的完整写了一下
    }

    @Override
    public String getUsername() {
        return email;  // 以email作为用户名
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // 如果不需要过期逻辑，直接返回 true
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // 如果不涉及账户锁定逻辑，直接返回 true
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // 如果不处理凭证过期问题，返回 true
    }

    @Override
    public boolean isEnabled() {
        return true;  // 如果所有账户默认启用，直接返回 true
    }


    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
