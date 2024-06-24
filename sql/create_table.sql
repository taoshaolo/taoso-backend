# 数据库初始化


-- 创建库
create database if not exists my_db;

-- 切换库
use my_db;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_unionId (unionId)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 帖子表
create table if not exists post
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(512)                       null comment '标题',
    content    text                               null comment '内容',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    thumbNum   int      default 0                 not null comment '点赞数',
    favourNum  int      default 0                 not null comment '收藏数',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '帖子' collate = utf8mb4_unicode_ci;

-- 帖子点赞表（硬删除）
create table if not exists post_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子点赞';

-- 帖子收藏表（硬删除）
create table if not exists post_favour
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子收藏';


INSERT INTO `user` (`userAccount`, `userPassword`, `unionId`, `mpOpenId`, `userName`, `userAvatar`, `userProfile`,
                    `userRole`, `createTime`, `updateTime`, `isDelete`)
VALUES ('张三', 'zhangsan123', 'wx_union_id_14', 'wx_mp_openid_14', '张三', 'https://example.com/zhangsan_avatar.jpg',
        '我是一名软件工程师。', 'user', '2022-09-01 10:00:00', '2023-05-22 17:55:34', 0);

INSERT INTO `user` (`userAccount`, `userPassword`, `unionId`, `mpOpenId`, `userName`, `userAvatar`, `userProfile`,
                    `userRole`, `createTime`, `updateTime`, `isDelete`)
VALUES ('李四', 'lisi456', 'wx_union_id_15', 'wx_mp_openid_15', '李四', 'https://example.com/lisi_avatar.jpg',
        '我是一名产品经理。', 'admin', '2023-02-15 14:30:00', '2023-05-22 17:55:34', 0);

INSERT INTO `user` (`userAccount`, `userPassword`, `unionId`, `mpOpenId`, `userName`, `userAvatar`, `userProfile`,
                    `userRole`, `createTime`, `updateTime`, `isDelete`)
VALUES ('王五', 'wangwu789', 'wx_union_id_16', 'wx_mp_openid_16', '王五', 'https://example.com/wangwu_avatar.jpg',
        '我是一名市场专员。', 'ban', '2021-12-01 16:20:00', '2023-05-22 17:55:34', 0);

INSERT INTO `user` (`userAccount`, `userPassword`, `unionId`, `mpOpenId`, `userName`, `userAvatar`, `userProfile`,
                    `userRole`, `createTime`, `updateTime`, `isDelete`)
VALUES ('赵六', 'zhaoliu159', 'wx_union_id_17', 'wx_mp_openid_17', '赵六', 'https://example.com/zhaoliu_avatar.jpg',
        '我是一名设计师。', 'user', '2022-05-20 13:45:00', '2023-05-22 17:55:34', 0);

INSERT INTO `user` (`userAccount`, `userPassword`, `unionId`, `mpOpenId`, `userName`, `userAvatar`, `userProfile`,
                    `userRole`, `createTime`, `updateTime`, `isDelete`)
VALUES ('孙七', 'sunqi357', 'wx_union_id_18', 'wx_mp_openid_18', '孙七', 'https://example.com/sunqi_avatar.jpg',
        '我是一名数据分析师。', 'admin', '2023-03-30 11:15:00', '2023-05-22 17:55:34', 0);