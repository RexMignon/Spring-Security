package com.mignon.springsecurity.service

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.extension.service.IService
import com.mignon.springsecurity.model.po.RolePo
import com.mignon.springsecurity.model.po.UserPo

interface RoleService : IService<RolePo> {

    fun selectRoleByRoleName(roleName: String): Long
}