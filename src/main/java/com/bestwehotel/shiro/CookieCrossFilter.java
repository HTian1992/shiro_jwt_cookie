package com.bestwehotel.shiro;

import com.bestwehotel.shiro.config.JwtProperties;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
/**
 * 自定义拦截器在http request时，把从cookie获取到的token塞入header中
 * @author Htian
 * */
@Component
public class CookieCrossFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(CookieCrossFilter.class);

    @Override
    protected void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        ModifyHttpServletRequestWrapper httpServletRequestWrapper = new ModifyHttpServletRequestWrapper(httpServletRequest);
        Cookie cookie = CookiesUtil.getCookieByName(httpServletRequest,JwtProperties.TOKEN_COOKIE_NAME);
        String authToken = null!=cookie?cookie.getValue():"";
        if(!StringUtils.isEmpty(authToken)){
            httpServletRequestWrapper.putHeader(JwtProperties.AUTH_HEADER, JwtProperties.JWT_TOKEN_HEADER_PREFIX + authToken);
        }
        try {
            filterChain.doFilter(httpServletRequestWrapper, servletResponse);
        }catch (Exception e){
            log.error("CookieCrossFilter Exception -> {}",e);
        }
    }
}
