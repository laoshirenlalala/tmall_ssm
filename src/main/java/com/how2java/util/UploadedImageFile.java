package com.how2java.util;

import org.springframework.web.multipart.MultipartFile;

public class UploadedImageFile {
    MultipartFile image;
    public MultipartFile getImage(){
        return image;
    }
    public void setImage(MultipartFile mul){
        this.image = mul;
    }
}
