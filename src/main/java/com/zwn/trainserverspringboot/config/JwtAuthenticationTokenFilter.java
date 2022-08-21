package com.zwn.trainserverspringboot.config;

import com.zwn.trainserverspringboot.command.bean.UserDetail;
import com.zwn.trainserverspringboot.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * token校验
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	// 令牌自定义标识
	private final String tokenHeader = "token";
	@Resource
	private JwtUtils jwtUtils;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String auth_token = request.getHeader(this.tokenHeader);
		
		logger.debug("auth_token {}", auth_token);
		
		final String auth_token_start = "Bearer:";
		if (auth_token != null && auth_token.startsWith(auth_token_start)) {
			auth_token = auth_token.substring(auth_token_start.length());
		} else {
			// 不按规范,不允许通过验证
			auth_token = null;
		}

		long userId = jwtUtils.getUserIdFromToken(auth_token);
		if (jwtUtils.containToken(userId, auth_token) && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetail userDetail = jwtUtils.getUserFromToken(auth_token);
			if (jwtUtils.validateToken(auth_token, userDetail)) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail,
						null, userDetail.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		chain.doFilter(request, response);
	}

}
