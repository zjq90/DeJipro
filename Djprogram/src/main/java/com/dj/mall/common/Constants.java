package com.dj.mall.common;

public class Constants {

    public static final String DEFAULT_PASSWORD = "123456";

    public static final String TOKEN_PREFIX = "token:";

    public static final String SMS_CODE_PREFIX = "sms:code:";

    public static final String USER_INFO_PREFIX = "user:info:";

    public static final Integer SMS_CODE_EXPIRE = 5;

    public static final Integer TOKEN_EXPIRE = 7 * 24 * 60 * 60;

    public static class Status {
        public static final Integer DISABLE = 0;
        public static final Integer ENABLE = 1;
        public static final Integer DELETED = 2;
    }

    public static class UserStatus {
        public static final Integer DISABLE = 0;
        public static final Integer NORMAL = 1;
        public static final Integer CANCEL = 2;
    }

    public static class Gender {
        public static final Integer UNKNOWN = 0;
        public static final Integer MALE = 1;
        public static final Integer FEMALE = 2;
    }

    public static class LoginType {
        public static final Integer PASSWORD = 1;
        public static final Integer PHONE = 2;
        public static final Integer SMS_CODE = 3;
        public static final Integer WECHAT = 4;
    }

    public static class SmsCodeType {
        public static final Integer REGISTER = 1;
        public static final Integer LOGIN = 2;
        public static final Integer RESET_PASSWORD = 3;
        public static final Integer BIND_PHONE = 4;
    }

    public static class AuthStatus {
        public static final Integer PENDING = 0;
        public static final Integer PASS = 1;
        public static final Integer REJECT = 2;
    }

    public static class DefaultFlag {
        public static final Integer NO = 0;
        public static final Integer YES = 1;
    }

    public static class BalanceType {
        public static final Integer RECHARGE = 1;
        public static final Integer CONSUME = 2;
        public static final Integer REFUND = 3;
        public static final Integer WITHDRAW = 4;
        public static final Integer COMMISSION = 5;
    }

    public static class CommissionStatus {
        public static final Integer PENDING = 0;
        public static final Integer SETTLED = 1;
        public static final Integer CANCEL = 2;
    }

    public static class IntegralType {
        public static final Integer GAIN = 1;
        public static final Integer CONSUME = 2;
        public static final Integer EXPIRE = 3;
        public static final Integer REFUND = 4;
    }

    public static class IntegralSource {
        public static final Integer CONSUME = 1;
        public static final Integer SIGN = 2;
        public static final Integer ACTIVITY = 3;
        public static final Integer REGISTER = 4;
        public static final Integer INVITE = 5;
    }

    public static class WithdrawType {
        public static final Integer BANK = 1;
        public static final Integer ALIPAY = 2;
        public static final Integer WECHAT = 3;
    }

    public static class WithdrawStatus {
        public static final Integer PENDING = 0;
        public static final Integer PROCESSING = 1;
        public static final Integer SUCCESS = 2;
        public static final Integer REJECT = 3;
        public static final Integer CANCEL = 4;
    }

    public static class RechargeStatus {
        public static final Integer PENDING = 0;
        public static final Integer PAID = 1;
        public static final Integer CANCEL = 2;
        public static final Integer REFUND = 3;
    }

    public static class PayType {
        public static final Integer ALIPAY = 1;
        public static final Integer WECHAT_MINI = 2;
        public static final Integer WECHAT_APP = 3;
        public static final Integer WECHAT_OFFICIAL = 4;
    }

    public static class Platform {
        public static final Integer MINI_PROGRAM = 1;
        public static final Integer APP = 2;
        public static final Integer OFFICIAL_ACCOUNT = 3;
    }

    public static class GoodsStatus {
        public static final Integer OFF_SHELF = 0;
        public static final Integer ON_SHELF = 1;
    }

    public static class CommentStatus {
        public static final Integer HIDE = 0;
        public static final Integer SHOW = 1;
    }

