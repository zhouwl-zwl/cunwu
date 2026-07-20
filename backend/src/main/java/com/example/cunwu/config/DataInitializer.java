package com.example.cunwu.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.cunwu.entity.User;
import com.example.cunwu.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DataInitializer {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        User admin = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, "admin"));
        if (admin != null && !passwordEncoder.matches("123456", admin.getPassword())) {
            admin.setPassword(passwordEncoder.encode("123456"));
            userMapper.updateById(admin);
        }

        User villageOfficial = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, "village_official"));
        if (villageOfficial != null && !passwordEncoder.matches("123456", villageOfficial.getPassword())) {
            villageOfficial.setPassword(passwordEncoder.encode("123456"));
            userMapper.updateById(villageOfficial);
        }

        User residentOfficial = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, "resident_official"));
        if (residentOfficial != null && !passwordEncoder.matches("123456", residentOfficial.getPassword())) {
            residentOfficial.setPassword(passwordEncoder.encode("123456"));
            userMapper.updateById(residentOfficial);
        }

        User villager = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, "villager"));
        if (villager != null && !passwordEncoder.matches("123456", villager.getPassword())) {
            villager.setPassword(passwordEncoder.encode("123456"));
            userMapper.updateById(villager);
        }
    }
}