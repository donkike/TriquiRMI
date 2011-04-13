import java.rmi.*;
import java.rmi.server.*;

@SuppressWarnings("serial")
public class TriquiImpl extends UnicastRemoteObject implements ITriquiRMI{
	
	public static final int N = 0;
	public static final int X = 1;
	public static final int O = 2;
	
	private IJugador[] jugadores;
	private int turno;
	private int jugadas;
	private int[] tablero;
	
	public TriquiImpl(IJugador j1, IJugador j2) throws RemoteException {
		jugadores = new IJugador[2];
		jugadores[0] = j1;
		jugadores[1] = j2;
		turno = 0;
		jugadas = 0;
		tablero = new int[9];
	}
	
	private boolean hayGanador() {
		return ((tablero[0] == tablero[1] && tablero[1] == tablero[2] && tablero[0] != N) ||
				(tablero[3] == tablero[4] && tablero[4] == tablero[5] && tablero[3] != N) ||
				(tablero[6] == tablero[7] && tablero[7] == tablero[7] && tablero[6] != N) ||
				(tablero[0] == tablero[3] && tablero[3] == tablero[6] && tablero[0] != N) ||
				(tablero[1] == tablero[4] && tablero[4] == tablero[7] && tablero[1] != N) ||
				(tablero[2] == tablero[5] && tablero[5] == tablero[8] && tablero[2] != N) ||
				(tablero[0] == tablero[4] && tablero[4] == tablero[8] && tablero[0] != N) ||
				(tablero[2] == tablero[4] && tablero[4] == tablero[6] && tablero[2] != N));	
	}
	
	private void actualizarTableros() {
		try {
			for (IJugador jugador : jugadores) {				
				jugador.actualizarTablero(tablero);
			}
		} catch(RemoteException re) {
			System.out.println("No se pudo actualizar los tableros.");
		}
	}
	
	private void jugar() {
		boolean ganador = false;
		try {
			while(jugadas < 9 && !ganador) {
				actualizarTableros();
				int jugada = -1;
				while ((jugada < 0 || jugada > 8) || tablero[jugada] != N) {
					jugada = jugadores[turno].jugarTurno();
				}
				tablero[jugada] = ++turno;
				turno %= 2; 
				jugadas++;
				ganador = hayGanador();
			}
			actualizarTableros();
			if (ganador) {
				jugadores[turno].finalizarJuego(false);
				jugadores[(turno+1) % 2].finalizarJuego(true);
			} else {
				jugadores[turno].finalizarJuego(false);
				jugadores[(turno+1) % 2].finalizarJuego(false);
			}
		} catch(RemoteException re) {
			System.out.println("Error durante el juego.");
		}
	}
	
	@Override
	public void comenzar() throws RemoteException{
		jugadores[0].iniciarJuego(jugadores[1].getNick(), this);
		jugadores[1].iniciarJuego(jugadores[0].getNick(), this);
		jugar();
	}	

}
