package com.carrental.service;

import com.carrental.dto.CarDTO;
import com.carrental.dto.CreateRentalRequest;
import com.carrental.dto.RentalDTO;
import com.carrental.dto.UserDTO;
import com.carrental.exception.BadRequestException;
import com.carrental.exception.ResourceNotFoundException;
import com.carrental.model.Car;
import com.carrental.model.Rental;
import com.carrental.model.RentalStatus;
import com.carrental.model.User;
import com.carrental.repository.CarRepository;
import com.carrental.repository.RentalRepository;
import com.carrental.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    @Transactional
    public RentalDTO createRental(Long clientId, CreateRentalRequest request) {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));

        Car car = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + request.getCarId()));

        if (!Boolean.TRUE.equals(car.getActive()) || !Boolean.TRUE.equals(car.getAvailable())) {
            throw new BadRequestException("Car is not available for rental");
        }

        if (request.getStartDate().isAfter(request.getEndDate()) || request.getStartDate().isEqual(request.getEndDate())) {
            throw new BadRequestException("End date must be after start date");
        }

        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        if (days <= 0) {
            throw new BadRequestException("Rental period must be at least 1 day");
        }

        List<RentalStatus> blockingStatuses = List.of(RentalStatus.ACTIVE);
        boolean hasOverlap = !rentalRepository
                .findByCarAndStatusInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        car,
                        blockingStatuses,
                        request.getEndDate(),
                        request.getStartDate()
                )
                .isEmpty();

        if (hasOverlap) {
            throw new BadRequestException("Car is already rented for the selected period");
        }

        BigDecimal totalPrice = car.getPricePerDay().multiply(BigDecimal.valueOf(days));

        Rental rental = Rental.builder()
                .client(client)
                .car(car)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .totalPrice(totalPrice)
                .status(RentalStatus.ACTIVE)
                .notes(request.getNotes())
                .build();

        car.setAvailable(false);
        carRepository.save(car);

        Rental savedRental = rentalRepository.save(rental);
        return toRentalDTO(savedRental);
    }

    @Transactional(readOnly = true)
    public List<RentalDTO> getClientRentals(Long clientId) {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));

        return rentalRepository.findByClientOrderByCreatedAtDesc(client)
                .stream()
                .map(this::toRentalDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RentalDTO> getAllRentals() {
        return rentalRepository.findAll()
                .stream()
                .map(this::toRentalDTO)
                .toList();
    }

    @Transactional
    public RentalDTO returnCar(Long rentalId, Long requesterId, boolean isAdmin) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found with id: " + rentalId));

        if (!isAdmin && !rental.getClient().getId().equals(requesterId)) {
            throw new BadRequestException("You are not allowed to return this rental");
        }

        if (rental.getStatus() != RentalStatus.ACTIVE) {
            throw new BadRequestException("Rental is not active");
        }

        rental.returnCar();
        Rental savedRental = rentalRepository.save(rental);

        Car car = savedRental.getCar();
        car.setAvailable(true);
        carRepository.save(car);

        return toRentalDTO(savedRental);
    }

    private RentalDTO toRentalDTO(Rental rental) {
        return RentalDTO.builder()
                .id(rental.getId())
                .startDate(rental.getStartDate())
                .endDate(rental.getEndDate())
                .returnDate(rental.getReturnDate())
                .totalPrice(rental.getTotalPrice())
                .status(rental.getStatus())
                .notes(rental.getNotes())
                .rentalDays(rental.getRentalDays())
                .isOverdue(rental.isOverdue())
                .client(toUserDTO(rental.getClient()))
                .car(toCarDTO(rental.getCar()))
                .createdAt(rental.getCreatedAt())
                .build();
    }

    private UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .build();
    }

    private CarDTO toCarDTO(Car car) {
        return CarDTO.builder()
                .id(car.getId())
                .brand(car.getBrand())
                .model(car.getModel())
                .year(car.getYear())
                .licensePlate(car.getLicensePlate())
                .pricePerDay(car.getPricePerDay())
                .description(car.getDescription())
                .available(car.getAvailable())
                .active(car.getActive())
                .averageRating(car.getAverageRating())
                .reviewCount(car.getReviews() != null ? car.getReviews().size() : 0)
                .createdAt(car.getCreatedAt())
                .build();
    }
}
