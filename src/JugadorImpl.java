import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


@SuppressWarnings("serial")
public class JugadorImpl extends UnicastRemoteObject implements IJugador {	
	
	public static final int LISTAR = 1;
	public static final int JUGAR = 2;
	public static final int ESPERAR = 3;
	public static final int SALIR = 0;
	
	private String nick;
	private IGestorUsuarios gestor;
	private boolean jugando;
	
	public JugadorImpl(String nick, IGestorUsuarios gestor) throws RemoteException {
		this.nick = nick;
		this.gestor = gestor;
		jugando = false;
		verMenu();
	}	
	
	public void imprimirMenu() {
		System.out.println("\nSelecciona la opci—n:");
		System.out.println("1. Listar jugadores.");
		System.out.println("2. Pedir juego.");
		System.out.println("3. Esperar juego.");
		System.out.println("0. Salir.");
	}	
	
	public void verMenu() {
		while (true) {
			int op = -1;
			while (op < 0 || op > 3) {
				imprimirMenu();
				op = Integer.parseInt(Reader.getInstance().read().trim());
			}
			switch (op) {
				case (LISTAR):
					verJugadores();
					break;
				case (JUGAR):
					pedirJuego();
					break;
				case(ESPERAR):
					try {
						gestor.registrar(this, nick);
						System.out.println("Esperando juego...");
						while(!jugando) {
							try {
								Thread.sleep(500);
							} catch(InterruptedException ie) {
								ie.printStackTrace();
							}
							System.out.flush();
						}	
						while (jugando) {
							try {
								Thread.sleep(500);
							} catch(InterruptedException ie) {
								ie.printStackTrace();
							}
							System.out.flush();
						}
					} catch(RemoteException re) {
						System.out.println("No se pudo registrar al servidor.");
					}
					break;
				case (SALIR):
					System.out.println("Finalizado.");
					System.exit(0);
			}
		}
	}
	
	public void verJugadores() {
		try {
			String[] jugadores = gestor.listar();
			System.out.println("\nJugadores:");
			for (String jug : jugadores) {
				System.out.println(jug);
			}
			System.out.println();
		} catch(Exception e) {
			System.out.println("No se pudo listar los jugadores.");
		}
	}
	
	public void pedirJuego() {
		System.out.println("\nIngresa el nombre del jugador con quien quieres jugar:");
		String jug = Reader.getInstance().read();
		System.out.println("\nEsperando respuesta...");
		try {			
			gestor.registrar(this, this.nick);
			gestor.pedirJuego(this.nick, jug);
		} catch (RemoteException re) {
			System.out.println("No se pudo conectar con el jugador.");
			System.out.println(re.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void imprimirTablero(int[] tablero) {		
		System.out.println();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				switch(tablero[i*3+j]) {
					case(TriquiImpl.X):
						System.out.print("X");
						break;
					case(TriquiImpl.O):
						System.out.print("O");
						break;
					case(TriquiImpl.N):
						System.out.print((i*3+j+1));
						break;
				}
				if (j < 2)
					System.out.print(" | ");
			}
			if (i < 2)
				System.out.println("\n----------");
		}
		System.out.println();
	}
	

	@Override
	public boolean aceptarJuego(String nick) throws RemoteException {
		System.out.println(nick + " desea jugar contigo. ÀAceptas el reto? (S/N)");
		String res = Reader.getInstance().read();
		return res.trim().toUpperCase().equals("S");
	}

	@Override
	public void iniciarJuego(String contrincante, ITriquiRMI triqui)
			throws RemoteException {
		System.out.println("\nHas comenzado el juego contra " + contrincante);
		jugando = true;
	}

	@Override
	public int jugarTurno() throws RemoteException {
		System.out.println("\nEs tu turno de jugar:");
		int jugada = Integer.parseInt(Reader.getInstance().read());	
		return jugada-1;
	}

	@Override
	public void finalizarJuego(boolean ganador) throws RemoteException {
		if (ganador) {
			System.out.println("\nÁHas ganado el juego!");
		} else {
			System.out.println("\nHas perdido el juego.");
		}
		System.out.println("Juego terminado.");
		gestor.salir(this.nick);
		jugando = false;
	}

	@Override
	public String getNick() throws RemoteException {
		return nick;
	}
	
	@Override
	public void actualizarTablero(int[] tablero) {
		imprimirTablero(tablero);
	}

}
