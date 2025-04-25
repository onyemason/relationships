package com.relationship.relationshipMapping.service;

import com.relationship.relationshipMapping.dto.requestDto.ZipCodeRequestDto;
import com.relationship.relationshipMapping.model.City;
import com.relationship.relationshipMapping.model.Zipcode;
import com.relationship.relationshipMapping.repository.ZipcodeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ZipcodeServiceImpl implements ZipcodeService {
    private final ZipcodeRepository zipcodeRepository;
    private final CityService cityService;

    public ZipcodeServiceImpl(ZipcodeRepository zipcodeRepository, CityService cityService) {
        this.zipcodeRepository = zipcodeRepository;
        this.cityService = cityService;
    }


    @Override
    public Zipcode addZipcode(ZipCodeRequestDto zipCodeRequestDto) {
       Zipcode zipcode = new Zipcode();
       zipcode.setName(zipCodeRequestDto.getName());
       if (zipCodeRequestDto.getCityId() == null){
           return zipcodeRepository.save(zipcode);
       }
       City city = cityService.getCity(zipCodeRequestDto.getCityId());
       zipcode.setCity(city);
       return zipcodeRepository.save(zipcode);
    }

    @Override
    public List<Zipcode> getZipcodes() {
        return StreamSupport.stream(zipcodeRepository.findAll().spliterator(),false)
                .collect(Collectors.toList());
    }

    @Override
    public Zipcode getZipcode(Long zipcodeId) {
       return zipcodeRepository.findById(zipcodeId).orElseThrow(()->new IllegalArgumentException("zipcode with id:" + zipcodeId + "Could not be found"));
    }

    @Override
    public Zipcode deleteZipcode(Long zipcodeId) {
       Zipcode zipcode = getZipcode(zipcodeId);
       zipcodeRepository.delete(zipcode);
       return zipcode;
    }
    @Transactional
    @Override
    public Zipcode editZipcode(Long zipcodeId, ZipCodeRequestDto zipCodeRequestDto) {
        Zipcode zipcodeToEdit = getZipcode(zipcodeId);
        zipcodeToEdit.setName(zipCodeRequestDto.getName());
        if(zipCodeRequestDto.getCityId() != null){
            return zipcodeToEdit;
        }
        City city = cityService.getCity(zipCodeRequestDto.getCityId());
        zipcodeToEdit.setCity(city);
        return zipcodeToEdit;
    }
    @Transactional
    @Override
    public Zipcode addCityToZipcode(Long zipcodeId, Long cityId) {
       Zipcode zipcode = getZipcode(zipcodeId);
       City city = cityService.getCity(cityId);
       if (Objects.nonNull(zipcode.getCity())){
           throw new IllegalArgumentException("zipcode already has a city");
       }
       zipcode.setCity(city);
       return zipcode;
    }
    @Transactional
    @Override
    public Zipcode removeCityFromZipcode(Long zipcodeId) {
        Zipcode zipcode = getZipcode(zipcodeId);
        if(!Objects.nonNull(zipcode.getCity())){
            throw new IllegalArgumentException("zipcode does not have a city");
        }
        zipcode.setCity(null);
        return zipcode;
    }
}
