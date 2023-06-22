package com.prostudy.mapper;

import com.prostudy.entity.Account;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    /**
     * 用户名或者邮箱登录
     *
     * @param text
     * @return
     */
    @Select("select * from db_account where username = #{text} or email = #{text}")
    Account findAccountByNameOrEmail(String text);

}
