package Reports;

import com.mentor.chs.api.IXAbstractPin;
import com.mentor.chs.api.IXCavity;
import com.mentor.chs.api.IXDevicePin;
import com.mentor.chs.api.IXLibrariedObject;
import com.mentor.chs.api.IXLibraryObject;
import com.mentor.chs.api.IXObject;
import com.mentor.chs.api.IXWire;
import com.mentor.chs.api.IXWireEnd;
import com.mentor.chs.api.query.IXFilterExpression;
import com.mentor.chs.api.query.IXQuery;
import com.mentor.chs.api.query.IXQueryFactory;
import com.mentor.chs.api.query.IXResultExpression;
import com.mentor.chs.api.report.IXReport;
import com.mentor.chs.api.report.IXReportContext;
import com.mentor.chs.api.report.IXReportFactory;
import com.mentor.chs.api.report.IXReportModule;
import com.mentor.chs.api.report.IXReportTemplate;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.util.Set;

public class BaseReport {

	String m_name;
	String m_version;
	String m_description;

	BaseReport(String name, String version, String description) {
		m_name = name;
		m_version = version;
		m_description = description;
	}

	public StreamSource getStyleSheet() {
		String chs_home = System.getenv("CHS_HOME");
		String styleSheetFileName = chs_home.replace("\\", "/") + File.separator + "/reporter/templates/report.xsl";
		File file = new File(styleSheetFileName);
		return new StreamSource(file);
	}


	
	private IXFilterExpression getPartNumberExpression(final String partNumberAttributeName) {
		return new IXFilterExpression() {
			public String getName() {
				return "";
			}

			public String getCollapsedForm() {
				return "";
			}

			public boolean isSatisfiedBy(IXObject entity) {
				// make sure that the entity has a partnumber and include on bom set
				// to true
				final String s = entity.getAttribute(partNumberAttributeName);
				return s != null && !s.trim().isEmpty() && !"false".equals(entity.getAttribute("IncludeOnBOM"));
			}
		};
	}

	public String getDescription() {
		return m_description;
	}

	public String getName() {
		return m_name;
	}

	public String getVersion() {
		return m_version;
	}

	protected class AbstractResultExpression 
        {

		public String getName() {
			return "";
		}

		public Object evaluate(IXObject entity) {
			return "";
		}

		public Object evaluate(IXObject entity, String executionContext) {
			return evaluate(entity);
		}

	}

	protected class AttributeResultExpression extends AbstractResultExpression implements IXResultExpression {

		AttributeResultExpression(String attribName) {
			m_attributeName = attribName;
		}

		String m_attributeName;

		public Object evaluate(IXObject entity) {
			return entity.getAttribute(m_attributeName);
		}

	}

	protected class PropertyResultExpression extends AbstractResultExpression implements IXResultExpression {

		PropertyResultExpression(String attribName) {
			m_propertyName = attribName;
		}

		String m_propertyName;

		public Object evaluate(IXObject entity) {
			return entity.getProperty(m_propertyName);
		}
	}

	public class PinResultExpression extends AbstractResultExpression implements IXResultExpression {

		private int m_index;

		public PinResultExpression(int i) {
			m_index = i;
		}

		public Object evaluate(IXObject entity) {
			if (entity instanceof IXWire) {
				IXWire wire = (IXWire) entity;
				Set<IXAbstractPin> pins = wire.getAbstractPins();
				int i = 0;
				// assumes that pins are already ordered.
				for (IXAbstractPin abstractPin : pins) {
					if (i == m_index) {

						return abstractPin.getAttribute("Name");
					}
					++i;
				}
			}
			return "";
		}

	}
        
        public class TerminalResultExpression extends AbstractResultExpression implements IXResultExpression {

		private int m_index;

		public TerminalResultExpression(int i) {
			m_index = i;
		}

