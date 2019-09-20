/**
 *
 */
package com.oseasy.com.pcore.modules.gen.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.mapper.JaxbMapper;
import com.oseasy.com.pcore.modules.gen.entity.GenCategory;
import com.oseasy.com.pcore.modules.gen.entity.GenConfig;
import com.oseasy.com.pcore.modules.gen.entity.GenScheme;
import com.oseasy.com.pcore.modules.gen.entity.GenTable;
import com.oseasy.com.pcore.modules.gen.entity.GenTableColumn;
import com.oseasy.com.pcore.modules.gen.entity.GenTemplate;
import com.oseasy.com.pcore.modules.sys.entity.Area;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.FreeMarkers;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 代码生成工具类.


 */
public class GenUtils {
    private static final String GEN_UTIL = "util.";//UTIL包
    private static final String GEN_TEMPLATE = "template";//模板路径
    private static final String GEN_FUNCTION_VERSION = "functionVersion";//功能版本
    private static final String GEN_FUNCTION_AUTHOR = "functionAuthor";//功能作者
    private static final String GEN_FUNCTION_NAME_SIMPLE = "functionNameSimple";//功能简称
    private static final String GEN_FUNCTION_NAME = "functionName";//功能名
    private static final String GEN_LAST_PACKAGE_NAME = "lastPackageName";//最终包名
    private static final String GEN_VIEW_PREFIX = "viewPrefix";//视图前缀
    private static final String GEN_PERMISSION_PREFIX = "permissionPrefix";//授权前缀
    private static final String GEN_TABLE = "table";//表名
    private static final String GEN_JDBC_TYPE = "jdbc.type";//数据库列名
    private static final String GEN_DB_TYPE = "dbType";//数据库类型
    private static final String GEN_URL_PREFIX = "urlPrefix";//路径前缀
    private static final String GEN_SUB_MODULE_NAME = "subModuleName";//子模块名
    private static final String GEN_PACKAGE_NAME = "packageName";//包名
    private static final String GEN_MODULE_NAME = "moduleName";//模块名
    private static final String GEN_ENTITY_NAME = "entityName";//实体名
    private static final String GEN_CLASS_LNAME = "className";//小写类名
    private static final String GEN_CLASS_UNAME = "ClassName";//大写类名
    private static final String GEN_CONFIG_XML = "config.xml";//配置文件名
    private static final String GEN_ENTITY = "entity";//实体目录字符
    private static Logger logger = LoggerFactory.getLogger(GenUtils.class);

