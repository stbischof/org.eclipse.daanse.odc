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
 * Represents an Office Data Connection (ODC) file structure. An ODC file
 * contains metadata and connection information for data sources used by
 * Microsoft Office applications.
 *
 * @param title                Optional title of the ODC file
 * @param documentProperties   Document metadata and properties
 * @param officeDataConnection The main data connection configuration
 * @param catalog              Optional database catalog name
 * @param schema               Optional database schema name
 * @param table                Optional database table name
 */
public record OdcFile(Optional<String> title, DocumentProperties documentProperties,
        OfficeDataConnection officeDataConnection, Optional<String> catalog, Optional<String> schema,
        Optional<String> table) {
    /**
     * Creates an ODC file with a title and office data connection. Other fields are
     * set to empty defaults.
     *
     * @param title                The title for the ODC file
     * @param officeDataConnection The office data connection configuration
     * @return A new ODC file instance
     */
    public static OdcFile of(String title, OfficeDataConnection officeDataConnection) {
        return new OdcFile(Optional.of(title), DocumentProperties.of(title), officeDataConnection, Optional.empty(),
                Optional.empty(), Optional.empty());
    }
}
