package cz.fi.muni.pv243.app;

import java.lang.annotation.*;

@javax.inject.Qualifier
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserCache {
}
