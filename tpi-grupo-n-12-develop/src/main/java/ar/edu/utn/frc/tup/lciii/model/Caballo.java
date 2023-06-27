package ar.edu.utn.frc.tup.lciii.model;


import ar.edu.utn.frc.tup.lciii.model.Enums.Direction;
import ar.edu.utn.frc.tup.lciii.model.Enums.Tipo_Color;
import ar.edu.utn.frc.tup.lciii.model.Enums.Tipo_Pieza;

import java.util.HashMap;

public class Caballo extends Pieza {
    public Caballo(Tipo_Color Color) {
        super(Color);
        setTipoPieza(Tipo_Pieza.CABALLO);
    }

    @Override
    public HashMap<Direction, Integer> Mov_Pieza(){
        HashMap<Direction, Integer> DireccYmovimiento = new HashMap<>();

        DireccYmovimiento.put(Direction.TOP, 1);
        DireccYmovimiento.put(Direction.BOTTOM, 1);
        DireccYmovimiento.put(Direction.LEFT, 1);
        DireccYmovimiento.put(Direction.RIGHT, 1);

        DireccYmovimiento.put(Direction.TOPLEFT, 1);
        DireccYmovimiento.put(Direction.TOPRIGHT, 1);
        DireccYmovimiento.put(Direction.BOTTOMLEFT, 1);
        DireccYmovimiento.put(Direction.BOTTOMRIGHT, 1);

        return DireccYmovimiento;
    }
}
