package rental_agency;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rental.CarRentalCompany;
import rental.CarType;
import rental.ICarRentalCompany;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;

public class RentalAgency implements ICarRentalAgency {

	private static ICentralNamingService cns;
	private static List<ICarRentalCompany> companies;
	private static SessionManager sessionManager;

	public RentalAgency() throws NotBoundException {
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry();
			cns =  (ICentralNamingService) registry.lookup("naming");
			companies = cns.getCompanies();
			sessionManager = new SessionManager();
		} catch (RemoteException e) {
			e.printStackTrace();
		
		
	}
	}
	
	@Override
	public Collection<CarType> getAvailableCarTypes(Date from, Date end) throws RemoteException {
		List<CarType> carTypes = new ArrayList<CarType>();
		for (ICarRentalCompany company : companies) {
			carTypes.addAll(company.getAvailableCarTypes(from, end));
		}
		return carTypes;
	}

	@Override
	public Quote createQuote(ReservationConstraints constraints, String client)
			throws ReservationException, RemoteException {		
	
		
		for (ICarRentalCompany company : companies) {
			
			if (company.isCarAvailable(constraints)) {
				return company.createQuote(constraints, client);
			}
		}
		
		throw new ReservationException("length of companies is:" + companies.size());
/**
		throw new ReservationException("Could not find a cars of type " + constraints.getCarType() + " available from "
				+ constraints.getStartDate() + " to " + constraints.getEndDate() + " in the region "
				+ constraints.getRegion() + ".");
**/
	}
	

	@Override
	public Reservation confirmQuote(Quote quote)
			throws ReservationException, RemoteException, IllegalArgumentException {

		for (ICarRentalCompany company : companies) {
			if (company.getName().equals(quote.getRentalCompany()))
				return company.confirmQuote(quote);
		}

		throw new ReservationException("Company with name: " + quote.getRentalCompany() + "is not registered and the reservation cannot be created.");

	}
	
	
	@Override
	public Collection<Reservation> confirmQuotes(Collection<Quote> quotes) throws ReservationException, RemoteException {
		Collection<Reservation> reservations = new ArrayList<Reservation>();
		for (Quote quote : quotes) {
			confirmQuote(quote);
		}
		return reservations;
	}
	
	
	@Override
	public List<Reservation> getReservationsByRenter(String clientName) throws RemoteException {
		List<Reservation> reservations = new ArrayList<Reservation>();
		for (ICarRentalCompany company : companies) {
			reservations.addAll(company.getReservationsByRenter(clientName));
		}
		return reservations;
	}

	@Override
	public int getNumberOfReservationsForCarType(String carType) throws RemoteException {
		int nbOfReservations = 0;
		for (ICarRentalCompany company : companies) {
			nbOfReservations += company.getNumberOfReservationsForCarType(carType);
		}
		return nbOfReservations;
	}

	@Override
	public String getCheapestCarType(Date start, Date end) throws RemoteException {
		double min = Integer.MAX_VALUE;
		String cheapestType = "";
		
		for (ICarRentalCompany company : companies) {
			Collection<CarType> availableTypes  = company.getAvailableCarTypes(start, end);
			for (CarType type : availableTypes) {
				double currentPrice = type.getRentalPricePerDay();
				if(currentPrice < min) {
					min = currentPrice;
					cheapestType = type.getName();
				}
			}
		}
		
		return cheapestType;
	}

	public SessionManager session_() {
		return sessionManager;
	}

	public Collection<? extends Reservation> confirmQuotes(String name, List<Quote> quotes) throws RemoteException {
		ArrayList<Reservation> confirmed = new ArrayList<>();
		try {
			for (Quote q : quotes) {
				confirmed.add(confirmQuote(q));
			}
		}
		catch (Exception e ) {
			for (Reservation r : confirmed) {
				CarRentalCompany c = (CarRentalCompany) cns.getCompany(r.getRentalCompany());
				c.cancelReservation(r);
			}
		}
		
		return confirmed;
	}

}
