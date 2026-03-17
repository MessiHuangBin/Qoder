package com.example.springbootmybatis.infrastructure.repository.mapper;

import com.example.springbootmybatis.infrastructure.repository.po.UserPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper（只操作PO）
 */
@Mapper
public interface UserMapper {

    /**
     * 根据ID查询
     */
    UserPO selectById(@Param("id") Long id);

    /**
     * 根据用户名查询
     */
    UserPO selectByUsername(@Param("username") String username);

    /**
     * 根据状态查询列表
     */
    List<UserPO> selectByStatus(@Param("status") Integer status);

    /**
     * 查询所有
     */
    List<UserPO> selectAll();

    /**
     * 插入
     */
    int insert(UserPO userPO);

    /**
     * 更新
     */
    int update(UserPO userPO);

    /**
     * 删除
     */
    int deleteById(@Param("id") Long id);
}
