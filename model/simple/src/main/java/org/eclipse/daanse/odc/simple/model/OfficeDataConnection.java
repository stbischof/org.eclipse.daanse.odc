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

import java.util.List;
import java.util.Optional;

/**
 * Represents the main office data connection configuration within an ODC file.
 * Contains connection details, Power Query configurations, and related
 * metadata.
 *
 * @param sourceFile           Optional path to the source file
 * @param connections          List of database connections
 * @param powerQueryConnection Optional Power Query connection configuration
 * @param powerQueryMashupData Optional Power Query mashup data
 */
public record OfficeDataConnection(Optional<String> sourceFile, List<Connection> connections,
        Optional<PowerQueryConnection> powerQueryConnection, Optional<String> powerQueryMashupData) {
    /**
     * Creates an office data connection with a single database connection. Other
     * fields are set to empty defaults.
     *
     * @param connection The database connection to include
     * @return A new office data connection instance
     */
    public static OfficeDataConnection of(Connection connection) {
        return new OfficeDataConnection(Optional.empty(), List.of(connection), Optional.empty(), Optional.empty());
    }

    /**
     * Creates an office data connection with a Power Query connection. Other fields
     * are set to empty defaults.
     *
     * @param powerQueryConnection The Power Query connection to include
     * @return A new office data connection instance
     */
    public static OfficeDataConnection of(PowerQueryConnection powerQueryConnection) {
        return new OfficeDataConnection(Optional.empty(), List.of(), Optional.of(powerQueryConnection),
                Optional.empty());
    }
}
