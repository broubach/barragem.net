package net.barragem.view.converter;

import java.util.List;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

import net.barragem.persistence.entity.Categoria;

public class CategoriaConverter implements Converter {

	@SuppressWarnings("unchecked")
	public Object getAsObject(FacesContext context, UIComponent arg1, String arg2) {
		List<UIComponent> l = arg1.getChildren();
		for (UIComponent uic : l) {
			if (uic.getFamily().equals("javax.faces.SelectItems")) {
				UISelectItems uis = (UISelectItems) uic;

				ValueExpression valueExpression = uis.getValueExpression("value");
				ELContext elContext = context.getELContext();

				List<SelectItem> al = (List<SelectItem>) valueExpression.getValue(elContext);

				for (SelectItem si : al) {
					if (((Categoria) si.getValue()).getNome().equals(arg2)) {
						return si.getValue();
					}
				}
			}
		}

		return null;
	}

	public String getAsString(FacesContext context, UIComponent arg1, Object arg2) {
		if (arg2 == null || arg2.equals(""))
			return "";

		return (arg2 instanceof Categoria) ? ((Categoria) arg2).getNome() : "";
	}
}