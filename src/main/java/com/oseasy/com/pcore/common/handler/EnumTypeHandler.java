package com.oseasy.com.pcore.common.handler;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import com.oseasy.com.fileserver.modules.attachment.enums.StringEnum;
@MappedJdbcTypes({JdbcType.VARCHAR})
@MappedTypes({StringEnum.class})
public class EnumTypeHandler<E extends Enum<E> & StringEnum<E>> extends  
        BaseTypeHandler<StringEnum<E>> {  
    private Class<StringEnum<E>> type;  
  
    public EnumTypeHandler(Class<StringEnum<E>> type) {  
        if (type == null)  
            throw new IllegalArgumentException("Type argument cannot be null");  
        this.type = type;  
    }  
  
    private StringEnum<E> convert(String status) {  
        StringEnum<E>[] objs = type.getEnumConstants();  
        for (StringEnum<E> em : objs) {  
            if (em.getStringValue().equals(status)) {  
                return em;  
            }  
        }  
        return null;  
    }  
  
    @Override  
    public StringEnum<E> getNullableResult(ResultSet rs, String columnName)  
            throws SQLException {  
        return convert(rs.getString(columnName));  
    }  
  
    @Override  
    public StringEnum<E> getNullableResult(ResultSet rs, int columnIndex)  
            throws SQLException {  
        return convert(rs.getString(columnIndex));  
    }  
  
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, StringEnum<E> parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setString(i, parameter.getStringValue());
	}

	@Override
	public StringEnum<E> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return convert(cs.getString(columnIndex));  
	}  
}  