package client;

import java.util.List;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import rental.CarType;
import rental.ICarRentalCompany;
import rental.Quote;

public class AgencyReservationSession {
	public Collection<Quote> Quotes; 
	public ICarRentalCompany RentalCompany;
	
	public AgencyReservationSession(ICarRentalCompany rentalCompany) {
		this.Quotes = new ArrayList<Quote>();
		this.RentalCompany = rentalCompany;
	}
	
	public void createQuote(Quote quote) {
		this.Quotes.add(quote);
	}
	
	public List<Quote> getCurrentQuotes() {
		return Quotes;
	} 
	
	public void confirmQuotes() {
		// TODO: check roll back
		Quote currentQuote = null;
		try {			
			for (Quote quote : Quotes) {	
				currentQuote = quote;
				RentalCompany.confirmQuote(quote);
			}
		}  catch (Exception e) {
			System.err.println("Error confirming: " + currentQuote.toString());
			System.err.println("Exception: " + e.getMessage());
		}
	}
	
	public Collection<CarType> getAvailableCarTypes(Date from, Date end) throws RemoteException {
		return RentalCompany.getAvailableCarTypes(from, end);
	}
	
	public CarType getCheapestCarTypes(Date from, Date end) throws RemoteException {
		int minPrice = Integer.MAX_VALUE;
		CarType cheapestType = null;
		Collection<CarType> availableTypes = getAvailableCarTypes(from, end);
		for (CarType type : availableTypes) {
			if (type.getRentalPricePerDay() < minPrice) {
				cheapestType = type;
			}
		}
		return cheapestType;
	}

}
