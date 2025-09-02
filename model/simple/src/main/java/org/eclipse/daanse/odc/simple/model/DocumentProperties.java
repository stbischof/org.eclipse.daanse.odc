/*
* Copyright (c) 2025 Contributors to the Eclipse Foundation.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*   SmartCity Jena - initial
*   Stefan Bischof (bipolis.org) - initial
*/
package org.eclipse.daanse.odc.simple.model;

import java.util.Optional;

/**
 * Represents document metadata properties for an ODC file. Contains descriptive
 * information about the document.
 *
 * @param description Optional description of the document
 * @param name        Optional name of the document
 * @param keywords    Optional keywords for the document
 */
public record DocumentProperties(Optional<String> description, Optional<String> name, Optional<String> keywords) {
    /**
     * Creates empty document properties with all fields set to empty.
     *
     * @return A new document properties instance with empty values
     */
    public static DocumentProperties empty() {
        return new DocumentProperties(Optional.empty(), Optional.empty(), Optional.empty());
    }

    /**
     * Creates document properties with a name. Other fields are set to empty.
     *
     * @param name The document name
     * @return A new document properties instance with the specified name
     */
    public static DocumentProperties of(String name) {
        return new DocumentProperties(Optional.empty(), Optional.of(name), Optional.empty());
    }
}
