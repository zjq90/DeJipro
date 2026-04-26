-- ============================================
-- 小程序后端API测试数据
-- 创建时间: 2026-04-26
-- 说明: 包含系统配置、会员等级、商品分类、商品数据、用户数据等
-- ============================================

USE dj_mall;

-- ============================================
-- 会员等级数据
-- ============================================
INSERT INTO user_level (level_name, level_value, min_experience, max_experience, discount, icon, description, privileges, create_time) VALUES
('普通会员', 1, 0, 999, 1.00, '', '普通会员，享受基础权益', '{"privileges":[]}', NOW()),
('银卡会员', 2, 1000, 4999, 0.95, '', '银卡会员，享受9.5折优惠', '{"privileges":["discount"]}', NOW()),
('金卡会员', 3, 5000, 19999, 0.90, '', '金卡会员，享受9折优惠，生日双倍积分', '{"privileges":["discount","doubleIntegral"]}', NOW()),
('钻石会员', 4, 20000, 49999, 0.85, '', '钻石会员，享受8.5折优惠，优先客服', '{"privileges":["discount","doubleIntegral","priorityService"]}', NOW()),
('至尊会员', 5, 50000, 999999999, 0.80, '', '至尊会员，享受8折优惠，专属客服，免费运费', '{"privileges":["discount","doubleIntegral","priorityService","freeShipping"]}', NOW());

-- ============================================
-- 角色数据
-- ============================================
INSERT INTO sys_role (role_name, role_code, description, privileges, status, create_time) VALUES
('普通用户', 'USER', '普通注册用户', '{"permissions":[]}', 1, NOW()),
('VIP会员', 'VIP', 'VIP会员用户', '{"permissions":["vip_privilege"]}', 1, NOW()),
('代理商', 'AGENT', '代理商用户', '{"permissions":["agent_privilege","commission"]}', 1, NOW()),
('合作伙伴', 'PARTNER', '合作伙伴用户', '{"permissions":["partner_privilege","high_commission","team_management"]}', 1, NOW());

-- ============================================
-- 商品分类数据
-- ============================================
INSERT INTO goods_category (parent_id, category_name, icon, banner, sort, level, status, create_time) VALUES
(0, '服饰箱包', '', '', 1, 1, 1, NOW()),
(0, '数码家电', '', '', 2, 1, 1, NOW()),
(0, '美妆个护', '', '', 3, 1, 1, NOW()),
(0, '食品生鲜', '', '', 4, 1, 1, NOW()),
(0, '家居日用', '', '', 5, 1, 1, NOW()),
(0, '母婴用品', '', '', 6, 1, 1, NOW()),
(0, '运动户外', '', '', 7, 1, 1, NOW()),
(0, '图书文具', '', '', 8, 1, 1, NOW()),

(1, '男装', '', '', 1, 2, 1, NOW()),
(1, '女装', '', '', 2, 2, 1, NOW()),
(1, '童装', '', '', 3, 2, 1, NOW()),
(1, '鞋靴', '', '', 4, 2, 1, NOW()),
(1, '箱包', '', '', 5, 2, 1, NOW()),

(2, '手机', '', '', 1, 2, 1, NOW()),
(2, '电脑', '', '', 2, 2, 1, NOW()),
(2, '平板', '', '', 3, 2, 1, NOW()),
(2, '耳机音响', '', '', 4, 2, 1, NOW()),
(2, '智能手表', '', '', 5, 2, 1, NOW()),

(3, '护肤', '', '', 1, 2, 1, NOW()),
(3, '彩妆', '', '', 2, 2, 1, NOW()),
(3, '个护', '', '', 3, 2, 1, NOW()),
(3, '香水', '', '', 4, 2, 1, NOW());

