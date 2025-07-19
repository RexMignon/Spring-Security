package com.mignon.springsecurity.model.po

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import jakarta.persistence.*


/**
 * 系统角色表的实体类 (PO)
 * 对应数据库中的 `role` 表
 */
@Entity // 标记这是一个 JPA 实体
@TableName(value = "role") // 对应数据库表名
@Table(name = "role")
data class RolePo(
    /**
     * 角色ID
     * 对应数据库 `id` 字段，主键，自增长
     */
    @Id // 标记为主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 标记为自增长策略
    @field:TableId(value = "id",type = IdType.AUTO)
    var id: Long? = null, // Long 类型，可空，因为在插入时可能为 null (由数据库生成)

    /**
     * 角色名称，例如 ROOT, ADMIN, USER
     * 对应数据库 `role_name` 字段，非空，唯一
     */
    @Column(name = "role_name", nullable = false, unique = true, length = 50)
    @field:TableField(value = "role_name")
    var roleName: String, // String 类型，非空

    /**
     * 角色描述
     * 对应数据库 `description` 字段，可空
     */
    @Column(name = "description", length = 255)
    @field:TableField(value = "description")
    var description: String? = null // String 类型，可空
)