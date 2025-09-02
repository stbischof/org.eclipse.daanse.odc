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
 * Represents a database connection configuration within an ODC file. Contains
 * connection details, parameters, and authentication settings.
 *
 * @param type                    The type of connection (OLEDB, ODBC, DATAFEED)
 * @param connectionString        The database connection string
 * @param commandType             Optional command type for the connection
 * @param parameters              List of connection parameters
 * @param commandText             Optional SQL command text
 * @param ssoApplicationId        Optional single sign-on application ID
 * @param credentialsMethod       Optional credentials authentication method
 * @param alwaysUseConnectionFile Optional flag to always use connection file
 * @param culture                 Optional culture/locale setting
 */
public record Connection(ConnectionType type, String connectionString, Optional<CommandType> commandType,
        List<Parameter> parameters, Optional<String> commandText, Optional<String> ssoApplicationId,
        Optional<CredentialsMethod> credentialsMethod, Optional<Boolean> alwaysUseConnectionFile,
        Optional<String> culture) {
    /**
     * Creates a basic connection with type and connection string. Optional fields
     * are set to empty defaults.
     *
     * @param type             The connection type
     * @param connectionString The database connection string
     * @return A new connection instance
     */
    public static Connection of(ConnectionType type, String connectionString) {
        return new Connection(type, connectionString, Optional.empty(), List.of(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty());
    }
}
