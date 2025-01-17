/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agus.egg.cursoegg.controladores;

import agus.egg.cursoegg.entidades.Mascota;
import agus.egg.cursoegg.entidades.Usuario;
import agus.egg.cursoegg.errores.ErrorServicio;
import agus.egg.cursoegg.servicios.MascotaServicio;
import agus.egg.cursoegg.servicios.UsuarioServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 *
 * @author agust
 */

@Controller
@RequestMapping("/foto")
public class FotoController {
    
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @Autowired
    private MascotaServicio mascotaServicio;
    
    @GetMapping("/usuario/{id}")
    public ResponseEntity<byte[]> fotoUsuario(@PathVariable String id) throws ErrorServicio {
        try {
            Usuario usuario = usuarioServicio.buscarPorId(id);
            if (usuario.getFoto() == null) {
                throw new ErrorServicio("El usuario no tiene una foto asignada.");
            }
            
            
            byte[] foto=usuario.getFoto().getContenido();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            
            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (ErrorServicio ex) {
            Logger.getLogger(FotoController.class.getName()).log(Level.SEVERE, null, ex);
              return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
    }
    
    @GetMapping("/mascota/{id}")
    public ResponseEntity<byte[]> fotoMascota(@PathVariable String id) throws ErrorServicio {
        try {
            Mascota mascota = mascotaServicio.buscarPorId(id);
            if (mascota.getFoto() == null) {
                throw new ErrorServicio("La mascota no tiene una foto asignada.");
            }
            
            
            byte[] foto=mascota.getFoto().getContenido();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            
            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (ErrorServicio ex) {
            Logger.getLogger(FotoController.class.getName()).log(Level.SEVERE, null, ex);
              return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
    }
    
   
    
}
