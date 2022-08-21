package com.zwn.trainserverspringboot.config;

import com.alibaba.fastjson.JSON;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * 认证失败处理类，返回401
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		logger.debug("认证失败 {} ", authException.getMessage());
		response.setStatus(ResultCodeEnum.SUCCESS.getCode());
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		//响应中增加允许跨域，解决认证失败是的跨域问题
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,Authorization,SessionToken,JSESSIONID,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");

		PrintWriter printWriter = response.getWriter();
		String body = JSON.toJSONString(Result.getResult(ResultCodeEnum.UNAUTHORIZED,authException.getMessage()));
		printWriter.write(body);
		printWriter.flush();
	}

}