	/**
	 * 初始化列属性字段
	 * @param genTable
	 */
	public static void initColumnField(GenTable genTable) {
		for (GenTableColumn column : genTable.getColumnList()) {

			// 如果是不是新增列，则跳过。
			if (StringUtil.isNotBlank(column.getId())) {
				continue;
			}

			// 设置字段说明
			if (StringUtil.isBlank(column.getComments())) {
				column.setComments(column.getName());
			}

			// 设置java类型
			if (StringUtil.startsWithIgnoreCase(column.getJdbcType(), "CHAR")
					|| StringUtil.startsWithIgnoreCase(column.getJdbcType(), "VARCHAR")
					|| StringUtil.startsWithIgnoreCase(column.getJdbcType(), "NARCHAR")) {
				column.setJavaType("String");
			}else if (StringUtil.startsWithIgnoreCase(column.getJdbcType(), "DATETIME")
					|| StringUtil.startsWithIgnoreCase(column.getJdbcType(), "DATE")
					|| StringUtil.startsWithIgnoreCase(column.getJdbcType(), "TIMESTAMP")) {
				column.setJavaType("java.util.Date");
				column.setShowType("dateselect");
			}else if (StringUtil.startsWithIgnoreCase(column.getJdbcType(), "BIGINT")
					|| StringUtil.startsWithIgnoreCase(column.getJdbcType(), "NUMBER")) {
				// 如果是浮点型
				String[] ss = StringUtil.split(StringUtil.substringBetween(column.getJdbcType(), "(", ")"), ",");
				if (ss != null && ss.length == 2 && Integer.parseInt(ss[1])>0) {
					column.setJavaType("Double");
				}
				// 如果是整形
				else if (ss != null && ss.length == 1 && Integer.parseInt(ss[0])<=10) {
					column.setJavaType("Integer");
				}
				// 长整形
				else{
					column.setJavaType("Long");
				}
			}

			// 设置java字段名
			column.setJavaField(StringUtil.toCamelCase(column.getName()));

			// 是否是主键
			column.setIsPk(genTable.getPkList().contains(column.getName())?"1":"0");

			// 插入字段
			column.setIsInsert("1");

			// 编辑字段
			if (!StringUtil.equalsIgnoreCase(column.getName(), "id")
					&& !StringUtil.equalsIgnoreCase(column.getName(), "create_by")
					&& !StringUtil.equalsIgnoreCase(column.getName(), "create_date")
					&& !StringUtil.equalsIgnoreCase(column.getName(), "del_flag")) {
				column.setIsEdit("1");
			}

			// 列表字段
			if (StringUtil.equalsIgnoreCase(column.getName(), "name")
					|| StringUtil.equalsIgnoreCase(column.getName(), "title")
					|| StringUtil.equalsIgnoreCase(column.getName(), "remarks")
					|| StringUtil.equalsIgnoreCase(column.getName(), "update_date")) {
				column.setIsList("1");
			}

			// 查询字段
			if (StringUtil.equalsIgnoreCase(column.getName(), "name")
					|| StringUtil.equalsIgnoreCase(column.getName(), "title")) {
				column.setIsQuery("1");
			}

			// 查询字段类型
			if (StringUtil.equalsIgnoreCase(column.getName(), "name")
					|| StringUtil.equalsIgnoreCase(column.getName(), "title")) {
				column.setQueryType("like");
			}

			// 设置特定类型和字段名

			// 用户
			if (StringUtil.startsWithIgnoreCase(column.getName(), "user_id")) {
				column.setJavaType(User.class.getName());
				column.setJavaField(column.getJavaField().replaceAll("Id", ".id|name"));
				column.setShowType("userselect");
			}
			// 部门
			else if (StringUtil.startsWithIgnoreCase(column.getName(), "office_id")) {
				column.setJavaType(Office.class.getName());
				column.setJavaField(column.getJavaField().replaceAll("Id", ".id|name"));
				column.setShowType("officeselect");
			}
			// 区域
			else if (StringUtil.startsWithIgnoreCase(column.getName(), "area_id")) {
				column.setJavaType(Area.class.getName());
				column.setJavaField(column.getJavaField().replaceAll("Id", ".id|name"));
				column.setShowType("areaselect");
			}
			// 创建者、更新者
			else if (StringUtil.startsWithIgnoreCase(column.getName(), "create_by")
					|| StringUtil.startsWithIgnoreCase(column.getName(), "update_by")) {
				column.setJavaType(User.class.getName());
				column.setJavaField(column.getJavaField() + ".id");
			}
			// 创建时间、更新时间
			else if (StringUtil.startsWithIgnoreCase(column.getName(), "create_date")
					|| StringUtil.startsWithIgnoreCase(column.getName(), "update_date")) {
				column.setShowType("dateselect");
			}
			// 备注、内容
			else if (StringUtil.equalsIgnoreCase(column.getName(), "remarks")
					|| StringUtil.equalsIgnoreCase(column.getName(), "content")) {
				column.setShowType("textarea");
			}
			// 父级ID
			else if (StringUtil.equalsIgnoreCase(column.getName(), "parent_id")) {
				column.setJavaType("This");
				column.setJavaField("parent.id|name");
				column.setShowType("treeselect");
			}
			// 所有父级ID
			else if (StringUtil.equalsIgnoreCase(column.getName(), "parent_ids")) {
				column.setQueryType("like");
			}
			// 删除标记
			else if (StringUtil.equalsIgnoreCase(column.getName(), "del_flag")) {
				column.setShowType("radiobox");
				column.setDictType("del_flag");
			}
		}
	}

