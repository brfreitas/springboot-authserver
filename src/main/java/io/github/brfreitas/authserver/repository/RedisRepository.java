/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.brfreitas.authserver.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Bruno Freitas
 */
public abstract class RedisRepository<T extends Serializable> {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private RedisTemplate<String, Object> template;

    protected synchronized Object getValue(final String key) {
        template.setHashValueSerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template.opsForValue().get(key);
    }

    protected synchronized T getValue(final String key, Class<T> clazz) {
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));

        Object obj = template.opsForValue().get(key);
        return mapper.convertValue(obj, clazz);
    }

    protected void setValue(final String key, final T value) {
        setValue(key, value, false);
    }

    protected void setValue(final String key, final T value, boolean marshal) {
        if (marshal) {
            template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
            template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        } else {
            template.setHashValueSerializer(new StringRedisSerializer());
            template.setValueSerializer(new StringRedisSerializer());
        }
        template.opsForValue().set(key, value);
    }

    protected void delKey(final String key) {
        template.delete(key);
    }

    protected void setValue(final String key, final T value, TimeUnit unit, long timeout) {
        setValue(key, value, unit, timeout, false);
    }

    protected void setValue(final String key, final T value, TimeUnit unit, long timeout, boolean marshal) {
        if (marshal) {
            template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
            template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        } else {
            template.setHashValueSerializer(new StringRedisSerializer());
            template.setValueSerializer(new StringRedisSerializer());
        }
        template.opsForValue().set(key, value);
        // set a expire for a message
        template.expire(key, timeout, unit);
    }

}
