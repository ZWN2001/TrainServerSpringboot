package com.zwn.trainserverspringboot.config;

import com.alibaba.fastjson.JSON;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 权限不足处理类，返回403
 */

@Component("RestAuthenticationAccessDeniedHandler")
public class RestAuthenticationAccessDeniedHandler implements AccessDeniedHandler {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
			throws IOException, ServletException {
		logger.error("权限不足：{}", e.getMessage());
		// 因为我们使用的REST API,所以我们认为到达后台的请求都是正常的，
		// 所以返回的HTTP状态码都是200，用接口返回的code来确定请求是否正常。
		response.setStatus(ResultCodeEnum.SUCCESS.getCode());
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		// 响应中增加允许跨域，解决认证失败时的跨域问题
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,Authorization,SessionToken,JSESSIONID,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		PrintWriter printWriter = response.getWriter();
		String body = JSON.toJSONString(Result.getResult(ResultCodeEnum.FORBIDDEN,e.getMessage()));
		printWriter.write(body);
		printWriter.flush();

	}

}