-- ============================================
-- 商品数据
-- ============================================
INSERT INTO goods (category_id, goods_name, goods_subtitle, main_image, sub_images, description, unit, weight, is_integral_goods, sell_price, market_price, cost_price, integral_price, stock, sell_count, view_count, favorite_count, comment_count, good_comment_rate, is_hot, is_recommend, is_new, status, sort, limit_buy, is_free_shipping, commission_rate, give_integral, tags, create_time) VALUES
(9, '男士纯棉T恤', '舒适透气，百搭款，多色可选', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=男士纯棉T恤商品图片%2C白色背景%2C电商产品摄影&image_size=square_hd', '["https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=男士纯棉T恤商品图片%2C白色背景%2C电商产品摄影&image_size=square_hd"]', '优质纯棉面料，柔软舒适，透气吸汗，简约大方的设计，适合日常穿着。', '件', 0.20, 0, 99.00, 199.00, 49.00, 0, 1000, 568, 2356, 89, 156, 98.50, 1, 1, 0, 1, 1, 0, 1, 0.1000, 10, '["热销","推荐","纯棉"]', NOW()),

(10, '女士连衣裙', '优雅气质，修身显瘦，夏季新品', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=女士连衣裙商品图片%2C白色背景%2C电商产品摄影&image_size=square_hd', '["https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=女士连衣裙商品图片%2C白色背景%2C电商产品摄影&image_size=square_hd"]', '精选优质面料，垂坠感好，修身显瘦设计，展现女性优雅气质。', '件', 0.30, 0, 199.00, 399.00, 99.00, 0, 500, 325, 1856, 56, 89, 97.80, 1, 1, 1, 1, 2, 0, 0, 0.1500, 20, '["新品","热销","优雅"]', NOW()),

(13, '智能手机Pro', '旗舰配置，超清屏幕，超长续航', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=智能手机商品图片%2C白色背景%2C电商产品摄影&image_size=square_hd', '["https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=智能手机商品图片%2C白色背景%2C电商产品摄影&image_size=square_hd"]', '最新旗舰处理器，超清2K屏幕，5000mAh大电池，支持快充，拍照效果出色。', '部', 0.18, 0, 3999.00, 4999.00, 2999.00, 0, 100, 189, 5689, 125, 89, 99.20, 1, 1, 0, 1, 3, 0, 0, 0.0500, 100, '["旗舰","拍照","快充"]', NOW()),

(14, '轻薄笔记本电脑', '高性能轻薄本，商务办公首选', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=笔记本电脑商品图片%2C白色背景%2C电商产品摄影&image_size=square_hd', '["https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=笔记本电脑商品图片%2C白色背景%2C电商产品摄影&image_size=square_hd"]', '全金属机身，轻薄便携，高性能处理器，大容量固态硬盘，高清屏幕，适合商务办公和学习。', '台', 1.50, 0, 5999.00, 6999.00, 4500.00, 0, 50, 78, 2356, 45, 36, 98.60, 0, 1, 0, 1, 4, 0, 0, 0.0500, 150, '["轻薄","高性能","商务"]', NOW()),

(19, '保湿补水精华液', '深层补水，持久保湿，改善肌肤', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=保湿精华液商品图片%2C白色背景%2C电商产品摄影&image_size=square_hd', '["https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=保湿精华液商品图片%2C白色背景%2C电商产品摄影&image_size=square_hd"]', '富含玻尿酸和多种植物精华，深层补水，持久保湿，改善干燥肌肤，令肌肤水润有光泽。', '瓶', 0.05, 0, 299.00, 499.00, 150.00, 0, 200, 456, 3256, 89, 125, 97.50, 1, 1, 0, 1, 5, 0, 1, 0.1200, 30, '["保湿","补水","护肤"]', NOW()),

(20, '丝绒哑光唇釉', '持久显色，丝绒质感，不沾杯', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=唇釉口红商品图片%2C白色背景%2C电商产品摄影&image_size=square_hd', '["https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=唇釉口红商品图片%2C白色背景%2C电商产品摄影&image_size=square_hd"]', '丝绒哑光质地，显色度高，持久不脱妆，多种色号可选，适合各种场合。', '支', 0.02, 0, 168.00, 299.00, 80.00, 0, 300, 689, 4568, 156, 236, 98.20, 1, 1, 1, 1, 6, 0, 1, 0.1500, 17, '["哑光","持久","显色"]', NOW()),

(1, '积分兑换毛巾', '纯棉毛巾，柔软吸水', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=纯棉毛巾商品图片%2C白色背景%2C电商产品摄影&image_size=square_hd', '["https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=纯棉毛巾商品图片%2C白色背景%2C电商产品摄影&image_size=square_hd"]', '优质纯棉材质，柔软舒适，吸水性强，适合家庭使用。', '条', 0.10, 1, 0.00, 29.90, 10.00, 500, 100, 0, 0, 0, 0, 100.00, 0, 0, 0, 1, 10, 0, 1, 0.0000, 0, '["积分兑换"]', NOW()),

(1, '积分兑换水杯', '304不锈钢保温杯', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=不锈钢保温杯商品图片%2C白色背景%2C电商产品摄影&image_size=square_hd', '["https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=不锈钢保温杯商品图片%2C白色背景%2C电商产品摄影&image_size=square_hd"]', '304不锈钢内胆，12小时保温保冷，大容量设计，适合日常使用。', '个', 0.30, 1, 0.00, 99.00, 30.00, 2000, 50, 0, 0, 0, 0, 100.00, 0, 0, 0, 1, 11, 0, 1, 0.0000, 0, '["积分兑换"]', NOW());

-- ============================================
-- 商品规格名数据
-- ============================================
INSERT INTO goods_spec_name (goods_id, spec_name, sort, create_time) VALUES
(1, '颜色', 1, NOW()),
(1, '尺码', 2, NOW()),
(2, '颜色', 1, NOW()),
(2, '尺码', 2, NOW()),
(3, '颜色', 1, NOW()),
(3, '存储容量', 2, NOW()),
(5, '颜色', 1, NOW()),
(6, '色号', 1, NOW());

-- ============================================
-- 商品规格值数据
-- ============================================
INSERT INTO goods_spec_value (spec_name_id, goods_id, spec_value, sort, create_time) VALUES
(1, 1, '白色', 1, NOW()),
(1, 1, '黑色', 2, NOW()),
(1, 1, '灰色', 3, NOW()),
(1, 1, '蓝色', 4, NOW()),
(2, 1, 'M', 1, NOW()),
(2, 1, 'L', 2, NOW()),
(2, 1, 'XL', 3, NOW()),
(2, 1, 'XXL', 4, NOW()),

(3, 2, '红色', 1, NOW()),
(3, 2, '黑色', 2, NOW()),
(3, 2, '蓝色', 3, NOW()),
(4, 2, 'S', 1, NOW()),
(4, 2, 'M', 2, NOW()),
(4, 2, 'L', 3, NOW()),
(4, 2, 'XL', 4, NOW()),

(5, 3, '星空黑', 1, NOW()),
(5, 3, '皓月白', 2, NOW()),
(5, 3, '极光蓝', 3, NOW()),
(6, 3, '128GB', 1, NOW()),
(6, 3, '256GB', 2, NOW()),
(6, 3, '512GB', 3, NOW()),

(7, 5, '经典款', 1, NOW()),
(7, 5, '升级版', 2, NOW()),
(8, 6, '豆沙色', 1, NOW()),
(8, 6, '正红色', 2, NOW()),
(8, 6, '南瓜色', 3, NOW()),
(8, 6, '复古红', 4, NOW());

-- ============================================
-- 商品SKU数据
-- ============================================
INSERT INTO goods_sku (goods_id, sku_name, specs, spec_value_ids, sell_price, cost_price, integral_price, stock, image, weight, code, barcode, status, create_time) VALUES
(1, '白色 M', '{"颜色":"白色","尺码":"M"}', '1,5', 99.00, 49.00, 0, 200, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=男士纯棉T恤白色M码商品图片%2C白色背景&image_size=square_hd', 0.20, 'SKU001001', '6901234567001', 1, NOW()),
(1, '白色 L', '{"颜色":"白色","尺码":"L"}', '1,6', 99.00, 49.00, 0, 200, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=男士纯棉T恤白色L码商品图片%2C白色背景&image_size=square_hd', 0.20, 'SKU001002', '6901234567002', 1, NOW()),
(1, '黑色 M', '{"颜色":"黑色","尺码":"M"}', '2,5', 99.00, 49.00, 0, 150, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=男士纯棉T恤黑色M码商品图片%2C白色背景&image_size=square_hd', 0.20, 'SKU001003', '6901234567003', 1, NOW()),
(1, '黑色 L', '{"颜色":"黑色","尺码":"L"}', '2,6', 99.00, 49.00, 0, 150, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=男士纯棉T恤黑色L码商品图片%2C白色背景&image_size=square_hd', 0.20, 'SKU001004', '6901234567004', 1, NOW()),

(2, '红色 S', '{"颜色":"红色","尺码":"S"}', '9,12', 199.00, 99.00, 0, 100, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=女士连衣裙红色S码商品图片%2C白色背景&image_size=square_hd', 0.30, 'SKU002001', '6901234567005', 1, NOW()),
(2, '红色 M', '{"颜色":"红色","尺码":"M"}', '9,13', 199.00, 99.00, 0, 100, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=女士连衣裙红色M码商品图片%2C白色背景&image_size=square_hd', 0.30, 'SKU002002', '6901234567006', 1, NOW()),
(2, '黑色 M', '{"颜色":"黑色","尺码":"M"}', '10,13', 199.00, 99.00, 0, 100, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=女士连衣裙黑色M码商品图片%2C白色背景&image_size=square_hd', 0.30, 'SKU002003', '6901234567007', 1, NOW()),
(2, '黑色 L', '{"颜色":"黑色","尺码":"L"}', '10,14', 199.00, 99.00, 0, 100, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=女士连衣裙黑色L码商品图片%2C白色背景&image_size=square_hd', 0.30, 'SKU002004', '6901234567008', 1, NOW()),

(3, '星空黑 128GB', '{"颜色":"星空黑","存储容量":"128GB"}', '17,20', 3999.00, 2999.00, 0, 30, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=智能手机星空黑128GB商品图片%2C白色背景&image_size=square_hd', 0.18, 'SKU003001', '6901234567009', 1, NOW()),
(3, '星空黑 256GB', '{"颜色":"星空黑","存储容量":"256GB"}', '17,21', 4499.00, 3499.00, 0, 30, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=智能手机星空黑256GB商品图片%2C白色背景&image_size=square_hd', 0.18, 'SKU003002', '6901234567010', 1, NOW()),
(3, '皓月白 128GB', '{"颜色":"皓月白","存储容量":"128GB"}', '18,20', 3999.00, 2999.00, 0, 20, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=智能手机皓月白128GB商品图片%2C白色背景&image_size=square_hd', 0.18, 'SKU003003', '6901234567011', 1, NOW()),
(3, '皓月白 256GB', '{"颜色":"皓月白","存储容量":"256GB"}', '18,21', 4499.00, 3499.00, 0, 20, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=智能手机皓月白256GB商品图片%2C白色背景&image_size=square_hd', 0.18, 'SKU003004', '6901234567012', 1, NOW()),

(5, '经典款', '{"颜色":"经典款"}', '24', 299.00, 150.00, 0, 100, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=保湿精华液经典款商品图片%2C白色背景&image_size=square_hd', 0.05, 'SKU005001', '6901234567013', 1, NOW()),
(5, '升级版', '{"颜色":"升级版"}', '25', 399.00, 200.00, 0, 100, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=保湿精华液升级版商品图片%2C白色背景&image_size=square_hd', 0.05, 'SKU005002', '6901234567014', 1, NOW()),

(6, '豆沙色', '{"色号":"豆沙色"}', '26', 168.00, 80.00, 0, 80, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=唇釉豆沙色商品图片%2C白色背景&image_size=square_hd', 0.02, 'SKU006001', '6901234567015', 1, NOW()),
(6, '正红色', '{"色号":"正红色"}', '27', 168.00, 80.00, 0, 80, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=唇釉正红色商品图片%2C白色背景&image_size=square_hd', 0.02, 'SKU006002', '6901234567016', 1, NOW()),
(6, '南瓜色', '{"色号":"南瓜色"}', '28', 168.00, 80.00, 0, 60, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=唇釉南瓜色商品图片%2C白色背景&image_size=square_hd', 0.02, 'SKU006003', '6901234567017', 1, NOW()),

(7, '积分毛巾', '{}', '', 0.00, 10.00, 500, 100, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=纯棉毛巾商品图片%2C白色背景&image_size=square_hd', 0.10, 'SKU007001', '6901234567018', 1, NOW()),

(8, '积分水杯', '{}', '', 0.00, 30.00, 2000, 50, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=不锈钢保温杯商品图片%2C白色背景&image_size=square_hd', 0.30, 'SKU008001', '6901234567019', 1, NOW());

-- ============================================
-- 签到配置数据
-- ============================================
INSERT INTO sign_config (day, integral, amount, description, create_time) VALUES
(1, 10, 0.00, '第1天签到', NOW()),
(2, 15, 0.00, '第2天签到', NOW()),
(3, 20, 0.00, '第3天签到', NOW()),
(4, 25, 0.00, '第4天签到', NOW()),
(5, 30, 0.50, '第5天签到', NOW()),
(6, 40, 1.00, '第6天签到', NOW()),
(7, 50, 2.00, '第7天签到', NOW());

-- ============================================
-- 充值额度配置数据
-- ============================================
INSERT INTO recharge_amount (amount, give_amount, give_integral, sort, status, description, create_time) VALUES
(10.00, 0.00, 0, 1, 1, '充值10元', NOW()),
(50.00, 5.00, 50, 2, 1, '充值50元送5元', NOW()),
(100.00, 15.00, 100, 3, 1, '充值100元送15元', NOW()),
(200.00, 40.00, 200, 4, 1, '充值200元送40元', NOW()),
(500.00, 120.00, 500, 5, 1, '充值500元送120元', NOW()),
(1000.00, 300.00, 1000, 6, 1, '充值1000元送300元', NOW());

-- ============================================
-- 提现配置数据
-- ============================================
INSERT INTO withdraw_config (min_amount, max_amount, daily_max_amount, daily_max_times, fee_rate, min_fee, max_fee, audit_required, bank_enable, alipay_enable, wechat_enable, description, create_time) VALUES
(1.00, 50000.00, 50000.00, 3, 0.0000, 0.00, 0.00, 1, 1, 1, 1, '最低提现1元，无手续费', NOW());

-- ============================================
-- 系统配置数据
-- ============================================
INSERT INTO sys_config (config_key, config_value, config_name, description, create_time) VALUES
('app_name', '得际商城', '应用名称', '小程序应用名称', NOW()),
('app_logo', '', '应用Logo', '应用Logo图片URL', NOW()),
('customer_service_phone', '400-123-4567', '客服电话', '客服热线电话', NOW()),
('register_give_integral', '100', '注册赠送积分', '新用户注册赠送积分数量', NOW()),
('register_give_amount', '0.00', '注册赠送金额', '新用户注册赠送金额', NOW()),
('invite_give_integral', '200', '邀请奖励积分', '邀请好友注册奖励积分', NOW()),
('invite_give_amount', '10.00', '邀请奖励金额', '邀请好友注册奖励金额', NOW()),
('order_auto_confirm_days', '15', '自动确认收货天数', '订单发货后自动确认收货天数', NOW()),
('order_auto_cancel_hours', '24', '自动取消订单小时', '下单后未支付自动取消小时数', NOW()),
('order_commission_settle_days', '7', '佣金结算天数', '订单完成后佣金结算天数', NOW());
