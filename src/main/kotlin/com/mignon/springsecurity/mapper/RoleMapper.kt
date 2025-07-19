package com.mignon.springsecurity.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.mignon.springsecurity.model.po.RolePo
import org.apache.ibatis.annotations.Mapper

@Mapper
interface RoleMapper : BaseMapper<RolePo> {
}