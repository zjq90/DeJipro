-- ============================================
-- 小程序后端API数据库设计
-- 创建时间: 2026-04-26
-- 说明: 包含用户、商品、订单、支付等完整模块
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS dj_mall DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE dj_mall;

-- ============================================
-- 1. 用户相关表
-- ============================================

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) COMMENT '用户名',
    password VARCHAR(255) COMMENT '密码(加密)',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar VARCHAR(500) COMMENT '头像URL',
    gender TINYINT DEFAULT 0 COMMENT '性别: 0未知 1男 2女',
    birthday DATE COMMENT '生日',
    status TINYINT DEFAULT 1 COMMENT '状态: 0禁用 1正常 2注销',
    last_login_time DATETIME COMMENT '最后登录时间',
    last_login_ip VARCHAR(50) COMMENT '最后登录IP',
    invite_code VARCHAR(20) COMMENT '邀请码',
    parent_id BIGINT DEFAULT 0 COMMENT '上级用户ID',
    level_id BIGINT DEFAULT 1 COMMENT '会员等级ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除: 0未删除 1已删除',
    UNIQUE KEY uk_phone (phone),
    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_invite_code (invite_code),
    KEY idx_parent_id (parent_id),
    KEY idx_level_id (level_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 用户实名认证表
CREATE TABLE IF NOT EXISTS user_auth (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    real_name VARCHAR(50) COMMENT '真实姓名',
    id_card VARCHAR(30) COMMENT '身份证号',
    id_card_front VARCHAR(500) COMMENT '身份证正面照',
    id_card_back VARCHAR(500) COMMENT '身份证背面照',
    id_card_hand VARCHAR(500) COMMENT '手持身份证照',
    status TINYINT DEFAULT 0 COMMENT '认证状态: 0待审核 1已通过 2已拒绝',
    reject_reason VARCHAR(200) COMMENT '拒绝原因',
    audit_time DATETIME COMMENT '审核时间',
    audit_admin_id BIGINT COMMENT '审核管理员ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY uk_user_id (user_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户实名认证表';

-- 会员等级表
CREATE TABLE IF NOT EXISTS user_level (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '等级ID',
    level_name VARCHAR(50) NOT NULL COMMENT '等级名称',
    level_value INT NOT NULL COMMENT '等级值(数字越大等级越高)',
    min_experience BIGINT DEFAULT 0 COMMENT '最小经验值',
    max_experience BIGINT DEFAULT 999999999 COMMENT '最大经验值',
    discount DECIMAL(5,2) DEFAULT 1.00 COMMENT '折扣率',
    icon VARCHAR(500) COMMENT '等级图标',
    description VARCHAR(500) COMMENT '等级描述',
    privileges TEXT COMMENT '权益(JSON格式)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    KEY idx_level_value (level_value)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员等级表';

-- 角色权益表
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_code VARCHAR(50) NOT NULL COMMENT '角色编码',
    description VARCHAR(200) COMMENT '角色描述',
    privileges TEXT COMMENT '角色权限(JSON格式)',
    status TINYINT DEFAULT 1 COMMENT '状态: 0禁用 1启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY uk_role_code (role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    expire_time DATETIME COMMENT '过期时间(NULL为永久)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_role (user_id, role_id),
    KEY idx_user_id (user_id),
    KEY idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 验证码表
CREATE TABLE IF NOT EXISTS sms_code (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    phone VARCHAR(20) NOT NULL COMMENT '手机号',
    code VARCHAR(10) NOT NULL COMMENT '验证码',
    type TINYINT NOT NULL COMMENT '类型: 1注册 2登录 3重置密码 4绑定手机',
    expire_time DATETIME NOT NULL COMMENT '过期时间',
    used TINYINT DEFAULT 0 COMMENT '是否使用: 0否 1是',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_phone_type (phone, type),
    KEY idx_expire_time (expire_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='短信验证码表';

-- ============================================
-- 2. 资产管理表
-- ============================================

-- 用户资产表
CREATE TABLE IF NOT EXISTS user_asset (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    balance DECIMAL(12,2) DEFAULT 0.00 COMMENT '账户余额',
    frozen_balance DECIMAL(12,2) DEFAULT 0.00 COMMENT '冻结余额',
    total_commission DECIMAL(12,2) DEFAULT 0.00 COMMENT '累计佣金',
    available_commission DECIMAL(12,2) DEFAULT 0.00 COMMENT '可用佣金',
    total_integral BIGINT DEFAULT 0 COMMENT '累计积分',
    available_integral BIGINT DEFAULT 0 COMMENT '可用积分',
    experience BIGINT DEFAULT 0 COMMENT '经验值',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户资产表';

-- 资金流水表
CREATE TABLE IF NOT EXISTS balance_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    order_no VARCHAR(50) COMMENT '关联订单号',
    type TINYINT NOT NULL COMMENT '类型: 1充值 2消费 3退款 4提现 5佣金转入',
    amount DECIMAL(12,2) NOT NULL COMMENT '变动金额',
    balance_before DECIMAL(12,2) NOT NULL COMMENT '变动前余额',
    balance_after DECIMAL(12,2) NOT NULL COMMENT '变动后余额',
    description VARCHAR(200) COMMENT '描述',
    status TINYINT DEFAULT 1 COMMENT '状态: 0失败 1成功',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_user_id (user_id),
    KEY idx_order_no (order_no),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资金流水表';

-- 佣金记录表
CREATE TABLE IF NOT EXISTS commission_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID(获得佣金者)',
    order_id BIGINT COMMENT '关联订单ID',
    order_no VARCHAR(50) COMMENT '关联订单号',
    from_user_id BIGINT COMMENT '来源用户ID(下级)',
    level TINYINT DEFAULT 1 COMMENT '层级: 1一级 2二级',
    amount DECIMAL(10,2) NOT NULL COMMENT '佣金金额',
    rate DECIMAL(5,4) COMMENT '佣金比例',
    description VARCHAR(200) COMMENT '描述',
    status TINYINT DEFAULT 0 COMMENT '状态: 0待结算 1已结算 2已取消',
    settle_time DATETIME COMMENT '结算时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_user_id (user_id),
    KEY idx_order_no (order_no),
    KEY idx_from_user (from_user_id),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='佣金记录表';

-- 积分记录表
CREATE TABLE IF NOT EXISTS integral_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    order_id BIGINT COMMENT '关联订单ID',
    order_no VARCHAR(50) COMMENT '关联订单号',
    type TINYINT NOT NULL COMMENT '类型: 1获得 2消费 3过期 4退款',
    source TINYINT COMMENT '来源: 1消费 2签到 3活动 4注册 5邀请',
    integral BIGINT NOT NULL COMMENT '变动积分',
    integral_before BIGINT NOT NULL COMMENT '变动前积分',
    integral_after BIGINT NOT NULL COMMENT '变动后积分',
    description VARCHAR(200) COMMENT '描述',
    expire_time DATETIME COMMENT '过期时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_user_id (user_id),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分记录表';

-- ============================================
-- 3. 地址管理表
-- ============================================

CREATE TABLE IF NOT EXISTS user_address (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '地址ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    receiver_name VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    receiver_phone VARCHAR(20) NOT NULL COMMENT '收货人电话',
    province VARCHAR(50) COMMENT '省',
    city VARCHAR(50) COMMENT '市',
    district VARCHAR(50) COMMENT '区',
    address VARCHAR(200) NOT NULL COMMENT '详细地址',
    full_address VARCHAR(300) COMMENT '完整地址(省市区+详细地址)',
    zip_code VARCHAR(10) COMMENT '邮编',
    is_default TINYINT DEFAULT 0 COMMENT '是否默认: 0否 1是',
    longitude DECIMAL(10,7) COMMENT '经度',
    latitude DECIMAL(10,7) COMMENT '纬度',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    KEY idx_user_id (user_id),
    KEY idx_default (user_id, is_default)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户地址表';

-- ============================================
-- 4. 收藏管理表
-- ============================================

CREATE TABLE IF NOT EXISTS user_favorite (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_goods (user_id, goods_id),
    KEY idx_user_id (user_id),
    KEY idx_goods_id (goods_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收藏表';

-- ============================================
-- 5. 提现管理表
-- ============================================

-- 提现配置表
CREATE TABLE IF NOT EXISTS withdraw_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    min_amount DECIMAL(10,2) DEFAULT 1.00 COMMENT '最低提现金额',
    max_amount DECIMAL(10,2) DEFAULT 10000.00 COMMENT '单次最高提现金额',
    daily_max_amount DECIMAL(10,2) DEFAULT 50000.00 COMMENT '每日最高提现金额',
    daily_max_times INT DEFAULT 3 COMMENT '每日最多提现次数',
    fee_rate DECIMAL(5,4) DEFAULT 0.0000 COMMENT '手续费率',
    min_fee DECIMAL(10,2) DEFAULT 0.00 COMMENT '最低手续费',
    max_fee DECIMAL(10,2) DEFAULT 0.00 COMMENT '最高手续费',
    audit_required TINYINT DEFAULT 1 COMMENT '是否需要审核: 0否 1是',
    bank_enable TINYINT DEFAULT 1 COMMENT '银行卡提现是否启用: 0否 1是',
    alipay_enable TINYINT DEFAULT 1 COMMENT '支付宝提现是否启用: 0否 1是',
    wechat_enable TINYINT DEFAULT 1 COMMENT '微信提现是否启用: 0否 1是',
    description VARCHAR(500) COMMENT '提现说明',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='提现配置表';

-- 用户银行卡表
CREATE TABLE IF NOT EXISTS user_bank (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    bank_name VARCHAR(100) NOT NULL COMMENT '银行名称',
    bank_code VARCHAR(20) COMMENT '银行编码',
    branch_name VARCHAR(100) COMMENT '支行名称',
    account_name VARCHAR(50) NOT NULL COMMENT '开户人姓名',
    account_no VARCHAR(50) NOT NULL COMMENT '银行卡号',
    is_default TINYINT DEFAULT 0 COMMENT '是否默认: 0否 1是',
    status TINYINT DEFAULT 1 COMMENT '状态: 0禁用 1正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户银行卡表';

-- 提现记录表
CREATE TABLE IF NOT EXISTS withdraw_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    withdraw_no VARCHAR(50) NOT NULL COMMENT '提现单号',
    amount DECIMAL(10,2) NOT NULL COMMENT '提现金额',
    fee DECIMAL(10,2) DEFAULT 0.00 COMMENT '手续费',
    actual_amount DECIMAL(10,2) NOT NULL COMMENT '实际到账金额',
    withdraw_type TINYINT NOT NULL COMMENT '提现方式: 1银行卡 2支付宝 3微信',
    bank_id BIGINT COMMENT '银行卡ID(银行卡提现时)',
    bank_name VARCHAR(100) COMMENT '银行名称',
    branch_name VARCHAR(100) COMMENT '支行名称',
    account_name VARCHAR(50) COMMENT '开户人姓名',
    account_no VARCHAR(50) COMMENT '银行卡号/支付宝账号/微信openid',
    alipay_name VARCHAR(50) COMMENT '支付宝真实姓名',
    alipay_account VARCHAR(100) COMMENT '支付宝账号',
    wechat_name VARCHAR(50) COMMENT '微信昵称',
    wechat_openid VARCHAR(100) COMMENT '微信openid',
    status TINYINT DEFAULT 0 COMMENT '状态: 0待审核 1处理中 2已成功 3已拒绝 4已取消',
    reject_reason VARCHAR(200) COMMENT '拒绝原因',
    audit_time DATETIME COMMENT '审核时间',
    audit_admin_id BIGINT COMMENT '审核管理员ID',
    transfer_time DATETIME COMMENT '打款时间',
    transfer_no VARCHAR(100) COMMENT '打款流水号',
    remark VARCHAR(200) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_withdraw_no (withdraw_no),
    KEY idx_user_id (user_id),
    KEY idx_status (status),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='提现记录表';

-- ============================================
-- 6. 签到管理表
-- ============================================

-- 签到配置表
CREATE TABLE IF NOT EXISTS sign_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    day INT NOT NULL COMMENT '连续签到天数(1表示第1天,7表示第7天)',
    integral BIGINT DEFAULT 0 COMMENT '奖励积分',
    amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '奖励金额',
    description VARCHAR(200) COMMENT '描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY uk_day (day)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='签到配置表';

-- 签到记录表
CREATE TABLE IF NOT EXISTS sign_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    sign_date DATE NOT NULL COMMENT '签到日期',
    continuous_days INT DEFAULT 1 COMMENT '连续签到天数',
    integral BIGINT DEFAULT 0 COMMENT '获得积分',
    amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '获得金额',
    description VARCHAR(200) COMMENT '描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_date (user_id, sign_date),
    KEY idx_user_id (user_id),
    KEY idx_sign_date (sign_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='签到记录表';

-- ============================================
-- 7. 充值管理表
-- ============================================

-- 充值额度配置表
CREATE TABLE IF NOT EXISTS recharge_amount (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    amount DECIMAL(10,2) NOT NULL COMMENT '充值金额',
    give_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '赠送金额',
    give_integral BIGINT DEFAULT 0 COMMENT '赠送积分',
    sort INT DEFAULT 0 COMMENT '排序(数字越小越靠前)',
    status TINYINT DEFAULT 1 COMMENT '状态: 0下架 1上架',
    description VARCHAR(200) COMMENT '描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    KEY idx_sort (sort)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='充值额度配置表';

-- 充值记录表
CREATE TABLE IF NOT EXISTS recharge_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    recharge_no VARCHAR(50) NOT NULL COMMENT '充值单号',
    order_no VARCHAR(50) COMMENT '第三方订单号',
    amount DECIMAL(10,2) NOT NULL COMMENT '充值金额',
    give_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '赠送金额',
    give_integral BIGINT DEFAULT 0 COMMENT '赠送积分',
    pay_type TINYINT NOT NULL COMMENT '支付方式: 1支付宝 2微信小程序 3微信App 4微信公众号',
    status TINYINT DEFAULT 0 COMMENT '状态: 0待支付 1已支付 2已取消 3已退款',
    pay_time DATETIME COMMENT '支付时间',
    platform TINYINT DEFAULT 1 COMMENT '平台: 1小程序 2App 3公众号',
    remark VARCHAR(200) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_recharge_no (recharge_no),
    KEY idx_user_id (user_id),
    KEY idx_status (status),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='充值记录表';

-- ============================================
-- 8. 商品管理表
-- ============================================

-- 商品分类表
CREATE TABLE IF NOT EXISTS goods_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID(0表示一级分类)',
    category_name VARCHAR(50) NOT NULL COMMENT '分类名称',
    icon VARCHAR(500) COMMENT '分类图标',
    banner VARCHAR(500) COMMENT '分类banner图',
    sort INT DEFAULT 0 COMMENT '排序(数字越小越靠前)',
    level TINYINT DEFAULT 1 COMMENT '分类层级: 1一级 2二级',
    status TINYINT DEFAULT 1 COMMENT '状态: 0下架 1上架',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    KEY idx_parent_id (parent_id),
    KEY idx_sort (sort)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 商品表
CREATE TABLE IF NOT EXISTS goods (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商品ID',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    goods_name VARCHAR(200) NOT NULL COMMENT '商品名称',
    goods_subtitle VARCHAR(500) COMMENT '商品副标题',
    main_image VARCHAR(500) COMMENT '主图URL',
    sub_images TEXT COMMENT '副图URL(JSON数组)',
    description TEXT COMMENT '商品详情(富文本)',
    unit VARCHAR(20) DEFAULT '件' COMMENT '单位',
    weight DECIMAL(10,2) DEFAULT 0.00 COMMENT '重量(kg)',
    is_integral_goods TINYINT DEFAULT 0 COMMENT '是否积分商品: 0否 1是',
    sell_price DECIMAL(10,2) DEFAULT 0.00 COMMENT '销售价格',
    market_price DECIMAL(10,2) DEFAULT 0.00 COMMENT '市场价格',
    cost_price DECIMAL(10,2) DEFAULT 0.00 COMMENT '成本价格',
    integral_price BIGINT DEFAULT 0 COMMENT '积分价格',
    stock INT DEFAULT 0 COMMENT '库存',
    sell_count INT DEFAULT 0 COMMENT '销量',
    view_count INT DEFAULT 0 COMMENT '浏览量',
    favorite_count INT DEFAULT 0 COMMENT '收藏量',
    comment_count INT DEFAULT 0 COMMENT '评论数',
    good_comment_rate DECIMAL(5,2) DEFAULT 100.00 COMMENT '好评率',
    is_hot TINYINT DEFAULT 0 COMMENT '是否热门: 0否 1是',
    is_recommend TINYINT DEFAULT 0 COMMENT '是否推荐: 0否 1是',
    is_new TINYINT DEFAULT 0 COMMENT '是否新品: 0否 1是',
    status TINYINT DEFAULT 1 COMMENT '状态: 0下架 1上架',
    sort INT DEFAULT 0 COMMENT '排序',
    limit_buy INT DEFAULT 0 COMMENT '限购数量(0不限购)',
    is_free_shipping TINYINT DEFAULT 0 COMMENT '是否包邮: 0否 1是',
    shipping_template_id BIGINT COMMENT '运费模板ID',
    commission_rate DECIMAL(5,4) DEFAULT 0.0000 COMMENT '佣金比例',
    give_integral BIGINT DEFAULT 0 COMMENT '购买赠送积分',
    tags VARCHAR(500) COMMENT '商品标签(JSON数组)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    KEY idx_category_id (category_id),
    KEY idx_status (status),
    KEY idx_sell_count (sell_count),
    KEY idx_is_hot (is_hot),
    KEY idx_is_recommend (is_recommend)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 商品规格名表
CREATE TABLE IF NOT EXISTS goods_spec_name (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    spec_name VARCHAR(50) NOT NULL COMMENT '规格名称(如:颜色、尺码)',
    sort INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_goods_id (goods_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品规格名表';

-- 商品规格值表
CREATE TABLE IF NOT EXISTS goods_spec_value (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    spec_name_id BIGINT NOT NULL COMMENT '规格名ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    spec_value VARCHAR(100) NOT NULL COMMENT '规格值(如:红色、XL)',
    image VARCHAR(500) COMMENT '规格图片',
    sort INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_spec_name_id (spec_name_id),
    KEY idx_goods_id (goods_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品规格值表';

-- 商品SKU表
CREATE TABLE IF NOT EXISTS goods_sku (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'SKU ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    sku_name VARCHAR(500) NOT NULL COMMENT 'SKU名称(如:红色 XL)',
    specs TEXT COMMENT '规格组合(JSON格式)',
    spec_value_ids VARCHAR(200) COMMENT '规格值ID组合(逗号分隔)',
    sell_price DECIMAL(10,2) DEFAULT 0.00 COMMENT '销售价格',
    cost_price DECIMAL(10,2) DEFAULT 0.00 COMMENT '成本价格',
    integral_price BIGINT DEFAULT 0 COMMENT '积分价格',
    stock INT DEFAULT 0 COMMENT '库存',
    image VARCHAR(500) COMMENT 'SKU图片',
    weight DECIMAL(10,2) DEFAULT 0.00 COMMENT '重量',
    code VARCHAR(100) COMMENT '商品编码',
    barcode VARCHAR(100) COMMENT '条形码',
    status TINYINT DEFAULT 1 COMMENT '状态: 0禁用 1启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_goods_id (goods_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品SKU表';

-- 商品评论表
CREATE TABLE IF NOT EXISTS goods_comment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '评论ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_item_id BIGINT NOT NULL COMMENT '订单商品ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    sku_id BIGINT COMMENT 'SKU ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单号',
    content TEXT NOT NULL COMMENT '评论内容',
    images TEXT COMMENT '图片URL(JSON数组)',
    video VARCHAR(500) COMMENT '视频URL',
    star TINYINT DEFAULT 5 COMMENT '评分: 1-5星',
    is_anonymous TINYINT DEFAULT 0 COMMENT '是否匿名: 0否 1是',
    is_top TINYINT DEFAULT 0 COMMENT '是否置顶: 0否 1是',
    is_hot TINYINT DEFAULT 0 COMMENT '是否精选: 0否 1是',
    reply TEXT COMMENT '商家回复',
    reply_time DATETIME COMMENT '回复时间',
    like_count INT DEFAULT 0 COMMENT '点赞数',
    status TINYINT DEFAULT 1 COMMENT '状态: 0隐藏 1显示',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_goods_id (goods_id),
    KEY idx_user_id (user_id),
    KEY idx_order_id (order_id),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品评论表';

-- ============================================
-- 9. 购物车表
-- ============================================

CREATE TABLE IF NOT EXISTS cart (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '购物车ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    sku_id BIGINT NOT NULL COMMENT 'SKU ID',
    quantity INT DEFAULT 1 COMMENT '数量',
    selected TINYINT DEFAULT 1 COMMENT '是否选中: 0未选 1已选',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_user_goods_sku (user_id, goods_id, sku_id),
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- ============================================
-- 10. 系统配置表
-- ============================================

CREATE TABLE IF NOT EXISTS sys_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    config_key VARCHAR(100) NOT NULL COMMENT '配置key',
    config_value TEXT COMMENT '配置value',
    config_name VARCHAR(100) COMMENT '配置名称',
    description VARCHAR(500) COMMENT '配置描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_config_key (config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';
