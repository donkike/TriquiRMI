import java.rmi.*;

public interface IGestorUsuarios extends Remote {

	public void registrar(IJugador j, String nick) throws RemoteException;
	public void salir(String nick) throws RemoteException;
	public String[] listar() throws RemoteException;
	public void pedirJuego(String pedidor, String pedido) throws RemoteException, Exception;
	
}
