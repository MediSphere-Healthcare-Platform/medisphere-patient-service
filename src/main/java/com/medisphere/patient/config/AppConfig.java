package com.medisphere.patient.config;

import com.cloudinary.Cloudinary;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dwy1en5h0");
        config.put("api_key", "552436198242555");
        config.put("api_secret", "t_s2ELsB4EiAlksiBcTUk0_wYnk");
        return new Cloudinary(config);
    }
}
