package rental;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;


public interface ICarRentalCompany extends Serializable {
	
	public Collection<CarType> getAvailableCarTypes(Date from, Date end) throws RemoteException ;
	
	public Quote createQuote(ReservationConstraints constraints, String client) throws ReservationException, RemoteException ;
	
	public Reservation confirmQuote(Quote quote) throws ReservationException, RemoteException;
	
	public List<Reservation> getReservationsByRenter(String clientName)throws RemoteException;
	
	public int getNumberOfReservationsForCarType(String carType) throws RemoteException;
	
	public boolean isCarAvailable(ReservationConstraints constraints) throws RemoteException;

	public String getName();
	
	public Map<String, Integer> getBestCustomers();
	
	public CarType getMostPopularCarTypePerYear(int year) ;
	
	public Collection<CarType> getAllCarTypes();
}
