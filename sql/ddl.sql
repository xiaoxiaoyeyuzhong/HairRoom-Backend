-- 创建库
create database if not exists HairRoom;

-- 切换库
use HairRoom;

# -- 角色表
# create table if not exists role
# (
#     id          bigint auto_increment comment 'id' primary key,
#     roleName    varchar(256)                           not null comment '角色名称 Customer/ Staff / Manager',
#     roleKey     varchar(256)                           not null comment '权限描述 客户/员工/管理员',
#     createTime    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
#     updateTime    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
#     isDelete      tinyint  default 0                 not null comment '是否删除'
# );

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userName     varchar(256)                       null comment '用户昵称',
    userAccount  varchar(256)                       not null comment '账号',
    userAvatar   varchar(1024)                      null comment '用户头像',
    userPassword varchar(512)                       not null comment '密码',
    userRole     varchar(256)                       not null comment '用户角色',
    createTime   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除',
    constraint uni_userAccount unique (userAccount)

) comment '用户';

-- 客户表
create table if not exists customer
(
    id            bigint auto_increment comment 'id' primary key,
    customerName  varchar(256)                       not null comment '客户姓名',
    customerSex   varchar(256)                       null comment ' 客户性别',
    customerAge   bigint                             null comment '客户年龄',
    customerPhone varchar(256)                       not null comment '客户手机号',
    customerEmail varchar(512)                       null comment '客户邮箱',
    userId        bigint                             not null comment '创建用户id',
    createTime    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete      tinyint  default 0                 not null comment '是否删除'
#     foreign key (userId) references user (id)
) comment '客户';

-- 门店表
create table if not exists store
(
    id           bigint auto_increment comment 'id' primary key,
    storeName    varchar(256)                       not null comment '门店名称',
    storeAddress varchar(256)                       not null comment '门店地址',
    storePhone   varchar(256)                       not null comment '门店电话',
    storeEmail   varchar(512)                       null comment '门店邮箱',
    createTime   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除'
) comment '门店';

-- 员工表
create table if not exists staff
(
    id             bigint auto_increment comment 'id' primary key,
    staffName      varchar(256)                       not null comment '员工姓名',
    staffSex       varchar(256)                       null comment ' 员工性别',
    staffAge       bigint                             null comment '员工年龄',
    staffPhone     varchar(256)                       not null comment '员工手机号',
    staffEmail     varchar(512)                       null comment '员工邮箱',
    userId         bigint                             not null comment '创建用户id',
    storeId        bigint                             not null comment '所属门店id',
    isStoreManager tinyint  default 0                 not null comment '是否为店长',
    createTime     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete       tinyint  default 0                 not null comment '是否删除'
#     foreign key (userId) references user (id),
#     foreign key (storeId) references store (id)
) comment '员工';

-- 营业情况表
create table if not exists business_situation
(
    id             bigint auto_increment comment 'id' primary key,
    storeId        bigint         not null comment '门店id',
    businessAmount decimal(10, 2) not null comment '营业额',
    businessCost   decimal(10, 2) not null comment '成本',
    businessProfit decimal(10, 2) not null comment '利润',
    createTime     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete       tinyint  default 0                 not null comment '是否删除'
#     foreign key (storeId) references store (id)
) comment '营业情况';

-- 账单表
create table if not exists bill
(
    id         bigint auto_increment comment 'id' primary key,
    tradeNo   bigint                             not null comment '支付宝交易凭证号',
    billName   varchar(256)                       null comment '账单名称',
    billAmount decimal(10, 2)                     not null comment '账单金额',
    billType   varchar(256)                       not null comment '账单类型',
    billDesc   varchar(256)                       null comment '账单描述',
    CustomerId bigint                             not null comment '客户id',
    StaffId    bigint                             not null comment '员工id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
#     foreign key (CustomerId) references user (id),
#     foreign key (StaffId) references user (id)
) comment '账单';

-- 员工评价表
create table if not exists staff_evaluation
(
    id              bigint auto_increment comment 'id' primary key,
    customerId      bigint                             not null comment '客户id',
    staffId         bigint                             not null comment '员工id',
    evaluation      varchar(256)                       not null comment '评价内容',
    evaluationScore int                                not null comment '评价分数',
    createTime      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete        tinyint  default 0                 not null comment '是否删除'
#     foreign key (staffId) references staff (id)
)

# -- 帖子表
# create table if not exists post
# (
#     id            bigint auto_increment comment 'id' primary key,
#     age           int comment '年龄',
#     gender        tinyint  default 0                 not null comment '性别（0-男, 1-女）',
#     education     varchar(512)                       null comment '学历',
#     place         varchar(512)                       null comment '地点',
#     job           varchar(512)                       null comment '职业',
#     contact       varchar(512)                       null comment '联系方式',
#     loveExp       varchar(512)                       null comment '感情经历',
#     content       text                               null comment '内容（个人介绍）',
#     photo         varchar(1024)                      null comment '照片地址',
#     reviewStatus  int      default 0                 not null comment '状态（0-待审核, 1-通过, 2-拒绝）',
#     reviewMessage varchar(512)                       null comment '审核信息',
#     viewNum       int                                not null default 0 comment '浏览数',
#     thumbNum      int                                not null default 0 comment '点赞数',
#     userId        bigint                             not null comment '创建用户 id',
#     createTime    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
#     updateTime    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
#     isDelete      tinyint  default 0                 not null comment '是否删除'
# ) comment '帖子';


