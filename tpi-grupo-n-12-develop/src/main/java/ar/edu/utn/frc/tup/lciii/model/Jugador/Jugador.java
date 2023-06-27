package ar.edu.utn.frc.tup.lciii.model.Jugador;


import ar.edu.utn.frc.tup.lciii.model.Enums.Tipo_Color;

public class Jugador {

    private String nombre;
    private Tipo_Color jugadorColor;

    public String getNombre() {
        return nombre;
    }

    public Tipo_Color getJugadorColor() {
        return jugadorColor;
    }


    public void setJugadorColor(Tipo_Color jugadorColor) {
        this.jugadorColor = jugadorColor;
    }

    public Jugador(){
        this.nombre = "";
        this.jugadorColor = null;
    }

    public Jugador(String nombre, Tipo_Color jugadorColor) {
        this.nombre = nombre;
        this.jugadorColor = jugadorColor;
    }
}
