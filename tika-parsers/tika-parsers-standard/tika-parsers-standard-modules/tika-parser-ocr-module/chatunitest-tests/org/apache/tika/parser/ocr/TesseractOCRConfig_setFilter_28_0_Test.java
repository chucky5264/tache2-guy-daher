package org.apache.tika.parser.ocr;

import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
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
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TesseractOCRConfig_setFilter_28_0_Test {

    @Test
    public void testSetFilterValidValue() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        config.setFilter("triangle");
        assertEquals("triangle", config.getFilter());
    }

    @Test
    public void testSetFilterInvalidValue() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        assertThrows(IllegalArgumentException.class, () -> config.setFilter("invalid"));
    }

    @Test
    public void testSetFilterNullValue() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        assertThrows(IllegalArgumentException.class, () -> config.setFilter(null));
    }

    @Test
    public void testSetFilterPreserveCase() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        config.setFilter("Triangle");
        assertEquals("Triangle", config.getFilter());
    }

    @Test
    public void testSetFilterCaseInsensitive() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        config.setFilter("TriaNgLE");
        assertEquals("triangle", config.getFilter());
    }

    @Test
    public void testSetFilterMultipleCases() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        config.setFilter("TriAnGle");
        assertEquals("triangle", config.getFilter());
    }

    @Test
    public void testSetFilterAllowableFilter() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        config.setFilter("POINT");
        assertEquals("POINT", config.getFilter());
    }

    @Test
    public void testSetFilterAllowableFilterCaseInsensitive() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        config.setFilter("pOint");
        assertEquals("POINT", config.getFilter());
    }

    @Test
    public void testSetFilterAllowableFilterMixedCase() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        config.setFilter("pOiNT");
        assertEquals("POINT", config.getFilter());
    }

    @Test
    public void testSetFilterAllowableFilterMixedCases() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        config.setFilter("pOiNT");
        assertEquals("POINT", config.getFilter());
    }
}
