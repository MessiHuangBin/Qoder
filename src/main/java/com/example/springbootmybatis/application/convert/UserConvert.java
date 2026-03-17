package com.example.springbootmybatis.application.convert;

import com.example.springbootmybatis.application.dto.UserDTO;
import com.example.springbootmybatis.domain.model.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * X包与Domain对象转换器
 */
@Component
public class UserConvert {

    /**
     * X包 -> Domain（用于新增）
     */
    public User toDomain(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        return User.create(
                dto.getUsername(),
                dto.getPassword(),
                dto.getRealName(),
                dto.getEmail(),
                dto.getPhone()
        );
    }

    /**
     * X包 -> Domain（用于更新）
     */
    public User toDomainForUpdate(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRealName(dto.getRealName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setUserType(dto.getUserType());
        user.setStatus(dto.getStatus());
        user.setMaxBorrowCount(dto.getMaxBorrowCount());
        user.setCurrentBorrowCount(dto.getCurrentBorrowCount());
        return user;
    }

    /**
     * Domain -> X包（用于返回）
     */
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setRealName(user.getRealName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setUserType(user.getUserType());
        dto.setStatus(user.getStatus());
        dto.setMaxBorrowCount(user.getMaxBorrowCount());
        dto.setCurrentBorrowCount(user.getCurrentBorrowCount());
        return dto;
    }

    /**
     * Domain列表 -> X包列表
     */
    public List<UserDTO> toDTOList(List<User> users) {
        if (users == null) {
            return null;
        }
        List<UserDTO> dtoList = new ArrayList<>();
        for (User user : users) {
            dtoList.add(toDTO(user));
        }
        return dtoList;
    }
}
