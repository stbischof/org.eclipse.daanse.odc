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
 * Represents a Power Query connection configuration. Power Query is a data
 * transformation and connectivity feature in Microsoft Office.
 *
 * @param type                    The type of connection (OLEDB, ODBC, DATAFEED)
 * @param connectionString        The database connection string
 * @param commandType             Optional command type for the connection
 * @param commandText             Optional query command text
 * @param ssoApplicationId        Optional single sign-on application ID
 * @param credentialsMethod       Optional credentials authentication method
 * @param alwaysUseConnectionFile Optional flag to always use connection file
 */
public record PowerQueryConnection(ConnectionType type, String connectionString, Optional<CommandType> commandType,
        Optional<String> commandText, Optional<String> ssoApplicationId, Optional<CredentialsMethod> credentialsMethod,
        Optional<Boolean> alwaysUseConnectionFile) {
    /**
     * Creates a basic Power Query connection with type and connection string.
     * Optional fields are set to empty defaults.
     *
     * @param type             The connection type
     * @param connectionString The database connection string
     * @return A new Power Query connection instance
     */
    public static PowerQueryConnection of(ConnectionType type, String connectionString) {
        return new PowerQueryConnection(type, connectionString, Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty());
    }
}
