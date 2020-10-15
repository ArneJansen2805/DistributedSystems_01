package client;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.List;

import rental.ICarRentalCompany;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;

public class Client extends AbstractTestBooking {

	/********
	 * MAIN *
	 ********/

	private final static int LOCAL = 0;
	private final static int REMOTE = 1;
	private ICarRentalCompany crc;

	/**
	 * The `main` method is used to launch the client application and run the test
	 * script.
	 */
	public static void main(String[] args) throws Exception {
		// The first argument passed to the `main` method (if present)
		// indicates whether the application is run on the remote setup or not.
		int localOrRemote = (args.length == 1 && args[0].equals("REMOTE")) ? REMOTE : LOCAL;
		System.setSecurityManager(null);
		String carRentalCompanyName = "Hertz";

		// An example reservation scenario on car rental company 'Hertz' would be...
		Client client = new Client("simpleTrips", carRentalCompanyName, localOrRemote);
		client.run();
	}

	/***************
	 * CONSTRUCTOR 
	 * @throws NotBoundException *
	 ***************/

	public Client(String scriptFile, String carRentalCompanyName, int localOrRemote) throws NotBoundException {
		super(scriptFile);
		Registry registry;
		
		try {
			registry = LocateRegistry.getRegistry();
			 crc = (ICarRentalCompany) registry.lookup("cars");
		} catch (RemoteException e) {
			
			e.printStackTrace();
		}
	
		
	}

	/**
	 * Check which car types are available in the given period (across all companies
	 * and regions) and print this list of car types.
	 *
	 * @param start start time of the period
	 * @param end   end time of the period
	 * @throws Exception if things go wrong, throw exception
	 */
	@Override
	protected void checkForAvailableCarTypes(Date start, Date end) throws Exception {
		crc.getAvailableCarTypes(start, end);
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
	@Override
	protected Quote createQuote(String clientName, Date start, Date end, String carType, String region)
			throws Exception {
		ReservationConstraints rsc = new ReservationConstraints(start, end, carType, region);
		Quote quote = crc.createQuote(rsc, clientName);
		System.out.println(quote);
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
	@Override
	protected Reservation confirmQuote(Quote quote) throws Exception {
		Reservation res =  crc.confirmQuote(quote);
		System.out.println(res);
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
	@Override
	protected List<Reservation> getReservationsByRenter(String clientName) throws Exception {
		List<Reservation> reservations = crc.getReservationsByRenter(clientName);
		
		for(Reservation r : reservations) {
			System.out.println(r.getCarType());
			System.out.println(r.getCarId());
			System.out.println(r.getStartDate() + " until " + r.getEndDate());
			System.out.println(r.getRentalPrice());
		}
		
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
	@Override
	protected int getNumberOfReservationsForCarType(String carType) throws Exception {
		int stats = crc.getNumberOfReservationsForCarType(carType);
		System.out.println("Amount of reservations for: " + carType + " equals "  + stats);
		return stats;
	}
}