package ar.edu.utn.frc.tup.lciii.service;


import ar.edu.utn.frc.tup.lciii.model.Partida;

public interface PartidaService {

    public Partida guardarPartida(Partida partida);

    public Partida cargarPartida(Long partidaId);

    public Partida actualizarPartida(Long partidaId, Partida partidaActualizada);
}
