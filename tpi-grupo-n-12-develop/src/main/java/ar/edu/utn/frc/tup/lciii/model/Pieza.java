package ar.edu.utn.frc.tup.lciii.model;


import ar.edu.utn.frc.tup.lciii.model.Enums.Direction;
import ar.edu.utn.frc.tup.lciii.model.Enums.Tipo_Color;
import ar.edu.utn.frc.tup.lciii.model.Enums.Tipo_Pieza;
import lombok.SneakyThrows;

import javax.swing.*;
import java.util.HashMap;

public class Pieza implements Cloneable
{
    protected Tipo_Color color;
    private Tipo_Pieza tipoPieza;
    protected int fila;
    protected int columna;

    //Gets y set
    public Tipo_Color getColor(){
        return color;
    }
    public void setColor(Tipo_Color _color){color = _color;}
    public Tipo_Pieza getTipoPieza(){return tipoPieza;}
    public void setTipoPieza(Tipo_Pieza _tipopieza){tipoPieza = _tipopieza;}
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

    //Constructor
    public Pieza(Tipo_Color Color)
    {
        color = Color;
    }
    public Pieza(){}
    public HashMap<Direction, Integer> Mov_Pieza(){
        return null;
    }

    @SneakyThrows
    @Override
    public Pieza clone() {
        Pieza clonedPiece = (Pieza) super.clone();
        clonedPiece.tipoPieza = tipoPieza;
        clonedPiece.fila = fila;
        clonedPiece.columna = columna;
        return clonedPiece;
    }


}
