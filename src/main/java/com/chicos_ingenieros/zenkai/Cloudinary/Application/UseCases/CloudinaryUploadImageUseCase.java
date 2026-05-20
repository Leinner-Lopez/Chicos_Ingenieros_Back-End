package com.chicos_ingenieros.zenkai.Cloudinary.Application.UseCases;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryUploadImageUseCase {
    String uploadImage(MultipartFile image) throws IOException;
}