		public Object evaluate(IXObject entity) {
			if (entity instanceof IXWire) {
				IXWire wire = (IXWire) entity;
				Set<IXWireEnd> pins = wire.getWireEnds();
				int i = 0;
				// assumes that pins are already ordered.
				for (IXWireEnd abstractPin : pins) {
                                    if (i == m_index) {
						return abstractPin.getAttribute("TerminalInternalPartNumber");
					}
					++i;
				}
			}
			return "";
		}

	}

	public class ConnectedPinListResultExpression extends AbstractResultExpression implements IXResultExpression {

		private int m_index;

		public ConnectedPinListResultExpression(int i) {
			m_index = i;
		}

		public Object evaluate(IXObject entity) {
			if (entity instanceof IXWire) {
				IXWire wire = (IXWire) entity;
				Set<IXAbstractPin> pins = wire.getAbstractPins();
				int i = 0;
				// assumes that pins are already ordered.
				for (IXAbstractPin abstractPin : pins) {
					if (i == m_index) {

						if (abstractPin instanceof IXCavity) {
							IXCavity cavity = (IXCavity) abstractPin;

							IXCavity matedCavity = cavity.getMatedCavity();
							if (matedCavity != null)
								return matedCavity.getOwner().getAttribute("Name");
							else {
								IXDevicePin devPin = cavity.getMatedDevicePin();
								if (devPin != null)
									return devPin.getOwner().getAttribute("Name");
							}
						}
					}
					++i;
				}
			}
			return "";
		}

	}

	// returns the name of pinlist that the wire is connected to
	// can be connector, backshell, splice, or device
	public class ConnectorResultExpression extends AbstractResultExpression implements IXResultExpression {

		private int m_index;

		public ConnectorResultExpression(int i) {
			m_index = i;
		}

		public Object evaluate(IXObject entity) {
			if (entity instanceof IXWire) {
				IXWire wire = (IXWire) entity;
				Set<IXAbstractPin> pins = wire.getAbstractPins();
				int i = 0;
				// assumes that pins are already ordered.
				for (IXAbstractPin abstractPin : pins) {
					if (i == m_index) {
						return abstractPin.getOwner().getAttribute("Name");
					}
					++i;
				}
			}
			return "";
		}

	}

	public class QuantityResultExpression extends AbstractResultExpression implements IXResultExpression {

		public Object evaluate(IXObject entity) {

			if (entity instanceof IXWire) {
				// get length of wires
				return entity.getAttribute("ModifiedLength");
			} else if (entity.getAttribute("Quantity") != null) {
				// return the attribute quantity if present
				return entity.getAttribute("Quantity");
			} else {
				return "1";
			}
		}
	}

	protected static class WireHarnessFilter implements IXFilterExpression {

		public String getCollapsedForm() {
			return "WireHarnessFilter";
		}

		public String getName() {
			return "WireHarnessFilter";
		}

		public boolean isSatisfiedBy(IXObject object) {

			if (object instanceof IXWire) {

				String harness = object.getAttribute("Harness");
				if (harness != null && harness.equals("H1"))
					return true;
			}
			return false;
		}
	}

	protected class LibraryAttributeResultExpression extends AbstractResultExpression implements IXResultExpression {
		
		LibraryAttributeResultExpression(String attribName) {
			m_attributeName = attribName;
		}

		String m_attributeName;

		public Object evaluate(IXObject entity) {
			if (entity instanceof IXLibrariedObject) {
				IXLibrariedObject objWithPart = (IXLibrariedObject) entity;
				IXLibraryObject libobj = objWithPart.getLibraryObject();
				if (libobj != null)
					return libobj.getAttribute(m_attributeName);
			}
			return "";
		}
	}
	
	protected class DesignExpression extends AbstractResultExpression implements IXResultExpression {

		public Object evaluate(IXObject entity) {
		
		return entity.getAttribute("Name") + ":" +entity.getAttribute("Revision");
		}
	}
	
	protected class DummyExpression extends AbstractResultExpression implements IXResultExpression {

		public Object evaluate(IXObject entity) {
		
		return "1";
		}
	}

}
