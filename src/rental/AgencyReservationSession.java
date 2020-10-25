package rental;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class AgencyReservationSession {
	
	private String userName;
	private Collection<Quote> quotes; 
	private CarRentalCompany rentalCompany;
	
	
	public String getCompanyName() {	
		return rentalCompany.getName();
	}
	
	public double getCompanyRentalPricePerDay(String carType) {
		return rentalCompany.getCarType(carType).getRentalPricePerDay();
	}
	
	public AgencyReservationSession(String userName, ICarRentalCompany rentalCompany) {
		this.userName = userName;
		this.quotes = new ArrayList<Quote>();
		this.rentalCompany = (CarRentalCompany)rentalCompany;
	}
	
	public void addQuote(String name, Date start, Date end,
			String carType, String region) {
		String companyName = getCompanyName();
		double price = getCompanyRentalPricePerDay(carType);
		Quote quote = new Quote(name, start, end, companyName, carType, price);
		this.quotes.add(quote);
	}
	
	public Collection<Quote> getCurrentQuotes() {
		return quotes;
	} 
	
	public List<Reservation> confirmQuotes(String name) {
		// TODO: check roll back
		List<Reservation> reservations = new ArrayList<Reservation>();
		if (name.equals(userName)) {			
			Quote currentQuote = null;
			
			try {			
				for (Quote quote : quotes) {	
					currentQuote = quote;
					reservations.add(rentalCompany.confirmQuote(quote));
				}
			}  catch (Exception e) {
				System.err.println("Error confirming: " + currentQuote.toString());
				System.err.println("Exception: " + e.getMessage());
			}
		}
 		return reservations;
	}
	
	public Collection<CarType> getAvailableCarTypes(Date start, Date end) throws RemoteException {
		return rentalCompany.getAvailableCarTypes(start, end);
	}
	
	public String getCheapestCarTypes(Date start, Date end) throws RemoteException {
		int minPrice = Integer.MAX_VALUE;
		CarType cheapestType = null;
		Collection<CarType> availableTypes = getAvailableCarTypes(start, end);
		for (CarType type : availableTypes) {
			if (type.getRentalPricePerDay() < minPrice) {
				cheapestType = type;
			}
		}
		return cheapestType.getName();
	}

}