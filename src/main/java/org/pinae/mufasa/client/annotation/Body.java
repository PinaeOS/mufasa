package org.pinae.mufasa.client.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.pinae.mufasa.client.http.Http;

@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Body {
	String contentType() default Http.APPLICATION_JSON;
	
	String charset() default "utf-8";
}
