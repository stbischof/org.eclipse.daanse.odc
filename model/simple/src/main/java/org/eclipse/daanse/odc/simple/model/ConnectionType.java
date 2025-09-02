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

/**
 * Enumeration of supported database connection types.
 */
public enum ConnectionType {
    /** OLE DB (Object Linking and Embedding Database) connection type */
    OLEDB,
    /** ODBC (Open Database Connectivity) connection type */
    ODBC,
    /** Data feed connection type for web-based data sources */
    DATAFEED
}
