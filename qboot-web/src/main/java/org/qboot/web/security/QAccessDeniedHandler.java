package org.qboot.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * <p>Title: QAccessDeniedHandler</p>
 * <p>Description: 定义未授权访问处理器/p>
 * 
 * @author history
 * @date 2018-09-11
 */
@ControllerAdvice
public class QAccessDeniedHandler implements AccessDeniedHandler{

	@ExceptionHandler(AccessDeniedException.class)
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		response.setStatus(SecurityStatus.NO_PERMISSION.getValue());
	}
}