    public static class OrderStatus {
        public static final Integer PENDING_PAYMENT = 0;
        public static final Integer PENDING_SHIPMENT = 1;
        public static final Integer PENDING_RECEIVE = 2;
        public static final Integer COMPLETED = 3;
        public static final Integer CANCELLED = 4;
        public static final Integer REFUNDED = 5;
    }

    public static class PayStatus {
        public static final Integer UNPAID = 0;
        public static final Integer PAID = 1;
        public static final Integer PARTIAL_REFUND = 2;
        public static final Integer FULL_REFUND = 3;
    }

    public static class OrderType {
        public static final Integer NORMAL = 1;
        public static final Integer GROUP = 2;
        public static final Integer BARGAIN = 3;
        public static final Integer INTEGRAL = 4;
    }

    public static class OrderSource {
        public static final Integer MINI_PROGRAM = 1;
        public static final Integer APP = 2;
        public static final Integer H5 = 3;
    }

    public static class PickupType {
        public static final Integer DELIVERY = 1;
        public static final Integer PICKUP = 2;
    }

    public static class CouponType {
        public static final Integer FULL_REDUCTION = 1;
        public static final Integer DISCOUNT = 2;
        public static final Integer NO_THRESHOLD = 3;
        public static final Integer NEW_USER = 4;
    }

    public static class DiscountType {
        public static final Integer AMOUNT = 1;
        public static final Integer DISCOUNT = 2;
    }

    public static class CouponApplyType {
        public static final Integer ALL = 0;
        public static final Integer CATEGORY = 1;
        public static final Integer GOODS = 2;
    }

    public static class UserCouponStatus {
        public static final Integer UNUSED = 0;
        public static final Integer USED = 1;
        public static final Integer EXPIRED = 2;
        public static final Integer INVALID = 3;
    }

    public static class GroupType {
        public static final Integer NORMAL = 1;
        public static final Integer OLD_NEW = 2;
        public static final Integer LEADER_FREE = 3;
    }

    public static class GroupActivityStatus {
        public static final Integer IN_PROGRESS = 0;
        public static final Integer SUCCESS = 1;
        public static final Integer FAILED = 2;
    }

    public static class GroupOrderStatus {
        public static final Integer PENDING_PAYMENT = 0;
        public static final Integer PAID = 1;
        public static final Integer CANCELLED = 2;
        public static final Integer REFUNDED = 3;
    }

    public static class BargainActivityStatus {
        public static final Integer IN_PROGRESS = 0;
        public static final Integer COMPLETED = 1;
        public static final Integer PURCHASED = 2;
        public static final Integer EXPIRED = 3;
        public static final Integer CANCELLED = 4;
    }

    public static class PosterType {
        public static final Integer PROMOTION = 1;
        public static final Integer INVITE = 2;
    }

    public static class ArticleStatus {
        public static final Integer OFF_SHELF = 0;
        public static final Integer ON_SHELF = 1;
    }

    public static class BannerType {
        public static final Integer HOME = 1;
        public static final Integer ARTICLE = 2;
        public static final Integer ACTIVITY = 3;
    }

    public static class BannerLinkType {
        public static final Integer NONE = 0;
        public static final Integer GOODS = 1;
        public static final Integer ARTICLE = 2;
        public static final Integer ACTIVITY = 3;
        public static final Integer EXTERNAL = 4;
    }

    public static class ServiceLinkType {
        public static final Integer NONE = 0;
        public static final Integer INTERNAL = 1;
        public static final Integer EXTERNAL = 2;
        public static final Integer MINI_PROGRAM = 3;
    }

    public static class SubscribeType {
        public static final Integer ONCE = 1;
        public static final Integer LONG_TERM = 2;
    }

    public static class UserSubscribeStatus {
        public static final Integer CANCELLED = 0;
        public static final Integer ACTIVE = 1;
        public static final Integer EXPIRED = 2;
    }
}
