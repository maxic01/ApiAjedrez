package ar.edu.utn.frc.tup.lciii.model;


import ar.edu.utn.frc.tup.lciii.model.Enums.Direction;
import ar.edu.utn.frc.tup.lciii.model.Enums.EstadosPartida;
import ar.edu.utn.frc.tup.lciii.model.Enums.Tipo_Color;
import ar.edu.utn.frc.tup.lciii.model.Enums.Tipo_Pieza;
import ar.edu.utn.frc.tup.lciii.model.Jugador.Jugador;

import java.util.*;

public class vTablero
{
    // region Atributos
    private Jugador Jugador1 = null;
    private Jugador Jugador2 = null;
    private static vTablero instance = null;
    public static String orden_Piezas = "Abajo_Blancas";
    private Pieza [] [] AjedrezTablero = new Pieza[8][8];
    public static ControladorTurno ctrl_turno = new ControladorTurno();

    private ArrayList<String> lst_HistorialMovimiento = new ArrayList<>();
    private ArrayList<String> lst_HisMovs_Notacion = new ArrayList<>();
    private EstadosPartida _EstadoDePartida;

    public void setAjedrezTablero(Pieza[][] ajedrezTablero) {
        AjedrezTablero = ajedrezTablero;
    }

    //Gets
    public Pieza[][] getAjedrezTablero(){return AjedrezTablero;}

    public ArrayList<String> getLst_HistorialMovimiento() {
        return lst_HistorialMovimiento;
    }

    public EstadosPartida get_EstadoDePartida() {
        return _EstadoDePartida;
    }

    public ArrayList<String> getLst_HisMovs_Notacion() {
        return lst_HisMovs_Notacion;
    }

    public Jugador getJugador1() {
        return Jugador1;
    }

    public Jugador getJugador2() {
        return Jugador2;
    }

    public void setJugador1(Jugador jugador1) {
        Jugador1 = jugador1;
    }

    public void setJugador2(Jugador jugador2) {
        Jugador2 = jugador2;
    }

    // endregion

    // region Constructor
    public vTablero(Jugador J1, Jugador J2)
    {

        //Iniciar jugadores
        Jugador1 = J1;
        Jugador2 = J2;

        _EstadoDePartida = EstadosPartida.ENCURSO;


        if(orden_Piezas.equalsIgnoreCase("Abajo_Blancas")) //Blancas abajo
        {
            Tipo_Color Color = Tipo_Color.NEGRO;

            Cargar_Arriba_Matriz(Color);

            Color = Tipo_Color.BLANCO;

            Cargar_Abajo_Matriz(Color);


        } else if (orden_Piezas.equalsIgnoreCase("Arriba_Blancas"))  //Negras abajo
        {
            Tipo_Color Color = Tipo_Color.BLANCO;

            Cargar_Arriba_Matriz(Color);

            Color = Tipo_Color.NEGRO;

            Cargar_Abajo_Matriz(Color);

        }

        ActualizarPosiciones();
    }

    public vTablero(){}

    // endregion

    // region Singleton
    public static vTablero getInstance(Jugador J1, Jugador J2) {
        if (instance == null) {
            instance = new vTablero(J1, J2);
        }
        return instance;
    }
    // endregion

    // region Carga de Matriz

    public void Cargar_Abajo_Matriz(Tipo_Color Color)
    {

        //Peones
        for (int j = 0; j <= 7; j++) {

            Peon Peon = new Peon(Color);

            AjedrezTablero[6][j] = Peon;
        }


        //Torre
        Torre _Torre1 = new Torre(Color);
        Torre _Torre2 = new Torre(Color);

        AjedrezTablero[7][0] = _Torre1;
        AjedrezTablero[7][7] = _Torre2;


        //Caballo
        Caballo _Caballo1 = new Caballo(Color);
        Caballo _Caballo2 = new Caballo(Color);

        AjedrezTablero[7][1] = _Caballo1;
        AjedrezTablero[7][6] = _Caballo2;

        //Alfil
        Alfil _Alfil1 = new Alfil(Color);
        Alfil _Alfil2 = new Alfil(Color);

        AjedrezTablero[7][2] = _Alfil1;
        AjedrezTablero[7][5] = _Alfil2;


        //Rey y reina
        Rey _Rey = new Rey(Color);
        Reina _Reina = new Reina(Color);

        AjedrezTablero[7][4] = _Rey;
        AjedrezTablero[7][3] = _Reina;

    }

