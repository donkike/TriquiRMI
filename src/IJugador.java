import java.rmi.*;

public interface IJugador extends Remote {

	public boolean aceptarJuego(String nick) throws RemoteException;
	public void iniciarJuego(String contrincante, ITriquiRMI triqui) throws RemoteException;
	public int jugarTurno() throws RemoteException;
	public void finalizarJuego(boolean ganador) throws RemoteException;
	public String getNick() throws RemoteException;
	public void actualizarTablero(int[] tablero) throws RemoteException;
	
}
