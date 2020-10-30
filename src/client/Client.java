package client;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import rental.CarType;
import rental.ICarRentalCompany;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;
import rental_agency.AgencyManagerSession;
import rental_agency.AgencyReservationSession;
import rental_agency.CentralNamingService;
import rental_agency.ICarRentalAgency;

public class Client extends AbstractTestManagement<AgencyReservationSession, AgencyManagerSession> {

	/********
	 * MAIN *
	 ********/

	private final static int LOCAL = 0;
	private final static int REMOTE = 1;
	private static ICarRentalAgency agency;
	/**
	 * The `main` method is used to launch the client application and run the test
	 * script.
	 */
	public static void main(String[] args) throws Exception {
		// The first argument passed to the `main` method (if present)
		// indicates whether the application is run on the remote setup or not.
		int localOrRemote = (args.length == 1 && args[0].equals("REMOTE")) ? REMOTE : LOCAL;
		System.setSecurityManager(null);
		//String[] carRentalCompanyNames = new String[] {"Hertz", "Dockx"};
		
		Client client = new Client("trips", localOrRemote);
		
		client.run();
	}


	/***************
	 * CONSTRUCTOR 
	 * @throws NotBoundException *
	 ***************/
	
	public Client(String scriptFile, int localOrRemote) throws NotBoundException {
		super(scriptFile);
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry();
			agency = (ICarRentalAgency) registry.lookup("agency");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	protected Set<String> getBestClients(AgencyManagerSession ms) throws Exception {
		return ms.getBestClients();
	}

	@Override
	protected String getCheapestCarType(AgencyReservationSession session, Date start, Date end, String region)
			throws Exception {
		return session.getCheapestCarType(start, end);
	}

	@Override
	protected CarType getMostPopularCarTypeInCRC(AgencyManagerSession ms, String carRentalCompanyName, int year)
			throws Exception {
		return ms.getMostPopularCarType(carRentalCompanyName, year);
	}

	@Override
	protected AgencyReservationSession getNewReservationSession(String name) throws Exception {
		return agency.session_().create(name, agency);
	}

	@Override
	protected AgencyManagerSession getNewManagerSession(String name) throws Exception {
		return agency.session_().createManager(name);
	}

	@Override
	protected void checkForAvailableCarTypes(AgencyReservationSession session, Date start, Date end) throws Exception {
		Collection<CarType> availableCarTypes = session.checkForAvailableCarTypes(start, end);
		
		
		System.out.println("Available Cars Types: ");
		for (CarType type: availableCarTypes) {			
			System.out.println(" - " + type.getName());
		}
	}

	@Override
	protected void addQuoteToSession(AgencyReservationSession session, String name, Date start, Date end,
			String carType, String region) throws Exception {
		session.addQuote(name, start, end, carType, region);
		
	}

	@Override
	protected List<Reservation> confirmQuotes(AgencyReservationSession session, String name) throws Exception {
		return session.confirmQuotes(name);
	}

	@Override
	protected int getNumberOfReservationsByRenter(AgencyManagerSession ms, String clientName) throws Exception {
		return ms.getNumberOfReservationsByRenter(clientName);
	}

	@Override
	protected int getNumberOfReservationsForCarType(AgencyManagerSession ms, String carRentalName, String carType)
			throws Exception {
		return ms.getNumberOfCompanyReservationsPerCarType(carRentalName, carType);
	}
}
	
	