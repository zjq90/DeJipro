-- ============================================
-- 新功能模块测试数据
-- 创建时间: 2026-04-28
-- 说明: 包含推广管理、优惠券、拼团、砍价、提货点、文章、订单、城市服务、订阅消息等模块的测试数据
-- ============================================

USE dj_mall;

-- ============================================
-- 推广管理模块测试数据
-- ============================================

-- 推广海报配置
INSERT INTO promotion_poster (poster_name, poster_type, background_image, qrcode_x, qrcode_y, qrcode_width, qrcode_height, avatar_x, avatar_y, avatar_width, avatar_height, nickname_x, nickname_y, nickname_font_size, nickname_color, sort, status, create_time) VALUES
('推广海报-简约版', 1, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=简约风格推广海报背景%2C商城推广%2C白色和蓝色渐变&image_size=portrait_4_3', 100, 400, 200, 200, 50, 50, 80, 80, 150, 70, 24, '#333333', 1, 1, NOW()),
('推广海报-商务版', 1, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=商务风格推广海报背景%2C金色和深色%2C高端大气&image_size=portrait_4_3', 120, 450, 180, 180, 60, 60, 70, 70, 150, 80, 22, '#ffffff', 2, 1, NOW()),
('邀新海报-好友版', 2, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=邀请好友注册海报背景%2C温馨风格%2C粉色和暖色调&image_size=portrait_4_3', 110, 380, 190, 190, 55, 55, 75, 75, 145, 75, 23, '#ff6b6b', 1, 1, NOW()),
('邀新海报-奖励版', 2, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=邀请奖励海报背景%2C金币红包元素%2C喜庆风格&image_size=portrait_4_3', 105, 420, 200, 200, 50, 50, 80, 80, 150, 70, 24, '#ff4757', 2, 1, NOW());

-- 推广统计数据(最近7天)
INSERT INTO promotion_stat (user_id, stat_date, pv_count, uv_count, register_count, order_count, order_amount, commission_amount, create_time) VALUES
(1, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 156, 89, 5, 3, 899.00, 45.00, NOW()),
(1, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 234, 120, 8, 5, 1560.00, 78.00, NOW()),
(1, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 189, 95, 6, 4, 1200.00, 60.00, NOW()),
(1, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 312, 156, 12, 8, 2450.00, 122.50, NOW()),
(1, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 256, 130, 9, 6, 1890.00, 94.50, NOW()),
(1, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 198, 105, 7, 5, 1450.00, 72.50, NOW()),
(1, CURDATE(), 89, 45, 3, 2, 560.00, 28.00, NOW());

-- ============================================
-- 优惠券模块测试数据
-- ============================================

INSERT INTO coupon (coupon_name, coupon_type, discount_type, discount_amount, min_amount, max_discount_amount, total_count, receive_count, use_count, per_limit, receive_start_time, receive_end_time, valid_start_time, valid_end_time, valid_days, apply_type, apply_value, description, sort, status, create_time) VALUES
('新人专享券', 4, 1, 50.00, 200.00, 0.00, 1000, 156, 89, 1, DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_ADD(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_ADD(NOW(), INTERVAL 60 DAY), 0, 0, NULL, '新用户专享，满200减50', 1, 1, NOW()),
('满100减20券', 1, 1, 20.00, 100.00, 0.00, 5000, 1234, 567, 3, DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_ADD(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_ADD(NOW(), INTERVAL 30 DAY), 0, 0, NULL, '满100元减20元，全场通用', 2, 1, NOW()),
('满200减50券', 1, 1, 50.00, 200.00, 0.00, 3000, 890, 423, 2, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_ADD(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_ADD(NOW(), INTERVAL 40 DAY), 0, 0, NULL, '满200元减50元，全场通用', 3, 1, NOW()),
('8折优惠券', 2, 2, 0.80, 0.00, 100.00, 2000, 567, 234, 1, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_ADD(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_ADD(NOW(), INTERVAL 20 DAY), 0, 0, NULL, '全场8折，最高优惠100元', 4, 1, NOW()),
('无门槛10元券', 3, 1, 10.00, 0.00, 0.00, 10000, 3456, 1890, 5, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), NOW(), DATE_ADD(NOW(), INTERVAL 14 DAY), 7, 0, NULL, '无门槛10元优惠券，领取后7天内有效', 5, 1, NOW()),
('服饰品类券', 1, 1, 30.00, 150.00, 0.00, 2000, 456, 189, 2, DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_ADD(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_ADD(NOW(), INTERVAL 14 DAY), 0, 1, '[1,2,3,4,5]', '服饰箱包品类专用，满150减30', 6, 1, NOW());

-- ============================================
-- 拼团模块测试数据
-- ============================================

INSERT INTO group_goods (goods_id, sku_id, activity_name, group_type, min_people, max_people, group_price, original_price, total_stock, used_stock, limit_buy, start_time, end_time, valid_hours, description, sort, is_recommend, is_hot, status, create_time) VALUES
(1, NULL, '纯棉T恤拼团特惠', 1, 2, 10, 69.00, 99.00, 500, 156, 2, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_ADD(NOW(), INTERVAL 7 DAY), 24, '2人即可成团，享受拼团特惠价', 1, 1, 1, 1, NOW()),
(2, NULL, '连衣裙拼团活动', 1, 3, 6, 159.00, 199.00, 200, 89, 1, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_ADD(NOW(), INTERVAL 5 DAY), 48, '3人成团，限时特惠', 2, 1, 0, 1, NOW()),
(5, NULL, '保湿精华液拼团', 2, 2, 5, 199.00, 299.00, 300, 123, 1, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 10 DAY), 24, '老带新拼团，新人专享价', 3, 0, 1, 1, NOW()),
(6, NULL, '唇釉团长免单', 3, 3, 5, 99.00, 168.00, 150, 45, 1, NOW(), DATE_ADD(NOW(), INTERVAL 14 DAY), 72, '团长免单活动，3人成团团长免费', 4, 1, 1, 1, NOW());

-- ============================================
-- 砍价模块测试数据
-- ============================================

INSERT INTO bargain_goods (goods_id, sku_id, activity_name, original_price, bargain_price, min_bargain, max_bargain, total_stock, used_stock, limit_buy, limit_help, start_time, end_time, valid_hours, description, sort, is_recommend, is_hot, status, create_time) VALUES
(1, NULL, '纯棉T恤砍价0元拿', 99.00, 0.00, 1.00, 20.00, 100, 23, 1, 5, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_ADD(NOW(), INTERVAL 14 DAY), 48, '邀请好友帮砍，最低0元拿', 1, 1, 1, 1, NOW()),
(2, NULL, '连衣裙砍价特惠', 199.00, 99.00, 5.00, 30.00, 80, 12, 1, 8, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_ADD(NOW(), INTERVAL 10 DAY), 72, '砍到99元即可购买', 2, 1, 0, 1, NOW()),
(3, NULL, '手机砍价活动', 3999.00, 2999.00, 20.00, 200.00, 50, 8, 1, 10, NOW(), DATE_ADD(NOW(), INTERVAL 20 DAY), 120, '邀请好友帮砍，最多省1000元', 3, 0, 1, 1, NOW()),
(5, NULL, '精华液砍价5折', 299.00, 149.00, 3.00, 50.00, 150, 34, 1, 6, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_ADD(NOW(), INTERVAL 7 DAY), 48, '限时砍价5折优惠', 4, 1, 1, 1, NOW());

-- ============================================
-- 提货点模块测试数据
-- ============================================

INSERT INTO pickup_point (point_name, point_code, contact_person, contact_phone, province, city, district, address, full_address, longitude, latitude, business_hours, images, description, sort, status, create_time) VALUES
('得际商城-朝阳店', 'DJ-001', '张经理', '13800138001', '北京市', '北京市', '朝阳区', '建国路88号SOHO现代城A座1层', '北京市北京市朝阳区建国路88号SOHO现代城A座1层', 116.481200, 39.912300, '[{"start":"09:00","end":"21:00"}]', '["https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=便利店门店外观%2C现代风格%2C明亮整洁&image_size=square_hd"]', '朝阳区核心商圈，交通便利', 1, 1, NOW()),
('得际商城-海淀店', 'DJ-002', '李店长', '13800138002', '北京市', '北京市', '海淀区', '中关村大街1号中关村大厦B座', '北京市北京市海淀区中关村大街1号中关村大厦B座', 116.321500, 39.982300, '[{"start":"08:30","end":"22:00"}]', '["https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=科技感门店外观%2C中关村风格&image_size=square_hd"]', '中关村科技园区，24小时服务', 2, 1, NOW()),
('得际商城-浦东店', 'DJ-003', '王主管', '13800138003', '上海市', '上海市', '浦东新区', '陆家嘴环路1000号恒生银行大厦', '上海市上海市浦东新区陆家嘴环路1000号恒生银行大厦', 121.503000, 31.242000, '[{"start":"09:00","end":"21:30"}]', '["https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=高端商务门店外观%2C陆家嘴风格&image_size=square_hd"]', '陆家嘴金融中心，高端服务', 3, 1, NOW()),
('得际商城-天河店', 'DJ-004', '陈店长', '13800138004', '广东省', '广州市', '天河区', '天河路385号太古汇广场', '广东省广州市天河区天河路385号太古汇广场', 113.332000, 23.128000, '[{"start":"10:00","end":"22:00"}]', '["https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=购物中心门店外观%2C广州风格&image_size=square_hd"]', '太古汇商圈，购物便利', 4, 1, NOW());

-- ============================================
-- 文章模块测试数据
-- ============================================

-- 文章分类
INSERT INTO article_category (parent_id, category_name, icon, sort, level, status, create_time) VALUES
(0, '商城公告', '', 1, 1, 1, NOW()),
(0, '活动资讯', '', 2, 1, 1, NOW()),
(0, '商品评测', '', 3, 1, 1, NOW()),
(0, '使用教程', '', 4, 1, 1, NOW()),
(0, '品牌故事', '', 5, 1, 1, NOW()),
(2, '限时活动', '', 1, 2, 1, NOW()),
(2, '新人专享', '', 2, 2, 1, NOW()),
(2, '节日特惠', '', 3, 2, 1, NOW());

-- 文章
INSERT INTO article (category_id, article_title, article_subtitle, cover_image, author, source, summary, content, view_count, like_count, favorite_count, comment_count, share_count, is_top, is_hot, is_recommend, allow_comment, sort, status, publish_time, create_time) VALUES
(1, '得际商城正式上线公告', '欢迎来到得际商城，开启您的购物之旅', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=商城上线公告配图%2C庆祝风格%2C金色气球&image_size=square_hd', '系统管理员', '得际商城', '得际商城正式上线，为您提供优质的购物体验和丰富的商品选择。', '<h3>欢迎来到得际商城</h3><p>感谢您选择得际商城，我们致力于为您提供优质的购物体验。</p><h4>我们的优势</h4><ul><li>优质商品，品质保证</li><li>优惠活动，惊喜不断</li><li>快速配送，贴心服务</li></ul>', 5689, 256, 123, 89, 456, 1, 1, 1, 1, 1, 1, DATE_SUB(NOW(), INTERVAL 30 DAY), NOW()),
(2, '限时特惠活动火热进行中', '全场满减，限时抢购', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=限时特惠活动配图%2C红色喜庆风格%2C折扣标签&image_size=square_hd', '活动运营', '得际商城', '限时特惠活动火热进行中，全场满100减20，满200减50，快来抢购吧！', '<h3>限时特惠活动</h3><p>活动时间：即日起至本月底</p><h4>活动内容</h4><ul><li>全场满100减20</li><li>全场满200减50</li><li>新人专享满200减50</li></ul>', 3456, 189, 78, 56, 234, 0, 1, 1, 1, 2, 1, DATE_SUB(NOW(), INTERVAL 15 DAY), NOW()),
(3, '夏季护肤攻略：选对产品很重要', '专业评测师为您推荐夏季护肤必备品', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=护肤攻略配图%2C清新风格%2C护肤品展示&image_size=square_hd', '护肤达人小美', '得际商城', '夏季来临，如何选择适合的护肤品？本文为您详细介绍夏季护肤的要点和推荐产品。', '<h3>夏季护肤要点</h3><p>夏季气温高，皮肤容易出油出汗，护肤重点应该是清洁、补水、防晒。</p><h4>推荐产品</h4><ul><li>保湿精华液：深层补水</li><li>防晒霜：SPF50+ PA++++</li><li>控油洁面乳：温和清洁</li></ul>', 8956, 567, 234, 156, 678, 0, 1, 1, 1, 3, 1, DATE_SUB(NOW(), INTERVAL 7 DAY), NOW()),
(4, '新用户注册教程', '三分钟快速上手得际商城', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=注册教程配图%2C简洁风格%2C手机界面&image_size=square_hd', '客服小助手', '得际商城', '详细介绍如何在得际商城注册账号、完善信息、开始购物。', '<h3>注册流程</h3><ol><li>打开得际商城小程序</li><li>点击"我的"进入个人中心</li><li>点击"注册/登录"</li><li>输入手机号获取验证码</li><li>设置密码完成注册</li></ol>', 2345, 123, 89, 45, 345, 1, 0, 1, 1, 4, 1, DATE_SUB(NOW(), INTERVAL 20 DAY), NOW());

-- 轮播图
INSERT INTO banner (banner_name, banner_type, image_url, link_type, link_value, sort, status, start_time, end_time, create_time) VALUES
('首页轮播-限时特惠', 1, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=限时特惠轮播图%2C红色喜庆%2C50%OFF标签&image_size=landscape_16_9', 3, '/pages/activity/list', 1, 1, DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_ADD(NOW(), INTERVAL 15 DAY), NOW()),
('首页轮播-新人专享', 1, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=新人专享轮播图%2C温馨粉色%2C新人礼包元素&image_size=landscape_16_9', 1, '1', 2, 1, DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_ADD(NOW(), INTERVAL 30 DAY), NOW()),
('首页轮播-拼团活动', 1, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=拼团活动轮播图%2C多人拼团元素%2C橙色活力&image_size=landscape_16_9', 3, '/pages/group/list', 3, 1, DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_ADD(NOW(), INTERVAL 20 DAY), NOW()),
('文章轮播-护肤攻略', 2, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=护肤攻略文章轮播图%2C清新自然风格&image_size=landscape_16_9', 2, '3', 1, 1, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_ADD(NOW(), INTERVAL 10 DAY), NOW()),
('活动轮播-砍价0元拿', 3, 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=砍价活动轮播图%2C0元拿元素%2C绿色省钱风格&image_size=landscape_16_9', 3, '/pages/bargain/list', 1, 1, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_ADD(NOW(), INTERVAL 25 DAY), NOW());

-- ============================================
-- 城市服务模块测试数据
-- ============================================

INSERT INTO city_service (parent_id, service_name, service_code, icon, image, link_type, link_value, description, sort, level, status, create_time) VALUES
(0, '生活服务', 'LIFE_SERVICE', '', '', 0, '', '日常生活相关服务', 1, 1, 1, NOW()),
(0, '交通出行', 'TRANSPORT', '', '', 0, '', '交通出行相关服务', 2, 1, 1, NOW()),
(0, '政务服务', 'GOVERNMENT', '', '', 0, '', '政务办理相关服务', 3, 1, 1, NOW()),
(0, '医疗健康', 'HEALTH', '', '', 0, '', '医疗健康相关服务', 4, 1, 1, NOW()),
(0, '教育学习', 'EDUCATION', '', '', 0, '', '教育学习相关服务', 5, 1, 1, NOW()),

(1, '外卖订餐', 'TAKEOUT', '', '', 3, 'https://waimai.example.com', '在线外卖订餐服务', 1, 2, 1, NOW()),
(1, '超市配送', 'SUPERMARKET', '', '', 3, 'https://supermarket.example.com', '超市商品送货上门', 2, 2, 1, NOW()),
(1, '家政服务', 'HOUSEKEEPING', '', '', 3, 'https://housekeeping.example.com', '家政保洁、维修服务', 3, 2, 1, NOW()),
(1, '快递服务', 'EXPRESS', '', '', 3, 'https://express.example.com', '快递查询、寄件服务', 4, 2, 1, NOW()),

(2, '公交地铁', 'PUBLIC_TRANSIT', '', '', 3, 'https://bus.example.com', '公交地铁查询服务', 1, 2, 1, NOW()),
(2, '打车服务', 'TAXI', '', '', 3, 'https://taxi.example.com', '在线打车服务', 2, 2, 1, NOW()),
(2, '停车缴费', 'PARKING', '', '', 3, 'https://parking.example.com', '停车场查询和缴费', 3, 2, 1, NOW()),

(3, '社保查询', 'SOCIAL_SECURITY', '', '', 3, 'https://social.example.com', '社保公积金查询', 1, 2, 1, NOW()),
(3, '违章查询', 'TRAFFIC_VIOLATION', '', '', 3, 'https://violation.example.com', '交通违章查询', 2, 2, 1, NOW()),
(3, '证件办理', 'CERTIFICATE', '', '', 3, 'https://certificate.example.com', '各类证件在线办理', 3, 2, 1, NOW()),

(4, '预约挂号', 'APPOINTMENT', '', '', 3, 'https://hospital.example.com', '医院预约挂号服务', 1, 2, 1, NOW()),
(4, '药店服务', 'PHARMACY', '', '', 3, 'https://pharmacy.example.com', '在线购药送药服务', 2, 2, 1, NOW()),
(4, '健康咨询', 'HEALTH_CONSULT', '', '', 3, 'https://health.example.com', '在线健康咨询服务', 3, 2, 1, NOW());

-- ============================================
-- 订阅消息模块测试数据
-- ============================================

INSERT INTO subscribe_template (template_name, template_code, template_type, scene, title, content, example, page_path, sort, status, create_time) VALUES
('订单支付成功通知', 'OPENTM412345678', 1, '订单支付', '订单支付成功', '{"first":{"value":"您的订单已支付成功"},"keyword1":{"value":"202404280001"},"keyword2":{"value":"299.00元"},"keyword3":{"value":"2024-04-28 10:30:00"},"remark":{"value":"感谢您的购买，我们将尽快为您发货"}}', '订单号：202404280001，支付金额：299.00元', '/pages/order/detail', 1, 1, NOW()),
('订单发货通知', 'OPENTM412345679', 1, '订单发货', '订单已发货', '{"first":{"value":"您的订单已发货"},"keyword1":{"value":"202404280001"},"keyword2":{"value":"顺丰速运"},"keyword3":{"value":"SF1234567890"},"remark":{"value":"点击查看物流详情"}}', '订单号：202404280001，物流公司：顺丰速运', '/pages/order/logistics', 2, 1, NOW()),
('优惠券到账通知', 'OPENTM412345680', 1, '优惠券', '优惠券已到账', '{"first":{"value":"恭喜您获得新的优惠券"},"keyword1":{"value":"满100减20券"},"keyword2":{"value":"满100元减20元"},"keyword3":{"value":"2024-05-28"},"remark":{"value":"快去使用吧"}}', '优惠券名称：满100减20券，有效期至2024-05-28', '/pages/coupon/list', 3, 1, NOW()),
('拼团成功通知', 'OPENTM412345681', 1, '拼团活动', '拼团成功', '{"first":{"value":"恭喜您，拼团成功了"},"keyword1":{"value":"纯棉T恤拼团"},"keyword2":{"value":"69.00元"},"keyword3":{"value":"3人团"},"remark":{"value":"商品将尽快为您发出"}}', '拼团商品：纯棉T恤拼团，拼团价格：69.00元', '/pages/group/detail', 4, 1, NOW()),
('砍价成功通知', 'OPENTM412345682', 1, '砍价活动', '砍价成功', '{"first":{"value":"恭喜您，砍价成功了"},"keyword1":{"value":"纯棉T恤砍价"},"keyword2":{"value":"0.00元"},"keyword3":{"value":"5人帮砍"},"remark":{"value":"快去下单购买吧"}}', '砍价商品：纯棉T恤砍价，最终价格：0.00元', '/pages/bargain/detail', 5, 1, NOW());

-- ============================================
-- 订单模块测试数据
-- ============================================

-- 订单
INSERT INTO order_info (order_no, parent_order_no, user_id, order_type, order_source, order_status, pay_status, goods_amount, freight_amount, discount_amount, coupon_amount, integral_amount, pay_amount, used_integral, give_integral, coupon_id, receiver_name, receiver_phone, receiver_province, receiver_city, receiver_district, receiver_address, receiver_full_address, pickup_type, user_remark, pay_type, pay_time, is_comment, create_time) VALUES
('DJ202604280001', NULL, 1, 1, 1, 3, 1, 299.00, 0.00, 20.00, 20.00, 0.00, 279.00, 0, 30, 2, '张三', '13800138001', '北京市', '北京市', '朝阳区', '建国路88号', '北京市北京市朝阳区建国路88号', 1, '请尽快发货', 2, DATE_SUB(NOW(), INTERVAL 3 DAY), 1, DATE_SUB(NOW(), INTERVAL 5 DAY)),
('DJ202604280002', NULL, 1, 1, 1, 1, 1, 199.00, 10.00, 0.00, 0.00, 0.00, 209.00, 0, 20, NULL, '李四', '13800138002', '上海市', '上海市', '浦东新区', '陆家嘴环路1000号', '上海市上海市浦东新区陆家嘴环路1000号', 1, '', 2, DATE_SUB(NOW(), INTERVAL 1 DAY), 0, DATE_SUB(NOW(), INTERVAL 2 DAY)),
('DJ202604280003', NULL, 1, 2, 1, 1, 1, 69.00, 0.00, 30.00, 0.00, 0.00, 69.00, 0, 7, NULL, '王五', '13800138003', '广东省', '广州市', '天河区', '天河路385号', '广东省广州市天河区天河路385号', 2, '', 2, DATE_SUB(NOW(), INTERVAL 12 HOUR), 0, DATE_SUB(NOW(), INTERVAL 12 HOUR)),
('DJ202604280004', NULL, 1, 1, 1, 0, 0, 599.00, 0.00, 50.00, 50.00, 0.00, 549.00, 0, 60, 3, '赵六', '13800138004', '北京市', '北京市', '海淀区', '中关村大街1号', '北京市北京市海淀区中关村大街1号', 1, '', NULL, NULL, 0, NOW()),
('DJ202604280005', NULL, 1, 1, 1, 4, 0, 159.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0, 0, NULL, '钱七', '13800138005', '浙江省', '杭州市', '西湖区', '文三路100号', '浙江省杭州市西湖区文三路100号', 1, '', NULL, NULL, 0, DATE_SUB(NOW(), INTERVAL 25 HOUR));

-- 订单商品
INSERT INTO order_item (order_id, order_no, user_id, goods_id, sku_id, goods_name, goods_image, sku_name, sku_specs, unit, quantity, original_price, sell_price, discount_amount, total_amount, give_integral, is_comment, create_time) VALUES
(1, 'DJ202604280001', 1, 5, 15, '保湿补水精华液', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=保湿精华液商品图片&image_size=square_hd', '经典款', '{"颜色":"经典款"}', '瓶', 1, 299.00, 299.00, 20.00, 279.00, 30, 1, DATE_SUB(NOW(), INTERVAL 5 DAY)),
(2, 'DJ202604280002', 1, 2, 5, '女士连衣裙', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=女士连衣裙商品图片&image_size=square_hd', '红色 M', '{"颜色":"红色","尺码":"M"}', '件', 1, 199.00, 199.00, 0.00, 199.00, 20, 0, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(3, 'DJ202604280003', 1, 1, 1, '男士纯棉T恤', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=男士纯棉T恤商品图片&image_size=square_hd', '白色 M', '{"颜色":"白色","尺码":"M"}', '件', 1, 99.00, 69.00, 0.00, 69.00, 7, 0, DATE_SUB(NOW(), INTERVAL 12 HOUR)),
(4, 'DJ202604280004', 1, 3, 13, '智能手机Pro', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=智能手机商品图片&image_size=square_hd', '星空黑 256GB', '{"颜色":"星空黑","存储容量":"256GB"}', '部', 1, 4499.00, 4499.00, 0.00, 4499.00, 450, 0, NOW()),
(4, 'DJ202604280004', 1, 6, 17, '丝绒哑光唇釉', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=唇釉口红商品图片&image_size=square_hd', '正红色', '{"色号":"正红色"}', '支', 1, 168.00, 168.00, 0.00, 168.00, 17, 0, NOW()),
(5, 'DJ202604280005', 1, 1, 2, '男士纯棉T恤', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=男士纯棉T恤商品图片&image_size=square_hd', '白色 L', '{"颜色":"白色","尺码":"L"}', '件', 1, 99.00, 99.00, 0.00, 99.00, 10, 0, DATE_SUB(NOW(), INTERVAL 25 HOUR));

-- 支付记录
INSERT INTO pay_record (pay_no, order_id, order_no, user_id, pay_type, pay_amount, pay_status, third_pay_no, pay_time, create_time) VALUES
('PAY202604280001', 1, 'DJ202604280001', 1, 2, 279.00, 1, 'wx202604280001', DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY)),
('PAY202604280002', 2, 'DJ202604280002', 1, 2, 209.00, 1, 'wx202604280002', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),
('PAY202604280003', 3, 'DJ202604280003', 1, 2, 69.00, 1, 'wx202604280003', DATE_SUB(NOW(), INTERVAL 12 HOUR), DATE_SUB(NOW(), INTERVAL 12 HOUR));

-- ============================================
-- 用户优惠券测试数据
-- ============================================

INSERT INTO user_coupon (user_id, coupon_id, coupon_name, coupon_type, discount_type, discount_amount, min_amount, status, receive_time, valid_start_time, valid_end_time, create_time) VALUES
(1, 2, '满100减20券', 1, 1, 20.00, 100.00, 0, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_ADD(NOW(), INTERVAL 15 DAY), NOW()),
(1, 3, '满200减50券', 1, 1, 50.00, 200.00, 0, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_ADD(NOW(), INTERVAL 30 DAY), NOW()),
(1, 5, '无门槛10元券', 3, 1, 10.00, 0.00, 0, DATE_SUB(NOW(), INTERVAL 1 DAY), NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), NOW()),
(1, 2, '满100减20券', 1, 1, 20.00, 100.00, 1, DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_ADD(NOW(), INTERVAL 15 DAY), NOW()),
(1, 4, '8折优惠券', 2, 2, 0.80, 0.00, 2, DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_ADD(NOW(), INTERVAL 5 DAY), NOW());
