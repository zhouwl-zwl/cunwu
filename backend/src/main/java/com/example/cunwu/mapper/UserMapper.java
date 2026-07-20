
package com.example.cunwu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.cunwu.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
