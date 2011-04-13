import java.rmi.*;
import java.rmi.server.*;
import java.util.Enumeration;
import java.util.Hashtable;

@SuppressWarnings("serial")
public class GestorUsuariosImpl extends UnicastRemoteObject implements IGestorUsuarios{

	private Hashtable<String,IJugador> jugadores;
	
	public GestorUsuariosImpl() throws RemoteException{
		jugadores = new Hashtable<String,IJugador>();
	}
	
	@Override
	public String[] listar() throws RemoteException {
		String[] contenido = new String[jugadores.size()];
		Enumeration<String> valores = jugadores.keys();
		for (int i = 0; valores.hasMoreElements(); i++) {
			contenido[i] = valores.nextElement();
		}
		return contenido;
	}

	@Override
	public void pedirJuego(String pedidor, String pedido) throws RemoteException, Exception {
		System.out.println(pedidor + " quiere jugar con " + pedido);
		IJugador j = jugadores.get(pedido);
		if (j == null)
			throw new Exception("El jugador no existe.");
		ITriquiRMI triqui = null;
		if(j.aceptarJuego(pedidor)){
			System.out.println("Juego aceptado.");
			triqui = new TriquiImpl(jugadores.get(pedidor), jugadores.get(pedido));
			salir(pedido); salir(pedidor);
			triqui.comenzar();
		} else {
			throw new Exception("La solicitud fue rechazada.");
		}
	}

	@Override
	public void registrar(IJugador j, String nick) throws RemoteException {
		jugadores.put(nick, j);	
		System.out.println("Se ha registrado " + nick + ".");
	}

	@Override
	public void salir(String nick) throws RemoteException {
		jugadores.remove(nick);
		System.out.println("Se ha salido " + nick + ".");
		
	}

}
