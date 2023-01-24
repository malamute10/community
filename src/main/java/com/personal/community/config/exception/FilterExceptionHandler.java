package com.personal.community.config.exception;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.PageAttributes;
import java.io.IOException;
import java.net.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class FilterExceptionHandler extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            doFilter(request, response, filterChain);
        } catch (CommunityException ex){
            this.setExceptionResponse(ex, response);
        }
    }

    private void setExceptionResponse(CommunityException ex, HttpServletResponse response){
        ExceptionResponse exceptionResponse = ExceptionResponse.ofCreate(ex);

        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status", String.valueOf(exceptionResponse.getStatus().value()));
            jsonObject.addProperty("message", exceptionResponse.getMessage());

            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(jsonObject.toString());
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
