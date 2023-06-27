package ar.edu.utn.frc.tup.lciii.controller;


import ar.edu.utn.frc.tup.lciii.Exception.PartidaNotFoundException;
import ar.edu.utn.frc.tup.lciii.model.Enums.Tipo_Color;
import ar.edu.utn.frc.tup.lciii.model.Enums.Tipo_Pieza;
import ar.edu.utn.frc.tup.lciii.model.Jugador.Jugador;
import ar.edu.utn.frc.tup.lciii.model.Partida;
import ar.edu.utn.frc.tup.lciii.model.Pieza;
import ar.edu.utn.frc.tup.lciii.model.Posicion;
import ar.edu.utn.frc.tup.lciii.model.vTablero;
import ar.edu.utn.frc.tup.lciii.service.PartidaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/tablero")
public class AjedrezController {

    // region Atributos de API
    private vTablero tablero;
    private Tipo_Color J1Color = null;
    private Tipo_Color J2Color = null;
    @Autowired
    private PartidaService partidaService;

    // endregion

    // region Get's

    @GetMapping("/tablero")
    @Operation(summary = "Obtener tablero")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Tablero no iniciado")
    })
    public ResponseEntity<Pieza[][]> obtenerTablero() {
        if (tablero == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(tablero.getAjedrezTablero());
    }

    @GetMapping("/HistorialDeMovs")
    @Operation(summary = "Detallar movimientos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "No se realizo ningún movimiento")
    })
    public ResponseEntity<ArrayList<String>> HistorialDeMovimientos() {

        if (tablero == null) {
            ArrayList<String> BodyBadRequest = new ArrayList<>();
            BodyBadRequest.add("Tiene que inicializar tablero");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BodyBadRequest);
        }

        ArrayList<String> lst = tablero.getLst_HistorialMovimiento();

        if(lst.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(lst);
    }

    @GetMapping("/CheckEstadoDeJuego")
    @Operation(summary = "Obtener Estado de juego")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "500", description = "Error en el estado de juego")
    })
    public ResponseEntity<String> ObtenerEstadoDeJuego() {

        if (tablero == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El tablero no ha sido iniciado.");
        }

        return ResponseEntity.ok(tablero.get_EstadoDePartida().toString());
    }

    @GetMapping("/posicion")
    public ResponseEntity<String> obtenerPosicionPieza(@RequestParam("fila") int fila, @RequestParam("columna") int columna) {
        if (tablero == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El tablero no ha sido iniciado.");
        }

        Pieza[][] tableroActual = tablero.getAjedrezTablero();

        if (fila < 0 || fila >= tableroActual.length || columna < 0 || columna >= tableroActual[0].length) {
            return ResponseEntity.badRequest().body("Posición inválida.");
        }

        Pieza pieza = tableroActual[fila][columna];

        if (pieza == null) {
            return ResponseEntity.ok("No hay pieza en la posición especificada.");
        } else {
            String posicionActual = "La pieza "+ pieza.getTipoPieza() + " "+ pieza.getColor() + " se encuentra en la posición (" + fila + ", " + columna + ").";
            return ResponseEntity.ok(posicionActual);
        }
    }

    @GetMapping("/cargar-partida")
    @Operation(summary = "Cargar partida")
    @ApiResponse(responseCode = "200", description = "Partida cargada exitosamente")
    public ResponseEntity<Pieza[][]> CargarPartida(@RequestParam("partidaId") Long partidaId) {
        try {
            Partida partida = partidaService.cargarPartida(partidaId);

            List<Posicion> posiciones = partida.getPosiciones();
            Pieza[][] ajedrezTablero = convertirPosicionesATablero(posiciones);

            return ResponseEntity.ok(ajedrezTablero);
        } catch (PartidaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/actualizar-partida")
    @Operation(summary = "Actualizar partida")
    @ApiResponse(responseCode = "200", description = "Partida actualizada exitosamente")
    public ResponseEntity<String> actualizarPartida(@RequestParam("partidaId") Long partidaId) {
        if (tablero == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El tablero no ha sido iniciado.");
        }

        Partida partida = new Partida();
        Pieza[][] ajedrezTablero = tablero.getAjedrezTablero();
        List<Posicion> posiciones = convertirTableroAPosiciones(ajedrezTablero);
        partida.setPosiciones(posiciones);

        try {
            partida = partidaService.actualizarPartida(partidaId, partida);

            return ResponseEntity.ok("Partida actualizada exitosamente. ID: " + partida.getId());
        } catch (PartidaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // endregion

    // region Post's

    @PostMapping("/iniciar")
    @Operation(summary = "Iniciar tablero")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<String> iniciarTablero(@RequestParam("Nombre Jugador 1") String NombreJ1,@RequestParam("Nombre Jugador 2") String NombreJ2,
                                                 @RequestParam("Color de Jugador 1") String Color1, @RequestParam("Color de Jugador 2") String Color2) {

        if(NombreJ1 != null || NombreJ1 != ""
                && NombreJ2 != null || NombreJ2 != ""){
            if(ValidacionColores(Color1, Color2)){
                Jugador J1 = new Jugador(NombreJ1, J1Color);
                Jugador J2 = new Jugador(NombreJ2, J2Color);

                tablero = vTablero.getInstance(J1, J2);
                return ResponseEntity.ok("Tablero iniciado correctamente.");
            }
            else{
                return ResponseEntity.badRequest().body("Color invalido para jugador");
            }
        }
        else{
            return ResponseEntity.ofNullable("Nombre vacio");
        }
    }

    @PostMapping("/mover")
    @Operation(summary = "Mover pieza")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "OK"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<String> moverPieza(@RequestParam("fila") int fila, @RequestParam("columna") int columna,
                                             @RequestParam("movimientoFila") int movimientoFila, @RequestParam("movimientoColumna") int movimientoColumna){
        if (tablero == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El tablero no ha sido iniciado.");
        }

        Pieza pieza = tablero.getAjedrezTablero()[fila][columna];

        if(pieza == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay una pieza en este casillero.");
        }

        if(!pieza.getColor().equals(vTablero.ctrl_turno.getTurnoActual())) {
            return ResponseEntity.badRequest().body("No es el turno de: " +vTablero.ctrl_turno.getTurnoActual());
        }

        boolean movimientoValido = false;
        movimientoValido = tablero.MoverPieza(fila, columna, movimientoFila, movimientoColumna);

        if (!movimientoValido) {
            return ResponseEntity.badRequest().body("Movimiento inválido.");
        }
        return ResponseEntity.ok("Movimiento realizado correctamente.");
    }

    @PostMapping("/guardar-partida")
    @Operation(summary = "Guardar partida")
    @ApiResponse(responseCode = "200", description = "Partida guardada exitosamente")
    public ResponseEntity<String> guardarPartida() {
        if (tablero == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El tablero no ha sido iniciado.");
        }

        Partida partida = new Partida();
        Pieza[][] ajedrezTablero = tablero.getAjedrezTablero();
        List<Posicion> posiciones = convertirTableroAPosiciones(ajedrezTablero);
        partida.setPosiciones(posiciones);

        partida = partidaService.guardarPartida(partida);

        return ResponseEntity.ok("Partida guardada exitosamente. ID: " + partida.getId());
    }


    // endregion

    // region Metodos de Apoyo

    private boolean ValidacionColores(String Color1, String Color2){
        boolean retorno = false;

        if(Color1.equalsIgnoreCase("blanco") || Color1.equalsIgnoreCase("blancos")
                || Color1.equalsIgnoreCase("blanca") || Color1.equalsIgnoreCase("blancas")
                && Color2.equalsIgnoreCase("negro") || Color2.equalsIgnoreCase("negros")
                || Color2.equalsIgnoreCase("negra") || Color2.equalsIgnoreCase("negras")) {
            J1Color = Tipo_Color.BLANCO;
            J2Color = Tipo_Color.NEGRO;
            retorno=true;
        } else if (Color2.equalsIgnoreCase("blanco") || Color2.equalsIgnoreCase("blancos")
                || Color2.equalsIgnoreCase("blanca") || Color2.equalsIgnoreCase("blancas")
                && Color1.equalsIgnoreCase("negro") || Color1.equalsIgnoreCase("negros")
                || Color1.equalsIgnoreCase("negra") || Color1.equalsIgnoreCase("negras")) {
            J1Color = Tipo_Color.NEGRO;
            J2Color = Tipo_Color.BLANCO;
            retorno = true;
        }


        return  retorno;
    }

    private List<Posicion> convertirTableroAPosiciones(Pieza[][] ajedrezTablero) {
        List<Posicion> posiciones = new ArrayList<>();
        for (int fila = 0; fila < ajedrezTablero.length; fila++) {
            for (int columna = 0; columna < ajedrezTablero[fila].length; columna++) {
                Pieza pieza = ajedrezTablero[fila][columna];
                if (pieza != null) {
                    Tipo_Color color = pieza.getColor();
                    Tipo_Pieza tipoPieza = pieza.getTipoPieza();
                    Posicion posicion = new Posicion(fila, columna, color, tipoPieza);
                    posiciones.add(posicion);
                }
            }
        }
        return posiciones;
    }

    private Pieza[][] convertirPosicionesATablero(List<Posicion> posiciones) {
        Pieza[][] tablero = new Pieza[8][8];

        for (Posicion posicion : posiciones) {
            int fila = posicion.getFila();
            int columna = posicion.getColumna();
            Tipo_Color color = posicion.getColor();
            Tipo_Pieza tipoPieza = posicion.getTipoPieza();

            Pieza pieza = new Pieza();
            pieza.setColor(color);
            pieza.setTipoPieza(tipoPieza);
            pieza.setFila(fila);
            pieza.setColumna(columna);

            tablero[fila][columna] = pieza;
        }

        return tablero;
    }

    // endregion

}
