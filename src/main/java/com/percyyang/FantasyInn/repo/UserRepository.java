package com.percyyang.FantasyInn.repo;

import com.percyyang.FantasyInn.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    // 检查数据库中是否存在具有指定电子邮件的用户，常用于注册流程中的唯一性验证
    boolean existsByEmail(String email);

    // 根据电子邮件查找用户信息，常用于登录、检索用户信息等场景
    // 如果用户存在，则返回该用户；如果用户不存在，则返回 Optional.empty()
    Optional<User> findByEmail(String email);

}
