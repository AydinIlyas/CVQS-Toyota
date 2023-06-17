package com.toyota.errorloginservice.config;

import com.toyota.errorloginservice.service.common.MapUtil;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapUtilConfig
{
    @Bean
    public MapUtil mapUtil()
    {
        return new MapUtil(new ModelMapper());
    }
}
