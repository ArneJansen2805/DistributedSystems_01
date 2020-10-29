package rental_agency;

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

public class rentalAgency implements ICarRentalCompany{
	
	private Map<String, String> clientIdLookup = new HashMap<String, String>();
	
	private static CentralNamingService cns;
	private static List<ICarRentalCompany> companies;
	private static SessionManager sessionManager;

	public rentalAgency() throws NotBoundException {
	
		
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry();
			 cns = (CentralNamingService) registry.lookup("naming");
			 companies = cns.getCompanies();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		sessionManager = new SessionManager();
	}
	
	
	@Override
	public Collection<CarType> getAvailableCarTypes(Date from, Date end) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Quote createQuote(ReservationConstraints constraints, String client)
			throws ReservationException, RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reservation confirmQuote(Quote quote) throws ReservationException, RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public List<Reservation> getReservationsByRenter(String clientName) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumberOfReservationsForCarType(String carType) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}


	public String getCheapestCarType(Date start, Date end) throws RemoteException{
		return null;
	}

	
	@Override
	public String getName() {

		return "agency";
	}

	public SessionManager session_() {
		return sessionManager;
	}


	public Collection<? extends Reservation> confirmQuotes(String name, List<Quote> quotes) {
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
