package ar.edu.utn.frc.tup.lciii.model;


import ar.edu.utn.frc.tup.lciii.model.Enums.Tipo_Color;

public class ControladorTurno {
    private Tipo_Color turnoActual;

    public Tipo_Color getTurnoActual() {
        return turnoActual;
    }

    public ControladorTurno() {
        this.turnoActual = Tipo_Color.BLANCO;
    }

    public void CambiarTurno(){
        if(turnoActual.equals(Tipo_Color.BLANCO)){
            turnoActual = Tipo_Color.NEGRO;
        }
        else if(turnoActual.equals(Tipo_Color.NEGRO)){
            turnoActual = Tipo_Color.BLANCO;
        }
    }
}
