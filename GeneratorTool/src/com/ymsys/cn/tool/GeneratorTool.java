package com.jyd.bms.tool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.zhtml.Tr;

/**
 * @category 代码生成器工具类
 * @author mjy
 * @version 1.0
 */
public class GeneratorTool {
	private static final int COLSPAN_SIZE = 4;
	public static final String url = "jdbc:mysql://code-server/information_schema";
	public static final String name = "com.mysql.jdbc.Driver";
	public static final String user = "code";
	public static final String password = "jydcode";
	public static final String tableName = "cus_contract_overdue_amount";
	public static final String dataBase = "bms";
	public static final String filePath = "";
	public static boolean dataFlag = true;
	public static boolean timeStampflag = true;
	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static void main(String[] args) throws IOException {
		new GeneratorTool().start();
	}

	/**
	 * @category 开始生成
	 * @throws IOException
	 */
	public void start() throws IOException {
		List<Table> list = new TableDao().findByDataBase(dataBase);
		 //generatorBean(list);
		// generatorXml(list);
		// generatorDao();
		// generatorImpl(list);
		// generatorService();
//		generatorZul(list);
//		generatorWindow(list);
		 auxiliaryCode(list);
	}

	public void td(List<Table> list, StringBuilder code, int colsapn, int i) {
		code.append("<h:td width=\"150px\" class=\"tdEvennoBorder\"><hbox><label value=\"" + list.get(i).getComment()
				+ "\" /></hbox></h:td>");
		if (i == list.size() - 1 && colsapn != 0) {
			code.append("<h:td width=\"150px\" class=\"tdOddnoBorder\" colspan=\"" + colsapn + "\">\n");
			code.append("<label value=\"\" id=\"" + getFirstWordLower(list.get(i).getColumn())
					+ "Label\" visible=\"true\" />\n");
			code.append("<textbox id=\"" + getFirstWordLower(list.get(i).getColumn())
					+ "Textbox\" visible=\"false\" /></h:td>\n");
		} else {
			code.append("<h:td width=\"150px\" class=\"tdOddnoBorder\">\n");
			code.append("<label value=\"\" id=\"" + getFirstWordLower(list.get(i).getColumn())
					+ "Label\" visible=\"true\" />\n");
			code.append("<textbox id=\"" + getFirstWordLower(list.get(i).getColumn())
					+ "Textbox\" visible=\"false\" /></h:td>\n");
		}
	}

