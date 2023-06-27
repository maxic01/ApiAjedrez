package ar.edu.utn.frc.tup.lciii.model;



import ar.edu.utn.frc.tup.lciii.model.Enums.Direction;
import ar.edu.utn.frc.tup.lciii.model.Enums.Tipo_Color;
import ar.edu.utn.frc.tup.lciii.model.Enums.Tipo_Pieza;

import java.util.HashMap;


import static ar.edu.utn.frc.tup.lciii.model.vTablero.orden_Piezas;

public class Peon extends Pieza
{
    boolean primer_Mov = true;

    public void setPrimer_Mov(boolean primer_Mov) {
        this.primer_Mov = primer_Mov;
    }

    //Constructor
    public Peon(Tipo_Color Color)
    {
        color = Color;
        setTipoPieza(Tipo_Pieza.PEON);
    }
    public HashMap<Direction, Integer> Mov_Pieza(){
        HashMap<Direction, Integer> DireccYmovimiento = new HashMap<>();

        if(!primer_Mov) {
            if (orden_Piezas.equalsIgnoreCase("Abajo_Blancas")) {
                if (color.equals(Tipo_Color.BLANCO)) {
                    DireccYmovimiento.put(Direction.TOP, 1);
                } else if (color.equals(Tipo_Color.NEGRO)) {
                    DireccYmovimiento.put(Direction.BOTTOM, 1);
                }
            } else if (orden_Piezas.equalsIgnoreCase("Arriba_Blancas")) {
                if (color.equals(Tipo_Color.NEGRO)) {
                    DireccYmovimiento.put(Direction.TOP, 1);
                } else if (color.equals(Tipo_Color.BLANCO)) {
                    DireccYmovimiento.put(Direction.BOTTOM, 1);
                }
            }
        } else{
            if (orden_Piezas.equalsIgnoreCase("Abajo_Blancas")) {
                if (color.equals(Tipo_Color.BLANCO) && primer_Mov && fila == 6) {
                    DireccYmovimiento.put(Direction.TOP, 2);
                } else if (color.equals(Tipo_Color.NEGRO) && primer_Mov && fila == 1) {
                    DireccYmovimiento.put(Direction.BOTTOM, 2);
                }
            } else if (orden_Piezas.equalsIgnoreCase("Arriba_Blancas")) {
                if (color.equals(Tipo_Color.NEGRO) && primer_Mov && fila == 6) {
                    DireccYmovimiento.put(Direction.TOP, 2);
                } else if (color.equals(Tipo_Color.BLANCO) && primer_Mov && fila == 1) {
                    DireccYmovimiento.put(Direction.BOTTOM, 2);
                }
            }
        }

        return DireccYmovimiento;
    }

    public void ActualizarPeon(){
        if(color.equals(Tipo_Color.BLANCO) && fila != 6){
            primer_Mov = false;
        } else if (color.equals(Tipo_Color.NEGRO) && fila != 1) {
            primer_Mov=false;
        }
    }
}
