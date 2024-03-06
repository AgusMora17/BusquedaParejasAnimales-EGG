/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agus.egg.cursoegg.servicios;

import agus.egg.cursoegg.entidades.Foto;
import agus.egg.cursoegg.entidades.Usuario;
import agus.egg.cursoegg.errores.ErrorServicio;
import agus.egg.cursoegg.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author agust
 */
@Service
public class UsuarioServicio implements UserDetailsService{
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @Autowired
    private FotoServicio fotoServicio;
    
    
    
    public void registrar(MultipartFile archivo,String nombre, String apellido, String mail, String clave) throws ErrorServicio {
        
        validar(nombre, apellido, mail, clave);
        
        Usuario usuario=new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setMail(mail);
        usuario.setClave(clave);
        usuario.setAlta(new Date());
        
        Foto foto=fotoServicio.guardar(archivo);
        usuario.setFoto(foto);
        
        usuarioRepositorio.save(usuario);
        
    }
    
    public void modificar (MultipartFile archivo,String id, String nombre, String apellido, String mail, String clave) throws ErrorServicio{
        validar(nombre, apellido, mail, clave);
        
        Optional<Usuario> respuesta=usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
        Usuario usuario=respuesta.get();
        usuario.setApellido(apellido);
        usuario.setNombre(nombre);
        usuario.setMail(mail);
        usuario.setClave(clave);
        
        String idFoto=null;
        if (usuario.getFoto() != null) {
            idFoto = usuario.getFoto().getId();
        }
        
        Foto foto=fotoServicio.actualizar(idFoto, archivo);
        usuario.setFoto(foto);
        
        usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontró el usuario solicitado");
        }
        
    }
    
    public void deshabilitar(String id) throws ErrorServicio{
        Optional<Usuario> respuesta=usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
        Usuario usuario=respuesta.get();
        usuario.setBaja(new Date());
        usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontró el usuario solicitado");
        }   
    }
    
    public void habilitar(String id) throws ErrorServicio{
        Optional<Usuario> respuesta=usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
        Usuario usuario=respuesta.get();
        usuario.setBaja(null);
        usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontró el usuario solicitado");
        }   
    }
    
    
    public void validar(String nombre, String apellido, String mail, String clave) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio ("El nombre del usuario no puede ser nulo");
        }
        
        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio ("El apellido del usuario no puede ser nulo");
        }
        
        if (mail == null || mail.isEmpty()) {
            throw new ErrorServicio ("El mail del usuario no puede ser nulo");
        }
        
        if (clave == null || clave.isEmpty() || clave.length() <= 6) {
            throw new ErrorServicio ("La clave del usuario no puede ser nulo y/o tiene que tener mas de 6 digitos");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
       Usuario usuario=usuarioRepositorio.buscarPorMail(mail);
       if (usuario != null) {
           
           List<GrantedAuthority> permisos= new ArrayList<>();
           
           GrantedAuthority p1 =new SimpleGrantedAuthority("MODULO_FOTOS");
           permisos.add(p1);
           
           GrantedAuthority p2 =new SimpleGrantedAuthority("MODULO_MASCOTAS");
           permisos.add(p2);
           
           GrantedAuthority p3 =new SimpleGrantedAuthority("MODULO_VOTOS");
           permisos.add(p3);
           
           User user = new User(usuario.getMail(), usuario.getClave(), permisos); //en user el repositorio que debe llama es userpropierities.User y no el de userDetails ya que falla 
           return user;
       } else {
           return null;
       }
    }
    
    
}
