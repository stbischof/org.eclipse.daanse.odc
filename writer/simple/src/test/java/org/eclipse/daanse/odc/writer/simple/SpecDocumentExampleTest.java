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
package org.eclipse.daanse.odc.writer.simple;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.eclipse.daanse.odc.simple.model.CommandType;
import org.eclipse.daanse.odc.simple.model.Connection;
import org.eclipse.daanse.odc.simple.model.ConnectionType;
import org.eclipse.daanse.odc.simple.model.CredentialsMethod;
import org.eclipse.daanse.odc.simple.model.DocumentProperties;
import org.eclipse.daanse.odc.simple.model.OdcFile;
import org.eclipse.daanse.odc.simple.model.OfficeDataConnection;
import org.eclipse.daanse.odc.simple.model.PowerQueryConnection;
import org.junit.jupiter.api.Test;

class SpecDocumentExampleTest {

    private final OdcWriter writer = new OdcWriter();

    @Test
    void testNorthwindSqlExample() {
        Connection connection = new Connection(ConnectionType.ODBC,
                "DRIVER=SQL Server;SERVER=mysqlserver;APP=2007 Microsoft Office system;Trusted_Connection=Yes",
                Optional.empty(), List.of(), Optional.of("SELECT * FROM Northwind.dbo.Invoices Invoices"),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

        DocumentProperties props = new DocumentProperties(Optional.empty(), Optional.of("Northwind"), Optional.empty());

        OfficeDataConnection odc = OfficeDataConnection.of(connection);
        OdcFile odcFile = new OdcFile(Optional.of("Northwind"), props, odc, Optional.empty(), Optional.empty(),
                Optional.empty());

        String result = writer.write(odcFile);
        System.out.println("SQL Example Output:");
        System.out.println(result);

        assertThat(result)
            .contains("<title>Northwind</title>")
            .contains("ODC.Database")
            .contains("ODBC")
            .contains("SELECT * FROM Northwind.dbo.Invoices Invoices");
    }

    @Test
    void testAdventureWorksOlapExample() {
        Connection connection = new Connection(ConnectionType.OLEDB,
                "Provider=MSOLAP.3;Integrated Security=SSPI;Persist Security Info=True;Data Source=myolapserver;Initial Catalog=Adventure Works DW",
                Optional.of(CommandType.Cube), List.of(), Optional.of("Adventure Works"), Optional.of("Application1"),
                Optional.of(CredentialsMethod.Stored), Optional.of(true), Optional.empty());

        DocumentProperties props = new DocumentProperties(Optional.empty(), Optional.of("Adventure Works"),
                Optional.empty());

        OfficeDataConnection odc = OfficeDataConnection.of(connection);
        OdcFile odcFile = new OdcFile(Optional.of("Adventure Works"), props, odc, Optional.of("Adventure Works DW"),
                Optional.empty(), Optional.of("Adventure Works"));

        String result = writer.write(odcFile);
        System.out.println("\nOLAP Example Output:");
        System.out.println(result);

        assertThat(result)
            .contains("<title>Adventure Works</title>")
            .contains("ODC.Cube")
            .contains("OLEDB")
            .contains("<odc:CommandType>Cube</odc:CommandType>")
            .contains("<odc:CredentialsMethod>Stored</odc:CredentialsMethod>")
            .contains("Adventure Works DW");
    }

    @Test
    void testPowerQueryGetTransformExample() {
        PowerQueryConnection pqConnection = new PowerQueryConnection(ConnectionType.OLEDB,
                "Provider=Microsoft.Mashup.OleDb.1;Data Source=$Workbook$;Location=DimCustomer",
                Optional.of(CommandType.SQL), Optional.of("SELECT * FROM [DimCustomer]"), Optional.empty(),
                Optional.empty(), Optional.empty());

        String mashupData = "&lt;?xml version=&quot;1.0&quot; encoding=&quot;utf-16&quot;?&gt;"
                + "&lt;Mashup xmlns=&quot;http://schemas.microsoft.com/DataMashup&quot;&gt;"
                + "&lt;Client&gt;excel&lt;/Client&gt;" + "&lt;Version&gt;2.32.0.0&lt;/Version&gt;"
                + "&lt;Query Name=&quot;DimCustomer&quot;&gt;"
                + "&lt;Formula&gt;let Source = Sql.Databases(&quot;mysqlserver&quot;) in Source&lt;/Formula&gt;"
                + "&lt;/Query&gt;" + "&lt;/Mashup&gt;";

        DocumentProperties props = new DocumentProperties(
                Optional.of("Connection to the 'DimCustomer' query in the workbook."),
                Optional.of("Query - DimCustomer"), Optional.empty());

        OfficeDataConnection odc = new OfficeDataConnection(Optional.empty(), List.of(), Optional.of(pqConnection),
                Optional.of(mashupData));

        OdcFile odcFile = new OdcFile(Optional.of("Query - DimCustomer"), props, odc, Optional.empty(),
                Optional.empty(), Optional.empty());

        String result = writer.write(odcFile);
        System.out.println("\nPowerQuery Example Output:");
        System.out.println(result);

        assertThat(result)
            .contains("<title>Query - DimCustomer</title>")
            .contains("ODC.Table")
            .contains("OLEDB")
            .contains("<odc:PowerQueryConnection")
            .contains("Microsoft.Mashup.OleDb.1")
            .contains("<odc:PowerQueryMashupData>");
    }

    @Test
    void testDualModeExample() {
        Connection backwardCompatConnection = new Connection(ConnectionType.OLEDB,
                "Provider=SQLOLEDB;Data Source=mysqlserver;Initial Catalog=mysqldatabase;",
                Optional.of(CommandType.Table), List.of(), Optional.of("\"mysqldatabase\".\"dbo\".\"DimCustomer\""),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

        PowerQueryConnection pqConnection = new PowerQueryConnection(ConnectionType.OLEDB,
                "Provider=Microsoft.Mashup.OleDb.1;Data Source=$Workbook$;Location=DimCustomer",
                Optional.of(CommandType.SQL), Optional.of("SELECT * FROM [DimCustomer]"), Optional.empty(),
                Optional.empty(), Optional.empty());

        DocumentProperties props = new DocumentProperties(
                Optional.of("Connection to the 'DimCustomer' query in the workbook."),
                Optional.of("Query - DimCustomer"), Optional.empty());

        OfficeDataConnection odc = new OfficeDataConnection(Optional.empty(), List.of(backwardCompatConnection),
                Optional.of(pqConnection), Optional.of("&lt;Mashup&gt;...&lt;/Mashup&gt;"));

        OdcFile odcFile = new OdcFile(Optional.of("Query - DimCustomer"), props, odc, Optional.empty(),
                Optional.empty(), Optional.empty());

        String result = writer.write(odcFile);
        System.out.println("\nDual-Mode Example Output:");
        System.out.println(result);

        assertThat(result)
            .contains("<odc:Connection odc:Type=\"OLEDB\">")
            .contains("<odc:PowerQueryConnection odc:Type=\"OLEDB\">")
            .contains("SQLOLEDB")
            .contains("Microsoft.Mashup.OleDb.1");
    }
}
