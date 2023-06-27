package ar.edu.utn.frc.tup.lciii.model;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "partida")
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "partida", cascade = CascadeType.ALL)
    private List<Posicion> posiciones;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Posicion> getPosiciones() {
        return posiciones;
    }

    public void setPosiciones(List<Posicion> posiciones) {
        this.posiciones = posiciones;
    }
}