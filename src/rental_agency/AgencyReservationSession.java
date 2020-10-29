package rental_agency;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import rental.CarType;
import rental.ICarRentalCompany;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;

public class AgencyReservationSession {
	
	private String userName;
	private ArrayList<Quote> quotes; 
	private ArrayList<Reservation> reservations;
	private rentalAgency agency;
	
	public AgencyReservationSession(String userName, rentalAgency agency) {
		this.agency = agency;
		this.userName = userName;
		this.quotes = new ArrayList<Quote>();
		this.reservations = new ArrayList<Reservation>();
		
	}
	
	public void addQuote(String name, Date start, Date end,
		String carType, String region) throws RemoteException, ReservationException {
		ReservationConstraints con = new ReservationConstraints(start, end, carType, region);
		quotes.add(agency.createQuote(con, name));
	}
	
	public Collection<Quote> getCurrentQuotes() {
		return quotes;
	} 
	
	public List<Reservation> confirmQuotes(String name) {
		
		reservations.addAll(agency.confirmQuotes(name, quotes));
		return reservations;
		
	}	
	
	public Collection<CarType> checkForAvailableCarTypes(Date start, Date end) throws RemoteException{
		return agency.getAvailableCarTypes(start, end);
	}	

	public String getCheapestCarType(Date start, Date end) throws RemoteException {
		return agency.getCheapestCarType(start, end);
	}
	
	public void clear() {
		quotes = null;
		reservations = null; 
		userName = null; 
		agency = null;
		
	}

}