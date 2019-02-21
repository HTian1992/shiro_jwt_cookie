package com.bestwehotel.shiro;

import com.bestwehotel.shiro.config.JwtProperties;
import com.bestwehotel.shiro.exceptions.InvalidJwtTokenException;
import com.bestwehotel.shiro.exceptions.JwtExpiredTokenException;
import com.bestwehotel.shiro.jwt.extractor.TokenExtractor;
import com.bestwehotel.shiro.jwt.model.UserContext;
import com.bestwehotel.shiro.jwt.model.result.MsgCode;
import com.bestwehotel.shiro.jwt.model.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.subject.WebSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义shiro的过滤器
 *
 * @author lizehao
 */
public class JWTFilter extends BasicHttpAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(JWTFilter.class);

    private static ObjectMapper mapper = new ObjectMapper();

    private AuthServerUtils authServerUtils;

    private final TokenExtractor tokenExtractor;

    @Autowired
    public JWTFilter(final TokenExtractor tokenExtractor,AuthServerUtils authServerUtils) {
        this.tokenExtractor = tokenExtractor;
        this.authServerUtils = authServerUtils;
    }

    /***
     * 如果正在执行的filter中isAccessAllowed和onAccessDenied都返回false，则整个filter控制链都将结束，不会到达目标方法（客户端请求的接口），
     * */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (RequestMethod.OPTIONS.name().equals(((HttpServletRequest) request).getMethod())) {
            try {
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                //预请求需要往回写 让ajax预请求知道预请求是成功的
                httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                return true;
            } catch (Exception e) {
                log.error("OPTIONS请求放行错误，{}", e.getMessage());
            }
        }
        return false;
    }

    /**
     * 所有请求都会经过的方法。
     * 如果正在执行的filter中isAccessAllowed和onAccessDenied都返回false，
     * 则整个filter控制链都将结束，不会到达目标方法（客户端请求的接口），而是直接跳转到某个页面（由filter定义的，将会在authc中看到）
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        //带token的直接判断，没带的去登录验证
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String authenticationHeader = httpServletRequest.getHeader(JwtProperties.AUTH_HEADER);
        try {
            //提取token
            String authToken = tokenExtractor.extract(authenticationHeader);

            //校验并返回用户信息
            UserContext user = authServerUtils.parseRawToken(authToken);

            //添加用户凭证，即token的权限部分
            PrincipalCollection principals = new SimplePrincipalCollection(user, "shiroRealm");//拼装shiro用户信息
            WebSubject.Builder builder = new WebSubject.Builder(servletRequest, servletResponse);
            builder.principals(principals);
            builder.authenticated(true);
            builder.sessionCreationEnabled(false);
            WebSubject subject = builder.buildWebSubject();
            //塞入容器，统一调用，ThreadContext是Shiro内部定义的, 对ThreadLocal<T>进行了封装。
            ThreadContext.bind(subject);
            return true;
        } catch (JwtExpiredTokenException expiredEx) {
            //Refresh Token已过期
            this.jwtTokenExpired(servletResponse);
        } catch (InvalidJwtTokenException | IllegalArgumentException invaliEx) {
            //无效的Refresh Token
            this.jwtTokenInvalid(servletResponse);
        } catch (AuthenticationException aEx) {
            //头大小不合法
            this.unAuthorized(servletResponse);
        }
        return false;
    }

    // 未登陆
    private void unAuthorized(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_OK);//SC_UNAUTHORIZED
        httpResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String restMsg = mapper.writeValueAsString(Result.error(MsgCode.UNAUTHORIZED.getMsgCode(), MsgCode.UNAUTHORIZED.getMessage()));
        httpResponse.getWriter().write(restMsg);
    }

    // token已过期
    private void jwtTokenExpired(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_OK);//SC_UNAUTHORIZED
        httpResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String restMsg = mapper.writeValueAsString(Result.error(MsgCode.TOKEN_EXPIRE.getMsgCode(), MsgCode.TOKEN_EXPIRE.getMessage()));
        httpResponse.getWriter().write(restMsg);
    }

    // 无效，非法token
    private void jwtTokenInvalid(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_OK);//SC_UNAUTHORIZED
        httpResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String restMsg = mapper.writeValueAsString(Result.error(MsgCode.VAILD_TOKEN_FAIL.getMsgCode(), MsgCode.VAILD_TOKEN_FAIL.getMessage()));
        httpResponse.getWriter().write(restMsg);
    }
}
