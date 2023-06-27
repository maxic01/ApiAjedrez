package ar.edu.utn.frc.tup.lciii.model;




import ar.edu.utn.frc.tup.lciii.model.Enums.Direction;
import ar.edu.utn.frc.tup.lciii.model.Enums.Tipo_Color;
import ar.edu.utn.frc.tup.lciii.model.Enums.Tipo_Pieza;

import java.util.HashMap;

public class Reina extends Pieza{
    public Reina(Tipo_Color Color) {
        super(Color);
        setTipoPieza(Tipo_Pieza.REINA);
    }
    @Override
    public HashMap<Direction, Integer> Mov_Pieza() {
        HashMap<Direction, Integer> DireccYmovimiento = new HashMap<>();

        DireccYmovimiento.put(Direction.TOP, 8);
        DireccYmovimiento.put(Direction.BOTTOM, 8);
        DireccYmovimiento.put(Direction.LEFT, 8);
        DireccYmovimiento.put(Direction.RIGHT, 8);

        DireccYmovimiento.put(Direction.TOPLEFT, 8);
        DireccYmovimiento.put(Direction.TOPRIGHT, 8);
        DireccYmovimiento.put(Direction.BOTTOMLEFT, 8);
        DireccYmovimiento.put(Direction.BOTTOMRIGHT, 8);

        return DireccYmovimiento;
    }
}
