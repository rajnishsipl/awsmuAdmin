package com.awsmu.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class AnimalInterceptor extends HandlerInterceptorAdapter {
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		System.out.println("AnimalInterceptor: REQUEST Intercepted for URI: "
				+ request.getRequestURI());
		request.setAttribute("special", "I Love Animals!");
		return true;
	}
}
