package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.Exception.PartidaNotFoundException;
import ar.edu.utn.frc.tup.lciii.model.Enums.Tipo_Color;
import ar.edu.utn.frc.tup.lciii.model.Enums.Tipo_Pieza;
import ar.edu.utn.frc.tup.lciii.model.Partida;
import ar.edu.utn.frc.tup.lciii.model.Pieza;
import ar.edu.utn.frc.tup.lciii.model.Posicion;
import ar.edu.utn.frc.tup.lciii.model.vTablero;
import ar.edu.utn.frc.tup.lciii.repository.PartidaRepository;
import ar.edu.utn.frc.tup.lciii.repository.PosicionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class PartidaServiceImpl implements PartidaService {
    @Autowired
    private PartidaRepository partidaRepository;

    @Autowired
    private PosicionRepository posicionRepository;

    @Override
    public Partida guardarPartida(Partida partida) {
        Partida partidaGuardada = partidaRepository.save(partida);
        if (partidaGuardada.getPosiciones() != null) {
            for (Posicion posicion : partidaGuardada.getPosiciones()) {
                posicion.setPartida(partidaGuardada);
                posicionRepository.save(posicion);
            }
        }
        return partidaGuardada;
    }


    @Override
    public Partida cargarPartida(Long partidaId) {
        Optional<Partida> optionalPartida = partidaRepository.findById(partidaId);
        if (optionalPartida.isPresent()) {
            Partida partida = optionalPartida.get();

            List<Posicion> posiciones = posicionRepository.findByPartidaId(partidaId);

            Pieza[][] ajedrezTablero = convertirPosicionesATablero(posiciones);

            for (int fila = 0; fila < 8; fila++) {
                for (int columna = 0; columna < 8; columna++) {
                    Pieza pieza = ajedrezTablero[fila][columna];
                    if (pieza != null) {
                        pieza.setFila(fila);
                        pieza.setColumna(columna);
                    }
                }
            }

            vTablero tablero = new vTablero();
            tablero.setAjedrezTablero(ajedrezTablero);

            return partida;
        } else {
            throw new PartidaNotFoundException("No se encontró la partida con el ID: " + partidaId);
        }
    }

    @Override
    public Partida actualizarPartida(Long partidaId, Partida partidaActualizada) {
        Partida partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new PartidaNotFoundException("No se encontro la partida con el ID: " + partidaId));

        partida.setPosiciones(partidaActualizada.getPosiciones());

        return partidaRepository.save(partida);
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

            tablero[fila][columna] = pieza;
        }

        return tablero;
    }
}
