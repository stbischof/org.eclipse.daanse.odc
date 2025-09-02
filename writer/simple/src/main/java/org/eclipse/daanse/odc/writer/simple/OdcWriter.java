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

import org.eclipse.daanse.odc.simple.model.*;

/**
 * Writer class for generating Office Data Connection (ODC) files. This class
 * converts ODC file model objects into their HTML/XML representation that can
 * be saved as .odc files for use with Microsoft Office applications.
 */
public class OdcWriter {

    /**
     * Converts an ODC file model to its HTML/XML string representation.
     *
     * @param odcFile The ODC file model to convert
     * @return The HTML/XML string representation of the ODC file
     */
    public String write(OdcFile odcFile) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html xmlns:o=\"urn:schemas-microsoft-com:office:office\"");
        sb.append(" xmlns=\"http://www.w3.org/TR/REC-html40\">");
        sb.append("\n<head>");

        writeMetaTags(sb, odcFile);
        writeTitle(sb, odcFile);
        writeDocumentProperties(sb, odcFile);
        writeOfficeDataConnection(sb, odcFile);

        sb.append("\n</head>");
        sb.append("\n</html>");

        return sb.toString();
    }

    /**
     * Writes the HTML meta tags section for the ODC file.
     *
     * @param sb      The StringBuilder to append to
     * @param odcFile The ODC file containing the metadata
     */
    private void writeMetaTags(StringBuilder sb, OdcFile odcFile) {
        sb.append("\n<meta http-equiv=Content-Type content=\"text/x-ms-odc; charset=utf-8\">");

        OfficeDataConnection odc = odcFile.officeDataConnection();
        if (!odc.connections().isEmpty()) {
            Connection conn = odc.connections().get(0);
            writeProgIdMeta(sb, conn);
        } else if (odc.powerQueryConnection().isPresent()) {
            writeProgIdMeta(sb, odc.powerQueryConnection().get());
        }

        if (odcFile.catalog().isPresent()) {
            sb.append("\n<meta name=Catalog content=\"").append(escapeXml(odcFile.catalog().get())).append("\">");
        }
        if (odcFile.schema().isPresent()) {
            sb.append("\n<meta name=Schema content=\"").append(escapeXml(odcFile.schema().get())).append("\">");
        }
        if (odcFile.table().isPresent()) {
            sb.append("\n<meta name=Table content=\"").append(escapeXml(odcFile.table().get())).append("\">");
        }
    }

    /**
     * Writes the ProgId and SourceType meta tags for a database connection.
     *
     * @param sb   The StringBuilder to append to
     * @param conn The connection to write metadata for
     */
    private void writeProgIdMeta(StringBuilder sb, Connection conn) {
        sb.append("\n<meta name=ProgId content=");
        if (conn.commandType().isPresent()) {
            CommandType cmdType = conn.commandType().get();
            switch (cmdType) {
            case Cube -> sb.append("ODC.Cube");
            case Table -> sb.append("ODC.Table");
            case TableCollection -> sb.append("ODC.TableCollection");
            default -> sb.append("ODC.Database");
            }
        } else {
            sb.append("ODC.Database");
        }
        sb.append(">");

        sb.append("\n<meta name=SourceType content=").append(conn.type().name()).append(">");
    }

    /**
     * Writes the ProgId and SourceType meta tags for a Power Query connection.
     *
     * @param sb   The StringBuilder to append to
     * @param conn The Power Query connection to write metadata for
     */
    private void writeProgIdMeta(StringBuilder sb, PowerQueryConnection conn) {
        sb.append("\n<meta name=ProgId content=ODC.Table>");
        sb.append("\n<meta name=SourceType content=").append(conn.type().name()).append(">");
    }

    /**
     * Writes the HTML title element for the ODC file.
     *
     * @param sb      The StringBuilder to append to
     * @param odcFile The ODC file containing the title
     */
    private void writeTitle(StringBuilder sb, OdcFile odcFile) {
        if (odcFile.title().isPresent()) {
            sb.append("\n<title>").append(escapeXml(odcFile.title().get())).append("</title>");
        }
    }

    /**
     * Writes the document properties XML section.
     *
     * @param sb      The StringBuilder to append to
     * @param odcFile The ODC file containing document properties
     */
    private void writeDocumentProperties(StringBuilder sb, OdcFile odcFile) {
        DocumentProperties props = odcFile.documentProperties();
        if (hasDocumentProperties(props)) {
            sb.append("\n<xml id=docprops><o:DocumentProperties");
            sb.append(" xmlns:o=\"urn:schemas-microsoft-com:office:office\"");
            sb.append(" xmlns=\"http://www.w3.org/TR/REC-html40\">");

            if (props.description().isPresent()) {
                sb.append("\n <o:Description>").append(escapeXml(props.description().get())).append("</o:Description>");
            }
            if (props.name().isPresent()) {
                sb.append("\n <o:Name>").append(escapeXml(props.name().get())).append("</o:Name>");
            }
            if (props.keywords().isPresent()) {
                sb.append("\n <o:Keywords>").append(escapeXml(props.keywords().get())).append("</o:Keywords>");
            }

            sb.append("\n</o:DocumentProperties>");
            sb.append("\n</xml>");
        }
    }

    /**
     * Checks if the document properties contain any non-empty values.
     *
     * @param props The document properties to check
     * @return true if any property has a value, false otherwise
     */
    private boolean hasDocumentProperties(DocumentProperties props) {
        return props.description().isPresent() || props.name().isPresent() || props.keywords().isPresent();
    }

    /**
     * Writes the main Office Data Connection XML section.
     *
     * @param sb      The StringBuilder to append to
     * @param odcFile The ODC file containing the office data connection
     */
    private void writeOfficeDataConnection(StringBuilder sb, OdcFile odcFile) {
        sb.append("<xml id=msodc><odc:OfficeDataConnection");
        sb.append(" xmlns:odc=\"urn:schemas-microsoft-com:office:odc\"");
        sb.append(" xmlns=\"http://www.w3.org/TR/REC-html40\">");

        OfficeDataConnection odc = odcFile.officeDataConnection();

        if (odc.sourceFile().isPresent()) {
            sb.append("\n <odc:SourceFile>").append(escapeXml(odc.sourceFile().get())).append("</odc:SourceFile>");
        }

        for (Connection conn : odc.connections()) {
            writeConnection(sb, conn);
        }

        if (odc.powerQueryConnection().isPresent()) {
            writePowerQueryConnection(sb, odc.powerQueryConnection().get());
        }

        if (odc.powerQueryMashupData().isPresent()) {
            sb.append("\n <odc:PowerQueryMashupData>");
            sb.append(escapeXml(odc.powerQueryMashupData().get()));
            sb.append("</odc:PowerQueryMashupData>");
        }

        sb.append("\n</odc:OfficeDataConnection>");
        sb.append("\n</xml>");
    }

    /**
     * Writes a database connection XML element.
     *
     * @param sb   The StringBuilder to append to
     * @param conn The connection to write
     */
    private void writeConnection(StringBuilder sb, Connection conn) {
        sb.append("\n <odc:Connection odc:Type=\"").append(conn.type().name()).append("\">");

        sb.append("\n <odc:ConnectionString>").append(escapeXml(conn.connectionString()))
                .append("</odc:ConnectionString>");

        if (conn.commandType().isPresent()) {
            sb.append("\n <odc:CommandType>").append(conn.commandType().get().name()).append("</odc:CommandType>");
        }

        for (Parameter param : conn.parameters()) {
            sb.append("\n <odc:Parameter>");
            sb.append("\n  <odc:Name>").append(escapeXml(param.name())).append("</odc:Name>");
            if (param.dataType().isPresent()) {
                sb.append("\n  <odc:DataType>").append(param.dataType().get()).append("</odc:DataType>");
            }
            sb.append("\n </odc:Parameter>");
        }

        if (conn.commandText().isPresent()) {
            sb.append("\n <odc:CommandText>").append(escapeXml(conn.commandText().get())).append("</odc:CommandText>");
        }

        if (conn.ssoApplicationId().isPresent()) {
            sb.append("\n <odc:SSOApplicationID>").append(escapeXml(conn.ssoApplicationId().get()))
                    .append("</odc:SSOApplicationID>");
        }

        if (conn.credentialsMethod().isPresent()) {
            sb.append("\n <odc:CredentialsMethod>").append(conn.credentialsMethod().get().name())
                    .append("</odc:CredentialsMethod>");
        }

        if (conn.alwaysUseConnectionFile().isPresent()) {
            if (conn.alwaysUseConnectionFile().get()) {
                sb.append("\n <odc:AlwaysUseConnectionFile/>");
            } else {
                sb.append("\n <odc:AlwaysUseConnectionFile>false</odc:AlwaysUseConnectionFile>");
            }
        }

        if (conn.culture().isPresent()) {
            sb.append("\n <odc:Culture>").append(escapeXml(conn.culture().get())).append("</odc:Culture>");
        }

        sb.append("\n </odc:Connection>");
    }

    /**
     * Writes a Power Query connection XML element.
     *
     * @param sb   The StringBuilder to append to
     * @param conn The Power Query connection to write
     */
    private void writePowerQueryConnection(StringBuilder sb, PowerQueryConnection conn) {
        sb.append("\n <odc:PowerQueryConnection odc:Type=\"").append(conn.type().name()).append("\">");

        sb.append("\n <odc:ConnectionString>").append(escapeXml(conn.connectionString()))
                .append("</odc:ConnectionString>");

        if (conn.commandType().isPresent()) {
            sb.append("\n <odc:CommandType>").append(conn.commandType().get().name()).append("</odc:CommandType>");
        }

        if (conn.commandText().isPresent()) {
            sb.append("\n <odc:CommandText>").append(escapeXml(conn.commandText().get())).append("</odc:CommandText>");
        }

        if (conn.ssoApplicationId().isPresent()) {
            sb.append("\n <odc:SSOApplicationID>").append(escapeXml(conn.ssoApplicationId().get()))
                    .append("</odc:SSOApplicationID>");
        }

        if (conn.credentialsMethod().isPresent()) {
            sb.append("\n <odc:CredentialsMethod>").append(conn.credentialsMethod().get().name())
                    .append("</odc:CredentialsMethod>");
        }

        if (conn.alwaysUseConnectionFile().isPresent()) {
            if (conn.alwaysUseConnectionFile().get()) {
                sb.append("\n <odc:AlwaysUseConnectionFile/>");
            } else {
                sb.append("\n <odc:AlwaysUseConnectionFile>false</odc:AlwaysUseConnectionFile>");
            }
        }

        sb.append("\n </odc:PowerQueryConnection>");
    }

    /**
     * Escapes special XML characters in text content.
     *
     * @param text The text to escape
     * @return The escaped text safe for XML content
     */
    private String escapeXml(String text) {
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'",
                "&apos;");
    }
}
