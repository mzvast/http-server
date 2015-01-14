package com.silu.dinner.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import javax.annotation.PostConstruct;
import java.io.IOException;

public class CustomJacksonMessageConverter extends MappingJackson2HttpMessageConverter {
    private static final Logger log = LoggerFactory.getLogger(CustomJacksonMessageConverter.class);

    private ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.INDENT_OUTPUT,true);
        super.setObjectMapper(mapper);

    }

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {

        try {
            byte[] buffer = mapper.writeValueAsBytes(object);
            if (buffer != null) {
                outputMessage.getHeaders().setContentLength(buffer.length);
            }
            outputMessage.getHeaders().setCacheControl("no-cache");
            outputMessage.getHeaders().setExpires(-1);

            outputMessage.getBody().write(buffer);

            if (log.isDebugEnabled()) {
                ObjectMapper debugmapper = new ObjectMapper();
                debugmapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                debugmapper.configure(SerializationFeature.INDENT_OUTPUT,true);
                debugmapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

                log.debug(debugmapper.writeValueAsString(object));
            }

        } catch (JsonProcessingException ex) {
            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }
}
