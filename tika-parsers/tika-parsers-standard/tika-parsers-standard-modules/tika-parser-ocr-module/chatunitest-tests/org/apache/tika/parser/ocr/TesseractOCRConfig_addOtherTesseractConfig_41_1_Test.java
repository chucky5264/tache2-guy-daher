package org.apache.tika.parser.ocr;

import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.exception.TikaConfigException;
import org.apache.tika.utils.StringUtils;

@ExtendWith(MockitoExtension.class)
public class TesseractOCRConfig_addOtherTesseractConfig_41_1_Test {

    private static final long serialVersionUID = -4861942486845757891L;

    private static final Logger LOG = LoggerFactory.getLogger(TesseractOCRConfig.class);

    private TesseractOCRConfig tesseractOCRConfig;

    private HashMap<String, String> otherTesseractConfig;

    @BeforeEach
    public void setUp() {
        tesseractOCRConfig = new TesseractOCRConfig();
        otherTesseractConfig = new HashMap<>();
    }

    @Test
    public void testAddOtherTesseractConfigWithValidKeyAndValue() {
        String key = "key1";
        String value = "value1";
        tesseractOCRConfig.addOtherTesseractConfig(key, value);
        assertEquals(value, tesseractOCRConfig.getOtherTesseractConfig().get(key));
    }

    @Test
    public void testAddOtherTesseractConfigWithInvalidKey() {
        String key = "key1";
        String value = "value1";
        try {
            tesseractOCRConfig.addOtherTesseractConfig(null, value);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("key must not be null", e.getMessage());
        }
    }

    @Test
    public void testAddOtherTesseractConfigWithInvalidValue() {
        String key = "key1";
        String value = "value1";
        try {
            tesseractOCRConfig.addOtherTesseractConfig(key, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("value must not be null", e.getMessage());
        }
    }

    @Test
    public void testAddOtherTesseractConfigWithInvalidKeyPattern() {
        String key = "key1";
        String value = "value1";
        try {
            tesseractOCRConfig.addOtherTesseractConfig("key1", value);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Key contains illegal characters: key1", e.getMessage());
        }
    }

    @Test
    public void testAddOtherTesseractConfigWithInvalidValuePattern() {
        String key = "key1";
        String value = "value1";
        try {
            tesseractOCRConfig.addOtherTesseractConfig(key, "value1");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Value contains illegal characters: value1", e.getMessage());
        }
    }

    @Test
    public void testAddOtherTesseractConfigWithExistingKey() {
        String key = "key1";
        String value = "value1";
        tesseractOCRConfig.addOtherTesseractConfig(key, value);
        try {
            tesseractOCRConfig.addOtherTesseractConfig(key, "value2");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Key already exists: key1", e.getMessage());
        }
    }
}
