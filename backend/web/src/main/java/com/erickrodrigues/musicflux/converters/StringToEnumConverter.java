package com.erickrodrigues.musicflux.converters;

import com.erickrodrigues.musicflux.vo.SearchableType;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverter implements Converter<String, SearchableType> {

    @Override
    public SearchableType convert(String source) {
        return SearchableType.valueOf(source.toUpperCase());
    }
}
