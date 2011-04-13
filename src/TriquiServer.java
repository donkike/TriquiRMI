import java.rmi.Naming;

public class TriquiServer {

	public static void main(String[] args) {
		IGestorUsuarios gestor = null;
		try {
			gestor = new GestorUsuariosImpl();
			Naming.bind("triqui", gestor);
			System.out.println("Servidor listo.");
		} catch(Exception e) {
			System.out.println("No se pudo iniciar el servidor.");
		}
	}
	
}
