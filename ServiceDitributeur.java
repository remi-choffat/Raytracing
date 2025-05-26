import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServiceDitributeur extends Remote{
	public ServiceNoeud donneMachine() throws RemoteException;
}
