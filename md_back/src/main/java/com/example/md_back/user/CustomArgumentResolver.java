package com.example.md_back.user;

import com.example.md_back.dto.LoginDTO;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CustomArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> parameterType = methodParameter.getParameterType();
        return LoginDTO.class.equals(parameterType);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        Object principal = null;
        MyAuthentication authentication =
                (MyAuthentication) SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null ) {
            principal = authentication.principal;
        }
        if(principal == null || principal.getClass() == String.class) {
            return null;
        }

        return principal;
    }

}
