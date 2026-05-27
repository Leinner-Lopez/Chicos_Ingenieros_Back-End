package com.chicos_ingenieros.zenkai.Cloudinary.Application;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CloudinaryServiceTest {

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private CloudinaryService cloudinaryService;

    @Test
    void uploadImage_ShouldReturnSecureUrl_WhenUploadIsSuccessful() throws IOException {
        // 1. Arrange (Preparar los datos de prueba)
        byte[] imageBytes = "test image content".getBytes();
        String expectedUrl = "https://res.cloudinary.com/demo/image/upload/v123456/Zenkai/Products/sample.jpg";

        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("secure_url", expectedUrl);

        // Definimos el comportamiento de los mocks
        when(multipartFile.getBytes()).thenReturn(imageBytes);
        when(cloudinary.uploader()).thenReturn(uploader);

        // Simulamos la llamada a uploader().upload(...)
        when(uploader.upload(eq(imageBytes), any(Map.class))).thenReturn(mockResponse);

        // 2. Act (Ejecutar el método que estamos probando)
        String actualUrl = cloudinaryService.uploadImage(multipartFile);

        // 3. Assert (Verificar que los resultados sean los esperados)
        assertNotNull(actualUrl);
        assertEquals(expectedUrl, actualUrl);

        // Verificaciones adicionales para asegurar que se llamaron a los métodos correctos
        verify(multipartFile, times(1)).getBytes();
        verify(cloudinary, times(1)).uploader();
        verify(uploader, times(1)).upload(eq(imageBytes), any(Map.class));
    }

    @Test
    void uploadImage_ShouldThrowIOException_WhenCloudinaryFails() throws IOException {
        // Arrange
        byte[] imageBytes = "test image content".getBytes();

        when(multipartFile.getBytes()).thenReturn(imageBytes);
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(eq(imageBytes), any(Map.class))).thenThrow(new IOException("Cloudinary error"));

        // Act & Assert
        assertThrows(IOException.class, () -> {
            cloudinaryService.uploadImage(multipartFile);
        });

        verify(uploader, times(1)).upload(eq(imageBytes), any(Map.class));
    }
}