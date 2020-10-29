package rental_agency;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

	public rentalAgency() throws NotBoundException {
	
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry();
			 cns = (CentralNamingService) registry.lookup("naming");
			 companies = cns.getCompanies();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
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



	@Override
	public String getName() {

		return "agency";
	}
	
	
}
