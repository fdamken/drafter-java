/*
 * #%L
 * ClientGen
 * %%
 * Copyright (C) 2016 fdamken.de
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
package de.fdamken.drafter.java;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.fdamken.drafter.java.exception.DrafterProcessingException;
import de.fdamken.drafter.java.model.ApiDescription;
import de.fdamken.drafter.java.nlibrary.NativeDrafter;
import de.fdamken.drafter.java.nlibrary.NativeDrafterFormatConstants;
import de.fdamken.drafter.java.nlibrary.NativeDrafterOptions;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Drafter {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public String parseAsYaml(final String source, final boolean sourcemap) throws DrafterProcessingException {
        return Drafter.parseAs(source, sourcemap, NativeDrafterFormatConstants.DRAFTER_SERIALIZE_YAML);
    }

    public String parseAsYaml(final String source) throws DrafterProcessingException {
        return Drafter.parseAsYaml(source, false);
    }

    public String parseAsJson(final String source, final boolean sourcemap) throws DrafterProcessingException {
        return Drafter.parseAs(source, sourcemap, NativeDrafterFormatConstants.DRAFTER_SERIALIZE_JSON);
    }

    public String parseAsJson(final String source) throws DrafterProcessingException {
        return Drafter.parseAsJson(source, false);
    }

    public ApiDescription parse(final String source) throws DrafterProcessingException {
        final String json = Drafter.parseAsJson(source);
        final ApiDescription result;
        try {
            result = Drafter.OBJECT_MAPPER.readValue(json, ApiDescription.class);
        } catch (final IOException cause) {
            throw new DrafterProcessingException("Failed to parse JSON generated by Drafter!", cause);
        }
        return result;
    }

    public boolean isValid(final String source) {
        return NativeDrafter.INSTANCE.drafter_check_blueprint(source) == null;
    }

    private String parseAs(final String source, final boolean sourcemap, final int format) throws DrafterProcessingException {
        final String[] stringPointer = new String[1];
        final int resultCode = NativeDrafter.INSTANCE.drafter_parse_blueprint_to(source, stringPointer,
                new NativeDrafterOptions(sourcemap, format));
        final String result = stringPointer[0];

        if (resultCode != 0) {
            throw new DrafterProcessingException(resultCode);
        }

        return result;
    }
}
