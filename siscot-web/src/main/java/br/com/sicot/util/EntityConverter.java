package br.com.sicot.util;


import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@FacesConverter("br.com.sicot.util.EntityConverter")
public class EntityConverter implements Converter {

    public Object getAsObject(FacesContext ctx, UIComponent component,
                              String value) {
        if (value != null) {
            return component.getAttributes().get(value);
        }
        return null;
    }

    public String getAsString(FacesContext ctx, UIComponent component,
                              Object obj) {
        if (obj != null && !"".equals(obj)) {
            String id = null;

            try {
                id = this.getId(obj);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if (id == null) {
                id = "";
            }
            id = id.trim();
            component.getAttributes().put(id,
                    getClazz(ctx, component).cast(obj));
            return id;
        }
        return null;
    }

    private Class<?> getClazz(FacesContext facesContext, UIComponent component) {
        ValueExpression valueExpression = component.getValueExpression("value");

        return valueExpression.getType(facesContext.getELContext());
    }

    public String getId(Object obj) throws InvocationTargetException, IllegalAccessException {
        Object idValue = null;

        for (Method method : obj.getClass().getDeclaredMethods()) {

            Id id = null;

            Annotation[] as = method.getAnnotations();
            for (Annotation a : as) {
                if (a.annotationType() == Id.class) {
                    id = (Id) a;
                    idValue = method.invoke(obj);

                    break;
                }
            }

        }

        return String.valueOf(idValue);
    }
}