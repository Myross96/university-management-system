package com.foxminded.university.dao;

import java.time.LocalDate;
import java.util.Optional;

import com.foxminded.university.model.Holiday;

public interface HolidayDao extends GenericDao<Holiday> {

   Optional<Holiday> getByDate(LocalDate date);
}
