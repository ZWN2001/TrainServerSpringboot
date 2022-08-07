package com.zwn.trainserverspringboot.util;

import com.zwn.trainserverspringboot.command.bean.UserDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public final class UserUtil {
    /**
     * 获取当前登录的用户ID
     * @return 当前登陆的用户ID,未登录返回null
     */
    public static long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetail) {
            return ((UserDetail) authentication.getPrincipal()).getUserId();
        }
        return 0;
    }

    /**
     * 获取当前登录的用户信息
     * @return 当前登陆的用户,未登录返回null
     */
    public static UserDetail getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetail) {
            return (UserDetail) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * 获取当前登录状态
     * @return 是否是登录状态下的操作
     */
    public static boolean isLogin() {
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

    /**
     * 判断当前登陆的用户是否有某个角色
     * @param role 需要判断的角色
     * @return 是否含有角色
     */
    public static boolean hasRole(String role) {
        if (isLogin()){
            return SecurityContextHolder.getContext().getAuthentication()
                    .getAuthorities().contains(new SimpleGrantedAuthority(role));
        }
        return false;
    }
}
