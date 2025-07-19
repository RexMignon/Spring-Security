package com.mignon.springsecurity.service.Impl

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.mignon.springsecurity.mapper.RoleMapper
import com.mignon.springsecurity.model.po.RolePo
import com.mignon.springsecurity.service.RoleService
import org.springframework.stereotype.Service


@Service
class RoleSerivceImpl(
    private val roleMapper: RoleMapper
) : RoleService, ServiceImpl<RoleMapper, RolePo>() {
    public  fun buildQueryWrappers(permissionName: String): QueryWrapper<RolePo> {
        val queryWrapper: QueryWrapper<RolePo> = QueryWrapper<RolePo>()

        queryWrapper.eq("role_name", permissionName)
        return queryWrapper
    }

    override fun selectRoleByRoleName(roleName: String): Long {

        return roleMapper.selectOne(buildQueryWrappers(roleName)).id ?:7
    }
}