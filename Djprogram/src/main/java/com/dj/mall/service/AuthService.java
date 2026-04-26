package com.dj.mall.service;

import com.dj.mall.dto.LoginDTO;
import com.dj.mall.dto.RegisterDTO;
import com.dj.mall.vo.LoginVO;

public interface AuthService {

    LoginVO login(LoginDTO dto);

    LoginVO register(RegisterDTO dto);

    void logout(Long userId);

    void sendSmsCode(String phone, Integer type);

    void resetPassword(String phone, String code, String newPassword);

    void cancelAccount(Long userId, String reason);
}
