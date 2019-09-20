/**
 * Copyright (c) 2005-2012 springside.org.cn
 */
package com.oseasy.util.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 反射工具类.
 * 提供调用getter/setter方法, 访问私有变量, 调用私有方法, 获取泛型类型Class, 被AOP过的真实类等工具函数.
 * @author calvin

 */
@SuppressWarnings("rawtypes")
public class Reflections {

	private static final String SETTER_PREFIX = "set";

	private static final String GETTER_PREFIX = "get";

	private static final String CGLIB_CLASS_SEPARATOR = "$$";

	private static Logger logger = LoggerFactory.getLogger(Reflections.class);

	/**
	 * 调用Getter方法.
	 * 支持多级，如：对象名.对象名.方法
	 */
	public static Object invokeGetter(Object obj, String propertyName) {
		Object object = obj;
		for (String name : StringUtils.split(propertyName, ".")) {
			String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(name);
			object = invokeMethod(object, getterMethodName, new Class[] {}, new Object[] {});
		}
		return object;
	}

	/**
	 * 调用Setter方法, 仅匹配方法名。
	 * 支持多级，如：对象名.对象名.方法
	 */
	public static void invokeSetter(Object obj, String propertyName, Object value) {
		Object object = obj;
		String[] names = StringUtils.split(propertyName, ".");
		for (int i=0; i<names.length; i++) {
			if (i<names.length-1) {
				String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(names[i]);
				object = invokeMethod(object, getterMethodName, new Class[] {}, new Object[] {});
			}else{
				String setterMethodName = SETTER_PREFIX + StringUtils.capitalize(names[i]);
				invokeMethodByName(object, setterMethodName, new Object[] { value });
			}
		}
	}

