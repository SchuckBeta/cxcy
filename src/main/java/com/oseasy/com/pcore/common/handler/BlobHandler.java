/**
 * .
 */

package com.oseasy.com.pcore.common.handler;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.oseasy.util.common.utils.StringUtil;

public class BlobHandler extends BaseTypeHandler<String> {
    public void setNonNullParameter(PreparedStatement ps, int i,
            String parameter, JdbcType jdbcType) throws SQLException {
        ByteArrayInputStream bis;
        try {
            //###把String转化成byte流
            bis = new ByteArrayInputStream(parameter.getBytes(StringUtil.UTF_8));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Blob Encoding Error!");
        }
        ps.setBinaryStream(i, bis, parameter.length());
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        Blob blob = (Blob) rs.getBlob(columnName);
        byte[] returnValue = null;
        try {
            if (null != blob) {
                returnValue = blob.getBytes(1, (int) blob.length());
                //###把byte转化成string
                return new String(returnValue, StringUtil.UTF_8);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Blob Encoding Error!");
        }
        return null;
    }

    public String getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        Blob blob = (Blob) cs.getBlob(columnIndex);
        byte[] returnValue = null;
        if (null != blob) {
            returnValue = blob.getBytes(1, (int) blob.length());
        }
        try {
            return new String(returnValue, StringUtil.UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Blob Encoding Error!");
        }
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        return null;
    }
}