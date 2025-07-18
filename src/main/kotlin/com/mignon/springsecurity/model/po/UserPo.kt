package com.mignon.springsecurity.model.po

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName

@TableName("user")
data class
UserPo(
    @field:TableId(value = "id",type = IdType.AUTO)
    var id: Integer?,
    @field:TableField(value = "username")
    var username: String,
    @field:TableField(value = "password")
    var password: String,
    @field:TableField(value = "enabled")
    var enabled: Boolean
)