	/**
	 * 获取模板路径
	 * @return
	 */
	public static String getTemplatePath() {
		try{
			File file = new DefaultResourceLoader().getResource("").getFile();
			if (file != null) {
				return file.getAbsolutePath() + File.separator + StringUtil.replaceEach(GenUtils.class.getName(),
						new String[]{GEN_UTIL+GenUtils.class.getSimpleName(), "."}, new String[]{GEN_TEMPLATE, File.separator});
			}
		}catch(Exception e) {
			logger.error("{}", e);
		}

		return "";
	}

	/**
	 * XML文件转换为对象
	 * @param fileName
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fileToObject(String fileName, Class<?> clazz) {
		try {
			String pathName = "/templates/modules/gen/" + fileName;
//			logger.debug("File to object: {}", pathName);
			Resource resource = new ClassPathResource(pathName);
			InputStream is = resource.getInputStream();
			if (is != null) {
  			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
  			StringBuilder sb = new StringBuilder();
  			while (true) {
  				String line = br.readLine();
  				if (line == null) {
  					break;
  				}
  				sb.append(line).append("\r\n");
  			}
  			if (is != null) {
  				is.close();
  			}
  			if (br != null) {
  				br.close();
  			}
  //			logger.debug("Read file content: {}", sb.toString());
  			return (T) JaxbMapper.fromXml(sb.toString(), clazz);
      }
		} catch (IOException e) {
			logger.warn("Error file convert: {}", e.getMessage());
		}
//		String pathName = StringUtil.replace(getTemplatePath() + "/" + fileName, "/", File.separator);
//		logger.debug("file to object: {}", pathName);
//		String content = "";
//		try {
//			content = FileUtils.readFileToString(new File(pathName), "utf-8");
////			logger.debug("read config content: {}", content);
//			return (T) JaxbMapper.fromXml(content, clazz);
//		} catch (IOException e) {
//			logger.warn("error convert: {}", e.getMessage());
//		}
		return null;
	}


    /**
     * 获取代码生成配置对象
     * @return
     */
    public static GenConfig getConfig() {
        return getConfig(Lists.newArrayList());
    }
    public static GenConfig getConfig(List<GenScheme> genSchemes) {
        GenConfig genConfig =  fileToObject(GEN_CONFIG_XML, GenConfig.class);
        List<Dict> javaTypes = genConfig.getJavaTypeList();
        for (GenScheme genScheme : genSchemes) {
            if((genScheme == null) || (genScheme.getGenTable() == null)){
                continue;
            }

            Dict dict = new Dict();
            if(StringUtil.isNotEmpty(genScheme.getSubModuleName())){
                dict.setValue(genScheme.getPackageName() + StringUtil.DOT + genScheme.getModuleName() + StringUtil.DOT + genScheme.getSubModuleName() + StringUtil.DOT + GEN_ENTITY + StringUtil.DOT + genScheme.getGenTable().getClassName());
            }else{
                dict.setValue(genScheme.getPackageName() + StringUtil.DOT + genScheme.getModuleName() + StringUtil.DOT + GEN_ENTITY + StringUtil.DOT + genScheme.getGenTable().getClassName());
            }
            dict.setLabel(genScheme.getGenTable().getClassName());
            dict.setDescription(genScheme.getGenTable().getComments());
            javaTypes.add(dict);
        }
        return genConfig;
    }

	/**
	 * 根据分类获取模板列表
	 * @param config
	 * @param genScheme
	 * @param isChildTable 是否是子表
	 * @return
	 */
	public static List<GenTemplate> getTemplateList(GenConfig config, String category, boolean isChildTable) {
		List<GenTemplate> templateList = Lists.newArrayList();
		if (config !=null && config.getCategoryList() != null && category !=  null) {
			for (GenCategory e : config.getCategoryList()) {
				if (category.equals(e.getValue())) {
					List<String> list = null;
					if (!isChildTable) {
						list = e.getTemplate();
					}else{
						list = e.getChildTableTemplate();
					}
					if (list != null) {
						for (String s : list) {
							if (StringUtil.startsWith(s, GenCategory.CATEGORY_REF)) {
								templateList.addAll(getTemplateList(config, StringUtil.replace(s, GenCategory.CATEGORY_REF, ""), false));
							}else{
								GenTemplate template = fileToObject(s, GenTemplate.class);
								if (template != null) {
									templateList.add(template);
								}
							}
						}
					}
					break;
				}
			}
		}
		return templateList;
	}

