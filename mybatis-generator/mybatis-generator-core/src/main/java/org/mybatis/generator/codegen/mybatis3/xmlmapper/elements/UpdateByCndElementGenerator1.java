/**
 * Copyright ${license.git.copyrightYears} the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * @author shadow
 */
public class UpdateByCndElementGenerator1 extends
        AbstractXmlElementGenerator {

    public UpdateByCndElementGenerator1() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("update"); //$NON-NLS-1$

        answer.addAttribute(new Attribute(
                "id", introspectedTable.getMyBatis3UpdateByCnd())); //$NON-NLS-1$

        String parameterType = context.getJavaParamConfiguration().getCndClass();

        answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
                parameterType));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();

        sb.append("update "); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement dynamicElement = new XmlElement("set"); //$NON-NLS-1$
        answer.addElement(dynamicElement);

        for (IntrospectedColumn introspectedColumn : ListUtilities.removeGeneratedAlwaysColumns(introspectedTable
                .getNonPrimaryKeyColumns())) {
            XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
            sb.setLength(0);
            sb.append("updateObj.").append(introspectedColumn.getJavaProperty());
            sb.append(" != null"); //$NON-NLS-1$
            isNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$

            // 判断是否nullobject
            XmlElement whenxml = new XmlElement("when");
            StringBuffer whensb_test = new StringBuffer();
            whensb_test.append("@com.soho.mybatis.crud.aconst.NullObject@valid(").append("updateObj.").append(introspectedColumn.getJavaProperty()).append(")");
            whenxml.addAttribute(new Attribute("test", whensb_test.toString())); //$NON-NLS-1$
            StringBuffer whensb = new StringBuffer();
            whensb.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            whensb.append(" = "); //$NON-NLS-1$
            whensb.append("null");
            whensb.append(',');
            whenxml.addElement(new TextElement(whensb.toString()));

            XmlElement otherwisexml = new XmlElement("otherwise");
            StringBuffer otherwisesb = new StringBuffer();
            otherwisesb.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            otherwisesb.append(" = "); //$NON-NLS-1$
            otherwisesb.append(getParameterClause(introspectedColumn, null));
            otherwisesb.append(',');
            otherwisexml.addElement(new TextElement(otherwisesb.toString()));

            XmlElement choosexml = new XmlElement("choose");
            choosexml.addElement(whenxml);
            choosexml.addElement(otherwisexml);

            isNotNullElement.addElement(choosexml);

            dynamicElement.addElement(isNotNullElement);

            // isNotNullElement.addElement(new TextElement(sb.toString()));
        }

        answer.addElement(new TextElement("<include refid=\"Global.Where_Clause\" />"));

        if (context.getPlugins()
                .sqlMapUpdateByPrimaryKeySelectiveElementGenerated(answer,
                        introspectedTable)) {
            parentElement.addElement(answer);
        }
    }

    private String getParameterClause(
            IntrospectedColumn introspectedColumn, String prefix) {
        StringBuilder sb = new StringBuilder();

        sb.append("#{"); //$NON-NLS-1$
        sb.append("updateObj.");
        sb.append(introspectedColumn.getJavaProperty(prefix));
        sb.append(",jdbcType="); //$NON-NLS-1$
        sb.append(introspectedColumn.getJdbcTypeName());

        if (stringHasValue(introspectedColumn.getTypeHandler())) {
            sb.append(",typeHandler="); //$NON-NLS-1$
            sb.append(introspectedColumn.getTypeHandler());
        }

        sb.append('}');

        return sb.toString();
    }
}
