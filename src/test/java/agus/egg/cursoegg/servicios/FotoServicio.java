/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agus.egg.cursoegg.servicios;

import agus.egg.cursoegg.entidades.Foto;
import agus.egg.cursoegg.errores.ErrorServicio;
import agus.egg.cursoegg.repositorios.FotoRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author agust
 */
@Service
public class FotoServicio {
    
    @Autowired
    private FotoRepositorio fotoRepositorio;
    
    
    public Foto guardar(MultipartFile archivo) throws ErrorServicio{
        if (archivo != null) {
            try {
               Foto foto = new Foto();
               foto.setMime(archivo.getContentType());
               foto.setNombre(archivo.getName());
               foto.setContenido(archivo.getBytes());
               fotoRepositorio.save(foto);
               return fotoRepositorio.save(foto);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } 
            return null;
    }
    
    public Foto actualizar(String idFoto,MultipartFile archivo) throws ErrorServicio{
        if (archivo != null) {
            try {
               Foto foto = new Foto();
               
               if (idFoto != null) {
                   Optional <Foto> respuesta= fotoRepositorio.findById(idFoto);
                   if (respuesta.isPresent()){
                       foto=respuesta.get();
                   }
               }
               
               foto.setMime(archivo.getContentType());
               foto.setNombre(archivo.getName());
               foto.setContenido(archivo.getBytes());
               fotoRepositorio.save(foto);
               return fotoRepositorio.save(foto);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } 
            return null;
        
    }



}
    

