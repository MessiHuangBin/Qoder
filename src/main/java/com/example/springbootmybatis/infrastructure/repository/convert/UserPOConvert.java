package com.example.springbootmybatis.infrastructure.repository.convert;

import com.example.springbootmybatis.domain.model.entity.User;
import com.example.springbootmybatis.infrastructure.repository.po.UserPO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Domain对象与PO转换器
 */
@Component
public class UserPOConvert {

    /**
     * PO -> Domain
     */
    public User toDomain(UserPO po) {
        if (po == null) {
            return null;
        }
        User user = new User();
        user.setId(po.getId());
        user.setUsername(po.getUsername());
        user.setPassword(po.getPassword());
        user.setRealName(po.getRealName());
        user.setEmail(po.getEmail());
        user.setPhone(po.getPhone());
        user.setUserType(po.getUserType());
        user.setStatus(po.getStatus());
        user.setMaxBorrowCount(po.getMaxBorrowCount());
        user.setCurrentBorrowCount(po.getCurrentBorrowCount());
        user.setCreateTime(po.getCreateTime());
        user.setUpdateTime(po.getUpdateTime());
        user.setDeleted(po.getDeleted());
        return user;
    }

    /**
     * Domain -> PO
     */
    public UserPO toPO(User user) {
        if (user == null) {
            return null;
        }
        UserPO po = new UserPO();
        po.setId(user.getId());
        po.setUsername(user.getUsername());
        po.setPassword(user.getPassword());
        po.setRealName(user.getRealName());
        po.setEmail(user.getEmail());
        po.setPhone(user.getPhone());
        po.setUserType(user.getUserType());
        po.setStatus(user.getStatus());
        po.setMaxBorrowCount(user.getMaxBorrowCount());
        po.setCurrentBorrowCount(user.getCurrentBorrowCount());
        po.setCreateTime(user.getCreateTime());
        po.setUpdateTime(user.getUpdateTime());
        po.setDeleted(user.getDeleted());
        return po;
    }

    /**
     * PO列表 -> Domain列表
     */
    public List<User> toDomainList(List<UserPO> poList) {
        if (poList == null) {
            return null;
        }
        List<User> userList = new ArrayList<>();
        for (UserPO po : poList) {
            userList.add(toDomain(po));
        }
        return userList;
    }
}
