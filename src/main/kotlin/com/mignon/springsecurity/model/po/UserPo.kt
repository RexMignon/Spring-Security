package com.mignon.springsecurity.model.po

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import jakarta.persistence.Column

@TableName("user") // 对应数据库表名
data class UserPo(
    // id 字段，对应数据库 BIGINT，应为 Long
    @TableId(value = "id", type = IdType.AUTO)
    var id: Long? = null, // 修正为 Long 类型

    @TableField(value = "username")
    var username: String,

    @TableField(value = "password")
    // 移除 @Column 注解，因为它与 @TableField 重复且可能导致混淆
    // 如果你确实需要验证长度或非空，可以在Service层或Validation层处理
    var password: String, // 确保这里是 String 类型

    @TableField(value = "role_id")
    var roleId: Long? = null, // 假设 role_id 是 BIGINT，所以是 Long。如果数据库允许 null，则加 ?

    @TableField(value = "credentials_expired")
    var credentialsExpired: Boolean = false,

    @TableField(value = "account_expired")
    var accountExpired: Boolean = false,

    @TableField(value = "dis_enabled")
    var disenabled: Boolean = false, // 注意这里是 dis_enabled，数据库列名是 dis_enabled

    @TableField("account_locked")
    var accountLocked: Boolean = false
)