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
import rental_agency.rentalAgency;

public class Client extends AbstractTestManagement<AgencyReservationSession, AgencyManagerSession> {

	/********
	 * MAIN *
	 ********/

	private final static int LOCAL = 0;
	private final static int REMOTE = 1;

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
			 agency = (rentalAgency) registry.lookup("agency");
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
		return session.getCheapestCarTypes(start, end);
	}

	@Override
	protected CarType getMostPopularCarTypeInCRC(AgencyManagerSession ms, String carRentalCompanyName, int year)
			throws Exception {
		return ms.getMostPopularCarType(carRentalCompanyName, year);
	}

	@Override
	protected AgencyReservationSession getNewReservationSession(String name) throws Exception {
		return new AgencyReservationSession(name, id);
	}

	@Override
	protected AgencyManagerSession getNewManagerSession(String name) throws Exception {
		return new AgencyManagerSession(name);
	}

	@Override
	protected void checkForAvailableCarTypes(AgencyReservationSession session, Date start, Date end) throws Exception {
		Collection<CarType> availableCarTypes = cns.getAvailableCarTypes(start, end);
		
		
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
	
	
	
	/***************
	 * OLD VERSION METHODS
	 ***************/

	/**
	 * Check which car types are available in the given period (across all companies
	 * and regions) and print this list of car types.
	 *
	 * @param start start time of the period
	 * @param end   end time of the period
	 * @throws Exception if things go wrong, throw exception
	 */
	protected void checkForAvailableCarTypes(Date start, Date end) throws Exception {
		Collection<CarType> cars = crc.getAvailableCarTypes(start, end);
		
		System.out.println("These cars are currently available: ");
		for(CarType c : cars) {
			System.out.println(c);
		}
	}

	/**
	 * Retrieve a quote for a given car type (tentative reservation).
	 * 
	 * @param clientName name of the client
	 * @param start      start time for the quote
	 * @param end        end time for the quote
	 * @param carType    type of car to be reserved
	 * @param region     region in which car must be available
	 * @return the newly created quote
	 * 
	 * @throws Exception if things go wrong, throw exception
	 */
	protected Quote createQuote(String clientName, Date start, Date end, String carType, String region)
			throws Exception {
		ReservationConstraints rsc = new ReservationConstraints(start, end, carType, region);
		Quote quote = crc.createQuote(rsc, clientName);
		System.out.println("---- TENTATIVE RESERVATION ----");
		System.out.println(quote);
		System.out.println("---- TENTATIVE RESERVATION ----");
		return quote;
	}

	/**
	 * Confirm the given quote to receive a final reservation of a car.
	 * 
	 * @param quote the quote to be confirmed
	 * @return the final reservation of a car
	 * 
	 * @throws Exception if things go wrong, throw exception
	 */
	protected Reservation confirmQuote(Quote quote) throws Exception {
		Reservation res =  crc.confirmQuote(quote);
		System.out.println("---- FINAL RESERVATION ----");
		System.out.println(res);
		System.out.println("---- FINAL RESERVATION ----");
		return res;
	}

	/**
	 * Get all reservations made by the given client.
	 *
	 * @param clientName name of the client
	 * @return the list of reservations of the given client
	 * 
	 * @throws Exception if things go wrong, throw exception
	 */
	protected List<Reservation> getReservationsByRenter(String clientName) throws Exception {
		List<Reservation> reservations = crc.getReservationsByRenter(clientName);
		
		System.out.println("---- RESERVATIONS FOR: " + clientName + " ----");
		for(Reservation r : reservations) {
			System.out.println(r);
			//System.out.println(r.getCarType());
			//System.out.println(r.getCarId());
			//System.out.println(r.getStartDate() + " until " + r.getEndDate());
			//System.out.println(r.getRentalPrice());
		}
		System.out.println("---- RESERVATIONS FOR: " + clientName + " ----");
		return reservations;
	}

	/**
	 * Get the number of reservations for a particular car type.
	 * 
	 * @param carType name of the car type
	 * @return number of reservations for the given car type
	 * 
	 * @throws Exception if things go wrong, throw exception
	 */
	protected int getNumberOfReservationsForCarType(String carType) throws Exception {
		int stats = crc.getNumberOfReservationsForCarType(carType);
		//System.out.println("Amount of reservations for " + carType + ": "  + stats);
		return stats;
	}

}