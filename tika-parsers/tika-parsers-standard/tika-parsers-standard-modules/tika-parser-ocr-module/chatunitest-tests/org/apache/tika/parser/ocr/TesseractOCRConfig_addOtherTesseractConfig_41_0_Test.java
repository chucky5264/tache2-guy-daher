package org.apache.tika.parser.ocr;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.tika.exception.TikaConfigException;
import org.apache.tika.utils.StringUtils;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TesseractOCRConfig_addOtherTesseractConfig_41_0_Test {

    private static final Logger LOG = LoggerFactory.getLogger(TesseractOCRConfig.class);

    private TesseractOCRConfig config;

    @BeforeEach
    public void setUp() {
        config = new TesseractOCRConfig();
    }

    @Test
    public void testAddOtherTesseractConfigValidKeyAndValue() throws TikaConfigException {
        // Test with valid key and value
        String key = "key1";
        String value = "value1";
        config.addOtherTesseractConfig(key, value);
        assertEquals(1, config.getOtherTesseractConfig().size());
        assertTrue(config.getOtherTesseractConfig().containsKey(key));
        assertEquals(value, config.getOtherTesseractConfig().get(key));
    }

    @Test
    public void testAddOtherTesseractConfigNullKey() {
        // Test with null key
        assertThrows(IllegalArgumentException.class, () -> {
            config.addOtherTesseractConfig(null, "value");
        });
    }

    @Test
    public void testAddOtherTesseractConfigNullValue() {
        // Test with null value
        assertThrows(IllegalArgumentException.class, () -> {
            config.addOtherTesseractConfig("key", null);
        });
    }

    @Test
    public void testAddOtherTesseractConfigInvalidKey() {
        // Test with invalid key
        String key = "invalid-key";
        config.addOtherTesseractConfig(key, "value");
        assertEquals(0, config.getOtherTesseractConfig().size());
    }

    @Test
    public void testAddOtherTesseractConfigInvalidValue() {
        // Test with invalid value
        String value = "invalid-value";
        config.addOtherTesseractConfig("key", value);
        assertEquals(0, config.getOtherTesseractConfig().size());
    }
}