	/**
	 * 获取数据模型
	 * @param genScheme
	 * @param genTable
	 * @return
	 */
	public static Map<String, Object> getDataModel(GenScheme genScheme) {
		Map<String, Object> model = Maps.newHashMap();

		model.put(GEN_PACKAGE_NAME, StringUtil.lowerCase(genScheme.getPackageName()));
		model.put(GEN_LAST_PACKAGE_NAME, StringUtil.substringAfterLast((String)model.get(GEN_PACKAGE_NAME),"."));
		model.put(GEN_MODULE_NAME, StringUtil.lowerCase(genScheme.getModuleName()));
		model.put(GEN_SUB_MODULE_NAME, StringUtil.lowerCase(genScheme.getSubModuleName()));
		model.put(GEN_ENTITY_NAME, GEN_ENTITY);
		model.put(GEN_CLASS_LNAME, StringUtil.uncapitalize(genScheme.getGenTable().getClassName()));
		model.put(GEN_CLASS_UNAME, StringUtil.capitalize(genScheme.getGenTable().getClassName()));

		model.put(GEN_FUNCTION_NAME, genScheme.getFunctionName());
		model.put(GEN_FUNCTION_NAME_SIMPLE, genScheme.getFunctionNameSimple());
		model.put(GEN_FUNCTION_AUTHOR, StringUtil.isNotBlank(genScheme.getFunctionAuthor())?genScheme.getFunctionAuthor():UserUtils.getUser().getName());
		model.put(GEN_FUNCTION_VERSION, DateUtil.getDate());

		model.put(GEN_URL_PREFIX, model.get(GEN_MODULE_NAME)+(StringUtil.isNotBlank(genScheme.getSubModuleName())
				?"/"+StringUtil.lowerCase(genScheme.getSubModuleName()):"")+"/"+model.get(GEN_CLASS_LNAME));
		model.put(GEN_VIEW_PREFIX, //StringUtil.substringAfterLast(model.get("packageName"),".")+"/"+
				model.get(GEN_URL_PREFIX));
		model.put(GEN_PERMISSION_PREFIX, model.get(GEN_MODULE_NAME)+(StringUtil.isNotBlank(genScheme.getSubModuleName())
				?":"+StringUtil.lowerCase(genScheme.getSubModuleName()):"")+":"+model.get(GEN_CLASS_LNAME));

		model.put(GEN_DB_TYPE, CoreSval.getConfig(GEN_JDBC_TYPE));

		model.put(GEN_TABLE, genScheme.getGenTable());

		return model;
	}

	/**
	 * 生成到文件
	 * @param tpl
	 * @param model
	 * @param replaceFile
	 * @return
	 */
	public static String generateToFile(GenTemplate tpl, Map<String, Object> model, boolean isReplaceFile) {
		// 获取生成文件
		String fileName = CoreSval.getProjectPath() + File.separator
				+ StringUtil.replaceEach(FreeMarkers.renderString(tpl.getFilePath() + "/", model),
						new String[]{"//", "/", "."}, new String[]{File.separator, File.separator, File.separator})
				+ FreeMarkers.renderString(tpl.getFileName(), model);
		logger.debug(" fileName === " + fileName);

		// 获取生成文件内容
		String content = FreeMarkers.renderString(StringUtil.trimToEmpty(tpl.getContent()), model);
		logger.debug(" content === \r\n" + content);

		// 如果选择替换文件，则删除原文件
		if (isReplaceFile) {
			FileUtil.deleteFile(fileName);
		}

		// 创建并写入文件
		if (FileUtil.createFile(fileName)) {
			FileUtil.writeToFile(fileName, content, true);
			logger.debug(" file create === " + fileName);
			return "生成成功："+fileName+"<br/>";
		}else{
			logger.debug(" file extents === " + fileName);
			return "文件已存在："+fileName+"<br/>";
		}
	}

	public static void main(String[] args) {
		try {
			GenConfig config = getConfig();
			System.out.println(config);
			System.out.println(JaxbMapper.toXml(config));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
