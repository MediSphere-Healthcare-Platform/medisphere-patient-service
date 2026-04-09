package com.medisphere.patient.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    /**
     * Uploads a profile image to Cloudinary under the "medisphere/patients" folder.
     *
     * @param file the image file to upload
     * @return the secure URL of the uploaded image
     * @throws IOException if the upload fails
     */
    public String uploadProfileImage(MultipartFile file) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "medisphere/patients",
                        "resource_type", "image"
                )
        );
        return (String) uploadResult.get("secure_url");
    }
}
