package ar.edu.utn.frc.tup.lciii.model;


import ar.edu.utn.frc.tup.lciii.model.Enums.Tipo_Color;
import ar.edu.utn.frc.tup.lciii.model.Enums.Tipo_Pieza;
import jakarta.persistence.*;

@Entity
@Table(name = "posicion")
public class Posicion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int fila;
    private int columna;

    @Enumerated(EnumType.STRING)
    private Tipo_Color color;

    @Enumerated(EnumType.STRING)
    private Tipo_Pieza tipoPieza;

    @ManyToOne
    @JoinColumn(name = "partida_id")
    private Partida partida;

    public Posicion() {
    }

    public Posicion(int fila, int columna, Tipo_Color color, Tipo_Pieza tipoPieza) {
        this.fila = fila;
        this.columna = columna;
        this.color = color;
        this.tipoPieza = tipoPieza;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public Tipo_Color getColor() {
        return color;
    }

    public void setColor(Tipo_Color color) {
        this.color = color;
    }

    public Tipo_Pieza getTipoPieza() {
        return tipoPieza;
    }

    public void setTipoPieza(Tipo_Pieza tipoPieza) {
        this.tipoPieza = tipoPieza;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }
}

