/**
 * Copyright 2008 Mentor Graphics Corporation. All Rights Reserved.
 * <p>
 * Recipients who obtain this code directly from Mentor Graphics use it solely
 * for internal purposes to serve as example Java or Java Script plugins.
 * This code may not be used in a commercial distribution. Recipients may
 * duplicate the code provided that all notices are fully reproduced with
 * and remain in the code. No part of this code may be modified, reproduced,
 * translated, used, distributed, disclosed or provided to third parties
 * without the prior written consent of Mentor Graphics, except as expressly
 * authorized above.
 * <p>
 * THE CODE IS MADE AVAILABLE "AS IS" WITHOUT WARRANTY OR SUPPORT OF ANY KIND.
 * MENTOR GRAPHICS OFFERS NO EXPRESS OR IMPLIED WARRANTIES AND SPECIFICALLY
 * DISCLAIMS ANY WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * OR WARRANTY OF NON-INFRINGEMENT. IN NO EVENT SHALL MENTOR GRAPHICS OR ITS
 * LICENSORS BE LIABLE FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING LOST PROFITS OR SAVINGS) WHETHER BASED ON CONTRACT, TORT
 * OR ANY OTHER LEGAL THEORY, EVEN IF MENTOR GRAPHICS OR ITS LICENSORS HAVE BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * <p>
 */
package Reports;

import com.mentor.chs.api.IXConnector;
import com.mentor.chs.api.IXDevice;
import com.mentor.chs.api.IXInsulation;
import com.mentor.chs.api.IXInsulationRun;
import com.mentor.chs.api.IXShield;
import com.mentor.chs.api.IXWire;
import com.mentor.chs.api.query.IXFilterExpression;
import com.mentor.chs.api.query.IXQuery;
import com.mentor.chs.api.query.IXQueryFactory;
import com.mentor.chs.api.report.IXHarnessReport;
import com.mentor.chs.api.report.IXLogicReport;
import com.mentor.chs.api.report.IXReport;
import com.mentor.chs.api.report.IXReportContext;
import com.mentor.chs.api.report.IXReportFactory;
import com.mentor.chs.api.report.IXReportModule;
import com.mentor.chs.api.report.IXReportTemplate;

public class SimpleInsulationReport extends BaseReport implements IXLogicReport, IXHarnessReport {
	
	public SimpleInsulationReport() {
		super("[ Mentor ] Insulation List By Robin", "1.0", "[ Mentor ] Insulation List By Robin");
	}


	public IXReport createReport(IXReportContext context) {
		

		IXReportFactory rf = context.getReportFactory();
		IXQueryFactory qf = context.getQueryFactory();
		IXQuery query = null;
		IXReportModule module = rf.createReportModule("");
		
		try {
			query = qf.createQuery(IXInsulation.class, "Insulation", (IXFilterExpression) null );
			module.addQuery(query, "");
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		IXReport report = rf.createReport("Customized Insulation List by Robin xu");
		report.addModule(module);

		
                
                IXReportTemplate column = rf.createReportTemplate(new BaseReport.AttributeResultExpression("Name"), "Item Name");
		module.addReportTemplate(column);
                
                column = rf.createReportTemplate(new BaseReport.AttributeResultExpression("PartAssigned"), "PartAssigned");
		module.addReportTemplate(column);
                
		column = rf.createReportTemplate(new BaseReport.AttributeResultExpression("PartNumber"), "PN");
		module.addReportTemplate(column);
                
                column = rf.createReportTemplate(new BaseReport.AttributeResultExpression("SupplierName"), "SupplierName");
		module.addReportTemplate(column);

		column = rf.createReportTemplate(new BaseReport.LibraryAttributeResultExpression("Description"), "Description");
		module.addReportTemplate(column);

		column = rf.createReportTemplate(new BaseReport.LibraryAttributeResultExpression("UnitOfMeasure"), "UnitOfMeasure");
		module.addReportTemplate(column);
                
                column = rf.createReportTemplate(new BaseReport.LibraryAttributeResultExpression("ColorCode"), "ColorCode");
		module.addReportTemplate(column);
                
                column = rf.createReportTemplate(new BaseReport.LibraryAttributeResultExpression("Thickness"), "Thickness");
		module.addReportTemplate(column);
                
                column = rf.createReportTemplate(new BaseReport.AttributeResultExpression("IncludeOnBOM"), "IncludeOnBOM");
		module.addReportTemplate(column);
                
                column = rf.createReportTemplate(new BaseReport.AttributeResultExpression("InsulatedLength"), "InsulatedLength");
		module.addReportTemplate(column);
                
                column = rf.createReportTemplate(new BaseReport.AttributeResultExpression("TypeCode"), "TypeCode");
		module.addReportTemplate(column);
                
                column = rf.createReportTemplate(new BaseReport.AttributeResultExpression("Convoluted"), "Convoluted");
		module.addReportTemplate(column);
                
                column = rf.createReportTemplate(new BaseReport.AttributeResultExpression("Slit"), "Slit");
		module.addReportTemplate(column);


		return report;
	}


}
