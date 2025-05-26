import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServiceNoeud  extends Remote{
	public Image calculer(Scene scene, int x, int y, int w, int h) throws RemoteException;
	public boolean estLibre() throws RemoteException;
}
