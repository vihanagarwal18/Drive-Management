package com.vihan.Drive.Management.Security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    @Value("${jwt.cookie.name:jwt-token}")
    private String jwtCookieName;

    @Value("${jwt.refresh.cookie.name:jwt-refresh-token}")
    private String refreshTokenCookieName;

    @Value("${jwt.cookie.secure:false}")
    private boolean secureCookie;

    @Value("${jwt.cookie.http-only:true}")
    private boolean httpOnlyCookie;

    @Value("${jwt.cookie.max-age:86400}")
    private int cookieMaxAge;

    @Value("${jwt.refresh.cookie.max-age:604800}")
    private int refreshCookieMaxAge;

    @Value("${jwt.cookie.domain:}")
    private String cookieDomain;

    @Value("${jwt.cookie.path:/}")
    private String cookiePath;

    /**
     * Create a cookie with the JWT token
     */
    public void createTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(jwtCookieName, token);
        cookie.setHttpOnly(httpOnlyCookie);
        cookie.setSecure(secureCookie);
        cookie.setMaxAge(cookieMaxAge);
        cookie.setPath(cookiePath);
        
        if (!cookieDomain.isEmpty()) {
            cookie.setDomain(cookieDomain);
        }
        
        response.addCookie(cookie);
    }

    /**
     * Create a cookie with the refresh token
     */
    public void createRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie(refreshTokenCookieName, refreshToken);
        cookie.setHttpOnly(httpOnlyCookie);
        cookie.setSecure(secureCookie);
        cookie.setMaxAge(refreshCookieMaxAge);
        cookie.setPath(cookiePath);
        
        if (!cookieDomain.isEmpty()) {
            cookie.setDomain(cookieDomain);
        }
        
        response.addCookie(cookie);
    }

    /**
     * Get the JWT token from the cookie
     */
    public String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (jwtCookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        
        return null;
    }

    /**
     * Get the refresh token from the cookie
     */
    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (refreshTokenCookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        
        return null;
    }

    /**
     * Clear the JWT token cookie
     */
    public void clearTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(jwtCookieName, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(secureCookie);
        cookie.setMaxAge(0);
        cookie.setPath(cookiePath);
        
        if (!cookieDomain.isEmpty()) {
            cookie.setDomain(cookieDomain);
        }
        
        response.addCookie(cookie);
    }

    /**
     * Clear the refresh token cookie
     */
    public void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(refreshTokenCookieName, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(secureCookie);
        cookie.setMaxAge(0);
        cookie.setPath(cookiePath);
        
        if (!cookieDomain.isEmpty()) {
            cookie.setDomain(cookieDomain);
        }
        
        response.addCookie(cookie);
    }
}