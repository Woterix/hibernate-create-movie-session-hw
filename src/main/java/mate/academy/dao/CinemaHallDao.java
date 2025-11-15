package mate.academy.dao;

import java.util.List;
import java.util.Optional;
import mate.academy.model.CinemaHall;

public interface CinemaHallDao {
    Optional<CinemaHall> get(Long id);

    CinemaHall add(CinemaHall cinemaHall);

    List<CinemaHall> getAll();
}
