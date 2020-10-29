package rental;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;


public interface ICarRentalCompany extends Remote {
	
	public Collection<CarType> getAvailableCarTypes(Date from, Date end) throws RemoteException ;
	
	public Quote createQuote(ReservationConstraints constraints, String client) throws ReservationException, RemoteException ;
	
	public Reservation confirmQuote(Quote quote) throws ReservationException, RemoteException;
	
	public List<Reservation> getReservationsByRenter(String clientName)throws RemoteException;
	
	public int getNumberOfReservationsForCarType(String carType) throws RemoteException;

	public String getName();
}
