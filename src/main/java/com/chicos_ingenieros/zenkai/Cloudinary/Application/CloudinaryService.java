package com.chicos_ingenieros.zenkai.Cloudinary.Application;

import com.chicos_ingenieros.zenkai.Cloudinary.Application.UseCases.CloudinaryUploadImageUseCase;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService implements CloudinaryUploadImageUseCase {

    private final Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile image) throws IOException {
        Map<?, ?> result = cloudinary.uploader().upload(
          image.getBytes(),
                ObjectUtils.asMap(
                        "folder", "Zenkai/Products",
                        "resource_type", "image"
                )
        );
        return (String) result.get("secure_url");
    }
}
