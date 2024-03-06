/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agus.egg.cursoegg.servicios;

import agus.egg.cursoegg.entidades.Foto;
import agus.egg.cursoegg.entidades.Mascota;
import agus.egg.cursoegg.entidades.Usuario;
import agus.egg.cursoegg.enumeraciones.Sexo;
import agus.egg.cursoegg.enumeraciones.Tipo;
import agus.egg.cursoegg.errores.ErrorServicio;
import agus.egg.cursoegg.repositorios.MascotaRepositorio;
import agus.egg.cursoegg.repositorios.UsuarioRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author agust
 */
@Service
public class MascotaServicio {
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @Autowired
    private MascotaRepositorio mascotaRepositorio;
    
    @Autowired
    private FotoServicio fotoServicio;
    
    @Transactional
    public void agregarMascota(MultipartFile archivo,String idUsuario, String nombre, Sexo sexo, Tipo tipo) throws ErrorServicio{
        Usuario usuario=usuarioRepositorio.findById(idUsuario).get();
        
        validar(nombre,sexo);
        
        Mascota mascota=new Mascota();
        mascota.setUsuario(usuario); //me faltó tirarle este ya que pasaba un perro sin id de usuario
        mascota.setNombre(nombre);
        mascota.setSexo(sexo);
        mascota.setAlta(new Date());
        mascota.setTipo(tipo);
        
        Foto foto=fotoServicio.guardar(archivo);
        mascota.setFoto(foto);
        
        mascotaRepositorio.save(mascota);
    }
    
    @Transactional
    public void modificar(MultipartFile archivo,String idUsuario, String idMascota, String nombre, Sexo sexo, Tipo tipo) throws ErrorServicio{
        validar(nombre,sexo);
        
        Optional<Mascota> respuesta = mascotaRepositorio.findById(idMascota);
        if (respuesta.isPresent()) {
            Mascota mascota=respuesta.get();
            if (mascota.getUsuario().getId().equals(idUsuario)){
                mascota.setNombre(nombre);
                mascota.setSexo(sexo);
                
                String idFoto =null;
                if(mascota.getFoto() != null){
                    idFoto=mascota.getFoto().getId();
                }
                Foto foto =fotoServicio.actualizar(idFoto, archivo);
                mascota.setFoto(foto);
                mascota.setTipo(tipo);
                
                mascotaRepositorio.save(mascota);
            } else {
                throw new ErrorServicio("No tiene permisos suficientes para realizar la operación");
            }
        } else {
            throw new ErrorServicio("No existe una mascota con el identificador solicitado");
        }
    }
    
    @Transactional
    public void eliminar(String idUsuario,String idMascota) throws ErrorServicio{
        Optional<Mascota> respuesta = mascotaRepositorio.findById(idMascota);
        if (respuesta.isPresent()) {
            Mascota mascota=respuesta.get();
            if (mascota.getUsuario().getId().equals(idUsuario)){
                mascota.setBaja(new Date());
                mascotaRepositorio.save(mascota);
            }
        } else {
            throw new ErrorServicio("No existe una mascota con el identificador solicitado");
        }
    }
    
    
    public void validar (String nombre, Sexo sexo) throws ErrorServicio{
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre de la mascota no puede ser nulo o vacío");
        }
        if (sexo == null) {
            throw new ErrorServicio("El sexo de la mascota no puede ser nulo");
        }
    }
    
    public Mascota buscarPorId(String id) throws ErrorServicio {
        Optional<Mascota> respuesta= mascotaRepositorio.findById(id);
        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new ErrorServicio("La mascota solicitada no existe");
        }
            
    }
    
    public List<Mascota> buscarMascotasPorUsuario (String id) {
        return mascotaRepositorio.buscarMascotaPorUsuario(id);
    }
    
}
