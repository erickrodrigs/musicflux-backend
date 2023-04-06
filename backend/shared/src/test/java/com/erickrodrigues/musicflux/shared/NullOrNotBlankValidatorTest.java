package com.erickrodrigues.musicflux.shared;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class NullOrNotBlankValidatorTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldAllowNullValues() {
        final Foo foo = Foo.builder().build();
        final Set<ConstraintViolation<Foo>> violations = validator.validate(foo);

        assertTrue(violations.isEmpty());
        assertNull(foo.getBar());
    }

    @Test
    public void shouldNotAllowBlankStrings() {
        final Foo foo = Foo
            .builder()
            .bar("  ")
            .build();
        final Set<ConstraintViolation<Foo>> violations = validator.validate(foo);

        assertEquals(1, violations.size());
    }

    @Test
    public void shouldAllowNonBlankStrings() {
        final Foo foo = Foo
            .builder()
            .bar("bar")
            .build();
        final Set<ConstraintViolation<Foo>> violations = validator.validate(foo);

        assertTrue(violations.isEmpty());
    }

    @Data
    @AllArgsConstructor
    @Builder
    private static final class Foo {

        @NullOrNotBlank
        private String bar;
    }
}