	/**
	 * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
	 */
	public static Object getFieldValue(final Object obj, final String fieldName){
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		Object result = null;
		try {
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常{}", e.getMessage());
		}
		return result;
	}

	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 */
	public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}
	}

	/**
	 * 直接调用对象方法, 无视private/protected修饰符.
	 * 用于一次性调用的情况，否则应使用getAccessibleMethod()函数获得Method后反复调用.
	 * 同时匹配方法名+参数类型，
	 */
	public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
			final Object[] args) {
		Method method = getAccessibleMethod(obj, methodName, parameterTypes);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}

		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 直接调用对象方法, 无视private/protected修饰符，
	 * 用于一次性调用的情况，否则应使用getAccessibleMethodByName()函数获得Method后反复调用.
	 * 只匹配函数名，如果有多个同名函数调用第一个。
	 */
	public static Object invokeMethodByName(final Object obj, final String methodName, final Object[] args) {
		Method method = getAccessibleMethodByName(obj, methodName);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}

		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.
	 *
	 * 如向上转型到Object仍无法找到, 返回null.
	 */
	public static Field getAccessibleField(final Object obj, final String fieldName) {
		Validate.notNull(obj, "object can't be null");
		Validate.notBlank(fieldName, "fieldName can't be blank");
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				makeAccessible(field);
				return field;
			} catch (NoSuchFieldException e) {//NOSONAR
				// Field不在当前类定义,继续向上转型
				continue;// new add
			}
		}
		return null;
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
	 * 如向上转型到Object仍无法找到, 返回null.
	 * 匹配函数名+参数类型。
	 *
	 * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
	 */
	public static Method getAccessibleMethod(final Object obj, final String methodName,
			final Class<?>... parameterTypes) {
		Validate.notNull(obj, "object can't be null");
		Validate.notBlank(methodName, "methodName can't be blank");

		for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
			try {
				Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
				makeAccessible(method);
				return method;
			} catch (NoSuchMethodException e) {
				// Method不在当前类定义,继续向上转型
				continue;// new add
			}
		}
		return null;
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
	 * 如向上转型到Object仍无法找到, 返回null.
	 * 只匹配函数名。
	 *
	 * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
	 */
	public static Method getAccessibleMethodByName(final Object obj, final String methodName) {
		Validate.notNull(obj, "object can't be null");
		Validate.notBlank(methodName, "methodName can't be blank");

		for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
			Method[] methods = searchType.getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					makeAccessible(method);
					return method;
				}
			}
		}
		return null;
	}

	/**
	 * 改变private/protected的方法为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
	 */
	public static void makeAccessible(Method method) {
		if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
				&& !method.isAccessible()) {
			method.setAccessible(true);
		}
	}

	/**
	 * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
	 */
	public static void makeAccessible(Field field) {
		if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier
				.isFinal(field.getModifiers())) && !field.isAccessible()) {
			field.setAccessible(true);
		}
	}

	/**
	 * 通过反射, 获得Class定义中声明的泛型参数的类型, 注意泛型必须定义在父类处
	 * 如无法找到, 返回Object.class.
	 * eg.
	 * public UserDao extends HibernateDao<User>
	 *
	 * @param clazz The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be determined
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getClassGenricType(final Class clazz) {
		return getClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
	 * 如无法找到, 返回Object.class.
	 *
	 * 如public UserDao extends HibernateDao<User,Long>
	 *
	 * @param clazz clazz The class to introspect
	 * @param index the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or Object.class if cannot be determined
	 */
	public static Class getClassGenricType(final Class clazz, final int index) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}

		return (Class) params[index];
	}

	public static Class<?> getUserClass(Object instance) {
		Assert.notNull(instance, "Instance must not be null");
		Class clazz = instance.getClass();
		if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
			Class<?> superClass = clazz.getSuperclass();
			if (superClass != null && !Object.class.equals(superClass)) {
				return superClass;
			}
		}
		return clazz;

	}

	/**
	 * 将反射时的checked exception转换为unchecked exception.
	 */
	public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
		if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException) {
			return new IllegalArgumentException(e);
		} else if (e instanceof InvocationTargetException) {
			return new RuntimeException(((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException("Unexpected Checked Exception.", e);
	}

  /**
   * 检查类是否包含某些属性.
   * @param obj 需要检测的类
   * @param keys 需要检查的属性
   * @return Map
   */
  public static Boolean hasField(Object obj, String key) {
    Map<String, Boolean> result = hasFields(obj, Arrays.asList(new String[]{key}));
    return ((result != null) ? result.get(key) : false);
  }

	/**
	 * 检查类是否包含某些属性.
	 * @param obj 需要检测的类
	 * @param keys 需要检查的属性
	 * @return Map
	 */
	public static Map<String, Boolean> hasFields(Object obj, List<String> keys) {
	  if ((keys == null) || (keys.size() <= 0)) {
	    return null;
	  }

	  Map<String, Boolean> result = new HashMap<String, Boolean>();

	  /**
	   * 循环遍历所有的元素，检测有没有这个名字
	   */
	  List<Field> fields = getAllFields(obj);
	  for (String key : keys) {
	    boolean isHasFiled = false;
	    for (Field field : fields) {
        if ((field.getName()).equals(key)) {
          isHasFiled = true;
          break;
        }
      }
	    result.put(key, isHasFiled);
	  }
	  return result;
  }

  /**
   * 检查类是否包含某些属性.
   * @param obj 需要检测的类
   * @param keys 需要检查的属性
   * @return Map
   */
  public static Boolean hasMethod(Object obj, String key) {
    Map<String, Boolean> result = hasMethods(obj, Arrays.asList(new String[]{key}));
    return ((result != null) ? result.get(key) : false);
  }

	/**
	 * 检查类是否包含某些方法.
	 * @param obj 需要检测的类
	 * @param keys 需要检查的方法
	 * @return Map
	 */
	public static Map<String, Boolean> hasMethods(Object obj, List<String> keys) {
	  if ((keys == null) || (keys.size() <= 0)) {
	    return null;
	  }

	  Map<String, Boolean> result = new HashMap<String, Boolean>();

	  /**
	   * 循环遍历所有的元素，检测有没有这个名字
	   */
	  List<Method> methods = getAllMethods(obj);
	  for (String key : keys) {
	    boolean isHasMethod = false;
	    for (Method method : methods) {
	      if ((method.getName()).equals(key)) {
	        isHasMethod = true;
	        break;
	      }
	    }
	    result.put(key, isHasMethod);
	  }
	  return result;
	}


  /**
   * 循环向上转型, 获取对象的 DeclaredMethod.
   * @param object : 子类对象
   * @param methodName : 父类中的方法名
   * @param parameterTypes : 父类中的方法参数类型
   * @return 父类中的方法对象
   */
  public static Method getDeclaredMethod(Object object, String methodName, Class<?> ... parameterTypes) {
      Method method = null ;

      for(Class<?> clazz = object.getClass() ; clazz != Object.class ; clazz = clazz.getSuperclass()) {
          try {
              method = clazz.getDeclaredMethod(methodName, parameterTypes) ;
              return method ;
          } catch (Exception e) {
              //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
              //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了

          }
      }
      return null;
  }

  /**
   * 循环向上转型, 获取对象的 DeclaredField.
   * @param object : 子类对象
   * @param fieldName : 父类中的属性名
   * @return 父类中的属性对象
   */
  public static Field getDeclaredField(Object object, String fieldName) {
      Field field = null ;
      Class<?> clazz = object.getClass() ;
      for(; clazz != Object.class ; clazz = clazz.getSuperclass()) {
          try {
              field = clazz.getDeclaredField(fieldName) ;
              return field ;
          } catch (Exception e) {
              //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
              //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了

          }
      }
      return null;
  }

  /**
   * 循环向上转型, 获取对象的所有 DeclaredField，包括父类.
   * @param object : 子类对象
   * @return 父类中的属性对象
   */
  public static List<Field> getAllFields(Object object) {
    List<Field> fields = new ArrayList<Field>();
    Class<?> clazz = object.getClass() ;
    for(; clazz != Object.class ; clazz = clazz.getSuperclass()) {
      try {
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
      } catch (Exception e) {
        //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
        //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
      }
    }
    return fields;
  }

  /**
   * 循环向上转型, 获取对象的所有 DeclaredMethod，包括父类.
   * @param object : 子类对象
   * @return 父类中的方法对象
   */
  public static List<Method> getAllMethods(Object object) {
    List<Method> methods = new ArrayList<Method>();
    Class<?> clazz = object.getClass() ;
    for(; clazz != Object.class ; clazz = clazz.getSuperclass()) {
      try {
        methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
      } catch (Exception e) {
        //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
        //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
      }
    }
    return methods;
  }

  public static Class<?> getFieldGenericType(Object object, String fieldName) throws Exception {
      //我们项目的所有实体类都继承BaseDomain （所有实体基类：该类只是串行化一下）
      //不需要的自己去掉即可
      if (object != null && StringUtil.isNotEmpty(fieldName)) {
          Field field = getDeclaredField(object, fieldName);
          if(field == null){
              logger.info("Field is null");
              return null;
          }

          // 如果类型是String
          if ((field.getGenericType().toString()).equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
              return String.class;
          }else if ((field.getGenericType().toString()).equals("class java.lang.Integer")) {
              return Integer.class;
          }else if ((field.getGenericType().toString()).equals("class java.lang.Double")) {
              return Double.class;
          }else if ((field.getGenericType().toString()).equals("class java.lang.Boolean")) {
              return Boolean.class;
          }else if ((field.getGenericType().toString()).equals("boolean")) {
              return Boolean.class;
          }else if ((field.getGenericType().toString()).equals("class java.util.Date")) {
              return Date.class;
          }else if ((field.getGenericType().toString()).equals("class java.util.Short")) {
              return Short.class;
          }else{
              logger.info("Field Class<?> is undefine");
              return null;
          }
      }
      logger.info("params is null");
      return null;
  }


  /**
   * 获取变量属性值、和属性类型.
   * @param object 传递对象
   * @param varMaps 返回结果
   * @return List
   */
  public static List<VarMap> reflectVarMaps(Object object, List<VarMap> varMaps) {
      if((object == null) || StringUtil.checkEmpty(varMaps)){
          return null;
      }

      for (VarMap varMap : varMaps) {
          Object obj = null;
          try {
              obj = getFieldValue(object, varMap.getVar());
          } catch (Exception e) {}

          if(obj == null){
              continue;
          }
          varMap.setVal(obj);
          varMap.setVaclazz(obj.getClass());
      }
      return varMaps;
  }

  /**
   * 获取变量属性值、和属性类型.
   * @param object 传递对象
   * @param varMaps 返回结果
   * @return String
   */
  public static String reflectVarMapStr(Object object, List<VarMap> varMaps) {
      StringBuffer buffer = new StringBuffer();
      if((object == null) || StringUtil.checkEmpty(varMaps)){
          return buffer.toString();
      }

      for (VarMap varMap : varMaps) {
          buffer.append(varMap.getPreHtml());
          if(StringUtil.isEmpty(varMap.getVar())){
              continue;
          }

          Object obj = getFieldValue(object, varMap.getVar());
          if(obj == null){
              continue;
          }
          buffer.append(obj);
      }
      return buffer.toString();
  }

  /***
   * 获取指定类的指定方法的执行结果
   * @param className  全路径 带包名的类名
   * @param methodName 方法名
   * @return
   * @throws Exception
   */
  public static Method[] getMethods(String className) throws Exception{
      //返回指定包下指定类
      Class loadClass = Class.forName(className);
      if(loadClass == null){
          return null;
      }
      return loadClass.getDeclaredMethods();
  }

  /***
   * 获取指定类的指定方法的执行结果
   * @param className  全路径 带包名的类名
   * @param methodName 方法名
   * @return
   * @throws Exception
   */
  public static Object invokeMethod(String className, String methodName, Object... args) throws Exception{
      //返回指定包下指定类
      Class<?> loadClass = Class.forName(className);
      Object thisClass = loadClass.newInstance();
      Method[] mths = getMethods(className);
      //校验 目标类是否有对应的方法
      boolean flag = false;
      for (int i = 0; i < mths.length; i++) {
          if(mths[i].getName().equals(methodName)){
              flag = true;
              break;
          }
      }
      if(!flag){
          return "你想干啥？？？";
      }
      //取出对应的方法的执行结果
      Method mm = loadClass.getDeclaredMethod(methodName);
      return mm.invoke(thisClass, args);
  }


	/**
	 * 随机初始化对象属性值.
	 * @param arg Object
	 * @param filters List
	 * @return Object
	 */
	public static Object init(Object arg, List<String> filters){
		List<Field> fields = Reflections.getAllFields(arg);
		for (Field field: fields) {
			if(filters.contains(field.getName())){
				continue;
			}

			if(field.getType() == String.class){
				Reflections.invokeSetter(arg, field.getName(), genStr(10));
			}else if(field.getType() == Date.class){
				Reflections.invokeSetter(arg, field.getName(), new Date());
			}else if(field.getType() == Date.class){
				Reflections.invokeSetter(arg, field.getName(), new Timestamp(new Date().getTime()));
			}else{
				//忽略对象及对象属性.
			}
		}
		return arg;
	}

	/**
	 * 随机生成指定长度的字符串.
	 * @param length 长度
	 * @return String
	 */
	public static String genStr(int length) {
		Random random = new Random();
		char[] codeSeqs = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9' };
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < length; i++) {
			s.append(String.valueOf(codeSeqs[random.nextInt(codeSeqs.length)]));
		}
		return s.toString();
	}

//    private void check(Object robj) {
//        if((String.class).equals(robj)){
//            Reflections.invokeSetter(obj, col[0], rs.getString(col[1]));
//        }else if((Timestamp.class).equals(robj)){
//            Reflections.invokeSetter(obj, col[0], rs.getTimestamp(col[1]));
//        }else if((Date.class).equals(robj) || (java.util.Date.class).equals(robj)){
//            Reflections.invokeSetter(obj, col[0], rs.getDate(col[1]));
//        }else if((Boolean.class).equals(robj)){
//            Reflections.invokeSetter(obj, col[0], rs.getBoolean(col[1]));
//        }else if((Float.class).equals(robj)){
//            Reflections.invokeSetter(obj, col[0], rs.getFloat(col[1]));
//        }else if((Double.class).equals(robj)){
//            Reflections.invokeSetter(obj, col[0], rs.getDouble(col[1]));
//        }else if((Integer.class).equals(robj)){
//            Reflections.invokeSetter(obj, col[0], rs.getInt(col[1]));
//        }else if((Long.class).equals(robj)){
//            Reflections.invokeSetter(obj, col[0], rs.getLong(col[1]));
//        }else{
//    }
}
