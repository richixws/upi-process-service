package pe.gob.bcrp.upi.process.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Inyecciones {


    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