    public void Cargar_Arriba_Matriz(Tipo_Color Color)
    {
        //Peones
        for (int i = 0; i <= 7; i++) {

            Peon Peon = new Peon(Color);

            AjedrezTablero[1][i] = Peon;
        }

        //Torre
        Torre _Torre1 = new Torre(Color);
        Torre _Torre2 = new Torre(Color);

        AjedrezTablero[0][0] = _Torre1;
        AjedrezTablero[0][7] = _Torre2;

        //Caballo
        Caballo _Caballo1 = new Caballo(Color);
        Caballo _Caballo2 = new Caballo(Color);

        AjedrezTablero[0][1] = _Caballo1;
        AjedrezTablero[0][6] = _Caballo2;

        //Alfiles
        Alfil _Alfil1 = new Alfil(Color);
        Alfil _Alfil2 = new Alfil(Color);

        AjedrezTablero[0][2] = _Alfil1;
        AjedrezTablero[0][5] = _Alfil2;

        //Rey y reina
        Rey _Rey = new Rey(Color);
        Reina _Reina = new Reina(Color);

        AjedrezTablero[0][4] = _Rey;
        AjedrezTablero[0][3] = _Reina;
    }

    // endregion

    // region MoverPieza
    public boolean MoverPieza(int fila, int columna, int MovFila, int MovColumna) {
        Pieza _pieza = AjedrezTablero[fila][columna];
        ArrayList<String> PosiblesMovs = CalcularPosiblesMovs(_pieza, fila, columna, AjedrezTablero);

        boolean movimientoValido = MovimientoValido(MovFila, MovColumna, PosiblesMovs);

        if (_EstadoDePartida.equals(EstadosPartida.ENCURSO) && movimientoValido) {
            if (!CheckAutoJaque(_pieza, MovFila, MovColumna)) {
                ConfirmarMovimiento(fila, columna, MovFila, MovColumna, _pieza);
            } else {
                return false;
            }

        } else if (_EstadoDePartida.equals(EstadosPartida.JAQUE) && movimientoValido) {
            boolean check = CheckSigueEnJaque(_pieza, MovFila, MovColumna);

            if (!check) {
                ConfirmarMovimiento(fila, columna, MovFila, MovColumna, _pieza);
            } else {
                return false;
            }
        } else {
            return false;
        }

        return movimientoValido;
    }

    // endregion

