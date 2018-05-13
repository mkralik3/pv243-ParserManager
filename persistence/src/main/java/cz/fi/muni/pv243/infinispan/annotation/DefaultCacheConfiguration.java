package cz.fi.muni.pv243.infinispan.annotation;

import java.lang.annotation.*;

@javax.inject.Qualifier
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DefaultCacheConfiguration {
}
