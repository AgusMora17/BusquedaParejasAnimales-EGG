/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agus.egg.cursoegg;


import agus.egg.cursoegg.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author agust
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
@Order(1)
public class SeguridadConfiguracion extends WebSecurityConfigurerAdapter{
    
    @Autowired
    public UsuarioServicio usuarioServicio;
    
    @Autowired
    public void configureGlobal (AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(usuarioServicio)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
    
    @Bean
    public JavaMailSender javaMailSender(){
        return new JavaMailSenderImpl();
        
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
     http.headers().frameOptions().sameOrigin().and() 
          .authorizeRequests()
                .antMatchers("/css/*", "/js/*","/img/*", "/**")
                .permitAll()
          .and().formLogin()
                .loginPage("/login") 
                      .loginProcessingUrl("/logincheck")
                      .usernameParameter("username") 
                      .passwordParameter("password")
                      .defaultSuccessUrl("/inicio") 
                      .permitAll()
                .and().logout() // Aca configuro la salida
                      .logoutUrl("/logout")
                      .logoutSuccessUrl("/login") 
                      .permitAll().and().csrf().disable();
                       
    }
    
    
}
