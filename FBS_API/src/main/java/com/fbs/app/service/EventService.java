package com.fbs.app.service;

import com.fbs.app.dto.EventRequestDto;
import com.fbs.app.dto.TransactionRequestDto;
import com.fbs.app.model.EventModel;
import com.fbs.app.model.TransactionModel;
import com.fbs.app.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public String Event(EventRequestDto eventRequestDto){
        String success="SUCCESS";

        EventModel eventModel = new EventModel();
        if(Objects.nonNull(eventModel.getId()) && Objects.nonNull(eventModel.getEventName())){
            eventModel = eventRepository.findByIdAndEventName(eventRequestDto.getId(),eventRequestDto.getEventName());
            if (Objects.isNull(eventModel)) {
                throw new RuntimeException ("Invalid Signature Id!");
            }

        }
        else {
            eventModel = new EventModel();
        }
        eventModel.setEventName(eventRequestDto.getEventName());
        eventModel.setStatus(eventRequestDto.getStatus());
        eventModel.setEventDescription(eventRequestDto.getEventDescription());

        eventRepository.save(eventModel);


        return success;
    }

    public List<EventModel> readEvent(){
        return eventRepository.findAll();
    }

    public void delEvent(Long id){
        if(!eventRepository.existsById(id)) throw new RuntimeException("Transaction not found with this ID:");

        eventRepository.deleteById(id);
    }
}
