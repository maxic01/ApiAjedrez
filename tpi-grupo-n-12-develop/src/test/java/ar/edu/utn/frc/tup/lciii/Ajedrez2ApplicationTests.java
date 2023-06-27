package ar.edu.utn.frc.tup.lciii;

import ar.edu.utn.frc.tup.lciii.Exception.PartidaNotFoundException;
import ar.edu.utn.frc.tup.lciii.model.*;
import ar.edu.utn.frc.tup.lciii.model.Enums.Tipo_Color;
import ar.edu.utn.frc.tup.lciii.model.Enums.Tipo_Pieza;
import ar.edu.utn.frc.tup.lciii.model.Jugador.Jugador;
import ar.edu.utn.frc.tup.lciii.repository.PartidaRepository;
import ar.edu.utn.frc.tup.lciii.repository.PosicionRepository;
import ar.edu.utn.frc.tup.lciii.service.PartidaServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class Ajedrez2ApplicationTests {

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;
    private ByteArrayOutputStream testOut;
    
    @Test
    void contextLoads() {
    }

    @Mock
    private PartidaRepository partidaRepository;
    @Mock
    private PosicionRepository posicionRepository;

    @InjectMocks
    private PartidaServiceImpl partidaService;

    @BeforeEach
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    @Test
    public void testMain() {
        Ajedrez2Application.main(null);
        assertEquals("Hello, TPI Chess." + System.lineSeparator(),
                getOutput());
    }

    private String getOutput() {
        return testOut.toString();
    }

    @Test
    public void testCalcularPosiblesMovs() {
        Pieza[][] tablero = new Pieza[8][8];
        vTablero tablerovirtual = new vTablero();

        Reina reina = new Reina(Tipo_Color.BLANCO);
        reina.setFila(7);
        reina.setColumna(4);

        tablero[7][4] = reina;

        ArrayList<String> movimientos = tablerovirtual.CalcularPosiblesMovs(reina, reina.getFila(), reina.getColumna(), tablero);

        ArrayList<String> movimientosEsperados = new ArrayList<>();
        movimientosEsperados.add("6 4");
        movimientosEsperados.add("5 4");
        movimientosEsperados.add("4 4");
        movimientosEsperados.add("3 4");
        movimientosEsperados.add("2 4");
        movimientosEsperados.add("1 4");
        movimientosEsperados.add("0 4");
        movimientosEsperados.add("7 3");
        movimientosEsperados.add("7 2");
        movimientosEsperados.add("7 1");
        movimientosEsperados.add("7 0");
        movimientosEsperados.add("7 5");
        movimientosEsperados.add("7 6");
        movimientosEsperados.add("7 7");
        movimientosEsperados.add("6 3");
        movimientosEsperados.add("5 2");
        movimientosEsperados.add("4 1");
        movimientosEsperados.add("3 0");
        movimientosEsperados.add("6 5");
        movimientosEsperados.add("5 6");
        movimientosEsperados.add("4 7");

        Assertions.assertEquals(movimientosEsperados, movimientos);
    }

    @Test
    public void testMovimientoValido() throws Exception {
        vTablero tablero = new vTablero();

        vTablero tableroSpy = spy(tablero);

        int fila = 2;
        int columna = 3;
        ArrayList<String> posiblesMovs = new ArrayList<>();
        posiblesMovs.add("1 2");
        posiblesMovs.add("3 4");
        posiblesMovs.add("2 3");

        boolean resultado = tableroSpy.MovimientoValido(fila, columna, posiblesMovs);

        assertTrue(resultado);
    }

    @Test
    public void testMovimientoNoValido() throws Exception {
        vTablero tablero = new vTablero();

        vTablero tableroSpy = spy(tablero);

        int fila = 5;
        int columna = 6;
        ArrayList<String> posiblesMovs = new ArrayList<>();
        posiblesMovs.add("1 2");
        posiblesMovs.add("3 4");
        posiblesMovs.add("2 3");

        boolean resultado = tableroSpy.MovimientoValido(fila, columna, posiblesMovs);

        assertFalse(resultado);
    }

    @Test
    public void testActualizarPosiciones() {
        vTablero tablero = new vTablero();

        Pieza[][] ajedrezTablero = new Pieza[8][8];

        Peon peonMock = mock(Peon.class);
        when(peonMock instanceof Peon).thenReturn(true);
        when(peonMock.getFila()).thenReturn(0);
        when(peonMock.getColumna()).thenReturn(0);

        ajedrezTablero[5][5] = peonMock;

        tablero.setAjedrezTablero(ajedrezTablero);

        tablero.ActualizarPosiciones();

        assertEquals(0, peonMock.getFila());
        assertEquals(5, peonMock.getColumna());
        verify(peonMock).ActualizarPeon();
    }

    @Test
    void testCargarAbajoMatriz() {
        vTablero tablero = new vTablero();
        Tipo_Color color = Tipo_Color.BLANCO;

        tablero.Cargar_Abajo_Matriz(color);

        Pieza[][] ajedrezTablero = tablero.getAjedrezTablero();

        for (int j = 0; j <= 7; j++) {
            assertTrue(ajedrezTablero[6][j] instanceof Peon);
        }

        assertTrue(ajedrezTablero[7][0] instanceof Torre);
        assertTrue(ajedrezTablero[7][7] instanceof Torre);

        assertTrue(ajedrezTablero[7][1] instanceof Caballo);
        assertTrue(ajedrezTablero[7][6] instanceof Caballo);

        assertTrue(ajedrezTablero[7][2] instanceof Alfil);
        assertTrue(ajedrezTablero[7][5] instanceof Alfil);

        assertTrue(ajedrezTablero[7][4] instanceof Rey);

        assertTrue(ajedrezTablero[7][3] instanceof Reina);
    }

    @Test
    public void testCargarArribaMatriz() {
        vTablero tablero = new vTablero();
        Tipo_Color color = Tipo_Color.BLANCO;

        tablero.Cargar_Arriba_Matriz(color);

        Pieza[][] ajedrezTablero = tablero.getAjedrezTablero();

        for (int i = 0; i <= 7; i++) {
            assertTrue(ajedrezTablero[1][i] instanceof Peon);
        }

        assertTrue(ajedrezTablero[0][0] instanceof Torre);
        assertTrue(ajedrezTablero[0][7] instanceof Torre);

        assertTrue(ajedrezTablero[0][1] instanceof Caballo);
        assertTrue(ajedrezTablero[0][6] instanceof Caballo);

        assertTrue(ajedrezTablero[0][2] instanceof Alfil);
        assertTrue(ajedrezTablero[0][5] instanceof Alfil);

        assertTrue(ajedrezTablero[0][4] instanceof Rey);

        assertTrue(ajedrezTablero[0][3] instanceof Reina);
    }

    @Test
    void cambiarTurnoCuandoTurnoActualEsNegro() {
        ControladorTurno controladorTurno = new ControladorTurno();
        controladorTurno.CambiarTurno();
        controladorTurno.CambiarTurno();
        assertEquals(Tipo_Color.BLANCO, controladorTurno.getTurnoActual());
    }

    @Test
    void cambiarTurnoCuandoTurnoActualEsBlanco() {
        ControladorTurno controladorTurno = new ControladorTurno();
        assertEquals(Tipo_Color.BLANCO, controladorTurno.getTurnoActual());

        controladorTurno.CambiarTurno();

        assertEquals(Tipo_Color.NEGRO, controladorTurno.getTurnoActual());
    }


    @Test
    void ObtenerIntanciaCuandoSeCreanDosJugadores() {
        Jugador jugador1 = new Jugador("Santiago", Tipo_Color.BLANCO);
        Jugador jugador2 = new Jugador("Juan", Tipo_Color.NEGRO);

        vTablero tablero = vTablero.getInstance(jugador1, jugador2);

        assertNotNull(tablero);
        assertEquals(jugador1, tablero.getJugador1());
        assertEquals(jugador2, tablero.getJugador2());
    }

    @Test
    void convertirPosicionesATableroConListaVacia() {
        List<Posicion> posiciones = new ArrayList<>();
        Pieza[][] expectedTablero = new Pieza[8][8];

        Pieza[][] actualTablero = ReflectionTestUtils.invokeMethod(partidaService, "convertirPosicionesATablero", posiciones);

        assertArrayEquals(expectedTablero, actualTablero);
    }


    @Test
    void convertirPosicionesATableroConValoresNulos() {
        List<Posicion> posiciones = new ArrayList<>();
        posiciones.add(new Posicion(0, 0, Tipo_Color.BLANCO, Tipo_Pieza.PEON));
        posiciones.add(null);
        posiciones.add(new Posicion(1, 1, Tipo_Color.NEGRO, Tipo_Pieza.TORRE));

        Pieza[][] expectedTablero = new Pieza[8][8];
        expectedTablero[0][0] = new Pieza(Tipo_Color.BLANCO);
        expectedTablero[1][1] = new Pieza(Tipo_Color.NEGRO);

        Pieza[][] actualTablero = ReflectionTestUtils.invokeMethod(partidaService, "convertirPosicionesATablero", posiciones);

        assertArrayEquals(expectedTablero, actualTablero);
    }

    @Test
    void convertirPosicionesATableroConPiezasCorrectas() {
        List<Posicion> posiciones = new ArrayList<>();
        posiciones.add(new Posicion(0, 0, Tipo_Color.BLANCO, Tipo_Pieza.TORRE));
        posiciones.add(new Posicion(0, 1, Tipo_Color.BLANCO, Tipo_Pieza.CABALLO));
        posiciones.add(new Posicion(0, 2, Tipo_Color.BLANCO, Tipo_Pieza.ALFIL));
        posiciones.add(new Posicion(0, 3, Tipo_Color.BLANCO, Tipo_Pieza.REINA));
        posiciones.add(new Posicion(0, 4, Tipo_Color.BLANCO, Tipo_Pieza.REY));
        posiciones.add(new Posicion(0, 5, Tipo_Color.BLANCO, Tipo_Pieza.ALFIL));
        posiciones.add(new Posicion(0, 6, Tipo_Color.BLANCO, Tipo_Pieza.CABALLO));
        posiciones.add(new Posicion(0, 7, Tipo_Color.BLANCO, Tipo_Pieza.TORRE));
        posiciones.add(new Posicion(1, 0, Tipo_Color.BLANCO, Tipo_Pieza.PEON));
        posiciones.add(new Posicion(1, 1, Tipo_Color.BLANCO, Tipo_Pieza.PEON));
        posiciones.add(new Posicion(1, 2, Tipo_Color.BLANCO, Tipo_Pieza.PEON));
        posiciones.add(new Posicion(1, 3, Tipo_Color.BLANCO, Tipo_Pieza.PEON));
        posiciones.add(new Posicion(1, 4, Tipo_Color.BLANCO, Tipo_Pieza.PEON));
        posiciones.add(new Posicion(1, 5, Tipo_Color.BLANCO, Tipo_Pieza.PEON));
        posiciones.add(new Posicion(1, 6, Tipo_Color.BLANCO, Tipo_Pieza.PEON));
        posiciones.add(new Posicion(1, 7, Tipo_Color.BLANCO, Tipo_Pieza.PEON));

        Pieza[][] expectedTablero = new Pieza[8][8];
        expectedTablero[0][0] = new Pieza(Tipo_Color.BLANCO);
        expectedTablero[0][0].setTipoPieza(Tipo_Pieza.TORRE);
        expectedTablero[0][1] = new Pieza(Tipo_Color.BLANCO);
        expectedTablero[0][1].setTipoPieza(Tipo_Pieza.CABALLO);
        expectedTablero[0][2] = new Pieza(Tipo_Color.BLANCO);
        expectedTablero[0][2].setTipoPieza(Tipo_Pieza.ALFIL);
        expectedTablero[0][3] = new Pieza(Tipo_Color.BLANCO);
        expectedTablero[0][3].setTipoPieza(Tipo_Pieza.REINA);
        expectedTablero[0][4] = new Pieza(Tipo_Color.BLANCO);
        expectedTablero[0][4].setTipoPieza(Tipo_Pieza.REY);
        expectedTablero[0][5] = new Pieza(Tipo_Color.BLANCO);
        expectedTablero[0][5].setTipoPieza(Tipo_Pieza.ALFIL);
        expectedTablero[0][6] = new Pieza(Tipo_Color.BLANCO);
        expectedTablero[0][6].setTipoPieza(Tipo_Pieza.CABALLO);
        expectedTablero[0][7] = new Pieza(Tipo_Color.BLANCO);
    }

    @Test
    void actualizarPartidaSinoEncuentraIdException() {
        Long partidaId = 1L;
        Partida partidaActualizada = new Partida();
        partidaActualizada.setId(partidaId);

        when(partidaRepository.findById(partidaId)).thenReturn(Optional.empty());

        assertThrows(PartidaNotFoundException.class, () -> partidaService.actualizarPartida(partidaId, partidaActualizada));

        verify(partidaRepository, times(1)).findById(partidaId);
        verify(partidaRepository, never()).save(any(Partida.class));
    }

    @Test
    public void testActualizarPartida() {
        // Arrange
        Long partidaId = 1L;
        Partida partidaExistente = new Partida();
        Partida partidaActualizada = new Partida();

        when(partidaRepository.findById(partidaId)).thenReturn(Optional.of(partidaExistente));
        when(partidaRepository.save(any(Partida.class))).thenReturn(partidaExistente);

        // Act
        Partida partidaActual = partidaService.actualizarPartida(partidaId, partidaActualizada);

        // Assert
        verify(partidaRepository, times(1)).findById(partidaId);
        verify(partidaRepository, times(1)).save(partidaExistente);
        assertEquals(partidaActualizada.getPosiciones(), partidaActual.getPosiciones());
    }

    @Test
    void cargarPartidaCuandoPartidaIdNoSeEncuentraException() {
        Long partidaId = 1L;
        when(partidaRepository.findById(partidaId)).thenReturn(Optional.empty());

        assertThrows(PartidaNotFoundException.class, () -> partidaService.cargarPartida(partidaId));

        verify(partidaRepository, times(1)).findById(partidaId);
        verifyNoMoreInteractions(partidaRepository, posicionRepository);
    }

    @Test
    void cargarPartidaCuandoPartidaConIdEncontrado() {
        Partida partida = new Partida();
        partida.setId(1L);

        List<Posicion> posiciones = new ArrayList<>();
        Posicion posicion1 = new Posicion(0, 0, Tipo_Color.BLANCO, Tipo_Pieza.TORRE);
        Posicion posicion2 = new Posicion(0, 1, Tipo_Color.BLANCO, Tipo_Pieza.CABALLO);
        posiciones.add(posicion1);
        posiciones.add(posicion2);

        partida.setPosiciones(posiciones);

    }

    @Test
    void guardarPartidaSinPosicionesCuandoPosicionesSonNulas() {
        Partida partida = new Partida();
        partida.setId(1L);
        partida.setPosiciones(null);

        when(partidaRepository.save(partida)).thenReturn(partida);

        Partida partidaGuardada = partidaService.guardarPartida(partida);

        assertNotNull(partidaGuardada);
        assertNull(partidaGuardada.getPosiciones());

        verify(partidaRepository, times(1)).save(partida);
        verifyNoMoreInteractions(partidaRepository);
    }

    @Test
    void guardarPartidaConPosicionesCuandoPosicionesNoSonNulas() {
        Partida partida = new Partida();
        List<Posicion> posiciones = new ArrayList<>();
        posiciones.add(new Posicion(1, 1, Tipo_Color.BLANCO, Tipo_Pieza.PEON));
        posiciones.add(new Posicion(2, 2, Tipo_Color.NEGRO, Tipo_Pieza.TORRE));
        partida.setPosiciones(posiciones);

        when(partidaRepository.save(partida)).thenReturn(partida);

        Partida partidaGuardada = partidaService.guardarPartida(partida);

        verify(partidaRepository, times(1)).save(partida);

        for (Posicion posicion : posiciones) {
            verify(posicionRepository, times(1)).save(posicion);
        }

        assertEquals(partida.getPosiciones(), partidaGuardada.getPosiciones());
    }


}
