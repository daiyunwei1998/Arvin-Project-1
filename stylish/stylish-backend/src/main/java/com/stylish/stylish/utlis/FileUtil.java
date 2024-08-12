package com.stylish.stylish.utlis;
import org.springframework.web.multipart.MultipartFile;
public class FileUtil {
    /**
     * Gets the file extension from a MultipartFile.
     *
     * @param file the MultipartFile
     * @return the file extension
     */
    public static String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }
        return "";
    }
}
