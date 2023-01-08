package com.example.role.server;


import com.example.role.commons.util.R;
import jakarta.servlet.http.HttpServletRequest;


public interface CommonServer {
    R checkToken(HttpServletRequest rq, String tokenName);
}
