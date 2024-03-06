/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agus.egg.cursoegg.repositorios;

import agus.egg.cursoegg.entidades.Mascota;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author agust
 */
@Repository
public interface MascotaRepositorio extends JpaRepository<Mascota, String>{
    
    @Query("SELECT c FROM Mascota c WHERE c.usuario.id = :id AND c.baja IS NULL")
    public List<Mascota> buscarMascotaPorUsuario(@Param("id") String id);
}
