/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agus.egg.cursoegg.repositorios;

import agus.egg.cursoegg.entidades.Voto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author agust
 */
@Repository
public interface VotoRepositorio extends JpaRepository<Voto, String> {
    
    @Query("SELECT c FROM Voto c WHERE c.mascota1.id = :id ORDER BY c.fecha DESC")
    public List<Voto> buscarVotosPropios(String id);
    
    @Query("SELECT c FROM Voto c WHERE c.mascota2.id = :id ORDER BY c.fecha DESC")
    public List<Voto> buscarVotosRecibidos(String id);
}
