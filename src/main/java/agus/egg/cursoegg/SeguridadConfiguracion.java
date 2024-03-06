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
     http.headers().frameOptions().sameOrigin().and() //.headers().frameOptions().sameOrigin().and()   asi tiene el del video de egg pero no el chico
          .authorizeRequests()
                .antMatchers("/css/*", "/js/*","/img/*", "/**")
                .permitAll()
          .and().formLogin()
                .loginPage("/login") // Que formulario esta mi login
                      .loginProcessingUrl("/logincheck")
                      .usernameParameter("username") // Como viajan los datos del logueo
                      .passwordParameter("password")// Como viajan los datos del logueo
                      .defaultSuccessUrl("/inicio") // A que URL viaja 
                      .permitAll()
                .and().logout() // Aca configuro la salida
                      .logoutUrl("/logout")
                      .logoutSuccessUrl("/login") //ULTIMA ACTUALIZACION: ENTRE LAS COMILLAS ESTABA ASI "/" PERO AL DESLOGEARME ME SALIA AI INDEX Y CUANDO MODIFIQUE ESO
             //ME LLEVA AL LOGIN PERO AUN ASI NO ME MUESTRA EL MENSAJE DE DESLOGEO CORRECTAMENTE
                      .permitAll().and().csrf().disable();
                       //.and().csrf().disable(); y esto hago y funciona el tema de conexion con base de datos pero no funciona el tema de logout ya que marca error
                       // si copio eso despues de permitall() entonces funciona y entra a la base de datos pero no me deslogea si no que me lleva a la pagina localhost8080
                       //VIDEO V6 MINUTOS FINALES YA QUE ES CUANDO LOGRA HACERLO ANDAR
                       //UNICO ERROR HASTA AHORA ES ESTE DE LOGOUT NO ME SALE. INDICADO ARRIBA
    }
    
    
}
