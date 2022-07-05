package com.shop.config.argumentResolver;

import com.shop.entity.Member;
import com.shop.session.SessionMember;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        log.info("LoginMemberArgumentResolver supportsParameter 실행");
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean hasSessionMemberType = SessionMember.class.isAssignableFrom(parameter.getParameterType());
        //간단하게 instanceof는 특정 Object가 어떤 클래스/인터페이스를 상속/구현했는지를 체크하며
        //Class.isAssignableFrom()은 특정 Class가 어떤 클래스/인터페이스를 상속/구현했는지 체크합니다.
        return hasLoginAnnotation && hasSessionMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("LoginMemberArgumentResolver resolveArgument 실행");

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return session.getAttribute("member");


    }
}
