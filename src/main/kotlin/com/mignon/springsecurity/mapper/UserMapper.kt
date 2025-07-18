package com.mignon.springsecurity.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.mignon.springsecurity.model.po.UserPo
import org.apache.ibatis.annotations.Mapper

@Mapper
interface UserMapper : BaseMapper<UserPo> {

}