	public void auxiliaryCode(List<Table> list) throws IOException {
		StringBuilder code = new StringBuilder();
		List<Table> tempList = new ArrayList<Table>();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getName().equals(tableName)) {
				if (!list.get(i).getKey().equals("PRI")) {
					if (!list.get(i).getColumn().equals("create_date") && !list.get(i).getColumn().equals("update_date")
							&& !list.get(i).getColumn().equals("create_user")
							&& !list.get(i).getColumn().equals("update_user")) {
						tempList.add(list.get(i));
					}
				}
			}
		}
		int allSize = tempList.size();
		if (allSize == COLSPAN_SIZE) {
			code.append("<h:tr>\n");
			for (int i = 0; i < allSize; i++) {
				td(tempList, code, 0, i);
			}
			code.append("</h:tr>\n");
		} else if (allSize < COLSPAN_SIZE) {
			code.append("<h:tr>\n");
			for (int i = 0; i < allSize; i++) {
				td(tempList, code, (COLSPAN_SIZE - allSize) * 2 + 1, i);
			}
			code.append("</h:tr>\n");
		} else {
			int times = (int) Math.floor(allSize / COLSPAN_SIZE);
			int tdNum = 0;
			for (int i = 0; i < times; i++) {
				code.append("<h:tr>\n");
				for (int j = tdNum; j < tdNum + COLSPAN_SIZE; j++) {
					td(tempList, code, 0, j);
				}
				tdNum += COLSPAN_SIZE;
				code.append("</h:tr>\n");
			}
			int surplus = 0;
			int lastNum = allSize - times * COLSPAN_SIZE;
			if (lastNum != 0) {
				surplus = COLSPAN_SIZE - lastNum;
				code.append("<h:tr>\n");
				for (int i = tdNum; i < allSize; i++) {
					td(tempList, code, surplus * 2 + 1, i);
				}
				code.append("</h:tr>\n");
			}
		}

		code.append("\n");
		for (Table table : list) {
			if (table.getName().equals(tableName)) {
				if (!table.getKey().equals("PRI")) {
					if (!table.getColumn().equals("create_date") && !table.getColumn().equals("update_date")
							&& !table.getColumn().equals("create_user") && !table.getColumn().equals("update_user")) {
						code.append("private Label " + getFirstWordLower(table.getColumn()) + "Label;\n");
						code.append("//" + table.getComment() + "\n");
						code.append("private Textbox " + getFirstWordLower(table.getColumn()) + "Textbox;\n");
					}
				}
			}
		}
		code.append("\n");
		code.append("private Button add" + getFirstWordCapital(tableName) + "Button;\n");
		code.append("private Button save" + getFirstWordCapital(tableName) + "Button;\n");
		code.append("private Button edit" + getFirstWordCapital(tableName) + "Button;\n");
		code.append("private Button cancel" + getFirstWordCapital(tableName) + "Button;\n");
		code.append("private Button delete" + getFirstWordCapital(tableName) + "Button;\n");
		code.append("\n");

		code.append("private int " + getFirstWordLower(tableName) + "Id=0;\n");
		code.append("\n");

		code.append(
				"<button id=\"add" + getFirstWordCapital(tableName) + "Button\" visible=\"false\" label=\"增加\" />\n");
		code.append(
				"<button id=\"save" + getFirstWordCapital(tableName) + "Button\" visible=\"false\" label=\"保存\" />\n");
		code.append(
				"<button id=\"edit" + getFirstWordCapital(tableName) + "Button\" visible=\"false\" label=\"编辑\" />\n");
		code.append("<button id=\"cancel" + getFirstWordCapital(tableName)
				+ "Button\" visible=\"false\" label=\"取消\" />\n");
		code.append("<button id=\"delete" + getFirstWordCapital(tableName)
				+ "Button\" visible=\"false\" label=\"刪除\" />\n");
		code.append("\n");

		code.append("public void clear" + getFirstWordCapital(tableName) + "Textbox(){\n");
		for (Table table : list) {
			if (table.getName().equals(tableName)) {
				if (!table.getKey().equals("PRI")) {
					if (!table.getColumn().equals("create_date") && !table.getColumn().equals("update_date")
							&& !table.getColumn().equals("create_user") && !table.getColumn().equals("update_user")) {
						code.append("" + getFirstWordLower(table.getColumn()) + "Label.setValue(\"\");\n");
						code.append("" + getFirstWordLower(table.getColumn()) + "Textbox.setValue(\"\");\n");
					}
				}
			}
		}
		code.append("}\n");
		code.append("public void set" + getFirstWordCapital(tableName) + "Value(" + getFirstWordCapital(tableName) + " "
				+ getFirstWordLower(tableName) + "){\n");
		for (Table table : list) {
			if (table.getName().equals(tableName)) {
				if (!table.getKey().equals("PRI")) {
					if (!table.getColumn().equals("create_date") && !table.getColumn().equals("update_date")
							&& !table.getColumn().equals("create_user") && !table.getColumn().equals("update_user")) {
						code.append("" + getFirstWordLower(tableName) + ".set" + getFirstWordCapital(table.getColumn())
								+ "(" + getFirstWordLower(table.getColumn()) + "Textbox.getValue());\n");
					}
				}
			}
		}
		code.append("}\n");
		code.append("public void set" + getFirstWordCapital(tableName) + "Data(" + getFirstWordCapital(tableName) + " "
				+ getFirstWordLower(tableName) + "){\n");
		for (Table table : list) {
			if (table.getName().equals(tableName)) {
				if (!table.getKey().equals("PRI")) {
					if (!table.getColumn().equals("create_date") && !table.getColumn().equals("update_date")
							&& !table.getColumn().equals("create_user") && !table.getColumn().equals("update_user")) {
						code.append("" + getFirstWordLower(table.getColumn()) + "Label.setValue("
								+ getFirstWordLower(tableName) + ".get" + getFirstWordCapital(table.getColumn())
								+ "());\n");
						code.append("" + getFirstWordLower(table.getColumn()) + "Textbox.setValue("
								+ getFirstWordLower(tableName) + ".get" + getFirstWordCapital(table.getColumn())
								+ "());\n");
					}
				}
			}
		}
		code.append("}\n");
		code.append("\n");
		code.append("public void enable" + getFirstWordCapital(tableName) + "Button(String type) {\n");
		code.append("if (type.equals(\"add\")) {\nadd" + getFirstWordCapital(tableName)
				+ "Button.setVisible(false);\nedit" + getFirstWordCapital(tableName) + "Button.setVisible(false);\nsave"
				+ getFirstWordCapital(tableName) + "Button.setVisible(true);\ncancel" + getFirstWordCapital(tableName)
				+ "Button.setVisible(true);\n}\n");
		code.append("if (type.equals(\"update\")) {\nadd" + getFirstWordCapital(tableName)
				+ "Button.setVisible(false);\nedit" + getFirstWordCapital(tableName) + "Button.setVisible(false);\nsave"
				+ getFirstWordCapital(tableName) + "Button.setVisible(true);\ncancel" + getFirstWordCapital(tableName)
				+ "Button.setVisible(true);\n}\n");
		code.append("if (type.equals(\"cancel\")) {\nadd" + getFirstWordCapital(tableName)
				+ "Button.setVisible(true);\nedit" + getFirstWordCapital(tableName) + "Button.setVisible(false);\nsave"
				+ getFirstWordCapital(tableName) + "Button.setVisible(false);\ncancel" + getFirstWordCapital(tableName)
				+ "Button.setVisible(false);\n}\n");
		code.append("if (type.equals(\"save\")) {\nadd" + getFirstWordCapital(tableName)
				+ "Button.setVisible(true);\nedit" + getFirstWordCapital(tableName) + "Button.setVisible(false);\nsave"
				+ getFirstWordCapital(tableName) + "Button.setVisible(false);\ncancel" + getFirstWordCapital(tableName)
				+ "Button.setVisible(false);\n}\n");
		code.append("if (type.equals(\"select\")) {\nadd" + getFirstWordCapital(tableName)
				+ "Button.setVisible(true);\nedit" + getFirstWordCapital(tableName) + "Button.setVisible(true);\nsave"
				+ getFirstWordCapital(tableName) + "Button.setVisible(false);\ncancel" + getFirstWordCapital(tableName)
				+ "Button.setVisible(false);\n}\n");
		code.append("}\n");

		code.append("public void enable" + getFirstWordCapital(tableName) + "Textbox(boolean flag){\n");
		for (Table table : list) {
			if (table.getName().equals(tableName)) {
				if (!table.getKey().equals("PRI")) {
					if (!table.getColumn().equals("create_date") && !table.getColumn().equals("update_date")
							&& !table.getColumn().equals("create_user") && !table.getColumn().equals("update_user")) {
						code.append("" + getFirstWordLower(table.getColumn()) + "Label.setVisible(!flag);\n");
						code.append("" + getFirstWordLower(table.getColumn()) + "Textbox.setVisible(flag);\n");
					}
				}
			}
		}
		code.append("}\n");
		code.append("class " + getFirstWordCapital(tableName) + "Renderer implements ListitemRenderer {\n");
		code.append("public void render(Listitem arg0, Object arg1, int arg2) throws Exception {\n");
		code.append("" + getFirstWordCapital(tableName) + " " + getFirstWordLower(tableName) + " = ("
				+ getFirstWordCapital(tableName) + ") arg1;\n");
		for (Table table : list) {
			if (table.getName().equals(tableName)) {
				if (!table.getKey().equals("PRI")) {
					if (!table.getColumn().equals("create_date") && !table.getColumn().equals("update_date")
							&& !table.getColumn().equals("create_user") && !table.getColumn().equals("update_user")) {
						code.append("Listcell " + getFirstWordLower(table.getColumn()) + "Cell = new Listcell();\n");
						code.append("" + getFirstWordLower(table.getColumn()) + "Cell.setParent(arg0);\n");
						code.append("new Label(" + getFirstWordLower(tableName) + ".get"
								+ getFirstWordCapital(table.getColumn()) + "()).setParent("
								+ getFirstWordLower(table.getColumn()) + "Cell);\n");
					}
				}
			}
		}
		code.append("arg0.setValue(" + getFirstWordLower(tableName) + ");\n");
		code.append("}\n");
		code.append("}\n");
		code.append("\n");
		for (Table table : list) {
			if (table.getName().equals(tableName)) {
				if (!table.getKey().equals("PRI")) {
					if (!table.getColumn().equals("create_date") && !table.getColumn().equals("update_date")
							&& !table.getColumn().equals("create_user") && !table.getColumn().equals("update_user")) {
						code.append("<listheader label=\"" + table.getComment() + "\" />\n");
					}
				}
			}
		}
		code.append("\n");
		// check
		code.append("public boolean check" + getFirstWordCapital(tableName) + "Input() {\n");
		code.append("boolean flag = true;\n");
		for (Table table : list) {
			if (table.getName().equals(tableName)) {
				if (!table.getKey().equals("PRI")) {
					if (!table.getColumn().equals("create_date") && !table.getColumn().equals("update_date")
							&& !table.getColumn().equals("create_user") && !table.getColumn().equals("update_user")
							&& !table.getColumn().equals("remark")) {
						code.append(
								"if (" + getFirstWordLower(table.getColumn()) + "Textbox.getValue().equals(\"\")) {\n");
						code.append("" + getFirstWordLower(table.getColumn()) + "Textbox.focus();\n");
						code.append("Messagebox.show(\"" + getFirstWordLower(table.getComment()) + "不能为空！\");\n");
						code.append("flag = false;\n");
						code.append("}\n");
					}
				}
			}
		}
		code.append("return flag;\n");
		code.append("}\n");

		// add
		code.append("public void onClick$add" + getFirstWordCapital(tableName) + "Button() {\n");
		code.append("" + getFirstWordLower(tableName) + "Id = 0;\n");
		code.append("enable" + getFirstWordCapital(tableName) + "Textbox(true);\n");
		code.append("enable" + getFirstWordCapital(tableName) + "Button(\"add\");\n");
		code.append("clear" + getFirstWordCapital(tableName) + "Textbox();\n");
		code.append("}\n");
		code.append("\n");
		// edit
		code.append("public void onClick$save" + getFirstWordCapital(tableName) + "Button(){\n");
		code.append("if (" + getFirstWordLower(tableName) + "Id == 0) {\n");
		code.append("" + getFirstWordLower(tableName) + " = new xxxxxxxx();\n");
		code.append("set" + getFirstWordCapital(tableName) + "Value(" + getFirstWordLower(tableName) + ");\n");
		code.append("" + getFirstWordLower(tableName) + "List.add(" + getFirstWordLower(tableName) + ");\n");
		code.append("} else {\n");
		code.append("set" + getFirstWordCapital(tableName) + "Value(" + getFirstWordLower(tableName) + ");\n");
		code.append("}\n");
		code.append("" + getFirstWordLower(tableName) + "Listbox.setModel(new ListModelList<>("
				+ getFirstWordLower(tableName) + "List, true));\n");
		code.append("enable" + getFirstWordCapital(tableName) + "Textbox(false);\n");
		code.append("enable" + getFirstWordCapital(tableName) + "Button(\"save\");\n");
		code.append("clear" + getFirstWordCapital(tableName) + "Textbox();\n");
		code.append("}\n");
		code.append("\n");
		// del
		code.append("public void onClick$edit" + getFirstWordCapital(tableName) + "Button() {\n");
		code.append("" + getFirstWordLower(tableName) + "Id = -1;\n");
		code.append("enable" + getFirstWordCapital(tableName) + "Textbox(true);\n");
		code.append("enable" + getFirstWordCapital(tableName) + "Button(\"update\");\n");
		code.append("}\n");
		code.append("\n");
		// cancer
		code.append("public void onClick$cancel" + getFirstWordCapital(tableName) + "Button() {\n");
		code.append("enable" + getFirstWordCapital(tableName) + "Button(\"cancel\");\n");
		code.append("enable" + getFirstWordCapital(tableName) + "Textbox(false);\n");
		code.append("clear" + getFirstWordCapital(tableName) + "Textbox();\n");
		code.append("}\n");
		code.append("\n");
		// save
		code.append("public void onClick$delete" + getFirstWordCapital(tableName) + "Button() {\n");
		code.append("del" + getFirstWordCapital(tableName) + "List.add(" + getFirstWordLower(tableName) + ");\n");
		code.append("" + getFirstWordLower(tableName) + "List.remove(" + getFirstWordLower(tableName) + ");\n");
		code.append("" + getFirstWordLower(tableName) + "Listbox.setModel(new ListModelList<>("
				+ getFirstWordLower(tableName) + "List, true));\n");
		code.append("clear" + getFirstWordCapital(tableName) + "Textbox();\n");
		code.append("enable" + getFirstWordCapital(tableName) + "Button(\"cancel\");\n");
		code.append("}\n");
		code.append("\n");

		// select

		code.append("public void onSelect$" + getFirstWordLower(tableName) + "Listbox() {\n");
		code.append("" + getFirstWordLower(tableName) + "=" + getFirstWordLower(tableName)
				+ "Listbox.getSelectedItem().getValue();\n");
		code.append("set" + getFirstWordCapital(tableName) + "Data(" + getFirstWordLower(tableName) + ");\n");
		code.append("if (" + getFirstWordLower(tableName) + "Id == 2)\n");
		code.append("return;\n");
		code.append("enable" + getFirstWordCapital(tableName) + "Textbox(false);\n");
		code.append("enable" + getFirstWordCapital(tableName) + "Button(\"select\");\n");
		code.append("}\n");
		code.append("\n");

		File file = new File("/home/mjy/Desktop/auxiliaryCode.txt");
		createFile(file, code.toString());
	}

	/**
	 * @throws IOException
	 * @category 创建实体类
	 */
	private void generatorBean(List<Table> list) throws IOException {
		StringBuilder code = new StringBuilder();
		/*********************** 导入包 **********************/
		code.append("package com.jyd.bms.bean;\n");
		code.append("import javax.persistence.Entity;\n");
		code.append("import java.io.Serializable;\n");
		for (Table table : list) {
			if (table.getName().equals(tableName)) {
				if (table.getType().equals("date")) {
					if (dataFlag) {
						code.append("import java.util.Date;\n");
						dataFlag = false;
					}
				}
				if (table.getType().equals("datetime")) {
					if (timeStampflag) {
						code.append("import java.sql.Timestamp;\n");
						timeStampflag = false;
					}

				}
			}
		}
		code.append("/**\n");
		code.append(" * @category Generated " + sdf.format(new Date()) + " by GeneratedTool\n");
		code.append(" * @author mjy\n");
		code.append(" */\n");
		code.append("@Entity\n");
		code.append("public class " + getFirstWordCapital(tableName) + " implements Serializable {\n");
		code.append("private int id;\n");

		for (Table table : list) {
			if (table.getName().equals(tableName) && !table.getKey().equals("PRI")) {
				/*********************** 定义字段 **********************/
				code.append("private ");
				code.append(getSwithType(table));
				String[] arrs = table.getColumn().split("_");
				if (arrs.length > 1) {
					code.append(getFirstWordLower(table.getColumn()));
				} else {
					code.append(table.getColumn());
				}
				code.append(";");
				code.append("// " + table.getComment() + "\n");
			}
		}
		for (Table table : list) {
			if (table.getName().equals(tableName) && !table.getKey().equals("PRI")) {
				/*********************** get方法 **********************/
				code.append("public ");
				code.append(getSwithType(table));
				code.append("get" + getFirstWordCapital(table.getColumn()) + "(){\n");
				code.append(" return " + getFirstWordLower(table.getColumn()) + ";\n");
				code.append("}\n");
				code.append("\n");
			}
		}
		code.append("public int getId() {\n");
		code.append(" return id;\n");
		code.append("}\n");

		for (Table table : list) {
			if (table.getName().equals(tableName) && !table.getKey().equals("PRI")) {
				/*********************** set方法 **********************/
				code.append("public void ");
				code.append("set" + getFirstWordCapital(table.getColumn()) + "(");
				code.append(getSwithType(table));
				code.append(getFirstWordLower(table.getColumn()));
				code.append("){\n");
				code.append(" this." + getFirstWordLower(table.getColumn()) + "=" + getFirstWordLower(table.getColumn())
						+ ";\n");
				code.append("}\n");
				code.append("\n");
			}
		}
		code.append("public void setId(int id) {\n");
		code.append(" this.id = id;\n");
		code.append("}\n");
		// 最后大括号结尾
		code.append("}");
		/******************* 生成文件 **************************/
		File file = new File(getRootPath() + "/src/com/jyd/bms/bean/" + getFirstWordCapital(tableName) + ".java");
		createFile(file, code.toString());
		System.out.println("创建Bean层文件成功！");
	}

	/**
	 * @throws IOException
	 * @category 创建DAO
	 */
	private void generatorDao() throws IOException {
		StringBuilder code = new StringBuilder();
		code.append("package com.jyd.bms.dao;\n");
		code.append("import java.util.List;\n");
		code.append("import com.jyd.bms.bean." + getFirstWordCapital(tableName) + ";\n");
		code.append("import com.jyd.bms.tool.exception.DAOException;\n");
		code.append("/**\n");
		code.append(" * @category Generated " + sdf.format(new Date()) + " by GeneratedTool\n");
		code.append(" * @author mjy\n");
		code.append(" */\n");
		code.append("public interface " + getFirstWordCapital(tableName) + "DAO extends HibernateBase<"
				+ getFirstWordCapital(tableName) + "> {\n");
		code.append("\tpublic int get" + getFirstWordCapital(tableName) + "");
		code.append("Count(String condition) throws DAOException;\n");
		code.append("\tpublic List<" + getFirstWordCapital(tableName) + "> getPaging" + getFirstWordCapital(tableName)
				+ "");
		code.append("(int firstResult, int maxResults, String condition)throws DAOException;\n");
		code.append("\tpublic List<" + getFirstWordCapital(tableName) + "> getAll" + getFirstWordCapital(tableName)
				+ "() throws DAOException;\n");
		code.append("}");
		File file = new File(getRootPath() + "/src/com/jyd/bms/dao/" + getFirstWordCapital(tableName) + "DAO.java");
		createFile(file, code.toString());
		System.out.println("创建DAO层文件成功！");
	}

	/**
	 * @throws IOException
	 * @category 创建DAO具体实现
	 */
	private void generatorImpl(List<Table> list) throws IOException {
		StringBuilder code = new StringBuilder();
		code.append("package com.jyd.bms.dao.impl;\n");
		code.append("import java.util.HashMap;\n");
		code.append("import java.util.List;\n");
		code.append("import java.util.Map;\n");
		code.append("import org.springframework.stereotype.Repository;\n");
		code.append("import com.jyd.bms.tool.exception.DAOException;\n");
		code.append("import com.jyd.bms.bean." + getFirstWordCapital(tableName) + ";\n");
		code.append("import com.jyd.bms.dao." + getFirstWordCapital(tableName) + "DAO;\n");
		code.append("/**\n");
		code.append(" * @category Generated " + sdf.format(new Date()) + " by GeneratedTool\n");
		code.append(" * @author mjy\n");
		code.append(" */\n");
		code.append("@Repository\n");
		code.append("public class " + getFirstWordCapital(tableName) + "DAOImpl extends HibernateBaseTemplate<"
				+ getFirstWordCapital(tableName) + "> implements " + getFirstWordCapital(tableName) + "DAO {\n");
		code.append("\n");
		code.append(
				" public int get" + getFirstWordCapital(tableName) + "Count(String condition) throws DAOException {\n");
		code.append("\tString hql = \"\";\n");
		code.append("\tif (condition.equals(\"\")) {\n");
		code.append("\thql = \"select count(*) from " + getFirstWordCapital(tableName) + "\";\n");
		code.append("\tList<Long> lstCount = super.getQueryResult(hql);\n");
		code.append("\treturn lstCount.get(0).intValue();\n");
		code.append("\t} else {\n");
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getName().equals(tableName)) {
				if (list.get(i).getKey().equals("PRI")) {
					code.append("\thql = \"select count(*) from " + getFirstWordCapital(tableName) + " where "
							+ getFirstWordLower(list.get(++i).getColumn()) + " like :condition\";\n");
				}
			}
		}
		code.append("\tMap map = new HashMap();\n");
		code.append("\tmap.put(\"condition\", \"%\" + condition + \"%\");\n");
		code.append("\tList<Long> lstCount = super.getQueryResult(hql, map);\n");
		code.append("\treturn lstCount.get(0).intValue();\n");
		code.append("\t}\n");
		code.append(" }\n");
		code.append("\n");
		code.append(" public List<" + getFirstWordCapital(tableName) + "> getPaging" + getFirstWordCapital(tableName)
				+ "(int firstResult, int maxResults, String condition)throws DAOException {\n");
		code.append("\tString hql = \"\";\n");
		code.append("\tMap map = new HashMap();\n");
		code.append("\tif (condition.equals(\"\")) {\n");
		code.append("\thql = \"from " + getFirstWordCapital(tableName) + "\";\n");
		code.append("\t} else {\n");
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getName().equals(tableName)) {
				if (list.get(i).getKey().equals("PRI")) {
					code.append("\thql = \"from " + getFirstWordCapital(tableName) + " where "
							+ getFirstWordLower(list.get(++i).getColumn()) + " like :condition\";\n");
				}
			}
		}
		code.append("\tmap.put(\"condition\", \"%\" + condition + \"%\");\n");
		code.append("\t}\n");
		code.append("\treturn super.getPagingQueryResult(hql.toString(), map, firstResult, maxResults);\n");
		code.append(" }\n");
		code.append("\n");
		code.append(" public List<" + getFirstWordCapital(tableName) + "> getAll" + getFirstWordCapital(tableName)
				+ "() throws DAOException {\n");
		code.append("\tString hql = \"\";\n");
		code.append("\thql = \"from " + getFirstWordCapital(tableName) + "\";\n");
		code.append("\treturn super.getQueryResult(hql.toString());\n");
		code.append(" }\n");
		code.append("}\n");
		File file = new File(
				getRootPath() + "/src/com/jyd/bms/dao/impl/" + getFirstWordCapital(tableName) + "DAOImpl.java");
		createFile(file, code.toString());
		System.out.println("创建DAOImpl成功！");
	}

	/**
	 * @throws IOException
	 * @category 创建服务层
	 */
	private void generatorService() throws IOException {
		StringBuilder code = new StringBuilder();
		code.append("package com.jyd.bms.service;\n");
		code.append("import java.util.List;\n");
		code.append("import org.springframework.beans.factory.annotation.Autowired;\n");
		code.append("import org.springframework.stereotype.Service;\n");
		code.append("import com.jyd.bms.bean." + getFirstWordCapital(tableName) + ";\n");
		code.append("import com.jyd.bms.dao." + getFirstWordCapital(tableName) + "DAO;\n");
		code.append("import com.jyd.bms.tool.exception.DAOException;\n");
		code.append("/**\n");
		code.append(" * @category Generated " + sdf.format(new Date()) + " by GeneratedTool\n");
		code.append(" * @author mjy\n");
		code.append(" */\n");
		code.append("@Service(\"" + getFirstWordCapital(tableName) + "Service\")\n");
		code.append("public class " + getFirstWordCapital(tableName) + "Service extends BaseService<"
				+ getFirstWordCapital(tableName) + "> {\n");
		code.append("@Autowired(required = true)\n");
		code.append("private " + getFirstWordCapital(tableName) + "DAO " + getFirstWordLower(tableName) + "DAO;\n");
		code.append("\n");
		code.append(
				" public int get" + getFirstWordCapital(tableName) + "Count(String condition) throws DAOException {\n");
		code.append("\treturn " + getFirstWordLower(tableName) + "DAO.get" + getFirstWordCapital(tableName)
				+ "Count(condition);\n");
		code.append(" }\n");
		code.append("\n");
		code.append(" public List<" + getFirstWordCapital(tableName) + "> getPaging" + getFirstWordCapital(tableName)
				+ "(int firstResult, int maxResults, String condition) throws DAOException {\n");
		code.append("\treturn " + getFirstWordLower(tableName) + "DAO.getPaging" + getFirstWordCapital(tableName)
				+ "(firstResult, maxResults, condition);\n");
		code.append(" }\n");
		code.append("\n");
		code.append(" public List<" + getFirstWordCapital(tableName) + "> getAll" + getFirstWordCapital(tableName)
				+ "() throws DAOException {\n");
		code.append(
				"\treturn " + getFirstWordLower(tableName) + "DAO.getAll" + getFirstWordCapital(tableName) + "();\n");
		code.append(" }\n");
		code.append(" @Override\n");
		code.append("\n");
		code.append(" public void setDAO() {\n");
		code.append("\tthis.baseDAO = " + getFirstWordLower(tableName) + "DAO;\n");
		code.append(" }\n");
		code.append("}\n");
		File file = new File(
				getRootPath() + "/src/com/jyd/bms/service/" + getFirstWordCapital(tableName) + "Service.java");
		createFile(file, code.toString());
		System.out.println("创建Service层成功！");
	}

	/**
	 * @throws IOException
	 * @category 创建配置文件
	 */
	private void generatorXml(List<Table> list) throws IOException {
		StringBuilder code = new StringBuilder();
		code.append("<?xml version=\"1.0\"?>\n");
		code.append("<!DOCTYPE hibernate-mapping PUBLIC \"-//Hibernate/Hibernate Mapping DTD 3.0//EN\"\n");
		code.append("\"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd\">\n");
		code.append("<!-- Generated " + sdf.format(new Date()) + " by GeneratedTool mjy -->\n");
		code.append("<hibernate-mapping>\n");
		code.append("	<class name=\"com.jyd.bms.bean." + getFirstWordCapital(tableName) + "\" table=\"" + tableName
				+ "\">\n");
		code.append("		<id name=\"id\" type=\"int\">\n");
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getName().equals(tableName)) {
				if (list.get(i).getKey().equals("PRI")) {
					code.append("			<column name=\"" + list.get(i).getColumn() + "\" />\n");
					code.append("			<generator class=\"identity\" />\n");
					code.append("		</id>\n");
				} else {
					code.append("		<property name=\"" + getFirstWordLower(list.get(i).getColumn()) + "\" ");
					code.append("type=\"" + getSwithTypeFormat(list.get(i)) + "\">\n");
					code.append("			<column name=\"" + list.get(i).getColumn() + "\" />\n");
					code.append("		</property>\n");
				}
			}
		}
		code.append("	</class>\n");
		code.append("</hibernate-mapping>\n");
		File file = new File(getRootPath() + "/src/hbm/" + getFirstWordCapital(tableName) + ".hbm.xml");
		createFile(file, code.toString());
		System.out.println("创建xml配置文件成功！");
	}

	/**
	 * @throws IOException
	 * @category 创建窗口
	 */
	private void generatorWindow(List<Table> list) throws IOException {
		StringBuilder code = new StringBuilder();
		code.append("package com.jyd.bms.window.basedata;\n");
		code.append("import java.sql.Timestamp;\n");
		code.append("import java.util.ArrayList;\n");
		code.append("import java.util.Iterator;\n");
		code.append("import java.util.List;\n");
		code.append("import org.slf4j.Logger;\n");
		code.append("import org.slf4j.LoggerFactory;\n");
		code.append("import org.zkoss.zk.ui.Component;\n");
		code.append("import org.zkoss.zk.ui.SuspendNotAllowedException;\n");
		code.append("import org.zkoss.zul.Button;\n");
		code.append("import org.zkoss.zul.Label;\n");
		code.append("import org.zkoss.zul.Listcell;\n");
		code.append("import org.zkoss.zul.Listitem;\n");
		code.append("import org.zkoss.zul.ListitemRenderer;\n");
		code.append("import org.zkoss.zul.South;\n");
		code.append("import org.zkoss.zul.Textbox;\n");
		code.append("import com.jyd.bms.bean." + getFirstWordCapital(tableName) + ";\n");
		code.append("import com.jyd.bms.bean.User;\n");
		code.append("import com.jyd.bms.service." + getFirstWordCapital(tableName) + "Service;\n");
		code.append("import com.jyd.bms.tool.exception.CreateException;\n");
		code.append("import com.jyd.bms.tool.exception.DAOException;\n");
		code.append("import com.jyd.bms.tool.exception.UpdateException;\n");
		code.append("import com.jyd.bms.tool.zk.BaseWindow;\n");
		code.append("import com.jyd.bms.tool.zk.GridPaging;\n");
		code.append("import com.jyd.bms.tool.zk.Listbox;\n");
		code.append("import com.jyd.bms.tool.zk.Messagebox;\n");
		code.append("import com.jyd.bms.tool.zk.PagingControlComponentModelList;\n");
		code.append("import com.jyd.bms.tool.zk.UserSession;\n");
		code.append("/**\n");
		code.append(" * @category Generated " + sdf.format(new Date()) + " by GeneratedTool\n");
		code.append(" * @author mjy\n");
		code.append(" */\n");
		code.append("public class " + getFirstWordCapital(tableName) + "Window extends BaseWindow {\n");
		code.append(
				"private Button addButton;\nprivate Button editButton;\nprivate Button cancelButton;\nprivate Button saveButton;\nprivate GridPaging gridPaging;\n");
		code.append(
				"private Textbox conditionTextbox;\nprivate String condition = \"\";\nprivate South southPaging;\n");
		for (Table table : list) {
			if (table.getName().equals(tableName)) {
				if (!table.getKey().equals("PRI")) {
					if (!table.getColumn().equals("create_date") && !table.getColumn().equals("update_date")
							&& !table.getColumn().equals("create_user") && !table.getColumn().equals("update_user")) {
						code.append("private Label " + getFirstWordLower(table.getColumn()) + "Label;\n");
						code.append("private Textbox " + getFirstWordLower(table.getColumn()) + "Textbox;\n");
					}
				}
			}
		}
		code.append("private Listbox " + getFirstWordLower(tableName) + "Listbox;\n");

		code.append("private " + getFirstWordCapital(tableName) + " " + getFirstWordLower(tableName) + ";\n");
		code.append(
				"private " + getFirstWordCapital(tableName) + "Service " + getFirstWordLower(tableName) + "Service;\n");
		code.append("private List<" + getFirstWordCapital(tableName) + "> " + getFirstWordLower(tableName)
				+ "list = new ArrayList<" + getFirstWordCapital(tableName) + ">();\n");
		code.append("private static final Logger log = LoggerFactory.getLogger(" + getFirstWordCapital(tableName)
				+ "Window.class);\n");

		code.append("private int edit=0;\n");
		// code.append(
		// "public " + getFirstWordCapital(tableName) + "Window() {\nthis.menuId
		// = \"" + tableName + "\";\n}\n");
		code.append(
				"public " + getFirstWordCapital(tableName) + "Window() {\nthis.menuId = \"" + tableName + "\";\n}\n");
		code.append("public Listitem getSelectItem() {\nreturn " + getFirstWordLower(tableName)
				+ "Listbox.getSelectedItem();\n}\n");
		code.append("public void initUI() {\n" + getFirstWordLower(tableName) + "Service = getBean(\""
				+ getFirstWordCapital(tableName) + "Service\");\n" + getFirstWordLower(tableName)
				+ "Listbox.setItemRenderer(new " + getFirstWordCapital(tableName) + "Renderer());\n}\n");
		code.append("@Override\n");
		code.append("public void initData() {\n");
		code.append("try {\n");
		code.append("PagingControlComponentModelList pagingModelList = new PagingControlComponentModelList("
				+ getFirstWordLower(tableName) + "Service,\"getPaging" + getFirstWordCapital(tableName)
				+ "\", new Object[] { condition });\n");
		code.append("if (gridPaging == null) {\n");
		code.append("gridPaging = new GridPaging(" + getFirstWordLower(tableName) + "Listbox, pagingModelList, 20);\n");
		code.append("} else {\n");
		code.append("gridPaging.setPagingControlComponentModel(pagingModelList, 20);\n");
		code.append("}\n");
		code.append("gridPaging.setTotalSize(" + getFirstWordLower(tableName) + "Service.get"
				+ getFirstWordCapital(tableName) + "Count(condition));\n");
		code.append("gridPaging.setDetailed(true);gridPaging.setParent(southPaging);\n");
		code.append("} catch (DAOException e) {\nlog.error(\"" + getFirstWordCapital(tableName)
				+ "Window\", e);\nMessagebox.error(\"获取数据出错了!\");\n} catch (Exception e) {\nlog.error(\"AssetsTypeWindow\", e);\nMessagebox.error(\"未知错误！\");\n}\n}\n");
		code.append(
				"public void onClick$searchButton() throws SuspendNotAllowedException, InterruptedException {\ncondition = conditionTextbox.getValue();\ninitData();\n}\n");
		code.append(
				"public void onOKsearchButton() throws SuspendNotAllowedException, InterruptedException {\nonClick$searchButton();\n}\n");
		code.append("public void enableTextbox(boolean flag){\n");
		for (Table table : list) {
			if (table.getName().equals(tableName)) {
				if (!table.getKey().equals("PRI")) {
					if (!table.getColumn().equals("create_date") && !table.getColumn().equals("update_date")
							&& !table.getColumn().equals("create_user") && !table.getColumn().equals("update_user")) {
						code.append("" + getFirstWordLower(table.getColumn()) + "Label.setVisible(!flag);\n");
						code.append("" + getFirstWordLower(table.getColumn()) + "Textbox.setVisible(flag);\n");
					}
				}
			}
		}
		code.append("}\n");
		code.append("public void enableButton(String type) {\n");
		code.append(
				"if (type.equals(\"add\")) {\naddButton.setVisible(false);\neditButton.setVisible(false);\nsaveButton.setVisible(true);\ncancelButton.setVisible(true);\n}\n");
		code.append(
				"if (type.equals(\"update\")) {\naddButton.setVisible(false);\neditButton.setVisible(false);\nsaveButton.setVisible(true);\ncancelButton.setVisible(true);\n}\n");
		code.append(
				"if (type.equals(\"cancel\")) {\naddButton.setVisible(true);\neditButton.setVisible(false);\nsaveButton.setVisible(false);\ncancelButton.setVisible(false);\n}\n");
		code.append(
				"if (type.equals(\"save\")) {\naddButton.setVisible(true);\neditButton.setVisible(false);\nsaveButton.setVisible(false);\ncancelButton.setVisible(false);\n}\n");
		code.append(
				"if (type.equals(\"select\")) {\naddButton.setVisible(true);\neditButton.setVisible(true);\nsaveButton.setVisible(false);\ncancelButton.setVisible(false);\n}\n");
		code.append("}\n");
		code.append("public void clearTextbox(){\n");
		for (Table table : list) {
			if (table.getName().equals(tableName)) {
				if (!table.getKey().equals("PRI")) {
					if (!table.getColumn().equals("create_date") && !table.getColumn().equals("update_date")
							&& !table.getColumn().equals("create_user") && !table.getColumn().equals("update_user")) {
						code.append("" + getFirstWordLower(table.getColumn()) + "Label.setValue(\"\");\n");
						code.append("" + getFirstWordLower(table.getColumn()) + "Textbox.setValue(\"\");\n");
					}
				}
			}
		}
		code.append("}\n");
		code.append("public void set" + getFirstWordCapital(tableName) + "Value(" + getFirstWordCapital(tableName) + " "
				+ getFirstWordLower(tableName) + "){\n");
		for (Table table : list) {
			if (table.getName().equals(tableName)) {
				if (!table.getKey().equals("PRI")) {
					if (!table.getColumn().equals("create_date") && !table.getColumn().equals("update_date")
							&& !table.getColumn().equals("create_user") && !table.getColumn().equals("update_user")) {
						code.append("" + getFirstWordLower(tableName) + ".set" + getFirstWordCapital(table.getColumn())
								+ "(" + getFirstWordLower(table.getColumn()) + "Textbox.getValue());\n");
					}
				}
			}
		}
		code.append("}\n");
		code.append("public void set" + getFirstWordCapital(tableName) + "Data(" + getFirstWordCapital(tableName) + " "
				+ getFirstWordLower(tableName) + "){\n");
		for (Table table : list) {
			if (table.getName().equals(tableName)) {
				if (!table.getKey().equals("PRI")) {
					if (!table.getColumn().equals("create_date") && !table.getColumn().equals("update_date")
							&& !table.getColumn().equals("create_user") && !table.getColumn().equals("update_user")) {
						code.append("" + getFirstWordLower(table.getColumn()) + "Label.setValue("
								+ getFirstWordLower(tableName) + ".get" + getFirstWordCapital(table.getColumn())
								+ "());\n");
						code.append("" + getFirstWordLower(table.getColumn()) + "Textbox.setValue("
								+ getFirstWordLower(tableName) + ".get" + getFirstWordCapital(table.getColumn())
								+ "());\n");
					}
				}
			}
		}
		code.append("}\n");
		code.append("public void onSelect$" + getFirstWordLower(tableName)
				+ "Listbox() throws SuspendNotAllowedException, InterruptedException {\n");
		code.append("edit = -1;\n");
		code.append("" + getFirstWordLower(tableName) + " = getSelectItem().getValue();\n");
		code.append("clearTextbox();\n");
		code.append("set" + getFirstWordCapital(tableName) + "Data(" + getFirstWordLower(tableName) + ");\n");
		code.append("enableTextbox(false);\n");
		code.append("enableButton(\"select\");\n");
		code.append("}\n");
		code.append("public void onClick$cancelButton() {\n");
		code.append("enableButton(\"cancel\");\n");
		code.append("enableTextbox(false);\n");
		code.append("clearTextbox();\n");
		code.append("}\n");
		code.append("public void onClick$addButton() {\n");
		code.append("edit = 0;\n");
		code.append("enableTextbox(true);\n");
		code.append("enableButton(\"add\");\n");
		code.append("clearTextbox();\n");
		code.append("}\n");
		code.append("public void onClick$editButton() {\n");
		code.append("edit = -1;\n");
		code.append("enableTextbox(true);\n");
		code.append("enableButton(\"update\");\n");
		code.append("}\n");
		code.append("public boolean checkInput() {\n");
		code.append("boolean flag = true;\n");
		for (Table table : list) {
			if (table.getName().equals(tableName)) {
				if (!table.getKey().equals("PRI")) {
					if (!table.getColumn().equals("create_date") && !table.getColumn().equals("update_date")
							&& !table.getColumn().equals("create_user") && !table.getColumn().equals("update_user")
							&& !table.getColumn().equals("remark")) {
						code.append(
								"if (" + getFirstWordLower(table.getColumn()) + "Textbox.getValue().equals(\"\")) {\n");
						code.append("" + getFirstWordLower(table.getColumn()) + "Textbox.focus();\n");
						code.append("Messagebox.show(\"" + getFirstWordLower(table.getComment()) + "不能为空！\");\n");
						code.append("flag = false;\n");
						code.append("}\n");
					}
				}
			}
		}
		code.append("return flag;\n");
		code.append("}\n");

		code.append("public void onClick$saveButton() {\n");
		code.append("try {\n");
		code.append("User sessionUser = UserSession.getUser();\n");
		code.append("if (sessionUser == null) {\n");
		code.append("Messagebox.show(\"长时间未操作，出于安全考虑，请重新登陆！\");\n");
		code.append("return;\n");
		code.append("}\n");
		code.append("Timestamp date = new Timestamp(System.currentTimeMillis());\n");
		code.append("String user = sessionUser.getLoginName();\n");
		code.append("if (!checkInput()) {\n");
		code.append("return;\n");
		code.append("}\n");
		code.append("if (edit == 0) {\n");
		code.append("" + getFirstWordLower(tableName) + " = new " + getFirstWordCapital(tableName) + "();\n");
		code.append("set" + getFirstWordCapital(tableName) + "Value(" + getFirstWordLower(tableName) + ");\n");
		code.append("" + getFirstWordLower(tableName) + ".setCreateDate(date);\n");
		code.append("" + getFirstWordLower(tableName) + ".setCreateUser(user);\n");
		code.append("" + getFirstWordLower(tableName) + ".setUpdateDate(date);\n");
		code.append("" + getFirstWordLower(tableName) + ".setUpdateUser(user);\n");
		code.append("" + getFirstWordLower(tableName) + "Service.add(" + getFirstWordLower(tableName) + ");\n");
		code.append("} else {\n");
		code.append("set" + getFirstWordCapital(tableName) + "Value(" + getFirstWordLower(tableName) + ");\n");
		code.append("" + getFirstWordLower(tableName) + ".setUpdateDate(date);\n");
		code.append("" + getFirstWordLower(tableName) + ".setUpdateUser(user);\n");
		code.append("" + getFirstWordLower(tableName) + "Service.update(" + getFirstWordLower(tableName) + ");\n");
		code.append("}\n");
		code.append("onClick$cancelButton() ;\ninitData();\n");
		code.append("} catch (CreateException e) {\n");
		code.append("log.error(\"" + getFirstWordLower(tableName) + "Window\", e);\n");
		code.append("} catch (UpdateException e) {\n");
		code.append("log.error(\"" + getFirstWordLower(tableName) + "Window\", e);\n");
		code.append("}\n");
		code.append("}\n");
		code.append("\n");
		code.append("class " + getFirstWordCapital(tableName) + "Renderer implements ListitemRenderer {\n");
		code.append("public void render(Listitem arg0, Object arg1, int arg2) throws Exception {\n");
		code.append("" + getFirstWordCapital(tableName) + " " + getFirstWordLower(tableName) + " = ("
				+ getFirstWordCapital(tableName) + ") arg1;\n");
		for (Table table : list) {
			if (table.getName().equals(tableName)) {
				if (!table.getKey().equals("PRI")) {
					if (!table.getColumn().equals("create_date") && !table.getColumn().equals("update_date")
							&& !table.getColumn().equals("create_user") && !table.getColumn().equals("update_user")) {
						code.append("Listcell " + getFirstWordLower(table.getColumn()) + "Cell = new Listcell();\n");
						code.append("" + getFirstWordLower(table.getColumn()) + "Cell.setParent(arg0);\n");
						code.append("new Label(" + getFirstWordLower(tableName) + ".get"
								+ getFirstWordCapital(table.getColumn()) + "()).setParent("
								+ getFirstWordLower(table.getColumn()) + "Cell);\n");
					}
				}
			}
		}
		code.append("arg0.setValue(" + getFirstWordLower(tableName) + ");\n");
		code.append("}\n");
		code.append("}\n");
		code.append("}\n");
		File file = new File(
				getRootPath() + "/src/com/jyd/bms/window/basedata/" + getFirstWordCapital(tableName) + "Window.java");
		createFile(file, code.toString());
		System.out.println("创建Window文件成功！");
	}

	/**
	 * @throws IOException
	 * @category 创建页面
	 */
	private void generatorZul(List<Table> list) throws IOException {
		StringBuilder code = new StringBuilder();
		code.append("<?page contentType=\"text/html;charset=UTF-8\"?>\n");
		code.append("<zk xmlns:h=\"xhtml\" xmlns:zk=\"zk\">\n");
		code.append("<window border=\"normal\" width=\"100%\" height=\"100%\"\n");
		code.append("use=\"com.jyd.bms.window.basedata." + getFirstWordCapital(tableName) + "Window\">\n");
		code.append("<script src=\"/script/jquery-1.4.1.js\" type=\"text/javascript\" />\n");
		code.append("<script src=\"/script/enter.js\" type=\"text/javascript\" />\n");
		code.append("<borderlayout width=\"100%\" height=\"100%\">\n");
		code.append("<center>\n");
		code.append("<borderlayout width=\"100%\" height=\"100%\">\n");
		code.append("<north style=\"background:#F0FAFF\">\n");
		code.append("<hbox pack=\"right\" width=\"100%\">\n");
		code.append("<hbox>\n");
		code.append("<textbox id=\"conditionTextbox\" width=\"200px\" />\n");
		code.append("<button label=\"查询\" id=\"searchButton\" />\n");
		code.append("</hbox>\n");
		code.append("</hbox>\n");
		code.append("</north>\n");
		code.append("<center flex=\"true\">\n");
		code.append(
				"<listbox id=\"" + getFirstWordLower(tableName) + "Listbox\" vflex=\"true\" fixedLayout=\"true\">\n");
		code.append("<listhead sizable=\"true\">\n");
		for (Table table : list) {
			if (table.getName().equals(tableName)) {
				if (!table.getKey().equals("PRI")) {
					if (!table.getColumn().equals("create_date") && !table.getColumn().equals("update_date")
							&& !table.getColumn().equals("create_user") && !table.getColumn().equals("update_user")) {
						code.append("<listheader label=\"" + table.getComment() + "\" />\n");
					}
				}
			}
		}
		code.append("</listhead>\n");
		code.append("</listbox>\n");
		code.append("</center>\n");
		code.append("<south id=\"southPaging\"></south>\n");
		code.append("</borderlayout>\n");
		code.append("</center>\n");
		code.append("<south height=\"100px\" splittable=\"true\">\n");
		code.append(
				"<borderlayout><center flex=\"true\"><h:table width=\"100%\" cellpadding=\"1\" cellspacing=\"1\" class=\"tableBorderDark\">\n");

		// int allSize = list.size();
		// if (allSize == COLSPAN_SIZE) {
		// code.append("<h:tr>\n");
		// for (int i = 0; i < allSize; i++) {
		// td(list, code, 0, i);
		// }
		// code.append("</h:tr>\n");
		// } else if (allSize < COLSPAN_SIZE) {
		// code.append("<h:tr>\n");
		// for (int i = 0; i < allSize; i++) {
		// td(list, code, (COLSPAN_SIZE - allSize) * 2 + 1, i);
		// }
		// code.append("</h:tr>\n");
		// } else {
		// int times = (int) Math.floor(allSize / COLSPAN_SIZE);
		// int tdNum = 0;
		// for (int i = 0; i < times; i++) {
		// code.append("<h:tr>\n");
		// for (int j = tdNum; j < tdNum + COLSPAN_SIZE; j++) {
		// td(list, code, 0, j);
		// }
		// tdNum += COLSPAN_SIZE;
		// code.append("</h:tr>\n");
		// }
		// int surplus = 0;
		// int lastNum = allSize - times * COLSPAN_SIZE;
		// if (lastNum != 0) {
		// surplus = COLSPAN_SIZE - lastNum;
		// code.append("<h:tr>\n");
		// for (int i = tdNum; i < allSize; i++) {
		// td(list, code, surplus * 2 + 1, i);
		// }
		// code.append("</h:tr>\n");
		// }
		// }

		code.append("<h:tr>\n");
		for (Table table : list) {
			if (table.getName().equals(tableName)) {
				if (!table.getKey().equals("PRI")) {
					if (!table.getColumn().equals("create_date") && !table.getColumn().equals("update_date")
							&& !table.getColumn().equals("create_user") && !table.getColumn().equals("update_user")) {
						code.append("<h:td width=\"10%\" class=\"tdEvennoBorder\"><hbox><label value=\""
								+ table.getComment() + "\" /></hbox></h:td>");
						code.append("<h:td width=\"10%\" class=\"tdOddnoBorder\">\n");
						code.append("<label value=\"\" id=\"" + getFirstWordLower(table.getColumn())
								+ "Label\" visible=\"true\" />\n");
						code.append("<textbox id=\"" + getFirstWordLower(table.getColumn())
								+ "Textbox\" visible=\"false\" /></h:td>\n");

					}
				}
			}
		}
		code.append("</h:tr>\n");
		code.append("</h:table>\n</center>\n<south>\n");
		code.append("<hbox pack=\"center\" width=\"100%\">\n");
		code.append("<hbox align=\"center\">\n");
		code.append("<button id=\"addButton\" label=\"增加\" visible=\"true\" />\n");
		code.append("<button id=\"editButton\" label=\"修改\" visible=\"false\" />\n");
		code.append("<button id=\"saveButton\" label=\"保存\" visible=\"false\" />\n");
		code.append("<button id=\"cancelButton\" label=\"取消\" visible=\"false\" />\n");
		code.append("</hbox>\n</hbox>\n</south>\n</borderlayout>\n</south>\n</borderlayout>\n</window>\n</zk>\n");
		File file = new File(getRootPath() + "/WebContent/basedata/" + getFirstWordLower(tableName) + ".zul");
		createFile(file, code.toString());
		System.out.println("创建Zul文件成功！");
	}

	/**
	 * @category 得到第一个单词小写的字符串
	 * @param str
	 * @return
	 */
	private String getFirstWordLower(String str) {
		String[] arrTableNames = str.split("_");
		str = "";
		for (int i = 0; i < arrTableNames.length; i++) {
			char first = arrTableNames[i].charAt(0);
			if (i != 0) {
				first -= 32;
			}
			str += first + arrTableNames[i].substring(1);
		}
		return str;
	}

	/**
	 * @category 得到第一个单词大写的字符串
	 * @param str
	 * @return
	 */
	private String getFirstWordCapital(String str) {
		String[] arrTableNames = str.split("_");
		str = "";
		for (int i = 0; i < arrTableNames.length; i++) {
			char first = arrTableNames[i].charAt(0);
			first -= 32;
			str += first + arrTableNames[i].substring(1);
		}
		return str;
	}

	/**
	 * @category 新建文件
	 * @param file
	 * @param str
	 * @throws IOException
	 */
	private void createFile(File file, String str) throws IOException {
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter writer = new FileWriter(file);
		writer.write(str);
		writer.flush();
		writer.close();
	}

	@SuppressWarnings("unused")
	private void findFiles(String path) {
		File fileA = new File(path);
		File[] files = fileA.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				System.err.println(file.getName());
			} else {
				if (file.length() != 0) {
					System.err.println(file.getName());
				}
			}

		}
	}

	/**
	 * @category 根目录
	 * @return
	 */
	private String getRootPath() {
		File rootPth = new File("");
		return rootPth.getAbsolutePath();
	}

	/**
	 * @category 返回类型
	 * @param table
	 * @return
	 */
	public StringBuilder getSwithType(Table table) {
		StringBuilder code = new StringBuilder();
		if (table.getType().equals("varchar") || table.getType().equals("text")) {
			code.append("String ");
		} else if (table.getType().equals("datetime")) {
			code.append("Timestamp ");
		} else if (table.getType().equals("int")) {
			code.append("int ");
		} else if (table.getType().equals("date")) {
			code.append("Date ");
		} else if (table.getType().equals("double")) {
			code.append("double ");
		}
		return code;
	}

	public String getSwithTypeFormat(Table table) {
		StringBuilder code = new StringBuilder();
		if (table.getType().equals("varchar") || table.getType().equals("text")) {
			code.append("java.lang.String");
		} else if (table.getType().equals("datetime")) {
			code.append("java.sql.Timestamp");
		} else if (table.getType().equals("int")) {
			code.append("int");
		} else if (table.getType().equals("date")) {
			code.append("java.util.Date");
		} else if (table.getType().equals("double")) {
			code.append("double");
		}
		return code.toString();
	}

	class TableDao {
		public String sql = null;
		public DBHelper db1 = null;
		public ResultSet ret = null;

		public List<Table> findByDataBase(String database) {
			List<Table> list = new ArrayList<Table>();
			sql = "select table_name,column_name,data_type,column_key,column_comment from information_schema.columns where TABLE_SCHEMA ='"
					+ database + "'";// SQL语句
			db1 = new DBHelper(sql);// 创建DBHelper对象
			try {
				ret = db1.pst.executeQuery();// 执行语句，得到结果集
				while (ret.next()) {
					Table table = new Table();
					table.setName(ret.getString(1));
					table.setColumn(ret.getString(2));
					table.setType(ret.getString(3));
					table.setKey(ret.getString(4));
					table.setComment(ret.getString(5));
					list.add(table);
				}
				db1.close();// 关闭连接
				ret.close();
				// 显示数据
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return list;
		}

	}

	class Table {
		private int id;
		private String name;
		private String column;
		private String type;
		private String comment;
		private String key;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getColumn() {
			return column;
		}

		public void setColumn(String column) {
			this.column = column;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

	}

	class DBHelper {
		public Connection conn = null;
		public PreparedStatement pst = null;

		public DBHelper(String sql) {
			try {
				Class.forName(name);// 指定连接类型
				conn = DriverManager.getConnection(url, user, password);// 获取连接
				pst = conn.prepareStatement(sql);// 准备执行语句
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void close() {
			try {
				this.conn.close();
				this.pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
