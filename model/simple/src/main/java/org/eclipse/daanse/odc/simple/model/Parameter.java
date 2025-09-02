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
 * Represents a parameter for database connections.
 *
 * @param name     The parameter name
 * @param dataType Optional data type identifier for the parameter
 */
public record Parameter(String name, Optional<Integer> dataType) {
    /**
     * Creates a parameter with just a name. Data type is set to empty.
     *
     * @param name The parameter name
     * @return A new parameter instance
     */
    public static Parameter of(String name) {
        return new Parameter(name, Optional.empty());
    }

    /**
     * Creates a parameter with name and data type.
     *
     * @param name     The parameter name
     * @param dataType The data type identifier
     * @return A new parameter instance
     */
    public static Parameter of(String name, int dataType) {
        return new Parameter(name, Optional.of(dataType));
    }
}
