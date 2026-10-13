package org.apache.tika.parser.ocr;

import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.tika.exception.TikaConfigException;
import org.apache.tika.utils.StringUtils;

@ExtendWith(MockitoExtension.class)
public class TesseractOCRConfig_setTrustedPageSeparator_7_1_Test {

    private TesseractOCRConfig config;

    @Mock
    private Method privateSetTrustedPageSeparator;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        config = new TesseractOCRConfig();
        privateSetTrustedPageSeparator = TesseractOCRConfig.class.getDeclaredMethod("setTrustedPageSeparator", String.class);
        privateSetTrustedPageSeparator.setAccessible(true);
    }

    @Test
    public void testSetTrustedPageSeparator_WithValidInput() throws Exception {
        String validInput = "example-page-separator";
        privateSetTrustedPageSeparator.invoke(config, validInput);
        assertEquals(validInput, config.getPageSeparator());
    }

    @Test
    public void testSetTrustedPageSeparator_WithNullInput() throws Exception {
        privateSetTrustedPageSeparator.invoke(config, null);
        assertEquals("", config.getPageSeparator());
    }

    @Test
    public void testSetTrustedPageSeparator_WithEmptyInput() throws Exception {
        privateSetTrustedPageSeparator.invoke(config, "");
        assertEquals("", config.getPageSeparator());
    }

    @Test
    public void testSetTrustedPageSeparator_WithInvalidInput() throws Exception {
        String invalidInput = "invalid/page/separator";
        privateSetTrustedPageSeparator.invoke(config, invalidInput);
        assertEquals("", config.getPageSeparator());
    }

    @Test
    public void testSetTrustedPageSeparator_WithWhitespaceInput() throws Exception {
        String whitespaceInput = "   whitespace   ";
        privateSetTrustedPageSeparator.invoke(config, whitespaceInput);
        assertEquals("", config.getPageSeparator());
    }

    @Test
    public void testSetTrustedPageSeparator_WithAllowedPageSeparator() throws Exception {
        String allowedInput = "-_/\\.A-Z0-9";
        privateSetTrustedPageSeparator.invoke(config, allowedInput);
        assertEquals(allowedInput, config.getPageSeparator());
    }

    @Test
    public void testSetTrustedPageSeparator_WithAllowedOtherParams() throws Exception {
        String allowedInput = "-_/\\.A-Z0-9";
        privateSetTrustedPageSeparator.invoke(config, allowedInput);
        assertEquals(allowedInput, config.getPageSeparator());
    }

    @Test
    public void testSetTrustedPageSeparator_WithMixedInput() throws Exception {
        String mixedInput = "example-page-separator -_/\\.A-Z0-9";
        privateSetTrustedPageSeparator.invoke(config, mixedInput);
        assertEquals(mixedInput, config.getPageSeparator());
    }

    @Test
    public void testSetTrustedPageSeparator_WithInvalidRegexInput() throws Exception {
        String invalidRegexInput = ".*\\^";
        privateSetTrustedPageSeparator.invoke(config, invalidRegexInput);
        assertEquals("", config.getPageSeparator());
    }
}
