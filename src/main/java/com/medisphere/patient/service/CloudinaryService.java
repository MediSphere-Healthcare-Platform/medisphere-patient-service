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
     * Uploads a medical report to Cloudinary under the "medisphere/reports" folder.
     *
     * @param file the report file to upload
     * @return the secure URL of the uploaded report
     * @throws IOException if the upload fails
     */
    public String uploadReport(MultipartFile file) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "medisphere/reports",
                        "resource_type", "image"
                )
        );
        return (String) uploadResult.get("secure_url");
    }

    /**
     * Deletes an image or file from Cloudinary using its URL.
     *
     * @param imageUrl the URL of the resource to delete
     */
    public void deleteImage(String imageUrl) {
        try {
            if (imageUrl == null || imageUrl.isEmpty()) {
                return;
            }
            String publicId = extractPublicId(imageUrl);
            if (publicId != null) {
                // Try to delete as image first (most common), then as raw if needed
                // Or we can just use "resource_type" auto if available in destroy (it's not)
                // However, for simplicity and since most reports are images/PDFs (both treated as image usually)
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                // We could add raw check here if needed, but let's stick to this for now.
            }
        } catch (Exception e) {
            System.err.println("Failed to delete from Cloudinary: " + e.getMessage());
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
