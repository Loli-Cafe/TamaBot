package uwu.narumi.tama.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Retention(RetentionPolicy.RUNTIME)
@Target(TYPE)
public @interface CommandInfo {

    String alias();

    String description() default "";

    String usage() default "";

    CommandType type() default CommandType.USER;

    int argsLength() default 0;

    String[] aliases() default {};
}
