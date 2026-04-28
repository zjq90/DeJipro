-- ============================================
-- 新功能模块数据库设计
-- 创建时间: 2026-04-28
-- 说明: 包含推广管理、优惠券、拼团、砍价、提货点、文章、订单、城市服务、订阅消息等模块
-- ============================================

USE dj_mall;

-- ============================================
-- 11. 推广管理模块
-- ============================================

-- 推广海报配置表
CREATE TABLE IF NOT EXISTS promotion_poster (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '海报ID',
    poster_name VARCHAR(100) NOT NULL COMMENT '海报名称',
    poster_type TINYINT NOT NULL COMMENT '海报类型: 1推广海报 2邀新海报',
    background_image VARCHAR(500) NOT NULL COMMENT '背景图片URL',
    qrcode_x INT DEFAULT 100 COMMENT '二维码位置X坐标',
    qrcode_y INT DEFAULT 300 COMMENT '二维码位置Y坐标',
    qrcode_width INT DEFAULT 200 COMMENT '二维码宽度',
    qrcode_height INT DEFAULT 200 COMMENT '二维码高度',
    avatar_x INT DEFAULT 50 COMMENT '头像位置X坐标',
    avatar_y INT DEFAULT 50 COMMENT '头像位置Y坐标',
    avatar_width INT DEFAULT 80 COMMENT '头像宽度',
    avatar_height INT DEFAULT 80 COMMENT '头像高度',
    nickname_x INT DEFAULT 150 COMMENT '昵称位置X坐标',
    nickname_y INT DEFAULT 70 COMMENT '昵称位置Y坐标',
    nickname_font_size INT DEFAULT 24 COMMENT '昵称字体大小',
    nickname_color VARCHAR(20) DEFAULT '#333333' COMMENT '昵称颜色',
    description TEXT COMMENT '海报说明(JSON格式)',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态: 0禁用 1启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    KEY idx_poster_type (poster_type),
    KEY idx_status (status),
    KEY idx_sort (sort)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='推广海报配置表';

-- 推广数据统计表
CREATE TABLE IF NOT EXISTS promotion_stat (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID(推广人)',
    stat_date DATE NOT NULL COMMENT '统计日期',
    pv_count INT DEFAULT 0 COMMENT '页面浏览量',
    uv_count INT DEFAULT 0 COMMENT '独立访客数',
    register_count INT DEFAULT 0 COMMENT '注册用户数',
    order_count INT DEFAULT 0 COMMENT '订单数量',
    order_amount DECIMAL(12,2) DEFAULT 0.00 COMMENT '订单金额',
    commission_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '佣金金额',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_user_date (user_id, stat_date),
    KEY idx_user_id (user_id),
    KEY idx_stat_date (stat_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='推广数据统计表';

-- ============================================
-- 12. 优惠券模块
-- ============================================

-- 优惠券模板表
CREATE TABLE IF NOT EXISTS coupon (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '优惠券ID',
    coupon_name VARCHAR(100) NOT NULL COMMENT '优惠券名称',
    coupon_type TINYINT NOT NULL COMMENT '优惠券类型: 1满减券 2折扣券 3无门槛券 4新人券',
    discount_type TINYINT DEFAULT 1 COMMENT '折扣类型: 1金额 2折扣',
    discount_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '优惠金额/折扣值(如0.8表示8折)',
    min_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '最低消费金额(0表示无门槛)',
    max_discount_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '最大优惠金额(折扣券专用)',
    total_count INT DEFAULT -1 COMMENT '发放总量(-1表示不限)',
    receive_count INT DEFAULT 0 COMMENT '已领取数量',
    use_count INT DEFAULT 0 COMMENT '已使用数量',
    per_limit INT DEFAULT 1 COMMENT '每人限领数量(-1表示不限)',
    receive_start_time DATETIME COMMENT '领取开始时间',
    receive_end_time DATETIME COMMENT '领取结束时间',
    valid_start_time DATETIME COMMENT '有效期开始时间',
    valid_end_time DATETIME COMMENT '有效期结束时间',
    valid_days INT DEFAULT 0 COMMENT '领取后有效天数(0表示不按天数)',
    apply_type TINYINT DEFAULT 0 COMMENT '适用范围: 0全部商品 1指定分类 2指定商品',
    apply_value TEXT COMMENT '适用范围值(JSON格式:分类ID数组或商品ID数组)',
    exclude_value TEXT COMMENT '排除范围值(JSON格式:商品ID数组)',
    description VARCHAR(500) COMMENT '使用说明',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态: 0下架 1上架',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    KEY idx_coupon_type (coupon_type),
    KEY idx_status (status),
    KEY idx_receive_time (receive_start_time, receive_end_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券模板表';

-- 用户优惠券表
CREATE TABLE IF NOT EXISTS user_coupon (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    coupon_id BIGINT NOT NULL COMMENT '优惠券ID',
    coupon_name VARCHAR(100) NOT NULL COMMENT '优惠券名称(冗余)',
    coupon_type TINYINT NOT NULL COMMENT '优惠券类型(冗余)',
    discount_type TINYINT DEFAULT 1 COMMENT '折扣类型(冗余)',
    discount_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '优惠金额(冗余)',
    min_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '最低消费金额(冗余)',
    order_id BIGINT COMMENT '使用订单ID',
    status TINYINT DEFAULT 0 COMMENT '状态: 0未使用 1已使用 2已过期 3已作废',
    receive_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
    use_time DATETIME COMMENT '使用时间',
    valid_start_time DATETIME COMMENT '有效期开始时间',
    valid_end_time DATETIME COMMENT '有效期结束时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_user_id (user_id),
    KEY idx_coupon_id (coupon_id),
    KEY idx_status (status),
    KEY idx_valid_time (valid_end_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户优惠券表';

-- ============================================
-- 13. 拼团模块
-- ============================================

-- 拼团商品表
CREATE TABLE IF NOT EXISTS group_goods (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '拼团商品ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    sku_id BIGINT COMMENT 'SKU ID(为空表示全部规格)',
    activity_name VARCHAR(200) NOT NULL COMMENT '活动名称',
    group_type TINYINT DEFAULT 1 COMMENT '拼团类型: 1普通拼团 2老带新拼团 3团长免单',
    min_people INT DEFAULT 2 COMMENT '成团最少人数',
    max_people INT DEFAULT 10 COMMENT '成团最多人数(0表示不限)',
    group_price DECIMAL(10,2) NOT NULL COMMENT '拼团价格',
    original_price DECIMAL(10,2) COMMENT '原价(冗余)',
    total_stock INT DEFAULT 0 COMMENT '拼团库存',
    used_stock INT DEFAULT 0 COMMENT '已用库存',
    limit_buy INT DEFAULT 0 COMMENT '每人限购数量(0不限购)',
    start_time DATETIME NOT NULL COMMENT '活动开始时间',
    end_time DATETIME NOT NULL COMMENT '活动结束时间',
    valid_hours INT DEFAULT 24 COMMENT '拼团有效小时数',
    description VARCHAR(500) COMMENT '拼团说明',
    sort INT DEFAULT 0 COMMENT '排序',
    is_recommend TINYINT DEFAULT 0 COMMENT '是否推荐: 0否 1是',
    is_hot TINYINT DEFAULT 0 COMMENT '是否热门: 0否 1是',
    status TINYINT DEFAULT 1 COMMENT '状态: 0下架 1上架 2已结束',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    KEY idx_goods_id (goods_id),
    KEY idx_status (status),
    KEY idx_activity_time (start_time, end_time),
    KEY idx_recommend (is_recommend),
    KEY idx_hot (is_hot)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='拼团商品表';

-- 拼团活动表(用户发起的拼团)
CREATE TABLE IF NOT EXISTS group_activity (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '拼团活动ID',
    group_goods_id BIGINT NOT NULL COMMENT '拼团商品ID',
    leader_id BIGINT NOT NULL COMMENT '团长用户ID',
    leader_order_id BIGINT COMMENT '团长订单ID',
    current_people INT DEFAULT 1 COMMENT '当前人数',
    need_people INT DEFAULT 2 COMMENT '需要人数',
    status TINYINT DEFAULT 0 COMMENT '状态: 0进行中 1已成团 2已失败',
    start_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    success_time DATETIME COMMENT '成团时间',
    fail_reason VARCHAR(200) COMMENT '失败原因',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_group_goods_id (group_goods_id),
    KEY idx_leader_id (leader_id),
    KEY idx_status (status),
    KEY idx_end_time (end_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='拼团活动表';

-- 拼团订单关联表
CREATE TABLE IF NOT EXISTS group_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    group_activity_id BIGINT NOT NULL COMMENT '拼团活动ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单号',
    is_leader TINYINT DEFAULT 0 COMMENT '是否团长: 0否 1是',
    join_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
    status TINYINT DEFAULT 0 COMMENT '状态: 0待支付 1已支付 2已取消 3已退款',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_group_activity_id (group_activity_id),
    KEY idx_user_id (user_id),
    KEY idx_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='拼团订单关联表';

-- ============================================
-- 14. 砍价模块
-- ============================================

-- 砍价商品表
CREATE TABLE IF NOT EXISTS bargain_goods (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '砍价商品ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    sku_id BIGINT COMMENT 'SKU ID(为空表示全部规格)',
    activity_name VARCHAR(200) NOT NULL COMMENT '活动名称',
    original_price DECIMAL(10,2) NOT NULL COMMENT '原价',
    bargain_price DECIMAL(10,2) NOT NULL COMMENT '最低价',
    min_bargain DECIMAL(10,2) DEFAULT 1.00 COMMENT '单次最少砍价金额',
    max_bargain DECIMAL(10,2) DEFAULT 10.00 COMMENT '单次最多砍价金额',
    total_stock INT DEFAULT 0 COMMENT '活动库存',
    used_stock INT DEFAULT 0 COMMENT '已用库存',
    limit_buy INT DEFAULT 1 COMMENT '每人限购数量',
    limit_help INT DEFAULT 3 COMMENT '每人帮砍次数',
    start_time DATETIME NOT NULL COMMENT '活动开始时间',
    end_time DATETIME NOT NULL COMMENT '活动结束时间',
    valid_hours INT DEFAULT 48 COMMENT '砍价有效小时数',
    description VARCHAR(500) COMMENT '砍价说明',
    sort INT DEFAULT 0 COMMENT '排序',
    is_recommend TINYINT DEFAULT 0 COMMENT '是否推荐: 0否 1是',
    is_hot TINYINT DEFAULT 0 COMMENT '是否热门: 0否 1是',
    status TINYINT DEFAULT 1 COMMENT '状态: 0下架 1上架 2已结束',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    KEY idx_goods_id (goods_id),
    KEY idx_status (status),
    KEY idx_activity_time (start_time, end_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='砍价商品表';

-- 砍价活动表(用户发起的砍价)
CREATE TABLE IF NOT EXISTS bargain_activity (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '砍价活动ID',
    bargain_goods_id BIGINT NOT NULL COMMENT '砍价商品ID',
    user_id BIGINT NOT NULL COMMENT '发起用户ID',
    current_price DECIMAL(10,2) NOT NULL COMMENT '当前价格',
    bargain_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '已砍金额',
    bargain_count INT DEFAULT 0 COMMENT '帮砍人数',
    status TINYINT DEFAULT 0 COMMENT '状态: 0进行中 1已完成(可购买) 2已购买 3已过期 4已取消',
    start_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    order_id BIGINT COMMENT '购买订单ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_bargain_goods_id (bargain_goods_id),
    KEY idx_user_id (user_id),
    KEY idx_status (status),
    KEY idx_end_time (end_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='砍价活动表';

-- 砍价记录表
CREATE TABLE IF NOT EXISTS bargain_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    bargain_activity_id BIGINT NOT NULL COMMENT '砍价活动ID',
    help_user_id BIGINT NOT NULL COMMENT '帮砍用户ID',
    help_user_nickname VARCHAR(50) COMMENT '帮砍用户昵称',
    help_user_avatar VARCHAR(500) COMMENT '帮砍用户头像',
    bargain_amount DECIMAL(10,2) NOT NULL COMMENT '砍价金额',
    is_new_user TINYINT DEFAULT 0 COMMENT '是否新用户: 0否 1是',
    bargain_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '砍价时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_bargain_activity_id (bargain_activity_id),
    KEY idx_help_user_id (help_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='砍价记录表';

-- ============================================
-- 15. 提货点模块
-- ============================================

-- 提货点表
CREATE TABLE IF NOT EXISTS pickup_point (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '提货点ID',
    point_name VARCHAR(100) NOT NULL COMMENT '提货点名称',
    point_code VARCHAR(50) COMMENT '提货点编码',
    contact_person VARCHAR(50) COMMENT '联系人',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    province VARCHAR(50) COMMENT '省',
    city VARCHAR(50) COMMENT '市',
    district VARCHAR(50) COMMENT '区',
    address VARCHAR(200) NOT NULL COMMENT '详细地址',
    full_address VARCHAR(300) COMMENT '完整地址',
    longitude DECIMAL(10,7) COMMENT '经度',
    latitude DECIMAL(10,7) COMMENT '纬度',
    business_hours VARCHAR(200) COMMENT '营业时间(JSON格式)',
    images TEXT COMMENT '图片URL(JSON数组)',
    description VARCHAR(500) COMMENT '描述',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态: 0禁用 1启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    KEY idx_city (city),
    KEY idx_district (district),
    KEY idx_status (status),
    KEY idx_location (longitude, latitude)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='提货点表';

-- ============================================
-- 16. 文章模块
-- ============================================

-- 文章分类表
CREATE TABLE IF NOT EXISTS article_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID(0表示一级分类)',
    category_name VARCHAR(50) NOT NULL COMMENT '分类名称',
    icon VARCHAR(500) COMMENT '分类图标',
    sort INT DEFAULT 0 COMMENT '排序',
    level TINYINT DEFAULT 1 COMMENT '分类层级',
    status TINYINT DEFAULT 1 COMMENT '状态: 0禁用 1启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    KEY idx_parent_id (parent_id),
    KEY idx_sort (sort),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章分类表';

-- 文章表
CREATE TABLE IF NOT EXISTS article (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '文章ID',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    article_title VARCHAR(200) NOT NULL COMMENT '文章标题',
    article_subtitle VARCHAR(500) COMMENT '文章副标题',
    cover_image VARCHAR(500) COMMENT '封面图片URL',
    author VARCHAR(50) COMMENT '作者',
    source VARCHAR(100) COMMENT '来源',
    summary VARCHAR(500) COMMENT '摘要',
    content TEXT COMMENT '文章内容(富文本)',
    view_count INT DEFAULT 0 COMMENT '浏览量',
    like_count INT DEFAULT 0 COMMENT '点赞数',
    favorite_count INT DEFAULT 0 COMMENT '收藏数',
    comment_count INT DEFAULT 0 COMMENT '评论数',
    share_count INT DEFAULT 0 COMMENT '分享数',
    is_top TINYINT DEFAULT 0 COMMENT '是否置顶: 0否 1是',
    is_hot TINYINT DEFAULT 0 COMMENT '是否热门: 0否 1是',
    is_recommend TINYINT DEFAULT 0 COMMENT '是否推荐: 0否 1是',
    allow_comment TINYINT DEFAULT 1 COMMENT '是否允许评论: 0否 1是',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态: 0下架 1上架',
    publish_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    KEY idx_category_id (category_id),
    KEY idx_status (status),
    KEY idx_publish_time (publish_time),
    KEY idx_view_count (view_count),
    KEY idx_is_top (is_top),
    KEY idx_is_hot (is_hot)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章表';

-- 轮播图表
CREATE TABLE IF NOT EXISTS banner (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '轮播图ID',
    banner_name VARCHAR(100) NOT NULL COMMENT '轮播图名称',
    banner_type TINYINT DEFAULT 1 COMMENT '轮播类型: 1首页轮播 2文章轮播 3活动轮播',
    image_url VARCHAR(500) NOT NULL COMMENT '图片URL',
    link_type TINYINT DEFAULT 0 COMMENT '链接类型: 0无链接 1商品详情 2文章详情 3活动页面 4外部链接',
    link_value VARCHAR(500) COMMENT '链接值(商品ID/文章ID/URL)',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态: 0禁用 1启用',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    KEY idx_banner_type (banner_type),
    KEY idx_status (status),
    KEY idx_sort (sort),
    KEY idx_time (start_time, end_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='轮播图表';

-- ============================================
-- 17. 订单模块
-- ============================================

-- 订单主表
CREATE TABLE IF NOT EXISTS order_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单号',
    parent_order_no VARCHAR(50) COMMENT '父订单号(拆单时使用)',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    order_type TINYINT DEFAULT 1 COMMENT '订单类型: 1普通订单 2拼团订单 3砍价订单 4积分订单',
    order_source TINYINT DEFAULT 1 COMMENT '订单来源: 1小程序 2App 3H5',
    order_status TINYINT DEFAULT 0 COMMENT '订单状态: 0待付款 1待发货 2待收货 3已完成 4已取消 5已退款',
    pay_status TINYINT DEFAULT 0 COMMENT '支付状态: 0未支付 1已支付 2部分退款 3全额退款',
    goods_amount DECIMAL(12,2) DEFAULT 0.00 COMMENT '商品总金额',
    freight_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '运费金额',
    discount_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '优惠金额',
    coupon_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '优惠券金额',
    integral_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '积分抵扣金额',
    pay_amount DECIMAL(12,2) DEFAULT 0.00 COMMENT '实付金额',
    used_integral BIGINT DEFAULT 0 COMMENT '使用积分',
    give_integral BIGINT DEFAULT 0 COMMENT '赠送积分',
    coupon_id BIGINT COMMENT '使用优惠券ID',
    receiver_name VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    receiver_phone VARCHAR(20) NOT NULL COMMENT '收货人电话',
    receiver_province VARCHAR(50) COMMENT '收货省',
    receiver_city VARCHAR(50) COMMENT '收货市',
    receiver_district VARCHAR(50) COMMENT '收货区',
    receiver_address VARCHAR(200) NOT NULL COMMENT '收货详细地址',
    receiver_full_address VARCHAR(300) COMMENT '收货完整地址',
    receiver_longitude DECIMAL(10,7) COMMENT '收货经度',
    receiver_latitude DECIMAL(10,7) COMMENT '收货纬度',
    pickup_type TINYINT DEFAULT 1 COMMENT '收货方式: 1快递配送 2到店自提',
    pickup_point_id BIGINT COMMENT '提货点ID',
    user_remark VARCHAR(200) COMMENT '用户备注',
    merchant_remark VARCHAR(200) COMMENT '商家备注',
    pay_type TINYINT COMMENT '支付方式: 1支付宝 2微信小程序 3微信App 4微信公众号 5余额支付',
    pay_time DATETIME COMMENT '支付时间',
    pay_order_no VARCHAR(100) COMMENT '第三方支付订单号',
    delivery_time DATETIME COMMENT '发货时间',
    logistics_company VARCHAR(50) COMMENT '物流公司',
    logistics_no VARCHAR(100) COMMENT '物流单号',
    receive_time DATETIME COMMENT '收货时间',
    cancel_time DATETIME COMMENT '取消时间',
    cancel_reason VARCHAR(200) COMMENT '取消原因',
    finish_time DATETIME COMMENT '完成时间',
    auto_confirm_time DATETIME COMMENT '自动确认收货时间',
    is_comment TINYINT DEFAULT 0 COMMENT '是否已评价: 0否 1是',
    is_delete TINYINT DEFAULT 0 COMMENT '用户是否删除: 0否 1是',
    group_activity_id BIGINT COMMENT '拼团活动ID',
    bargain_activity_id BIGINT COMMENT '砍价活动ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_user_id (user_id),
    KEY idx_order_status (order_status),
    KEY idx_pay_status (pay_status),
    KEY idx_create_time (create_time),
    KEY idx_pay_time (pay_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单主表';

-- 订单商品表
CREATE TABLE IF NOT EXISTS order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    sku_id BIGINT NOT NULL COMMENT 'SKU ID',
    goods_name VARCHAR(200) NOT NULL COMMENT '商品名称',
    goods_image VARCHAR(500) COMMENT '商品图片',
    sku_name VARCHAR(500) COMMENT 'SKU名称',
    sku_specs TEXT COMMENT '规格(JSON格式)',
    unit VARCHAR(20) DEFAULT '件' COMMENT '单位',
    quantity INT DEFAULT 1 COMMENT '购买数量',
    original_price DECIMAL(10,2) DEFAULT 0.00 COMMENT '商品原价',
    sell_price DECIMAL(10,2) DEFAULT 0.00 COMMENT '商品售价',
    discount_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '优惠金额',
    total_amount DECIMAL(12,2) DEFAULT 0.00 COMMENT '商品总金额',
    give_integral BIGINT DEFAULT 0 COMMENT '赠送积分',
    is_comment TINYINT DEFAULT 0 COMMENT '是否已评价: 0否 1是',
    comment_id BIGINT COMMENT '评论ID',
    after_sale_status TINYINT DEFAULT 0 COMMENT '售后状态: 0无售后 1售后中 2售后完成',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_order_id (order_id),
    KEY idx_order_no (order_no),
    KEY idx_user_id (user_id),
    KEY idx_goods_id (goods_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单商品表';

-- 支付记录表
CREATE TABLE IF NOT EXISTS pay_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '支付记录ID',
    pay_no VARCHAR(50) NOT NULL COMMENT '支付流水号',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    pay_type TINYINT NOT NULL COMMENT '支付方式: 1支付宝 2微信小程序 3微信App 4微信公众号 5余额支付',
    pay_amount DECIMAL(12,2) DEFAULT 0.00 COMMENT '支付金额',
    pay_status TINYINT DEFAULT 0 COMMENT '支付状态: 0待支付 1支付成功 2支付失败 3已退款',
    third_pay_no VARCHAR(100) COMMENT '第三方支付流水号',
    pay_time DATETIME COMMENT '支付时间',
    fail_reason VARCHAR(200) COMMENT '失败原因',
    refund_amount DECIMAL(12,2) DEFAULT 0.00 COMMENT '退款金额',
    refund_time DATETIME COMMENT '退款时间',
    refund_reason VARCHAR(200) COMMENT '退款原因',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_pay_no (pay_no),
    KEY idx_order_id (order_id),
    KEY idx_order_no (order_no),
    KEY idx_user_id (user_id),
    KEY idx_pay_status (pay_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付记录表';

-- ============================================
-- 18. 城市服务模块
-- ============================================

-- 城市服务表(树形结构)
CREATE TABLE IF NOT EXISTS city_service (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '服务ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父服务ID(0表示一级分类)',
    service_name VARCHAR(100) NOT NULL COMMENT '服务名称',
    service_code VARCHAR(50) COMMENT '服务编码',
    icon VARCHAR(500) COMMENT '服务图标',
    image VARCHAR(500) COMMENT '服务图片',
    link_type TINYINT DEFAULT 0 COMMENT '链接类型: 0无链接 1内部页面 2外部链接 3小程序',
    link_value VARCHAR(500) COMMENT '链接值',
    description VARCHAR(500) COMMENT '服务描述',
    sort INT DEFAULT 0 COMMENT '排序',
    level TINYINT DEFAULT 1 COMMENT '层级',
    status TINYINT DEFAULT 1 COMMENT '状态: 0禁用 1启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    KEY idx_parent_id (parent_id),
    KEY idx_service_code (service_code),
    KEY idx_sort (sort),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='城市服务表';

-- ============================================
-- 19. 订阅消息模块
-- ============================================

-- 订阅消息模板表
CREATE TABLE IF NOT EXISTS subscribe_template (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '模板ID',
    template_name VARCHAR(100) NOT NULL COMMENT '模板名称',
    template_code VARCHAR(50) NOT NULL COMMENT '模板编码(微信平台的template_id)',
    template_type TINYINT DEFAULT 1 COMMENT '模板类型: 1一次性订阅 2长期订阅',
    scene VARCHAR(100) COMMENT '使用场景',
    title VARCHAR(100) COMMENT '消息标题',
    content TEXT COMMENT '消息内容模板(JSON格式)',
    example TEXT COMMENT '示例内容',
    page_path VARCHAR(200) COMMENT '跳转页面路径',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态: 0禁用 1启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY uk_template_code (template_code),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订阅消息模板表';

-- 用户订阅消息记录表
CREATE TABLE IF NOT EXISTS user_subscribe (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    template_id BIGINT NOT NULL COMMENT '模板ID',
    template_code VARCHAR(50) NOT NULL COMMENT '模板编码',
    subscribe_type TINYINT DEFAULT 1 COMMENT '订阅类型: 1一次性订阅 2长期订阅',
    subscribe_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '订阅时间',
    expire_time DATETIME COMMENT '过期时间(一次性订阅专用)',
    used_count INT DEFAULT 0 COMMENT '已使用次数',
    max_count INT DEFAULT 1 COMMENT '最大使用次数',
    status TINYINT DEFAULT 1 COMMENT '状态: 0已取消 1订阅中 2已过期',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_user_id (user_id),
    KEY idx_template_id (template_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户订阅消息记录表';

-- ============================================
-- 20. 文件上传记录表
-- ============================================

CREATE TABLE IF NOT EXISTS upload_file (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '文件ID',
    file_name VARCHAR(200) NOT NULL COMMENT '原始文件名',
    file_path VARCHAR(500) NOT NULL COMMENT '文件存储路径',
    file_url VARCHAR(500) NOT NULL COMMENT '文件访问URL',
    file_size BIGINT DEFAULT 0 COMMENT '文件大小(字节)',
    file_type VARCHAR(50) COMMENT '文件类型(MIME类型)',
    file_extension VARCHAR(20) COMMENT '文件扩展名',
    module_type VARCHAR(50) COMMENT '所属模块: goods/avatar/article等',
    user_id BIGINT COMMENT '上传用户ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_file_type (file_type),
    KEY idx_module_type (module_type),
    KEY idx_user_id (user_id),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件上传记录表';
