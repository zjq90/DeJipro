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
}
