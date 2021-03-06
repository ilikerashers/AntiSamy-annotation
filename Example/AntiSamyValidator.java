package co.blackdoglabs.common.validation.cutomValidator;

import org.owasp.validator.html.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import co.blackdoglabs.common.validation.NoJavascriptTags;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.io.InputStream;

/*
 * Strips text of tags
 */
public class AntiSamyValidator implements ConstraintValidator<NoJavascriptTags, String> {

    private static final Logger log = LoggerFactory.getLogger(AntiSamyValidator.class);

    static AntiSamy scanner;

    static {
        try (InputStream is = AntiSamyValidator.class.getClassLoader().getResourceAsStream("META-INF/antisamy.xml")) {
            scanner = new AntiSamy(Policy.getInstance(is));
        } catch (IOException | PolicyException e) {
            log.error("unable to load policy file.", e);
            throw new ExceptionInInitializerError(e);
        }
    }

    @Override
    public void initialize(NoJavascriptTags constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }

        try {

            CleanResults cr = scanner.scan(value);
            String cleanedHtml = cr.getCleanHTML();
            String unescaped = cleanedHtml.replace("&amp;", "&");

            if (!unescaped.equals(value)) log.warn("potential script injection attempted " + cr.getErrorMessages());


            return unescaped.equals(value);

        } catch (ScanException | PolicyException e) {
            log.warn(e.getMessage());
            log.debug(e.getMessage(), e);
        }
        return false;
    }
}
