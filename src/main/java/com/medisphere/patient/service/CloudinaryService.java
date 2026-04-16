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

    /**
     * Deletes an image from Cloudinary using its URL.
     *
     * @param imageUrl the URL of the image to delete
     */
    public void deleteImage(String imageUrl) {
        try {
            if (imageUrl == null || imageUrl.isEmpty()) {
                return;
            }
            String publicId = extractPublicId(imageUrl);
            if (publicId != null) {
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            }
        } catch (Exception e) {
            // Log the error but don't throw to prevent blocking the main process
            System.err.println("Failed to delete image from Cloudinary: " + e.getMessage());
        }
    }

    /**
     * Extracts the public ID from a Cloudinary URL.
     *
     * @param url the Cloudinary URL
     * @return the public ID, including folders
     */
    private String extractPublicId(String url) {
        try {
            int uploadIndex = url.indexOf("/upload/");
            if (uploadIndex == -1) return null;

            String afterUpload = url.substring(uploadIndex + 8);
            // Skip the version part (e.g., v12345678/)
            int firstSlash = afterUpload.indexOf("/");
            String publicIdWithExtension = afterUpload.substring(firstSlash + 1);

            // Remove file extension
            int lastDot = publicIdWithExtension.lastIndexOf(".");
            if (lastDot == -1) return publicIdWithExtension;

            return publicIdWithExtension.substring(0, lastDot);
        } catch (Exception e) {
            return null;
        }
    }
}
