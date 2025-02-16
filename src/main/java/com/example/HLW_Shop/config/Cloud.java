package com.example.HLW_Shop.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Cloud {
    @Bean
    public Cloudinary cloudinaryConfig() {
        Cloudinary cloudinary = null;
        Map config = new HashMap();
            config.put("cloud_name", "YOUR_CLOUD");
        config.put("api_key", "API_KEY");
        config.put("api_secret", "API_SECRET");
        cloudinary = new Cloudinary(config);
        return cloudinary;
    }
}
