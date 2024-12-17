package com.quad.project.uber.uberApp.configs;

import com.quad.project.uber.uberApp.dto.PointDto;
import com.quad.project.uber.uberApp.utils.GeometryUtil;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper(){

        ModelMapper mapper = new ModelMapper();

        mapper.typeMap(PointDto.class, Point.class).setConverter(
                context -> {
                    PointDto pointDto = context.getSource();
                    return GeometryUtil.createPoint(pointDto);
                }
        );

        mapper.typeMap(Point.class, PointDto.class).setConverter(
                context -> {
                    Point point = context.getSource();
                    double[] coordinates ={
                            point.getX(),
                            point.getY()
                    };
                    return new PointDto(coordinates);
                }
        );

        return mapper;
    }

}
