package com.fbs.app.repository;

import com.fbs.app.model.EventModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<EventModel, Long> {

    EventModel findByIdAndEventName(Long id, String eventName);
}
