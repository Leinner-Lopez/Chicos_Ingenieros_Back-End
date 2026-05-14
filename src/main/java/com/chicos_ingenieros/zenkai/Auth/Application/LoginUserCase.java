package com.chicos_ingenieros.zenkai.Auth.Application;

import com.chicos_ingenieros.zenkai.Auth.Infrastructure.AuthResponse;
import com.chicos_ingenieros.zenkai.Auth.Infrastructure.LoginRequest;
import com.chicos_ingenieros.zenkai.Auth.Infrastructure.RegisterRequest;

public interface LoginUserCase {
    AuthResponse login(LoginRequest request);
    AuthResponse register(RegisterRequest request);
}
