package com.zwn.trainserverspringboot.command.service;

import com.zwn.trainserverspringboot.command.bean.User;
import com.zwn.trainserverspringboot.command.bean.UserDetail;
import com.zwn.trainserverspringboot.command.mapper.UserMapper;
import com.zwn.trainserverspringboot.exception.CustomException;
import com.zwn.trainserverspringboot.util.JwtUtils;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class AuthServiceImpl {

	@Resource
	private AuthenticationManager authenticationManager;
	@Resource
	private JwtUtils jwtTokenUtil;
	@Resource
	private UserMapper userMapper;

//	@Value("${jwt.tokenHead}")
	private final String tokenHead = "Bearer";

	public Result register(User user){
		if (user.isRegisterLegal().getCode() == ResultCodeEnum.SUCCESS.getCode()){
			user.setRole("common");
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			final String rawPassword = user.getLoginKey();
			user.setLoginKey(encoder.encode(rawPassword));
			try{
				userMapper.register(user);
			}catch (Exception exception){
				exception.printStackTrace();
				if (exception.getClass().getName().equals("org.springframework.dao.DuplicateKeyException")){
					return  Result.getResult(ResultCodeEnum.REGISTER_EXIST);
				}else {
					return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR);
				}
			}
			return Result.getResult(ResultCodeEnum.SUCCESS);
		}else {
			return Result.getResult(user.isRegisterLegal());
		}

	}

	public Result login(String userId, String password) {
		//用户验证
        final Authentication authentication = authenticate(Long.parseLong(userId), password);
        //存储认证信息
        SecurityContextHolder.getContext().setAuthentication(authentication);
//		System.out.println(authentication.getPrincipal());
//		System.out.println(authentication.getAuthorities());
//		UserDetail(userId=15866554038, username=zwn, password=$2a$10$wpLZnoVxQQVtkHJGTiILzuQDs86SetQ1g49IGptrbgCYcjP35C4gG, role=user)
//		[user]
        //生成token
        final UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        final String token = jwtTokenUtil.generateAccessToken(userDetail);
        //存储token
        jwtTokenUtil.putToken(Long.parseLong(userId), token);
        return Result.getResult(ResultCodeEnum.SUCCESS,token);
	}
	
	private Authentication authenticate(long userId, String password) {
        try {
            //该方法会去调用userDetailsService.loadUserByUsername()去验证用户名和密码，如果正确，则存储该用户名密码到“security 的 context中”
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userId, password));
        } catch (DisabledException | BadCredentialsException e) {
            throw new CustomException(Result.getResult(ResultCodeEnum.LOGIN_ERROR, e.getMessage()));
        }
    }

	public Result logout(String token) {
		token = token.substring(tokenHead.length());
        long userId = jwtTokenUtil.getUserIdFromToken(token);
        jwtTokenUtil.deleteToken(userId);
		return Result.getResult(ResultCodeEnum.SUCCESS);
	}

	public Result refresh(String oldToken) {
		String token = oldToken.substring(tokenHead.length());
		token =  jwtTokenUtil.refreshToken(token);
		return Result.getResult(ResultCodeEnum.SUCCESS,token);

	}

//	public UserDetail getUserByToken(String token) {
//		token = token.substring(tokenHead.length());
//        return jwtTokenUtil.getUserFromToken(token);
//	}

}
