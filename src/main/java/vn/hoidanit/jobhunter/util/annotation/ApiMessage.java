package vn.hoidanit.jobhunter.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // hoat dong trong qua trinh chay du an
@Target(ElementType.METHOD) // decor cho method thoi
public @interface ApiMessage {
    String value();
}
