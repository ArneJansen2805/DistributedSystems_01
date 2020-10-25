package rental;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CarRentalCompany implements ICarRentalCompany {

	private static Logger logger = Logger.getLogger(CarRentalCompany.class.getName());

	private List<String> regions;
	private String name;
	private List<Car> cars;
	private Map<String, CarType> carTypes = new HashMap<String, CarType>();

	/***************
	 * CONSTRUCTOR *
	 ***************/

	public CarRentalCompany(String name, List<String> regions, List<Car> cars) {
		logger.log(Level.INFO, "<{0}> Car Rental Company {0} starting up...", name);
		setName(name);
		this.cars = cars;
		setRegions(regions);
		for (Car car : cars)
			carTypes.put(car.getType().getName(), car.getType());
		logger.log(Level.INFO, this.toString());
	}

	/********
	 * NAME *
	 ********/

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	/***********
	 * Regions *
	 **********/
	private void setRegions(List<String> regions) {
		this.regions = regions;
	}

	public List<String> getRegions() {
		return this.regions;
	}

	public boolean operatesInRegion(String region) {
		return this.regions.contains(region);
	}

	/*************
	 * CAR TYPES *
	 *************/

	public Collection<CarType> getAllCarTypes() {
		return carTypes.values();
	}

	public CarType getCarType(String carTypeName) {
		if (carTypes.containsKey(carTypeName))
			return carTypes.get(carTypeName);
		throw new IllegalArgumentException("<" + carTypeName + "> No car type of name " + carTypeName);
	}

	// mark
	public boolean isAvailable(String carTypeName, Date start, Date end) {
		logger.log(Level.INFO, "<{0}> Checking availability for car type {1}", new Object[] { name, carTypeName });
		if (carTypes.containsKey(carTypeName)) {
			return getAvailableCarTypes(start, end).contains(carTypes.get(carTypeName));
		} else {
			throw new IllegalArgumentException("<" + carTypeName + "> No car type of name " + carTypeName);
		}
	}

	public Set<CarType> getAvailableCarTypes(Date start, Date end) {
		Set<CarType> availableCarTypes = new HashSet<CarType>();
		for (Car car : cars) {
			if (car.isAvailable(start, end)) {
				availableCarTypes.add(car.getType());
			}
		}
		return availableCarTypes;
	}

	/*********
	 * CARS *
	 *********/

	private Car getCar(int uid) {
		for (Car car : cars) {
			if (car.getId() == uid)
				return car;
		}
		throw new IllegalArgumentException("<" + name + "> No car with uid " + uid);
	}

	private List<Car> getAvailableCars(String carType, Date start, Date end) {
		List<Car> availableCars = new LinkedList<Car>();
		for (Car car : cars) {
			if (car.getType().getName().equals(carType) && car.isAvailable(start, end)) {
				availableCars.add(car);
			}
		}
		return availableCars;
	}

	/****************
	 * RESERVATIONS *
	 ****************/

	public Quote createQuote(ReservationConstraints constraints, String client) throws ReservationException {
		logger.log(Level.INFO, "<{0}> Creating tentative reservation for {1} with constraints {2}",
				new Object[] { name, client, constraints.toString() });

		if (!operatesInRegion(constraints.getRegion())
				|| !isAvailable(constraints.getCarType(), constraints.getStartDate(), constraints.getEndDate()))
			throw new ReservationException("<" + name + "> No cars available to satisfy the given constraints.");

		CarType type = getCarType(constraints.getCarType());

		double price = calculateRentalPrice(type.getRentalPricePerDay(), constraints.getStartDate(),
				constraints.getEndDate());

		return new Quote(client, constraints.getStartDate(), constraints.getEndDate(), getName(),
				constraints.getCarType(), price);
	}

	// Implementation can be subject to different pricing strategies
	private double calculateRentalPrice(double rentalPricePerDay, Date start, Date end) {
		return rentalPricePerDay * Math.ceil((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24D));
	}

	public Reservation confirmQuote(Quote quote) throws ReservationException {
		logger.log(Level.INFO, "<{0}> Reservation of {1}", new Object[] { name, quote.toString() });
		List<Car> availableCars = getAvailableCars(quote.getCarType(), quote.getStartDate(), quote.getEndDate());
		if (availableCars.isEmpty())
			throw new ReservationException("Reservation failed, all cars of type " + quote.getCarType()
					+ " are unavailable from " + quote.getStartDate() + " to " + quote.getEndDate());
		Car car = availableCars.get((int) (Math.random() * availableCars.size()));

		Reservation res = new Reservation(quote, car.getId());
		car.addReservation(res);
		return res;
	}

	public void cancelReservation(Reservation res) {
		logger.log(Level.INFO, "<{0}> Cancelling reservation {1}", new Object[] { name, res.toString() });
		getCar(res.getCarId()).removeReservation(res);
	}

	@Override
	public String toString() {
		return String.format("<%s> CRC is active in regions %s and serving with %d car types", name,
				listToString(regions), carTypes.size());
	}

	private static String listToString(List<? extends Object> input) {
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < input.size(); i++) {
			if (i == input.size() - 1) {
				out.append(input.get(i).toString());
			} else {
				out.append(input.get(i).toString() + ", ");
			}
		}
		return out.toString();
	}

	@Override
	public List<Reservation> getReservationsByRenter(String clientName) throws RemoteException {
		List<Reservation> res = new ArrayList<>();
		for (Car c : cars) {
			for (Reservation r : c.getReservations()) {
				if (r.getCarRenter().equals(clientName)) {
					res.add(r);
				}
			}
		}
		return res;
	}

	@Override
	public int getNumberOfReservationsForCarType(String carType) throws RemoteException {
		int count = 0;
		for (Car c : cars) {
			if (c.getType().getName().equals(carType)) {
				count += c.getReservations().size();
			}
		}
		return count;
	}

	// Get customers with highest number of reservations
	public List<String> getBestCustomers() {
		List<String> bestRenters = new ArrayList<String>();
		Map<String, Integer> carRentalsMap = getCarRentalsReservationNumbersMap();
		int max = getMaxReservationsRented(carRentalsMap);

		for (String renter : carRentalsMap.keySet()) {
			if (carRentalsMap.get(renter) == max) {
				bestRenters.add(renter);
			}
		}
		
		return bestRenters;
	}
	
	public int getNumberOfReservationsForRenter(String renter) 
			throws IllegalArgumentException {
		Map<String, Integer> carRentalsMap = getCarRentalsReservationNumbersMap();
		
		if (!carRentalsMap.containsKey(renter)) {
			throw new IllegalArgumentException("No Renter with name: " + renter + " found.");
		}
		
		return carRentalsMap.get(renter);
	}
	
	public CarType getMostPopularCarTypePerYear(int year) 
		throws IllegalArgumentException {
		// 1885 is the year cars were invented
		if (year < 1885) {
			throw new IllegalArgumentException("No reservation information available before 1885.");
		}
		
		CarType mostPopularType = null; 
		
		// Used calendar to avoid using deprecated Date constructors
		Calendar calendar = new GregorianCalendar();

		calendar.set(year, 1, 1);
		Date start = Date.from(calendar.toInstant());
		
		calendar.set(year, 12, 31);
		Date end = Date.from(calendar.toInstant());
		
		Map<CarType, Integer> carTypesRented = new HashMap<CarType, Integer>();
		int maxRented = Integer.MIN_VALUE;
		
		for (Car car : cars) {
			if (car.isAvailable(start, end)) {
				CarType currentType = car.getType();
				if (!carTypesRented.containsKey(currentType)) {
					carTypesRented.put(currentType, 0);
				}
				
				int currentNumber = carTypesRented.get(currentType);
				int newTotal = currentNumber + car.getReservations().size();
				carTypesRented.replace(currentType, newTotal);
				
				if (newTotal > maxRented) {
					mostPopularType = currentType;
					maxRented = newTotal;
				}
			}
		}
		
		return mostPopularType;
	}

	private Map<String, Integer> getCarRentalsReservationNumbersMap() {
		Map<String, Integer> carRentalsMap = new HashMap<String, Integer>();

		for (Car c : cars) {
			for (Reservation reservation : c.getReservations()) {
				String renter = reservation.getCarRenter();
				if (!carRentalsMap.containsKey(renter)) {
					carRentalsMap.put(renter, 0);
				}
				int currentValue = carRentalsMap.get(renter);
				currentValue++;
				carRentalsMap.replace(renter, currentValue);
			}
		}
		return carRentalsMap;
	}

	private int getMaxReservationsRented(Map<String, Integer> carRentalsMap) {
		int max = Integer.MIN_VALUE;

		for (String renter : carRentalsMap.keySet()) {
			int currentRenterReservations = carRentalsMap.get(renter);
			max = currentRenterReservations > max ? currentRenterReservations : max;
		}
		return max;
	}

//	private int getMaxReservationsRented() {
//		Map<String, Integer> carRentalsMap = getCarRentalsReservationNumbersMap();
//		return getMaxReservationsRented(carRentalsMap);
//	}
}