package com.relationship.relationshipMapping.service;

import com.relationship.relationshipMapping.dto.requestDto.ZipCodeRequestDto;
import com.relationship.relationshipMapping.model.Zipcode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ZipcodeService {
    public Zipcode addZipcode(ZipCodeRequestDto zipCodeRequestDto);
    public List<Zipcode> getZipcodes();
    public Zipcode getZipcode(Long zipcodeId);
    public Zipcode deleteZipcode(Long zipcodeId);
    public Zipcode editZipcode(Long zipcodeId, ZipCodeRequestDto zipCodeRequestDto);
    public Zipcode addCityToZipcode(Long zipcodeId, Long cityId);
    public Zipcode removeCityFromZipcode(Long zipcodeId);

}
