package rental_agency;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import rental.CarType;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;

public interface ICarRentalAgency extends Remote {
	
	public Collection<CarType> getAvailableCarTypes(Date from, Date end) throws RemoteException ;
	
	public Quote createQuote(ReservationConstraints constraints, String client) throws ReservationException, RemoteException ;
	
	public Reservation confirmQuote(Quote quote) throws ReservationException, RemoteException;
	
	public Collection<Reservation> confirmQuotes(Collection<Quote> quote) throws ReservationException, RemoteException;
	
	public List<Reservation> getReservationsByRenter(String clientName)throws RemoteException;
	
	public int getNumberOfReservationsForCarType(String carType) throws RemoteException;
	
	public String getCheapestCarType(Date start, Date end) throws RemoteException;

	public SessionManager session_() throws RemoteException;

}
