package com.bark.mapper;

import com.bark.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户DAO接口
 */
@Mapper
public interface UserMapper {
    
    /**
     * 根据用户名查询用户
     */
    User findByUsername(String username);
    
    /**
     * 新增用户
     */
    int insert(User user);
    
    /**
     * 更新用户
     */
    int update(User user);
    
    /**
     * 修改用户状态
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status);
    
    /**
     * 更新最后登录信息
     */
    int updateLoginInfo(@Param("id") Long id, @Param("ip") String ip);
    
    /**
     * 查询所有用户
     */
    List<User> findAll();
    
    /**
     * 根据ID查询用户
     */
    User findById(Long id);

    /**
     * 查询是否有用户数据
     * @return
     */
    int countUser();
} 