    // region Actualizar Posiciones de las clases
    public void ActualizarPosiciones() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Pieza pieza = AjedrezTablero[i][j];
                if (pieza != null) {
                    pieza.setFila(i);
                    pieza.setColumna(j);

                    if(pieza instanceof  Peon){
                        Peon peon = (Peon) pieza;
                        peon.ActualizarPeon();
                    }
                }
            }
        }
    }

    // endregion

    // region Confirmar Movimiento
    private void ConfirmarMovimiento(int fila, int columna, Integer movimientoFila, Integer movimientoColumna, Pieza _pieza) {
        AjedrezTablero[fila][columna] = null;
        AjedrezTablero[movimientoFila][movimientoColumna] = _pieza;

        EstadoDeJaque(AjedrezTablero, EncontraReyContrario(AjedrezTablero));
        ActualizarPosiciones();

        verificarVictoria();

        GuardarMovimiento(fila, columna, _pieza);

        ctrl_turno.CambiarTurno();
    }
    //endregion

    // region Movimiento valido
    public boolean MovimientoValido(int fila, int columna, ArrayList<String> PosiblesMovs) {
        boolean retorno = false;

        List<String> AllMovs = new ArrayList<>();
        List<Integer> Fila = new ArrayList<>();
        List<Integer> Columna = new ArrayList<>();

        for (int i = 0; i < PosiblesMovs.size(); i++) {
            String[] movs = PosiblesMovs.get(i).split(" ");
            Collections.addAll(AllMovs, movs);
        }

        for (int i = 0; i < AllMovs.size(); i++) {
            if (i % 2 == 0) {
                Integer NuevaFila = Integer.parseInt(AllMovs.get(i));
                Fila.add(NuevaFila);
            } else {
                Integer NuevaColumna = Integer.parseInt(AllMovs.get(i));
                Columna.add(NuevaColumna);
            }
        }

        //Movimiento valido!
        for (int i = 0; i < Fila.size(); i++) {
            if (Fila.get(i) == fila) {
                for (int j = 0; j < Columna.size(); j++) {
                    if (Columna.get(i) == columna) {
                        retorno = true;
                        break;
                    }
                }
            }
        }

        return retorno;
    }
    // endregion

    // region Calculo de movimientos

    public ArrayList<String> CalcularPosiblesMovs(Pieza _pieza, int fila, int columna, Pieza[][] tablero) {
        ArrayList<String> lst_Retorno = new ArrayList<>();//Primero FILA y Segundo COLUMNA
        HashMap<Direction, Integer> Movimiento = _pieza.Mov_Pieza();
        int newfila = -1;
        int newcolumna = -1;

        if (_pieza instanceof Caballo) {
            if (Movimiento.containsKey(Direction.TOPLEFT)) {
                newfila = fila - 2;
                newcolumna = columna - 1;
                if (newfila < 8 && newfila > -1 && newcolumna < 8 && newcolumna > -1) {
                    if (tablero[newfila][newcolumna] == null
                            || !tablero[newfila][newcolumna].getColor().equals(_pieza.getColor())) {
                        lst_Retorno.add(newfila + " " + newcolumna);
                    }
                }

            }
            if (Movimiento.containsKey(Direction.TOP)) {
                newfila = fila - 2;
                newcolumna = columna + 1;
                if (newfila < 8 && newfila > -1 && newcolumna < 8 && newcolumna > -1) {
                    if (tablero[newfila][newcolumna] == null
                            || !tablero[newfila][newcolumna].getColor().equals(_pieza.getColor())) {
                        lst_Retorno.add(newfila + " " + newcolumna);
                    }
                }

            }
            if (Movimiento.containsKey(Direction.TOPRIGHT)) {
                newfila = fila - 1;
                newcolumna = columna + 2;
                if (newfila < 8 && newfila > -1 && newcolumna < 8 && newcolumna > -1) {
                    if (tablero[newfila][newcolumna] == null
                            || !tablero[newfila][newcolumna].getColor().equals(_pieza.getColor())) {
                        lst_Retorno.add(newfila + " " + newcolumna);
                    }
                }

            }
            if (Movimiento.containsKey(Direction.RIGHT)) {
                newfila = fila + 1;
                newcolumna = columna + 2;
                if (newfila < 8 && newfila > -1 && newcolumna < 8 && newcolumna > -1) {
                    if (tablero[newfila][newcolumna] == null
                            || !tablero[newfila][newcolumna].getColor().equals(_pieza.getColor())) {
                        lst_Retorno.add(newfila + " " + newcolumna);
                    }
                }

            }
            if (Movimiento.containsKey(Direction.BOTTOMRIGHT)) {
                newfila = fila + 2;
                newcolumna = columna + 1;
                if (newfila < 8 && newfila > -1 && newcolumna < 8 && newcolumna > -1) {
                    if (tablero[newfila][newcolumna] == null
                            || !tablero[newfila][newcolumna].getColor().equals(_pieza.getColor())) {
                        lst_Retorno.add(newfila + " " + newcolumna);
                    }
                }

            }
            if (Movimiento.containsKey(Direction.BOTTOM)) {
                newfila = fila + 2;
                newcolumna = columna - 1;
                if (newfila < 8 && newfila > -1 && newcolumna < 8 && newcolumna > -1) {
                    if (tablero[newfila][newcolumna] == null
                            || !tablero[newfila][newcolumna].getColor().equals(_pieza.getColor())) {
                        lst_Retorno.add(newfila + " " + newcolumna);
                    }
                }

            }
            if (Movimiento.containsKey(Direction.BOTTOMLEFT)) {
                newfila = fila + 1;
                newcolumna = columna - 2;
                if (newfila < 8 && newfila > -1 && newcolumna < 8 && newcolumna > -1) {
                    if (tablero[newfila][newcolumna] == null
                            || !tablero[newfila][newcolumna].getColor().equals(_pieza.getColor())) {
                        lst_Retorno.add(newfila + " " + newcolumna);
                    }
                }

            }
            if (Movimiento.containsKey(Direction.LEFT)) {
                newfila = fila - 1;
                newcolumna = columna + 2;
                if (newfila < 8 && newfila > -1 && newcolumna < 8 && newcolumna > -1) {
                    if (tablero[newfila][newcolumna] == null
                            || !tablero[newfila][newcolumna].getColor().equals(_pieza.getColor())) {
                        lst_Retorno.add(newfila + " " + newcolumna);
                    }
                }
            }

        }
        /*****PEON*****/
        else if (_pieza instanceof Peon) {

            if (Movimiento.containsKey(Direction.TOP)) {
                //Restar la fila porque el movimiento es hacia arriba
                for (int i = 1; i <= Movimiento.get(Direction.TOP); i++) {
                    newfila = fila - i;
                    if (newfila < 8 && newfila > -1) {
                        if (tablero[newfila][columna] == null) {
                            lst_Retorno.add(newfila + " " + columna);
                        }
                    }
                }
            }

            if (Movimiento.containsKey(Direction.BOTTOM)) {
                //Sumar la fila porque el movimiento es hacia abajo
                for (int i = 1; i <= Movimiento.get(Direction.BOTTOM); i++) {
                    newfila = fila + i;
                    if (newfila < 8 && newfila > -1) {
                        if (tablero[newfila][columna] == null) {
                            lst_Retorno.add(newfila + " " + columna);
                        }
                    }
                }
            }


            //ATAQUE DE PEON
            //NEGRO
            if (_pieza.getColor().equals(Tipo_Color.NEGRO)) {
                //Abajo derecha
                for (int i = 1; i <= Movimiento.get(Direction.BOTTOM); i++) {
                    newfila = fila + i;
                    newcolumna = columna + i;
                    if (newfila < 8 && newfila > -1 && newcolumna < 8 && newcolumna > -1) {
                        if (tablero[newfila][newcolumna] != null && tablero[newfila][newcolumna].getColor().equals(Tipo_Color.BLANCO)) {
                            lst_Retorno.add(newfila + " " + newcolumna);
                        }
                    }
                }

                //Abajo izquierda
                for (int i = 1; i <= Movimiento.get(Direction.BOTTOM); i++) {
                    newfila = fila + i;
                    newcolumna = columna - i;
                    if (newfila < 8 && newfila > -1 && newcolumna < 8 && newcolumna > -1) {
                        if (tablero[newfila][newcolumna] != null && tablero[newfila][newcolumna].getColor().equals(Tipo_Color.BLANCO)) {
                            lst_Retorno.add(newfila + " " + newcolumna);
                        }
                    }
                }
            }

            //BLANCO
            if (_pieza.getColor().equals(Tipo_Color.BLANCO)) {
                //Arriba derecha
                for (int i = 1; i <= Movimiento.get(Direction.TOP); i++) {
                    newfila = fila - i;
                    newcolumna = columna + i;
                    if (newfila < 8 && newfila > -1 && newcolumna < 8 && newcolumna > -1) {
                        if (tablero[newfila][newcolumna] != null && tablero[newfila][newcolumna].getColor().equals(Tipo_Color.NEGRO)) {
                            lst_Retorno.add(newfila + " " + newcolumna);
                        }
                    }
                }

                //Arriba izquierda
                for (int i = 1; i <= Movimiento.get(Direction.TOP); i++) {
                    newfila = fila - i;
                    newcolumna = columna - i;
                    if (newfila < 8 && newfila > -1 && newcolumna < 8 && newcolumna > -1) {
                        if (tablero[newfila][newcolumna] != null && tablero[newfila][newcolumna].getColor().equals(Tipo_Color.NEGRO)) {
                            lst_Retorno.add(newfila + " " + newcolumna);
                        }
                    }
                }
            }
        }
        /******LAS DEMAS PIEZAS*******/
        else {
            if (Movimiento.containsKey(Direction.TOP)) {
                //Restar la fila porque el movimiento es hacia arriba
                for (int i = 1; i <= Movimiento.get(Direction.TOP); i++) {
                    newfila = fila - i;
                    if (newfila < 8 && newfila > -1) {
                        if (tablero[newfila][columna] == null) {
                            lst_Retorno.add(newfila + " " + columna);

                        } else if (!tablero[newfila][columna].getColor().equals(_pieza.getColor())) {
                            lst_Retorno.add(newfila + " " + columna);
                            break;
                        } else {
                            break;
                        }
                    }
                }
            }
            if (Movimiento.containsKey(Direction.BOTTOM)) {
                //Sumar la fila porque el movimiento es hacia abajo
                for (int i = 1; i <= Movimiento.get(Direction.BOTTOM); i++) {
                    newfila = fila + i;
                    if (newfila < 8 && newfila > -1) {
                        if (tablero[newfila][columna] == null) {
                            lst_Retorno.add(newfila + " " + columna);
                        } else if (!tablero[newfila][columna].getColor().equals(_pieza.getColor())) {
                            lst_Retorno.add(newfila + " " + columna);
                            break;
                        } else {
                            break;
                        }
                    }
                }
            }
            if (Movimiento.containsKey(Direction.LEFT)) {
                //Restar la columna porque el movimiento es hacia la izquierda
                for (int i = 1; i <= Movimiento.get(Direction.LEFT); i++) {
                    newcolumna = columna - i;
                    if (newcolumna < 8 && newcolumna > -1) {
                        if (tablero[fila][newcolumna] == null) {
                            lst_Retorno.add(fila + " " + newcolumna);
                        } else if (!tablero[fila][newcolumna].getColor().equals(_pieza.getColor())) {
                            lst_Retorno.add(fila + " " + newcolumna);
                            break;
                        } else {
                            break;
                        }
                    }
                }
            }
            if (Movimiento.containsKey(Direction.RIGHT)) {
                //Sumar la columna porque el movimiento es hacia la derecha
                for (int i = 1; i <= Movimiento.get(Direction.RIGHT); i++) {
                    newcolumna = columna + i;
                    if (newcolumna < 8 && newcolumna > -1) {
                        if (tablero[fila][newcolumna] == null) {
                            lst_Retorno.add(fila + " " + newcolumna);
                        } else if (!tablero[fila][newcolumna].getColor().equals(_pieza.getColor())) {
                            lst_Retorno.add(fila + " " + newcolumna);
                            break;
                        } else {
                            break;
                        }
                    }

                }
            }
            /** DIAGONALES **/
            if (Movimiento.containsKey(Direction.TOPLEFT)) {
                //RESTAR FILA movimiento arriba
                //RESTAR COLUMNA movimiento hacia izquierda
                for (int i = 1; i <= Movimiento.get(Direction.TOPLEFT); i++) {
                    newfila = fila - i;
                    newcolumna = columna - i;
                    if (newfila < 8 && newfila > -1 && newcolumna < 8 && newcolumna > -1) {
                        if (tablero[newfila][newcolumna] == null) {
                            lst_Retorno.add(newfila + " " + newcolumna);
                        } else if (!tablero[newfila][newcolumna].getColor().equals(_pieza.getColor())) {
                            lst_Retorno.add(newfila + " " + newcolumna);
                            break;
                        } else {
                            break;
                        }
                    }
                }
            }
            if (Movimiento.containsKey(Direction.TOPRIGHT)) {
                //RESTAR FILA movimiento arriba
                //SUMAR COLUMNA movimiento hacia derecha
                for (int i = 1; i <= Movimiento.get(Direction.TOPLEFT); i++) {
                    newfila = fila - i;
                    newcolumna = columna + i;
                    if (newfila < 8 && newfila > -1 && newcolumna < 8 && newcolumna > -1) {
                        if (tablero[newfila][newcolumna] == null) {
                            lst_Retorno.add(newfila + " " + newcolumna);
                        } else if (!tablero[newfila][newcolumna].getColor().equals(_pieza.getColor())) {
                            lst_Retorno.add(newfila + " " + newcolumna);
                            break;
                        } else {
                            break;
                        }
                    }
                }
            }
            if (Movimiento.containsKey(Direction.BOTTOMLEFT)) {
                //SUMAR FILA movimiento abajo
                //RESTAR COLUMNA movimiento hacia izquierda
                for (int i = 1; i <= Movimiento.get(Direction.BOTTOMLEFT); i++) {
                    newfila = fila + i;
                    newcolumna = columna - i;
                    if (newfila < 8 && newfila > -1 && newcolumna < 8 && newcolumna > -1) {
                        if (tablero[newfila][newcolumna] == null) {
                            lst_Retorno.add(newfila + " " + newcolumna);
                        } else if (!tablero[newfila][newcolumna].getColor().equals(_pieza.getColor())) {
                            lst_Retorno.add(newfila + " " + newcolumna);
                            break;
                        } else {
                            break;
                        }
                    }
                }
            }
            if (Movimiento.containsKey(Direction.BOTTOMRIGHT)) {
                //SUMAR FILA movimiento abajo
                //SUMAR COLUMNA movimiento hacia derecha
                for (int i = 1; i <= Movimiento.get(Direction.BOTTOMRIGHT); i++) {
                    newfila = fila + i;
                    newcolumna = columna + i;
                    if (newfila < 8 && newfila > -1 && newcolumna < 8 && newcolumna > -1) {
                        if (tablero[newfila][newcolumna] == null) {
                            lst_Retorno.add(newfila + " " + newcolumna);
                        } else if (!tablero[newfila][newcolumna].getColor().equals(_pieza.getColor())) {
                            lst_Retorno.add(newfila + " " + newcolumna);
                            break;
                        } else {
                            break;
                        }
                    }
                }
            }
        }

        return lst_Retorno;
    }


    // endregion

    // region Clonación de tablero
    private Pieza[][] ClonarTableroActual() {

        Pieza[][] tableroTemporal = Arrays.copyOf(AjedrezTablero, AjedrezTablero.length);

        for (int i = 0; i < AjedrezTablero.length; i++) {
            tableroTemporal[i] = Arrays.copyOf(AjedrezTablero[i], AjedrezTablero[i].length);
        }

        return tableroTemporal;
    }
    // endregion

    // region Mostrar Posibles Movimientos

    //Mostrar Movimientos por consola - Sin notación Algebraica
    private void MostrarMovimientos(ArrayList<String> lst_Retorno) {
        for (int i = 0; i < lst_Retorno.size(); i++) {
            System.out.println("Movimiento: " + i + ":" + lst_Retorno.get(i) + "\n");
        }
    }

    // endregion

    // region Comprobación de condiciones de victoria

    public boolean verificarVictoria() {
        // Verificar jaque mate
        if (esJaqueMate()) {

            if (ctrl_turno.getTurnoActual().equals(Tipo_Color.BLANCO)) {
                _EstadoDePartida = EstadosPartida.GANADOR_BLANCO;
            } else {
                _EstadoDePartida = EstadosPartida.GANADOR_NEGRO;
            }

            return true;
        }

        // Verificar empate por ahogado
        if (esAhogado()) {

            _EstadoDePartida = EstadosPartida.EMPATE;

            return true;
        }


        return false;
    }

    // region Jaque
    private boolean EstadoDeJaque(Pieza[][] tablero, Pieza rey) {
        boolean retorno = false;

        if (estaEnJaque(rey, tablero)) {
            _EstadoDePartida = EstadosPartida.JAQUE;
            retorno = true;
        } else {
            _EstadoDePartida = EstadosPartida.ENCURSO;
        }
        return retorno;
    }

    private boolean estaEnJaque(Pieza _pieza, Pieza[][] tablero) {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Pieza pieza = tablero[i][j];
                if (pieza != null && pieza.getColor() != _pieza.getColor()) {
                    ArrayList<String> posiblesMovs = CalcularPosiblesMovs(pieza, i, j, tablero);
                    for (String mov : posiblesMovs) {
                        int movFila = Integer.parseInt(mov.split(" ")[0]);
                        int movColumna = Integer.parseInt(mov.split(" ")[1]);
                        if (movFila == _pieza.getFila() && movColumna == _pieza.getColumna()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    // endregion

    // region Autojaque y Sigue en Jaque

    private boolean CheckAutoJaque(Pieza _pieza, int filaMovimiento, int columnaMovimiento) {
        Pieza[][] tableroTemporal = ClonarTableroActual();

        tableroTemporal[filaMovimiento][columnaMovimiento] = _pieza;
        tableroTemporal[_pieza.getFila()][_pieza.getColumna()] = null;

        Pieza rey = EncontraReyActual(tableroTemporal);

        if (estaEnJaque(rey, tableroTemporal)) {
            return true;
        }

        return false;
    }

    private boolean CheckSigueEnJaque(Pieza pieza, Integer movimientoFila, Integer movimientoColumna) {
        boolean retorno;

        Pieza[][] tableroTemporal = ClonarTableroActual();

        tableroTemporal[pieza.getFila()][pieza.getColumna()] = null;
        tableroTemporal[movimientoFila][movimientoColumna] = pieza;

        retorno = EstadoDeJaque(tableroTemporal, EncontraReyActual(tableroTemporal));

        return retorno;
    }

    // endregion

    // region Encontrar Rey actual o contrario

    private Pieza EncontraReyActual(Pieza[][] tablero) {
        Pieza _rey = null;
        Pieza ReyTemporal = null;
        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                _rey = tablero[fila][columna];
                if (_rey instanceof Rey && _rey.getColor() == ctrl_turno.getTurnoActual()) {

                    ReyTemporal = _rey.clone();

                    ReyTemporal.setFila(fila);
                    ReyTemporal.setColumna(columna);
                    break;
                }
            }
            if (ReyTemporal != null) {
                break;
            }
        }

        return ReyTemporal;
    }

    private Pieza EncontraReyContrario(Pieza[][] tablero) {
        int reyFila = -1;
        int reyColumna = -1;
        Pieza _rey = null;
        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                _rey = tablero[fila][columna];
                if (_rey instanceof Rey && _rey.getColor() != ctrl_turno.getTurnoActual()) {
                    reyFila = fila;
                    reyColumna = columna;
                    break;
                }
            }
            if (reyFila != -1) {
                break;
            }
        }

        return _rey;
    }

    // endregion

    // region JaqueMate
    public boolean esJaqueMate() {

        // Encontrar el rey del jugador contrario al turno actual
        Pieza _rey = EncontraReyContrario(AjedrezTablero);
        if (_rey == null) {
            return false;
        }

        // Verificar si el rey esta en jaque
        if (!estaEnJaque(_rey, AjedrezTablero)) {
            return false;
        }

        // Verificar si el rey puede moverse a alguna casilla válida
        if (puedeMoverseJaque(CalcularPosiblesMovs(_rey, _rey.getFila(), _rey.getColumna(), AjedrezTablero), _rey)) {
            return false;
        }

        // Verificar si alguna pieza puede bloquear o capturar la pieza amenazante
        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                Pieza pieza = AjedrezTablero[fila][columna];
                if (pieza != null && pieza.getColor().equals(_rey.getColor()) && !pieza.getTipoPieza().equals(Tipo_Pieza.REY)) {
                    if (puedeAtacar(CalcularPosiblesMovs(pieza, pieza.getFila(), pieza.getColumna(), AjedrezTablero), pieza)) {
                        return false;
                    }
                }
            }
        }

        // Si no se encontraron movimientos válidos, es jaque mate
        return true;
    }

    // region Movimiento temporal, para verificación de jaquemate
    private boolean puedeMoverseJaque(ArrayList<String> lst_movs, Pieza pieza) {

        // Realizar el movimiento en una copia temporal del tablero
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (MovimientoValido(i, j, lst_movs)) {
                    Pieza[][] tableroTemporal = ClonarTableroActual();
                    tableroTemporal[pieza.getFila()][pieza.getColumna()] = null;
                    tableroTemporal[i][j] = pieza;

                    //Encontrar rey actual en la matriz temporal.
                    Pieza _reyViejo = EncontraReyContrario(tableroTemporal);
                    Pieza _rey = _reyViejo.clone();
                    _rey.setFila(i);
                    _rey.setColumna(j);

                    // Verificar si el rey queda en jaque después del movimiento
                    if (!estaEnJaque(_rey, tableroTemporal)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean puedeMoversePieza(ArrayList<String> lst_movs, Pieza pieza) {

        // Realizar el movimiento en una copia temporal del tablero
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (MovimientoValido(i, j, lst_movs)) {
                    Pieza[][] tableroTemporal = ClonarTableroActual();
                    tableroTemporal[pieza.getFila()][pieza.getColumna()] = null;
                    tableroTemporal[i][j] = pieza;

                    Pieza _reyViejo = EncontraReyContrario(tableroTemporal);

                    // Verificar si el rey queda en jaque después del movimiento
                    if (!estaEnJaque(_reyViejo, tableroTemporal)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // endregion

    public boolean puedeAtacar(ArrayList<String> lst_movs, Pieza pieza) {
        if (pieza != null) {
            return puedeMoversePieza(lst_movs, pieza);
        }
        return false;
    }
    // endregion

    // region Ahogado
    public boolean esAhogado() {
        // Obtener la posición del rey del jugador actual
        Pieza _rey = EncontraReyContrario(AjedrezTablero);

        // Verificar si el rey está en jaque
        if (estaEnJaque(_rey, AjedrezTablero)) {
            return false; // El rey está en jaque, no hay empate por ahogado
        }

        // Verificar si el rey no puede moverse a ninguna casilla
        if (puedeMoverseJaque(CalcularPosiblesMovs(_rey, _rey.getFila(), _rey.getColumna(), AjedrezTablero), _rey)) {
            return false;
        }

        // Verificar si no hay movimientos posibles para ninguna de las otras piezas del jugador
        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                Pieza pieza = AjedrezTablero[fila][columna];
                if (pieza != null && pieza.getColor().equals(_rey.getColor())) {
                    if (!(pieza instanceof Rey) && puedeMoverse(CalcularPosiblesMovs(pieza, pieza.getFila(), pieza.getColumna(), AjedrezTablero), pieza)) {
                        return false;
                    }
                }
            }
        }


        return true; // No hay movimientos posibles para ninguna pieza, hay empate por ahogado
    }

    private boolean puedeMoverse(ArrayList<String> calcularPosiblesMovs, Pieza pieza) {
        if (pieza != null) {
            return puedeMoverseAhogado(calcularPosiblesMovs, pieza);
        }
        return false;
    }

    private boolean puedeMoverseAhogado(ArrayList<String> lst_movs, Pieza pieza) {

        // Realizar el movimiento en una copia temporal del tablero
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (MovimientoValido(i, j, lst_movs)) {
                    Pieza[][] tableroTemporal = ClonarTableroActual();
                    tableroTemporal[pieza.getFila()][pieza.getColumna()] = null;
                    tableroTemporal[i][j] = pieza;

                    return true;
                }
            }
        }

        return false;
    }

    // endregion

    // endregion

    // region Guardar Movimientos
    private void GuardarMovimiento(Integer Fila, Integer Columna, Pieza pieza) {
        char[] columnas = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};

        if(Fila != -1 && Columna != -1 && pieza != null) {
            lst_HistorialMovimiento.add("Pieza: " + pieza.getTipoPieza() + " " + pieza.getColor() + " |" + "Fila Origen: " + pieza.getFila() + " |" + "Columna Origen: " + pieza.getColumna()
                    + " |" + "Fila Destino: " + Fila + "| Columna Destino: " + Columna);

            char columnaAlgebraica = columnas[Columna];
            char columnaOrigenAlgebraica = columnas[pieza.getColumna()];

            lst_HisMovs_Notacion.add("Pieza: " + pieza.getTipoPieza() + " " + pieza.getColor() + " |" + "Fila Origen: " + pieza.getFila() + " |" + "Columna Origen: " + columnaOrigenAlgebraica
                    + " |" + "Fila Destino: " + Fila + "| Columna Destino: " + columnaAlgebraica);
        }
        else{
            System.out.println("Error en el guardado de historial de movimientos");
        }
    }
    // endregion

}
